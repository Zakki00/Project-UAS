package com.mycompany.projectuas;

import java.net.URL;
import java.security.MessageDigest;
import java.util.ResourceBundle;

import com.mycompany.Model.GoogleUser;

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
       

        if (session.id_user == 0) {
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
        GoogleUser googleUser = new GoogleUser();
        String nama = tfnama.getText().trim();
        String username = tfUsername.getText().trim();
        String password = pfPassword.getText().trim();
        // VALIDASI NAMA
        if (nama.isEmpty() || username.equals("Masukkan username...")) {
            showError(errnama, "Username tidak boleh kosong!");
            return;
        }
        // VALIDASI USERNAME
        if (username.isEmpty() || username.equals("Masukkan username...")) {
            showError(errUsername, "Username tidak boleh kosong!");
            return;
        }
        String cekSql = "SELECT id_user FROM tb_user WHERE username = ?";
        if (!koneksi.ambilData(cekSql, username).isEmpty()) {
            showError(errUsername, "Username sudah digunakan");
            return;
        }

        if (username.length() < 4) {
            showError(errUsername, "Username minimal 4 karakter!");
            return;
        }

        if (!username.matches("[a-zA-Z0-9_]+")) {
            showError(errUsername, "Username hanya boleh huruf, angka, dan underscore");
            return;
        }

        // VALIDASI PASSWORD
        if (password.isEmpty() || password.equals("Masukkan password...")) {
            showError(errPassword, "Password tidak boleh kosong!");
            return;
        }

        if (password.length() < 6) {
            showError(errPassword, "Password minimal 6 karakter!");
            return;
        }

        if (!password.matches(".*[A-Z].*")) {
            showError(errPassword, "Password harus mengandung huruf besar!");
            return;
        }

        if (!password.matches(".*[a-z].*")) {
            showError(errPassword, "Password harus mengandung huruf kecil!");
            return;
        }

        if (!password.matches(".*\\d.*")) {
            showError(errPassword, "Password harus mengandung angka!");
            return;
        }

        Stage primaryStage = (Stage) tfUsername.getScene().getWindow();
        Popup popupHelper = new Popup();
        String role = "Admin";

        try {
            String sql = "INSERT INTO tb_user (username, password, nama_lengkap, role,email) VALUES (?, ?, ?, ?, ?)";
            koneksi.eksekusiQuery(sql,
                    username,
                    hashPassword(password),
                    nama,
                    role,
                    googleUser.getEmail()
                );

            popupHelper.showModernPopup("Akun Dibuat", "Akun Anda berhasil didaftarkan.", Popup.PopupType.SUCCESS,
                    primaryStage);
            goToLogin();
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

    private void showAlert(String title, String msg) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
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