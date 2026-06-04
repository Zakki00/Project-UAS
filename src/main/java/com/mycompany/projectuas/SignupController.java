package com.mycompany.projectuas;

import java.net.URL;
import java.security.MessageDigest;
import java.util.ResourceBundle;

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
    private TextField tfNamaLengkap;
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private HBox boxPassword;
    @FXML
    private Button btnShowPass;
    @FXML
    private Button btnDaftar;
    @FXML
    private Button btnGoogle;
    @FXML
    private Hyperlink lnkLogin;

    @FXML
    private Label errNama;
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
        tfNamaLengkap.textProperty().addListener((o, ov, nv) -> clearError(errNama));
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
    // DAFTAR
    // ═══════════════════════════════════════════════
    @FXML
    private void onDaftar() {
        if (!validasi())
            return;

        String nama = tfNamaLengkap.getText().trim();
        String username = tfUsername.getText().trim();
        String password = hashPassword(pfPassword.getText().trim());

        // cek username sudah dipakai
        String cekSql = "SELECT id_user FROM tb_user WHERE username = '"
                + username + "'";
        if (!koneksi.ambilData(cekSql).isEmpty()) {
            showError(errUsername, "Username sudah digunakan!");
            return;
        }

        // simpan ke database
        String sql = "INSERT INTO tb_user (username, password, nama_lengkap) "
                + "VALUES ('" + username + "', '"
                + password + "', '" + nama + "')";

        try {
            koneksi.eksekusiQuery(sql);
            showAlert("Berhasil", "Akun berhasil dibuat! Silakan login.");
            goToLogin();
        } catch (Exception e) {
            showAlert("Gagal", "Terjadi kesalahan: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════
    // LOGIN WITH GOOGLE
    // ═══════════════════════════════════════════════
    @FXML
    private void onLoginGoogle() {
        // TODO: implementasi OAuth Google
        // untuk sekarang tampilkan info
        showAlert("Google Sign-In",
                "Fitur login dengan Google akan segera hadir!");
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
        Stage stage = (Stage) btnDaftar.getScene().getWindow();
        stage.close();
    }

    // ═══════════════════════════════════════════════
    // VALIDASI
    // ═══════════════════════════════════════════════
    private boolean validasi() {
        boolean valid = true;

        String nama = tfNamaLengkap.getText().trim();
        String username = tfUsername.getText().trim();
        String password = pfPassword.getText().trim();

        // nama lengkap
        if (nama.isEmpty()) {
            showError(errNama, "Nama lengkap tidak boleh kosong!");
            valid = false;
        } else if (nama.length() < 3) {
            showError(errNama, "Nama minimal 3 karakter!");
            valid = false;
        }

        // username
        if (username.isEmpty()) {
            showError(errUsername, "Username tidak boleh kosong!");
            valid = false;
        } else if (username.length() < 4) {
            showError(errUsername, "Username minimal 4 karakter!");
            valid = false;
        } else if (!username.matches("[a-zA-Z0-9_]+")) {
            showError(errUsername, "Username hanya boleh huruf, angka, dan _");
            valid = false;
        }

        // password
        if (password.isEmpty()) {
            showError(errPassword, "Password tidak boleh kosong!");
            valid = false;
        } else if (password.length() < 6) {
            showError(errPassword, "Password minimal 6 karakter!");
            valid = false;
        }

        return valid;
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