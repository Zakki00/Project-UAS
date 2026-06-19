package com.mycompany.projectuas;

import java.net.URL;
import java.security.MessageDigest;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.mycompany.Model.GoogleUser;
import com.mycompany.services.GoogleDriveService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * LoginController Package : com.mycompany.projectuas FXML :
 * resources/fxml/login.fxml
 */
public class LoginController implements Initializable {
    
    @FXML
    private Button btnGoogle;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordVisible;
    @FXML
    private CheckBox rememberMe;
    @FXML
    private Button loginBtn;
    @FXML
    private Button togglePasswordBtn;

    private boolean showingPassword = false;
    Preferences prefs = Preferences.userNodeForPackage(session.class);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadRememberedCredentials();
        setupform();

    }




    //=========================================
    // CEK APAKAH APLIKASI SUDAH MEMLIKI OWNER
    //=========================================
    GoogleUser googleUser = new GoogleUser();
    session session = new session();

    void setupform() {
        // Sync PasswordField <-> TextField untuk show/hide password
        passwordVisible.textProperty().bindBidirectional(passwordField.textProperty());
        loginBtn.setDefaultButton(true);

        // seleksi data
        String sql_admin = "SELECT * FROM tb_user WHERE role = 'Admin'";
        List<Object[]> admin = koneksi.ambilData(sql_admin);
        String Admin = prefs.get("Admin", null);
        if (Admin == null || admin.isEmpty()) {
            System.out.print("Admin Belum Di Daftarkan Sebagai Pemilik Aplikasi" + Admin);
            btnGoogle.setVisible(true);
            btnGoogle.setManaged(true);
            try {
                prefs.clear();
            } catch (BackingStoreException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Admin Sudah Daftarkan Sebagai Pemilik Aplikasi");
            btnGoogle.setVisible(false);
            btnGoogle.setManaged(false);
            session.id = (int) admin.get(0)[0];
            session.username = (String) admin.get(0)[1];
            session.nama = (String) admin.get(0)[3];
            session.role = (String) admin.get(0)[4];
            session.email = (String) admin.get(0)[5];
            googleUser.setProfilePictureUrl((String) admin.get(0)[6]);
        }

    }



    // ==========================================
    // HANDLE LOGIN MENGGUNAKAN GOOGLE
    // ==========================================

    @FXML
    private void onLoginGoogle() {
        Stage primaryStage = (Stage) btnGoogle.getScene().getWindow();

        Popup popupHelper = new Popup();
        Popup.LoginProgressDialog progressDialog = popupHelper.new LoginProgressDialog(primaryStage);

        progressDialog.show(

                user -> {
                    System.out.println("DEBUG: SUCCESS CALLBACK MASUK");
                    googleUser = user;

                    try {
                        GoogleDriveService service = new GoogleDriveService();
                        boolean restore = service.restoreBackupAll();
                        System.out.println("Restore = " + restore);
                        session.googleUser = user; // simpan ke session
                        String cekSql = "SELECT id_user, username, nama_lengkap, role, foto_profil email FROM tb_user WHERE email = ?";
                        List<Object[]> hasil = koneksi.ambilData(cekSql, user.getEmail());
                        System.out.println("data admin berdasarkan email" + hasil.size());

                        if (!hasil.isEmpty()) {
                            Object[] row = hasil.get(0);

                            session.id = (int) row[0];
                            session.username = (String) row[1];
                            session.nama = (String) row[2];
                            session.role = (String) row[3];
                            session.email = (String) row[4];
                            googleUser.setProfilePictureUrl((String) row[5]);
                            navigation nav = new navigation();
                            prefs.put("Admin", "Admin");
                            nav.navigateToDashboard();
                            Stage stage = (Stage) btnGoogle.getScene().getWindow();
                            stage.close();

                            // // ── Tampilkan popup sukses dengan nama user ─────────────────
                            popupHelper.showGoogleSuccessPopup("Selamat Datang Kembali",
                                    "Selamat datang Kembali, " + user.getName() + "!", user);

                            System.out.println("Google User: " + user.getEmail() + " - " + user.getName());
                        } else {

                            navigation nav = new navigation();
                            nav.navigateToSignup();
                            Stage stage = (Stage) btnGoogle.getScene().getWindow();
                            stage.close();

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        popupHelper.showModernPopup(
                                "Error",
                                "Gagal memverifikasi akun.",
                                Popup.PopupType.ERROR, primaryStage);
                    }
                },

                // Browser ditutup sebelum login selesai
                () -> {
                    popupHelper.showModernPopup(
                            "Login Dibatalkan",
                            "Browser ditutup sebelum login selesai.",
                            Popup.PopupType.WARNING, primaryStage);
                },

                // Timeout 120 detik
                () -> {
                    popupHelper.showModernPopup(
                            "Waktu Habis",
                            "Login tidak diselesaikan dalam 120 detik.",
                            Popup.PopupType.WARNING, primaryStage);
                });
    }


    //==========================================
    // HANDLE LOGIN UMUM
    //==========================================


    @FXML
    private void handleLogin(ActionEvent event) {

        String username = usernameField.getText().trim();
        String password = showingPassword
                ? passwordVisible.getText()
                : passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {

            Stage ownerStage = (Stage) loginBtn.getScene().getWindow();

            new Popup().showModernPopup(
                    "WARNING",
                    "Username dan Password tidak boleh kosong",
                    Popup.PopupType.WARNING,
                    ownerStage);

            return;
        }

        String hashedPassword = hashPassword(password);

        // ==========================
        // CEK TB_USER
        // ==========================
        String queryUser = """
                SELECT *
                FROM tb_user
                WHERE username = ? AND password = ?
                """;

        List<Object[]> resultUser = koneksi.ambilData(
                queryUser,
                username,
                hashedPassword);

        if (!resultUser.isEmpty()) {
            GoogleUser sesigoogle = new GoogleUser();

            session.id = resultUser.get(0)[0];
            session.username = (String) resultUser.get(0)[1];
            session.nama = (String) resultUser.get(0)[3];
            session.role = (String) resultUser.get(0)[4];
            session.email = (String) resultUser.get(0)[5];
            googleUser.setProfilePictureUrl((String) resultUser.get(0)[6]);

            //  isi semua data GoogleUser
            sesigoogle.setName((String) resultUser.get(0)[3]);
            sesigoogle.setEmail((String) resultUser.get(0)[5]);
            sesigoogle.setProfilePictureUrl((String) resultUser.get(0)[6]);

            session.googleUser = sesigoogle;
            if (rememberMe.isSelected()) {
                prefs.put("username", username);
                prefs.put("password", password);
                prefs.putBoolean("remember", true);
            } else {
                prefs.remove("username");
                prefs.remove("password");
                prefs.putBoolean("remember", false);
            }

            

            navigation nav = new navigation();
            nav.navigateToDashboard();
            Stage ownerStage = (Stage) loginBtn.getScene().getWindow();
            Popup popupHelper = new Popup();
            popupHelper.showGoogleSuccessPopup("Selamat Datang kembali",
                    "Selamat datang Kembali, " + session.nama + "!", sesigoogle);
            ownerStage.close();
            return;
        }

        // ==========================
        // CEK TB_KARYAWAN
        // ==========================
        String queryKaryawan = """
                SELECT *
                FROM tb_karyawan
                WHERE username = ?
                  AND password = ?
                  AND status_kerja = 'Aktif'
                """;

        List<Object[]> resultKaryawan = koneksi.ambilData(
                queryKaryawan,
                username,
                password);

        if (!resultKaryawan.isEmpty()) {

            session.id = resultKaryawan.get(0)[0];
            session.username = (String) resultKaryawan.get(0)[1];
            session.nama = (String) resultKaryawan.get(0)[3];
            session.role = (String) resultKaryawan.get(0)[9];
            session.email = "";

            if (rememberMe.isSelected()) {
                prefs.put("username", username);
                prefs.put("password", password);
                prefs.putBoolean("remember", true);
            } else {
                prefs.remove("username");
                prefs.remove("password");
                prefs.putBoolean("remember", false);
            }


            navigation nav = new navigation();
            nav.navigateToDashboard();
            Stage ownerStage = (Stage) loginBtn.getScene().getWindow();
            new Popup().showSuccessPopup("Selmat Datang " + session.username, "Selamat Datang Dan Selamat Bekerja! @" + session.nama);

            ownerStage.close();
            return;
        }

        // ==========================
        // GAGAL LOGIN
        // ==========================
        Stage ownerStage = (Stage) loginBtn.getScene().getWindow();

        new Popup().showModernPopup(
                "ERROR",
                "Username atau Password salah",
                Popup.PopupType.ERROR,
                ownerStage);
    }

    @FXML
    private void togglePasswordVisibility(ActionEvent event) {
        showingPassword = !showingPassword;
        if (showingPassword) {
            passwordField.setManaged(false);
            passwordField.setVisible(false);
            passwordVisible.setManaged(true);
            passwordVisible.setVisible(true);
            togglePasswordBtn.setText("🙈");
        } else {
            passwordVisible.setManaged(false);
            passwordVisible.setVisible(false);
            passwordField.setManaged(true);
            passwordField.setVisible(true);
            togglePasswordBtn.setText("👁");
        }
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Lupa Password",
                "Silakan hubungi administrator untuk mereset password Anda.");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void loadRememberedCredentials() {
        if (prefs.getBoolean("remember", false)) {
            usernameField.setText(prefs.get("username", ""));
            passwordField.setText(prefs.get("password", ""));
            rememberMe.setSelected(true);
        }
    }


    //=================================
    //HASHING PASWORD
    //=================================

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
