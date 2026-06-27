package com.mycompany.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mycompany.Model.GoogleUser;
import com.mycompany.projectuas.session;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javafx.application.Platform;

public class GoogleAuthService {

    // Fallback timeout — hanya aktif jika browser crash / ditutup paksa
    private static final long LOGIN_TIMEOUT_MS = 120_000; // 2 menit

    private GoogleAuthorizationCodeFlow flow;
    private static Credential credential;

    private String authUrl;
    private final String redirectUri = "http://localhost:8080/callback";

    // Callback ke UI (opsional) — dipanggil saat ada perubahan status login
    // onSuccess -> GoogleUser
    // onCancelled -> null
    // onTimeout -> null
    private Consumer<GoogleUser> onSuccess;
    private Runnable onCancelled;
    private Runnable onTimeout;

    // =========================================================================
    // Setter Callback (opsional, untuk integrasi JavaFX)
    // =========================================================================

    public void setOnSuccess(Consumer<GoogleUser> onSuccess) {
        this.onSuccess = onSuccess;
    }

    public void setOnCancelled(Runnable onCancelled) {
        this.onCancelled = onCancelled;
    }

    public void setOnTimeout(Runnable onTimeout) {
        this.onTimeout = onTimeout;
    }

    // =========================================================================
    // Public API
    // =========================================================================

    /**
     * Jalankan login di background thread agar tidak memblokir JavaFX thread.
     * Callback (onSuccess/onCancelled/onTimeout) dipanggil di JavaFX thread.
     *
     * Contoh pemakaian:
     * GoogleAuthService auth = new GoogleAuthService();
     * auth.setOnSuccess(user -> { ... });
     * auth.setOnCancelled(() -> { ... });
     * auth.setOnTimeout(() -> { ... });
     * auth.loginAsync();
     */
    public void loginAsync() {
        Thread t = new Thread(() -> {
            try {
                GoogleUser user = login();
                if (onSuccess != null)
                    Platform.runLater(() -> onSuccess.accept(user));
            } catch (LoginCancelledException e) {
                System.out.println("[GoogleAuth] " + e.getMessage());
                if (e.getMessage().contains("timeout")) {
                    if (onTimeout != null)
                        Platform.runLater(onTimeout);
                } else {
                    if (onCancelled != null)
                        Platform.runLater(onCancelled);
                }
            } catch (Exception e) {
                System.err.println("[GoogleAuth] Error: " + e.getMessage());
                e.printStackTrace();
                if (onCancelled != null)
                    Platform.runLater(onCancelled);
            }
        }, "google-auth-thread");
        t.setDaemon(true);
        t.start();
    }

    private static final Path LOG_FILE = Path.of(System.getProperty("user.home"), "projectuas-debug.log");

    private void openBrowser(String url) {
        try {
            writeLog("Trying to open browser for URL: " + url);

            if (java.awt.Desktop.isDesktopSupported()
                    && java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {
                java.awt.Desktop.getDesktop().browse(new URI(url));
                writeLog("Desktop.browse() succeeded");
                return;
            }

            String os = System.getProperty("os.name").toLowerCase();
            writeLog("OS detected: " + os);

            Process browserProcess;
            if (os.contains("win")) {
                browserProcess = new ProcessBuilder("cmd", "/c", "start", "", url).start();
            } else if (os.contains("mac")) {
                browserProcess = new ProcessBuilder("open", url).start();
            } else {
                browserProcess = new ProcessBuilder("xdg-open", url).start();
            }
            writeLog("Browser process started, PID: " + browserProcess.pid());
        } catch (Exception e) {
            writeLog("openBrowser failed: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            try {
                if (java.awt.Desktop.isDesktopSupported()
                        && java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {
                    writeLog("Retry fallback Desktop.browse()");
                    java.awt.Desktop.getDesktop().browse(new URI(url));
                    writeLog("Fallback Desktop.browse() succeeded");
                }
            } catch (Exception ex) {
                writeLog("Fallback also failed: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
            }
        }
    }

    private void writeLog(String message) {
        try {
            Files.writeString(
                    LOG_FILE,
                    "[" + java.time.LocalDateTime.now() + "] " + message + "\n",
                    java.nio.file.StandardOpenOption.CREATE,
                    java.nio.file.StandardOpenOption.APPEND);
        } catch (Exception e) {
            try {
                Path fallback = Path.of(System.getProperty("user.home"), "Desktop", "projectuas-debug.log");
                Files.writeString(
                        fallback,
                        "[" + java.time.LocalDateTime.now() + "] " + message + "\n",
                        java.nio.file.StandardOpenOption.CREATE,
                        java.nio.file.StandardOpenOption.APPEND);
            } catch (Exception ignored) {
                System.err.println("[GoogleAuth] writeLog failed: " + e.getMessage());
            }
        }
    }

    // HAPUS semua isi resolveTokenFolder() dan ganti dengan ini:
    private File resolveTokenFolder() {
        String appData = System.getenv("APPDATA");
        File folder = (appData != null && !appData.isEmpty())
                ? new File(appData + "\\EnjoyCafe\\tokens")
                : new File(System.getProperty("user.home") + "/EnjoyCafe/tokens");
        if (!folder.exists())
            folder.mkdirs();
        writeLog("Token folder: " + folder.getAbsolutePath());
        return folder;
    }

    /**
     * Login sinkronus (blocking). Gunakan loginAsync() untuk JavaFX.
     */
    public GoogleUser login() throws Exception {
        writeLog("=== LOGIN DIMULAI ===");

        InputStream in = getClass().getResourceAsStream("/google/credentials.json");
        if (in == null) {
            writeLog("ERROR: credentials.json tidak ditemukan!");
            throw new IllegalStateException("credentials.json tidak ditemukan di resources/google/");
        }
        writeLog("credentials.json ditemukan");

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                GsonFactory.getDefaultInstance(), new InputStreamReader(in));
        writeLog("clientSecrets loaded");

        File tokenFolder = resolveTokenFolder();
        writeLog("tokenFolder: " + tokenFolder.getAbsolutePath());

        FileDataStoreFactory dataStoreFactory = new FileDataStoreFactory(tokenFolder);

        flow = new GoogleAuthorizationCodeFlow.Builder(
                new ApacheHttpTransport(),
                GsonFactory.getDefaultInstance(),
                clientSecrets,
                Arrays.asList("openid", "email", "profile", DriveScopes.DRIVE_FILE))
                .setAccessType("offline")
                .setDataStoreFactory(dataStoreFactory)
                .build();
        writeLog("flow dibuat");

        authUrl = flow.newAuthorizationUrl()
                .setRedirectUri(redirectUri)
                .build();
        writeLog("authUrl: " + authUrl);

        try {
            OAuthServer server = new OAuthServer(8080, flow, redirectUri, authUrl);
            server.start();
            writeLog("OAuthServer started di port 8080");

            String landingUrl = "http://localhost:8080/";
            openBrowser(landingUrl);
            writeLog("openBrowser() selesai dipanggil");

            LoginResult result = server.awaitResult(LOGIN_TIMEOUT_MS);
            writeLog("awaitResult selesai: " + result);
            server.stop();

            switch (result) {
                case SUCCESS:
                    GoogleUser user = server.getUser();
                    if (user == null)
                        throw new LoginCancelledException("Login gagal: tidak dapat mengambil info akun.");
                    return user;
                case CANCELLED:
                    throw new LoginCancelledException("Login dibatalkan: browser ditutup.");
                case TIMEOUT:
                default:
                    throw new LoginCancelledException("Login timeout. Silakan coba lagi.");
            }
        } catch (LoginCancelledException e) {
            throw e;
        } catch (Exception e) {
            writeLog("EXCEPTION di login(): " + e.getClass().getName() + " - " + e.getMessage());
            throw e;
        }
    }

    public GoogleUser getUserInfo(Credential credential) throws Exception {
        HttpRequestFactory requestFactory = new ApacheHttpTransport().createRequestFactory(credential);
        GenericUrl url = new GenericUrl("https://www.googleapis.com/oauth2/v2/userinfo");

        String json = requestFactory.buildGetRequest(url).execute().parseAsString();
        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

        String id = obj.has("sub") ? obj.get("sub").getAsString() : "";
        String email = obj.has("email") ? obj.get("email").getAsString() : "";
        String name = obj.has("name") ? obj.get("name").getAsString() : "";
        String picture = obj.has("picture") ? obj.get("picture").getAsString() : "";

        return new GoogleUser(id, email, name, picture);
    }

    // =========================================================================
    // Enum & Exception
    // =========================================================================

    private enum LoginResult {
        SUCCESS,
        CANCELLED,
        TIMEOUT
    }

    public static class LoginCancelledException extends Exception {
        public LoginCancelledException(String message) {
            super(message);
        }
    }

    // =========================================================================
    // OAuth Callback Server
    // =========================================================================

    private class OAuthServer {

        private final HttpServer server;
        private volatile GoogleUser user;
        private volatile LoginResult result = null;
        private final Object lock = new Object();

        private final GoogleAuthorizationCodeFlow flow;
        private final String redirectUri;
        private final String authUrl;

        OAuthServer(int port, GoogleAuthorizationCodeFlow flow,
                String redirectUri, String authUrl) throws IOException {
            this.flow = flow;
            this.redirectUri = redirectUri;
            this.authUrl = authUrl;

            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new LandingHandler());
            server.createContext("/callback", new CallbackHandler());
            server.createContext("/cancel", new CancelHandler());
            server.setExecutor(Executors.newSingleThreadExecutor());
        }

        void start() {
            server.start();
        }

        void stop() {
            server.stop(0);
        }

        GoogleUser getUser() {
            return user;
        }

        LoginResult awaitResult(long timeoutMillis) throws InterruptedException {
            synchronized (lock) {
                long deadline = System.currentTimeMillis() + timeoutMillis;
                while (result == null) {
                    long remaining = deadline - System.currentTimeMillis();
                    if (remaining <= 0) {
                        System.out.println("[GoogleAuth] Fallback timeout tercapai.");
                        return LoginResult.TIMEOUT;
                    }
                    lock.wait(remaining);
                }
                return result;
            }
        }

        private void setResult(LoginResult r) {
            synchronized (lock) {
                if (result == null) {
                    result = r;
                    lock.notifyAll();
                }
            }
        }

        private class LandingHandler implements HttpHandler {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                sendHtml(exchange, buildLandingHtml(authUrl), 200);
            }
        }

        private class CallbackHandler implements HttpHandler {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String query = exchange.getRequestURI().getQuery();
                String code = null;
                String error = null;

                if (query != null) {
                    for (String pair : query.split("&")) {
                        if (pair.startsWith("code="))
                            code = URLDecoder.decode(pair.substring(5), StandardCharsets.UTF_8);
                        else if (pair.startsWith("error="))
                            error = URLDecoder.decode(pair.substring(6), StandardCharsets.UTF_8);
                    }
                }

                if (error != null) {
                    System.out.println("[GoogleAuth] User menolak izin: " + error);
                    sendHtml(exchange, buildErrorHtml(
                            "Login Dibatalkan",
                            "Anda membatalkan proses login.<br>Silakan tutup tab ini dan coba lagi."), 200);
                    setResult(LoginResult.CANCELLED);
                    return;
                }

                if (code != null) {
                    try {
                        GoogleTokenResponse tokenResponse = flow.newTokenRequest(code)
                                .setRedirectUri(redirectUri)
                                .execute();
                        credential = flow.createAndStoreCredential(
                                tokenResponse,
                                "user");

                        System.out.println("Credential tersimpan = " + (credential != null));
                        GoogleUser fetchedUser = getUserInfo(credential);
                        java.util.prefs.Preferences prefs = java.util.prefs.Preferences
                                .userNodeForPackage(session.class);

                        prefs.put("google_name", fetchedUser.getName());
                        prefs.put("google_email", fetchedUser.getEmail());
                        prefs.put("google_photo", fetchedUser.getProfilePictureUrl());
                        user = fetchedUser;
                        sendHtml(exchange,
                                buildSuccessHtml(fetchedUser.getName(), fetchedUser.getProfilePictureUrl()), 200);
                        setResult(LoginResult.SUCCESS);
                        return;

                    } catch (Exception e) {
                        System.err.println("[GoogleAuth] Gagal menukar token: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                sendHtml(exchange, buildErrorHtml(
                        "Login Gagal",
                        "Terjadi kesalahan saat proses login.<br>Silakan tutup tab ini dan coba lagi."), 200);
                setResult(LoginResult.CANCELLED);
            }

        }

        private class CancelHandler implements HttpHandler {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
                exchange.getResponseHeaders().set("Content-Type", "text/plain");

                if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(204, -1);
                    return;
                }

                exchange.sendResponseHeaders(200, 0);
                exchange.getResponseBody().close();

                System.out.println("[GoogleAuth] Browser ditutup, sesi dibatalkan.");
                setResult(LoginResult.CANCELLED);
            }
        }

        private void sendHtml(HttpExchange exchange, String html, int statusCode) throws IOException {
            byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(statusCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }

    // =========================================================================
    // HTML Builders
    // =========================================================================

    private String buildLandingHtml(String googleAuthUrl) {
        String escapedUrl = googleAuthUrl.replace("\\", "\\\\").replace("'", "\\'");
        return "<!DOCTYPE html>" +
                "<html lang='id'><head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>Login dengan Google</title>" +
                "<link href='https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600&display=swap' rel='stylesheet'>"
                +
                buildCommonStyle() +
                "<style>" +
                ".btn-google { display: inline-flex; align-items: center; gap: 10px;" +
                "  background: #fff; border: 1.5px solid #dadce0; border-radius: 8px;" +
                "  padding: 10px 20px; font-size: 15px; font-weight: 500; color: #3c4043;" +
                "  font-family: inherit; cursor: pointer; margin-top: 1.5rem;" +
                "  transition: background 0.15s, box-shadow 0.15s; }" +
                ".btn-google:hover { background: #f8f9fa; box-shadow: 0 1px 3px rgba(0,0,0,0.12); }" +
                ".btn-google svg { width: 18px; height: 18px; flex-shrink: 0; }" +
                "</style>" +
                "<script>" +
                "var isRedirecting = false;" +
                "window.addEventListener('beforeunload', function() {" +
                "  if (!isRedirecting) {" +
                "    navigator.sendBeacon('http://localhost:8080/cancel', 'cancelled');" +
                "  }" +
                "});" +
                "function goToGoogle() {" +
                "  isRedirecting = true;" +
                "  window.location.href = '" + escapedUrl + "';" +
                "}" +
                "</script>" +
                "</head><body>" +
                "<div class='card'>" +
                googleLogo() +
                "<h1>Masuk ke Aplikasi</h1>" +
                "<p class='subtitle'>Klik tombol di bawah untuk melanjutkan<br>dengan akun Google Anda.</p>" +
                "<button class='btn-google' onclick='goToGoogle()'>" +
                "<svg viewBox='0 0 18 18' xmlns='http://www.w3.org/2000/svg'>" +
                "<path d='M17.64 9.2c0-.637-.057-1.251-.164-1.84H9v3.481h4.844c-.209 1.125-.843 2.078-1.796 2.717v2.258h2.908c1.702-1.567 2.684-3.875 2.684-6.615z' fill='#4285F4'/>"
                +
                "<path d='M9 18c2.43 0 4.467-.806 5.956-2.184l-2.908-2.258c-.806.54-1.837.86-3.048.86-2.344 0-4.328-1.584-5.036-3.711H.957v2.332A8.997 8.997 0 0 0 9 18z' fill='#34A853'/>"
                +
                "<path d='M3.964 10.707A5.41 5.41 0 0 1 3.682 9c0-.593.102-1.17.282-1.707V4.961H.957A8.996 8.996 0 0 0 0 9c0 1.452.348 2.827.957 4.039l3.007-2.332z' fill='#FBBC05'/>"
                +
                "<path d='M9 3.58c1.321 0 2.508.454 3.44 1.345l2.582-2.58C13.463.891 11.426 0 9 0A8.997 8.997 0 0 0 .957 4.961L3.964 7.293C4.672 5.163 6.656 3.58 9 3.58z' fill='#EA4335'/>"
                +
                "</svg>" +
                "Lanjutkan dengan Google" +
                "</button>" +
                "<p class='hint'>Tab ini akan otomatis selesai setelah login</p>" +
                "</div>" +
                "</body></html>";
    }

    private String buildSuccessHtml(String name, String picture) {
        String displayName = (name != null && !name.isEmpty()) ? name : "Pengguna";
        return "<!DOCTYPE html>" +
                "<html lang='id'><head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<meta http-equiv='Content-Security-Policy' content='img-src *'>" +
                "<title>Login Berhasil</title>" +
                "<link href='https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600&display=swap' rel='stylesheet'>"
                +
                buildCommonStyle() +
                "<script>window.onbeforeunload = null;</script>" +
                "</head><body>" +
                "<div class='card'>" +
                googleLogo() +
                "<div class='avatar-wrap'>" +
                "<img class='avatar' src='" + picture + "' alt='Foto Profil' " +
                "onerror=\"this.src='https://ui-avatars.com/api/?name=" + displayName +
                "&background=eafaf1&color=1a9e5c&size=100'\">" +
                "<div class='badge'>" +
                "<svg class='badge-svg' viewBox='0 0 36 36' xmlns='http://www.w3.org/2000/svg'>" +
                "<circle cx='18' cy='18' r='15'/>" +
                "<path d='M11 18l5 5 9-10'/>" +
                "</svg>" +
                "</div></div>" +
                "<h1>Halo, " + displayName + "!</h1>" +
                "<p class='subtitle'>Anda sudah masuk dengan akun Google.<br>Silakan kembali ke aplikasi.</p>" +
                "<p class='hint'>Tab ini aman untuk ditutup</p>" +
                "</div></body></html>";
    }

    private String buildErrorHtml(String title, String message) {
        return "<!DOCTYPE html>" +
                "<html lang='id'><head>" +
                "<meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "<title>" + title + "</title>" +
                "<link href='https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400;500;600&display=swap' rel='stylesheet'>"
                +
                buildCommonStyle() +
                "<style>.icon-error{font-size:48px;margin-bottom:1rem;} h1{color:#c0392b;}</style>" +
                "</head><body>" +
                "<div class='card'>" +
                googleLogo() +
                "<div class='icon-error'>&#x2715;</div>" +
                "<h1>" + title + "</h1>" +
                "<p class='subtitle'>" + message + "</p>" +
                "<p class='hint'>Tab ini aman untuk ditutup</p>" +
                "</div></body></html>";
    }

    private String buildCommonStyle() {
        return "<style>" +
                "*, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }" +
                "body { font-family: 'Plus Jakarta Sans', sans-serif; min-height: 100vh;" +
                "  display: flex; align-items: center; justify-content: center; background: #f4f4f0; }" +
                ".card { background: #fff; border: 1px solid rgba(0,0,0,0.08); border-radius: 20px;" +
                "  padding: 2.5rem 2rem; text-align: center; max-width: 360px; width: 90%; }" +
                ".g-logo { display: flex; align-items: center; justify-content: center; gap: 6px; margin-bottom: 1.5rem; }"
                +
                ".g-dot { width: 8px; height: 8px; border-radius: 50%; }" +
                ".avatar-wrap { position: relative; width: 100px; margin: 0 auto 1.25rem; }" +
                ".avatar { width: 100px; height: 100px; border-radius: 50%; object-fit: cover; display: block; border: 3px solid #eafaf1; }"
                +
                ".badge { position: absolute; bottom: 0; right: 0; width: 24px; height: 24px; border-radius: 50%; background: #eafaf1; display: flex; align-items: center; justify-content: center; border: 2px solid #fff; }"
                +
                ".badge-svg { width: 25px; height: 25px; }" +
                ".badge-svg circle { fill: none; stroke: #1a9e5c; stroke-width: 3; stroke-dasharray: 100; stroke-dashoffset: 100; animation: ring 1.5s ease forwards 1s; }"
                +
                ".badge-svg path { fill: none; stroke: #1a9e5c; stroke-width: 3; stroke-linecap: round; stroke-linejoin: round; stroke-dasharray: 50; stroke-dashoffset: 50; animation: check 1.4s ease forwards 1.6s; }"
                +
                "@keyframes ring { to { stroke-dashoffset: 0; } }" +
                "@keyframes check { to { stroke-dashoffset: 0; } }" +
                "h1 { font-size: 20px; font-weight: 600; color: #1a1a1a; margin-bottom: 0.5rem; }" +
                ".subtitle { font-size: 14px; color: #666; line-height: 1.6; margin-bottom: 0.5rem; }" +
                ".hint { font-size: 12px; color: #bbb; margin-top: 1rem; }" +
                "</style>";
    }

    private String googleLogo() {
        return "<div class='g-logo'>" +
                "<span class='g-dot' style='background:#4285F4'></span>" +
                "<span class='g-dot' style='background:#EA4335'></span>" +
                "<span class='g-dot' style='background:#FBBC05'></span>" +
                "<span class='g-dot' style='background:#34A853'></span>" +
                "</div>";
    }

    public static Credential getCredential() {
        return credential;
    }

    public static Credential loadCredential() throws Exception {

        InputStream in = GoogleAuthService.class.getResourceAsStream(
                "/google/credentials.json");

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                GsonFactory.getDefaultInstance(),
                new InputStreamReader(in));

        // GANTI dengan ini:
        String appData = System.getenv("APPDATA");
        File tokenFolder = (appData != null && !appData.isEmpty())
                ? new File(appData + "\\EnjoyCafe\\tokens")
                : new File(System.getProperty("user.home") + "/EnjoyCafe/tokens");
        if (!tokenFolder.exists())
            tokenFolder.mkdirs();
        FileDataStoreFactory dataStoreFactory = new FileDataStoreFactory(tokenFolder);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                new ApacheHttpTransport(),
                GsonFactory.getDefaultInstance(),
                clientSecrets,
                Arrays.asList(
                        "openid",
                        "email",
                        "profile",
                        DriveScopes.DRIVE_FILE))
                .setAccessType("offline")
                .setDataStoreFactory(dataStoreFactory)
                .build();

        return flow.loadCredential("user");
    }

}
