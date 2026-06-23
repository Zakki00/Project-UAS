package com.mycompany.projectuas;

import java.net.URL;
import java.security.MessageDigest;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import com.mycompany.Model.GoogleUser;
import com.mycompany.services.AutoBackupService;
import com.mycompany.services.GoogleDriveService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
        // VALIDASI NAMA
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
            String sql = "INSERT INTO tb_user (username, password, nama_lengkap, role,email,foto_profil) VALUES (?, ?, ?, ?, ?,?)";
            koneksi.eksekusiQuery(sql,
                    username,
                    hashPassword(password),
                    nama,
                    role,
                    googleUser.getEmail(),
                    googleUser.getProfilePictureUrl());

            

            System.out.println("Google User: " + googleUser.getEmail() + " - " + googleUser.getName());
            prefs.put("username", username);
            prefs.put("password", password);
            prefs.putBoolean("remember", true);

            //---melakukan backup
            GoogleDriveService googleDriveService = new GoogleDriveService();
            googleDriveService.uploadBackupAll();

            //---set sesion
            session.role = role;
            session.email = googleUser.getEmail();
            session.username = username;
            session.nama = nama;
            session.googleUser.setProfilePictureUrl(googleUser.getProfilePictureUrl());

            //---navigasi ke dashbord---
            navigation nav = new navigation();
            nav.navigateToDashboard();
            popupHelper.showGoogleSuccessPopup("Selamat Datang", "Selamat Datang " + googleUser.getName(), googleUser);
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
            popupHelper.showModernPopup("Error", "Gagal menyimpan akun.", Popup.PopupType.ERROR,
                    primaryStage);
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
    private void showError(Label lbl, String msg) {
        lbl.setText(msg);
        lbl.setVisible(true);
        lbl.setManaged(true);
    }

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
}