package com.mycompany.projectuas;

import java.net.URL;
import java.security.MessageDigest;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import java.util.prefs.Preferences;

public class LupaPasswordController implements Initializable {

    @FXML
    private ComboBox<String> cmbRole;
    @FXML
    private Label lblinput;
    @FXML
    private Label iconInput;
    @FXML
    private TextField tfinput;
    @FXML
    private TextField tfUsernameBaru;
    @FXML
    private PasswordField tfPasswordBaru;
    @FXML
    private TextField tfPasswordBaruVisible;
    @FXML
    private Button btnEyeBaru;
    @FXML
    private PasswordField tfKonfirmasi;
    @FXML
    private TextField tfKonfirmasiVisible;
    @FXML
    private Button btnEyeKonfirmasi;

    private boolean showPasswordBaru = false;
    private boolean showKonfirmasi = false;
    Preferences prefs = Preferences.userNodeForPackage(session.class);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbRole.setItems(FXCollections.observableArrayList("Admin", "Karyawan"));

        // Bind show/hide password
        tfPasswordBaruVisible.textProperty()
                .bindBidirectional(tfPasswordBaru.textProperty());
        tfKonfirmasiVisible.textProperty()
                .bindBidirectional(tfKonfirmasi.textProperty());

        cmbRole.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null)
                return;

            if (newVal.equals("Admin")) {
                lblinput.setText("Email Admin");
                tfinput.setPromptText("Masukkan email admin Anda");
                iconInput.setText("📧");
                // Hapus formatter agar email tidak diubah ke uppercase
                tfinput.setTextFormatter(null);
            } else {
                lblinput.setText("Nama Lengkap Karyawan");
                tfinput.setPromptText("Masukkan nama lengkap karyawan");
                iconInput.setText("👤");
                // Pasang formatter uppercase untuk nama karyawan
                tfinput.setTextFormatter(new TextFormatter<>(change -> {
                    change.setText(change.getText().toUpperCase());
                    return change;
                }));
            }

            bersihkanForm();
        });

    }

    @FXML
    private void handleReset(ActionEvent event) {
        Stage stage = (Stage) tfinput.getScene().getWindow();
        String role = cmbRole.getValue();
        String input = tfinput.getText().trim();
        String usernameBaru = tfUsernameBaru.getText().trim();
        String passwordBaru = tfPasswordBaru.getText();
        String konfirmasi = tfKonfirmasi.getText();

        // ── Validasi Role ──
        if (role == null) {
            new Popup().showModernPopup("WARNING", "Pilih role terlebih dahulu!",
                    Popup.PopupType.WARNING, stage);
            return;
        }

        // ── Validasi Input Pencarian ──
        if (input.isEmpty()) {
            String pesan = role.equals("Admin")
                    ? "Email tidak boleh kosong!"
                    : "Nama lengkap tidak boleh kosong!";
            new Popup().showModernPopup("WARNING", pesan,
                    Popup.PopupType.WARNING, stage);
            return;
        }

        // ── Validasi Username Baru ──
        if (usernameBaru.isEmpty()) {
            new Popup().showModernPopup("WARNING", "Username baru tidak boleh kosong!",
                    Popup.PopupType.WARNING, stage);
            return;
        }

        // ── Validasi Password Baru ──
        if (passwordBaru.isBlank()) {
            new Popup().showModernPopup("WARNING", "Password baru tidak boleh kosong!",
                    Popup.PopupType.WARNING, stage);
            return;
        }

        if (passwordBaru.length() < 6) {
            new Popup().showModernPopup("WARNING", "Password baru minimal 6 karakter!",
                    Popup.PopupType.WARNING, stage);
            return;
        }

        // ── Validasi Konfirmasi Password ──
        if (!passwordBaru.equals(konfirmasi)) {
            new Popup().showModernPopup("ERROR", "Konfirmasi password tidak cocok!",
                    Popup.PopupType.ERROR, stage);
            return;
        }

        // ── Proses Reset ──
        if (role.equals("Admin")) {
            resetAdmin(input, usernameBaru, passwordBaru, stage);
        } else {
            resetKaryawan(input, usernameBaru, passwordBaru, stage);
        }
    }

    private void resetAdmin(String email, String usernameBaru, String passwordBaru, Stage stage) {
        // Cek apakah email ada
        List<Object[]> data = koneksi.ambilData(
                "SELECT id_user FROM tb_user WHERE email = ?", email);

        if (data.isEmpty()) {
            new Popup().showModernPopup("ERROR", "Email admin tidak ditemukan!",
                    Popup.PopupType.ERROR, stage);
            return;
        }

        // Cek apakah username baru sudah dipakai akun lain
        List<Object[]> cekUsername = koneksi.ambilData(
                "SELECT id_user FROM tb_user WHERE username = ? AND email != ?",
                usernameBaru, email);

        if (!cekUsername.isEmpty()) {
            new Popup().showModernPopup("ERROR", "Username baru sudah digunakan akun lain!",
                    Popup.PopupType.ERROR, stage);
            return;
        }

        // Update username dan password
        koneksi.eksekusiQuery(
                "UPDATE tb_user SET username = ?, password = ? WHERE email = ?",
                usernameBaru, hashPassword(passwordBaru), email);

        new Popup().showModernPopup("SUCCESS",
                "Username dan password admin berhasil direset!",
                Popup.PopupType.SUCCESS, stage);
        bersihkanForm();

        prefs.put("username", usernameBaru);
        prefs.put("password", passwordBaru);
    }

    private void resetKaryawan(String nama, String usernameBaru, String passwordBaru, Stage stage) {
        // Cek apakah nama ada
        List<Object[]> data = koneksi.ambilData(
                "SELECT id_karyawan FROM tb_karyawan WHERE nama_lengkap = ? AND status_kerja = 'Aktif'",
                nama);

        if (data.isEmpty()) {
            new Popup().showModernPopup("ERROR",
                    "Nama karyawan tidak ditemukan atau tidak aktif!",
                    Popup.PopupType.ERROR, stage);
            return;
        }

        // Cek apakah username baru sudah dipakai karyawan lain
        List<Object[]> cekUsername = koneksi.ambilData(
                "SELECT id_karyawan FROM tb_karyawan WHERE username = ? AND nama_lengkap != ?",
                usernameBaru, nama);

        if (!cekUsername.isEmpty()) {
            new Popup().showModernPopup("ERROR", "Username baru sudah digunakan karyawan lain!",
                    Popup.PopupType.ERROR, stage);
            return;
        }

        // Update username dan password
        koneksi.eksekusiQuery(
                "UPDATE tb_karyawan SET username = ?, password = ? WHERE nama_lengkap = ?",
                usernameBaru, passwordBaru, nama);

        new Popup().showModernPopup("SUCCESS",
                "Username dan password karyawan berhasil direset!",
                Popup.PopupType.SUCCESS, stage);
        bersihkanForm();

        prefs.put("username", usernameBaru);
        prefs.put("password", passwordBaru);
    }

    @FXML
    private void togglePasswordBaru() {
        showPasswordBaru = !showPasswordBaru;
        if (showPasswordBaru) {
            tfPasswordBaru.setManaged(false);
            tfPasswordBaru.setVisible(false);
            tfPasswordBaruVisible.setManaged(true);
            tfPasswordBaruVisible.setVisible(true);
            btnEyeBaru.setText("🙈");
        } else {
            tfPasswordBaruVisible.setManaged(false);
            tfPasswordBaruVisible.setVisible(false);
            tfPasswordBaru.setManaged(true);
            tfPasswordBaru.setVisible(true);
            btnEyeBaru.setText("👁");
        }
    }

    @FXML
    private void toggleKonfirmasi() {
        showKonfirmasi = !showKonfirmasi;
        if (showKonfirmasi) {
            tfKonfirmasi.setManaged(false);
            tfKonfirmasi.setVisible(false);
            tfKonfirmasiVisible.setManaged(true);
            tfKonfirmasiVisible.setVisible(true);
            btnEyeKonfirmasi.setText("🙈");
        } else {
            tfKonfirmasiVisible.setManaged(false);
            tfKonfirmasiVisible.setVisible(false);
            tfKonfirmasi.setManaged(true);
            tfKonfirmasi.setVisible(true);
            btnEyeKonfirmasi.setText("👁");
        }
    }

    @FXML
    private void handleKembali(ActionEvent event) {
        new navigation().navigateToLogin();
        ((Stage) tfinput.getScene().getWindow()).close();
    }

    private void bersihkanForm() {
        tfinput.clear();
        tfUsernameBaru.clear();
        tfPasswordBaru.clear();
        tfKonfirmasi.clear();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash)
                hex.append(String.format("%02x", b));
            return hex.toString();
        } catch (Exception e) {
            return password;
        }
    }
}