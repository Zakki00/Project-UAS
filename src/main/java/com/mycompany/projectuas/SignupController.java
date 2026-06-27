package com.mycompany.projectuas;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import com.mycompany.Model.GoogleUser;
import com.mycompany.services.GoogleDriveService;
import java.io.File;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SignupController implements Initializable {

    // ── FXML refs ──────────────────────────────────

    @FXML
    private TextField tfnama;
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private HBox boxPassword;
    @FXML
    private Button btnShowPass;
    @FXML
    private Button loginBtn;

    @FXML
    private Hyperlink lnkLogin;
    @FXML
    private HBox boxmasuksekarang;
    @FXML
    private Label errnama;
    @FXML
    private Label errUsername;
    @FXML
    private Label errPassword;

    // ── State ──────────────────────────────────────
    private boolean passwordVisible = false;
    private TextField tfPasswordVisible; // untuk show/hide password
    Preferences prefs = Preferences.userNodeForPackage(session.class);
    koneksi db = new koneksi();

    // ═══════════════════════════════════════════════
    // INITIALIZE
    // ═══════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // bersihkan error saat user mulai ketik
        setupform();

    }

    void setupform() {
        String sql_admin = "SELECT * tb_user WHERE role = Admin";
        if (koneksi.ambilData(sql_admin).isEmpty()) {
            boxmasuksekarang.setVisible(false);
            boxmasuksekarang.setManaged(false);
        } else {
            boxmasuksekarang.setVisible(true);
            boxmasuksekarang.setManaged(true);
        }

        tfnama.setText(session.googleUser.getName());
        tfUsername.textProperty().addListener((o, ov, nv) -> clearError(errnama));
        tfUsername.textProperty().addListener((o, ov, nv) -> clearError(errUsername));
        pfPassword.textProperty().addListener((o, ov, nv) -> clearError(errPassword));

        // ===logo
        Platform.runLater(() -> {
            Stage stage = (Stage) btnShowPass.getScene().getWindow();
            Image icon = new Image(getClass().getResourceAsStream("/image/Logo.png"));
            stage.getIcons().add(icon);
        });
        try {
            resetDatabase();
        } catch (Exception e) {
            System.err.println("Gagal reset database: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════
    // TOGGLE PASSWORD VISIBILITY
    // ═══════════════════════════════════════════════
    @FXML
    private void onTogglePassword() {
        passwordVisible = !passwordVisible;

        if (passwordVisible) {
            // ganti PasswordField → TextField
            if (tfPasswordVisible == null) {
                tfPasswordVisible = new TextField();
                tfPasswordVisible.getStyleClass().add("field-input");
                tfPasswordVisible.setPromptText("Masukkan password...");
                javafx.scene.layout.HBox.setHgrow(
                        tfPasswordVisible,
                        javafx.scene.layout.Priority.ALWAYS);
            }
            tfPasswordVisible.setText(pfPassword.getText());
            // bind dua arah
            tfPasswordVisible.textProperty().addListener((o, ov, nv) -> pfPassword.setText(nv));

            // ganti node di HBox
            boxPassword.getChildren().remove(pfPassword);
            boxPassword.getChildren().add(1, tfPasswordVisible);
            tfPasswordVisible.requestFocus();
            btnShowPass.setText("🙈");
        } else {
            pfPassword.setText(tfPasswordVisible != null
                    ? tfPasswordVisible.getText()
                    : "");
            boxPassword.getChildren().remove(tfPasswordVisible);
            boxPassword.getChildren().add(1, pfPassword);
            pfPassword.requestFocus();
            btnShowPass.setText("👁");
        }
    }

    // ═══════════════════════════════════════════════
    // REGISTER ACCOUNT
    // ═══════════════════════════════════════════════
    @FXML
    private void handleLogin() {
        GoogleUser googleUser = session.googleUser; // ← ini yang bener
        String nama = tfnama.getText().trim();
        String username = tfUsername.getText().trim();
        String password = pfPassword.getText().trim();
        Stage stage = (Stage) btnShowPass.getScene().getWindow();

        // VALIDASI NAMA
        if (nama.isEmpty() || nama.equals("Masukkan nama...")) {
            new Popup().showModernPopup(
                    "WARNING",
                    "Nama tidak boleh kosong!",
                    Popup.PopupType.WARNING,
                    stage);
            return;
        }

        // VALIDASI USERNAME
        if (username.isEmpty() || username.equals("Masukkan username...")) {
            new Popup().showModernPopup(
                    "WARNING",
                    "Username tidak boleh kosong!",
                    Popup.PopupType.WARNING,
                    stage);
            return;
        }

        String cekSql = "SELECT id_user FROM tb_user WHERE username = ?";
        if (!koneksi.ambilData(cekSql, username).isEmpty()) {
            new Popup().showModernPopup(
                    "WARNING",
                    "Username sudah digunakan",
                    Popup.PopupType.WARNING,
                    stage);
            return;
        }

        if (username.length() < 4) {
            new Popup().showModernPopup(
                    "WARNING",
                    "Username minimal 4 karakter!",
                    Popup.PopupType.WARNING,
                    stage);
            return;
        }

        if (!username.matches("[a-zA-Z0-9_]+")) {
            new Popup().showModernPopup(
                    "WARNING",
                    "Username hanya boleh huruf, angka, dan underscore",
                    Popup.PopupType.WARNING,
                    stage);
            return;
        }

        // VALIDASI PASSWORD
        if (password.isEmpty() || password.equals("Masukkan password...")) {
            new Popup().showModernPopup(
                    "WARNING",
                    "Password tidak boleh kosong!",
                    Popup.PopupType.WARNING,
                    stage);
            return;
        }

        if (password.length() < 6) {
            new Popup().showModernPopup(
                    "WARNING",
                    "Password minimal 6 karakter!",
                    Popup.PopupType.WARNING,
                    stage);
            return;
        }

        if (!password.matches(".*[A-Z].*")) {
            new Popup().showModernPopup(
                    "WARNING",
                    "Password harus mengandung huruf besar!",
                    Popup.PopupType.WARNING,
                    stage);
            return;
        }

        if (!password.matches(".*[a-z].*")) {
            new Popup().showModernPopup(
                    "WARNING",
                    "Password harus mengandung huruf kecil!",
                    Popup.PopupType.WARNING,
                    stage);
            return;
        }

        if (!password.matches(".*\\d.*")) {
            new Popup().showModernPopup(
                    "WARNING",
                    "Password harus mengandung angka!",
                    Popup.PopupType.WARNING,
                    stage);
            return;
        }

        Stage primaryStage = (Stage) tfUsername.getScene().getWindow();
        Popup popupHelper = new Popup();
        String role = "Admin";
        prefs.put("Admin", role);

        try {
           

            String sql = "INSERT INTO tb_user (username, password, nama_lengkap, role, email, foto_profil) VALUES (?, ?, ?, ?, ?, ?)";
            koneksi.eksekusiQuery(sql,
                    username,
                    hashPassword(password),
                    nama,
                    role,
                    googleUser.getEmail(),
                    googleUser.getProfilePictureUrl());

            prefs.put("username", username);
            prefs.put("password", password);
            prefs.putBoolean("remember", true);

            session.role = role;
            session.email = googleUser.getEmail();
            session.username = username;
            session.nama = nama;
            session.googleUser.setProfilePictureUrl(googleUser.getProfilePictureUrl());

            Stage loadingStage = showLoadingOverlay(stage);

            javafx.concurrent.Task<Void> uploadTask = new javafx.concurrent.Task<>() {
                @Override
                protected Void call() throws Exception {
                    new GoogleDriveService().uploadBackupAll();
                    return null;
                }
            };

            Runnable onDone = () -> {
                loadingStage.close();
                new navigation().navigateToDashboard();
                new Popup().showGoogleSuccessPopup("Selamat Datang", "Selamat Datang " + googleUser.getName(),
                        googleUser);
                stage.close();
            };

            uploadTask.setOnSucceeded(e -> onDone.run());
            uploadTask.setOnFailed(e -> onDone.run());

            Thread t = new Thread(uploadTask);
            t.setDaemon(true);
            t.start();

        } catch (Exception e) {
            e.printStackTrace();
            popupHelper.showModernPopup("Error", "Gagal menyimpan akun.", Popup.PopupType.ERROR, primaryStage);
        }
    }

    // ═══════════════════════════════════════════════
    // GO TO LOGIN
    // ═══════════════════════════════════════════════
    @FXML
    private void onGoLogin() {
        goToLogin();
    }

    private void goToLogin() {
        navigation nav = new navigation();
        nav.navigateToLogin();
        Stage stage = (Stage) tfUsername.getScene().getWindow();
        stage.close();
    }

    // ═══════════════════════════════════════════════
    // HELPERS
    // ═══════════════════════════════════════════════
    private void clearError(Label lbl) {
        lbl.setVisible(false);
        lbl.setManaged(false);
    }

    // Hash password pakai SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            return password; // fallback tanpa hash
        }
    }

    // ── Buat loading stage ──────────────────────────────────────────
    private Stage showLoadingOverlay(Stage owner) {
        Stage loadingStage = new Stage();
        loadingStage.initOwner(owner);
        loadingStage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
        loadingStage.initStyle(javafx.stage.StageStyle.TRANSPARENT);

        javafx.scene.control.ProgressIndicator spinner = new javafx.scene.control.ProgressIndicator(-1);
        spinner.setPrefSize(52, 52);
        spinner.setStyle("-fx-progress-color: #6C63FF; -fx-background-color: transparent;");

        javafx.scene.control.Label d1 = makeDot("#6C63FF");
        javafx.scene.control.Label d2 = makeDot("#00D4FF");
        javafx.scene.control.Label d3 = makeDot("#00E5A0");
        javafx.scene.layout.HBox dots = new javafx.scene.layout.HBox(8, d1, d2, d3);
        dots.setAlignment(javafx.geometry.Pos.CENTER);
        animateDot(d1, 0);
        animateDot(d2, 200);
        animateDot(d3, 400);

        javafx.scene.control.Label lbl = new javafx.scene.control.Label("Mohon tunggu sebentar...");
        lbl.setStyle("-fx-text-fill: #8B8FA8; -fx-font-size: 14px;");

        javafx.scene.layout.VBox card = new javafx.scene.layout.VBox(20, spinner, dots, lbl);
        card.setAlignment(javafx.geometry.Pos.CENTER);
        card.setStyle(
                "-fx-background-color: #1A1D2E;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: #2E3250;" +
                        "-fx-border-radius: 16;" +
                        "-fx-border-width: 1;" +
                        "-fx-padding: 40 56 40 56;");

        javafx.scene.Scene scene = new javafx.scene.Scene(card);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        loadingStage.setScene(scene);

        // Posisikan di tengah window owner
        loadingStage.setOnShown(e -> {
            loadingStage.setX(owner.getX() + (owner.getWidth() - loadingStage.getWidth()) / 2);
            loadingStage.setY(owner.getY() + (owner.getHeight() - loadingStage.getHeight()) / 2);
        });

        loadingStage.show();
        return loadingStage;
    }

    private javafx.scene.control.Label makeDot(String color) {
        javafx.scene.control.Label dot = new javafx.scene.control.Label();
        dot.setPrefSize(8, 8);
        dot.setMinSize(8, 8);
        dot.setMaxSize(8, 8);
        dot.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-background-radius: 4;" +
                        "-fx-opacity: 0.4;");
        return dot;
    }

    private void animateDot(javafx.scene.control.Label dot, int delayMs) {
        javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(
                javafx.util.Duration.millis(600), dot);
        ft.setFromValue(0.4);
        ft.setToValue(1.0);
        ft.setAutoReverse(true);
        ft.setCycleCount(javafx.animation.Animation.INDEFINITE);
        ft.setDelay(javafx.util.Duration.millis(delayMs));
        ft.play();
    }

    private void resetDatabase() throws Exception {
        String appData = System.getenv("APPDATA");
        File dbFile = new File(appData + "\\EnjoyCafe\\db\\db_enjoy_cafe.db");

        // 1. Paksa tutup semua koneksi SQLite aktif dulu
        closeAllSQLiteConnections();


        // 2. Hapus file lama
        if (dbFile.exists()) {
            boolean deleted = dbFile.delete();
            System.out.println("DB lama dihapus: " + deleted + " | path: " + dbFile.getAbsolutePath());

            // Kalau gagal dihapus (masih dilock), tunggu sebentar dan coba lagi
            if (!deleted) {
                Thread.sleep(500);
                deleted = dbFile.delete();
                System.out.println("Retry hapus DB: " + deleted);
            }
        }

        // 3. Pastikan folder ada
        if (!dbFile.getParentFile().exists()) {
            dbFile.getParentFile().mkdirs();
        }

        // 4. Copy db fresh dari dalam JAR/resources
        try (InputStream in = getClass().getResourceAsStream("/db/db_enjoy_cafe.db");
                OutputStream out = new FileOutputStream(dbFile)) {
            if (in == null)
                throw new Exception("db_enjoy_cafe.db tidak ditemukan di resources!");
            in.transferTo(out);
            System.out.println("DB fresh berhasil disalin ke: " + dbFile.getAbsolutePath());
        }
    }

    private void closeAllSQLiteConnections() {
        try {
            // Coba ambil koneksi baru lalu langsung tutup — ini memaksa
            // connection pool SQLite flush semua handle lama
            try (Connection conn = koneksi.getConnection()) {
                // jalankan PRAGMA untuk memastikan semua write selesai
                conn.createStatement().execute("PRAGMA wal_checkpoint(FULL)");
                System.out.println("SQLite checkpoint selesai");
            }
        } catch (Exception e) {
            System.out.println("closeAllSQLiteConnections: " + e.getMessage());
        }
    }
}