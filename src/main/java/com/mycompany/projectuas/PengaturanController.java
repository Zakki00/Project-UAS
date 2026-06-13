/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectuas;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.mycompany.services.GoogleDriveService;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PopupControl;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.Circle;

/**
 * FXML Controller class
 *
 * @author zakki mubarroq
 */
public class PengaturanController implements Initializable {

    // ── FXML refs ──────────────────────────
    @FXML
    private Button tabAkun;
    @FXML
    private Button tabBackup;
    @FXML
    private Button tabDatabase;
    @FXML
    private Button tabTentang;

    @FXML
    private VBox panelAkun;
    @FXML
    private VBox panelBackup;
    @FXML
    private VBox panelDatabase;
    @FXML
    private VBox panelTentang;

    // Akun
    @FXML
    private Label lblAvatarInisial;
    @FXML
    private Label lblAvatarInisial1;
    @FXML
    private Label lblNamaAkun;
    @FXML
    private Label lblUsernameAkun;
    @FXML
    private Label lblRoleAkun;
    @FXML
    private Label lblEmailGoogle;
    @FXML
    private Label lblEmailGoogle2;
    @FXML
    private Button EditProfil;
    @FXML
    private VBox formEditAkun;
    @FXML
    private TextField tfEditNama;
    @FXML
    private TextField tfEditUsername;
    @FXML
    private javafx.scene.control.PasswordField pfEditPassword;
    @FXML
    private VBox cardAkunGoogle;
    // Backup
    @FXML
    private Label lblBackupTerakhir;
    @FXML
    private VBox backupProgressBox;
    @FXML
    private Button btnRestore;
    @FXML
    private Label lblBackupStatus;
    @FXML
    private ProgressBar pbBackup;
    @FXML
    private ProgressBar pbRestore;

    @FXML
    private javafx.scene.control.CheckBox cbBackupOtomatis;
    @FXML
    private javafx.scene.control.ComboBox<String> cbInterval;
    @FXML
    private TextField tfLokasiBackup;
    @FXML
    private TextField tfFileRestore;

    // Database
    @FXML
    private Label lblDbDot;
    @FXML
    private Label lblDbStatus;
    @FXML
    private TextField tfDbHost;
    @FXML
    private TextField tfDbPort;
    @FXML
    private TextField tfDbName;
    @FXML
    private TextField tfDbUser;
    @FXML
    private javafx.scene.control.PasswordField pfDbPassword;
    // ── Sidebar ───────────────────────────────────────────
    @FXML
    private VBox sidebar;
    @FXML
    private HBox logoRow;
    @FXML
    private ImageView imgAvatarGoogle;
    @FXML
    ImageView imgAvatarGoogle1;
    @FXML
    private VBox logoBrand;
    @FXML
    private VBox userInfo;
    @FXML
    private HBox userRow;
    @FXML
    private Button toggleBtn;
    @FXML
    private Button btnSimpanSeting;
    @FXML
    private VBox navMenu;

    // Nav items
    @FXML
    private HBox navDashboard;
    @FXML
    private HBox navProduk;
    @FXML
    private HBox navKasir;
    @FXML
    private HBox navLaporan;
    @FXML
    private HBox navPiutang;
    @FXML
    private HBox navPengaturan;

    // Nav labels (semua label teks nav)
    @FXML
    private Label navLblDashboard;
    @FXML
    private Label navLblProduk;
    @FXML
    private Label navLblKasir;
    @FXML
    private Label navLblPelanggan;
    @FXML
    private Label navLblLaporan;
    @FXML
    private Label navLblPengaturan;

    // ── Charts ────────────────────────────────────────────
    @FXML
    private AreaChart<String, Number> salesChart;
    @FXML
    private BarChart<String, Number> trxChart;
    @FXML
    private LineChart<String, Number> monthChart;

    // ── Stock list ────────────────────────────────────────
    @FXML
    private VBox stockList;

    // ── State ─────────────────────────────────────────────
    private boolean sidebarCollapsed = false;
    private static final double SIDEBAR_FULL = 220;
    private static final double SIDEBAR_MINI = 60;

    Preferences prefs = Preferences.userNodeForPackage(session.class);
    private Timeline autoBackupTimeline;

    // ═════════════════════════════════════════════════════
    // INITIALIZE
    // ═════════════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupPengaturan();
        setupNavHover();
        setActiveNav(navPengaturan);
        setupForm();
        loadLastBackupTime();

    }

    // ═════════════════════════════════════════════════════
    // SIDEBAR TOGGLE (animasi smooth)
    // ═════════════════════════════════════════════════════
    @FXML
    private void onToggleSidebar() {
        sidebarCollapsed = !sidebarCollapsed;
        double targetWidth = sidebarCollapsed ? SIDEBAR_MINI : SIDEBAR_FULL;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(sidebar.prefWidthProperty(), sidebar.getPrefWidth()),
                        new KeyValue(sidebar.minWidthProperty(), sidebar.getMinWidth())),
                new KeyFrame(Duration.millis(350), new KeyValue(sidebar.prefWidthProperty(), targetWidth),
                        new KeyValue(sidebar.minWidthProperty(), targetWidth)));
        if (sidebarCollapsed) {
            hideSidebarText();
            toggleBtn.setText("▶");
            logoRow.setAlignment(Pos.CENTER);
            logoRow.setPadding(new Insets(18, 0, 18, 0));
            userRow.setAlignment(Pos.CENTER);
            userRow.setPadding(new Insets(12, 0, 12, 0));
        } else {
            timeline.setOnFinished(e -> {
                showSidebarText();
                logoRow.setAlignment(Pos.CENTER_LEFT);
                logoRow.setPadding(new Insets(18, 16, 18, 16));
                userRow.setAlignment(Pos.CENTER_LEFT);
                userRow.setPadding(new Insets(12, 16, 12, 16));
            });
            toggleBtn.setText("◀");
        }
        updateNavPadding(sidebarCollapsed);
        timeline.play();
    }

    private void hideSidebarText() {
        logoBrand.setVisible(false);
        logoBrand.setManaged(false);
        userInfo.setVisible(false);
        userInfo.setManaged(false);
        setNavLabelsVisible(false);
    }

    private void showSidebarText() {
        logoBrand.setVisible(true);
        logoBrand.setManaged(true);
        userInfo.setVisible(true);
        userInfo.setManaged(true);
        setNavLabelsVisible(true);
    }

    private void setNavLabelsVisible(boolean visible) {
        List<Label> labels = List.of(navLblDashboard, navLblProduk, navLblKasir, navLblPelanggan, navLblLaporan,
                navLblPengaturan);
        for (Label lbl : labels) {
            lbl.setVisible(visible);
            lbl.setManaged(visible);
        }
    }

    private void updateNavPadding(boolean collapsed) {
        Insets collapsedPad = new Insets(10, 0, 10, 0);
        Insets normalPad = new Insets(10, 14, 10, 0);
        Insets pad = collapsed ? collapsedPad : normalPad;

        List<HBox> items = List.of(navDashboard, navProduk, navKasir, navLaporan, navPengaturan);
        for (HBox item : items) {
            item.setAlignment(collapsed ? Pos.CENTER : Pos.CENTER_LEFT);
            item.setPadding(pad);

        }
    }

    // ═════════════════════════════════════════════════════
    // NAV CLICK HANDLERS
    // ═════════════════════════════════════════════════════
    @FXML
    private void onNavDashboard() {
        setActiveNav(navDashboard);
        navigation nav = new navigation();
        nav.navigateToDashboard();
        Stage stage = (Stage) navDashboard.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void onNavProduk() {
        setActiveNav(navProduk);
        navigation nav = new navigation();
        nav.navigateToProduk();
        Stage stage = (Stage) navProduk.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onNavKasir() {
        setActiveNav(navKasir);
        navigation nav = new navigation();
        nav.navigateToTransaksi();
        Stage stage = (Stage) navKasir.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onNavLaporan() {
        setActiveNav(navLaporan);
        navigation nav = new navigation();
        nav.navigateToLaporan();
        Stage stage = (Stage) navLaporan.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onNavPiutang() {
        setActiveNav(navPiutang);
        navigation nav = new navigation();
        nav.navigateToPiutang();
        Stage stage = (Stage) navPiutang.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onNavPengaturan() {
        setActiveNav(navPengaturan);

    }

    private void setActiveNav(HBox selected) {
        List<HBox> all = List.of(navDashboard, navProduk, navKasir, navLaporan, navPengaturan);
        for (HBox item : all) {
            item.getStyleClass().removeAll("nav-active");
            if (!item.getStyleClass().contains("nav-item")) {
                item.getStyleClass().add("nav-item");
            }
        }
        selected.getStyleClass().add("nav-active");
    }

    private void setupNavHover() {
        List<HBox> all = List.of(navDashboard, navProduk, navKasir, navLaporan, navPengaturan);
        for (HBox item : all) {
            item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: #252840; -fx-background-radius: 10;"));
            item.setOnMouseExited(e -> item.setStyle(""));
        }
    }

    // ========================================
    // MAIN CONTENT
    // ========================================
    koneksi db = new koneksi();

    // ── setup di initialize() ──────────────
    private void setupPengaturan() {
        cbInterval.setItems(FXCollections.observableArrayList(
                "24 Jam", "3 Hari", "7 Hari"));

        // Load dari preferences
        boolean backupAktif = prefs.getBoolean("backup_otomatis", false);
        String interval = prefs.get("backup_interval", "24 Jam");

        cbBackupOtomatis.setSelected(backupAktif);
        cbInterval.setValue(interval);
        cbInterval.setDisable(!backupAktif);

        loadInfoAkun();
    }

    // ── Load info akun ──────────────────────
    private void loadInfoAkun() {
        String sql = "SELECT username, nama_lengkap FROM tb_user WHERE id_user = ?";
        List<Object[]> data = koneksi.ambilData(sql, session.id_user);

        if (data.isEmpty()) {
            return;
        }

        String username = String.valueOf(data.get(0)[0]);
        String nama = String.valueOf(data.get(0)[1]);

        lblNamaAkun.setText(nama);
        lblUsernameAkun.setText("@" + username);

        String role = session.role;

        if ("Admin".equalsIgnoreCase(role)) {

            String photoUrl = prefs.get("google_photo", "");

            if (!photoUrl.isEmpty()) {
                if (!photoUrl.isEmpty()) {
                    imgAvatarGoogle.setImage(new Image(photoUrl, true));
                    imgAvatarGoogle1.setImage(new Image(photoUrl, true));

                    imgAvatarGoogle.setVisible(true);
                    imgAvatarGoogle.setManaged(true);

                    imgAvatarGoogle1.setVisible(true);
                    imgAvatarGoogle1.setManaged(true);

                    lblAvatarInisial.setVisible(false);
                    lblAvatarInisial.setManaged(false);

                    lblAvatarInisial1.setVisible(false);
                    lblAvatarInisial1.setManaged(false);
                }
            }

        } else {

            imgAvatarGoogle.setVisible(false);
            imgAvatarGoogle.setVisible(false);
            imgAvatarGoogle1.setVisible(false);
            imgAvatarGoogle1.setManaged(false);

            lblAvatarInisial.setVisible(true);
            lblAvatarInisial.setManaged(true);
            lblAvatarInisial1.setVisible(true);
            lblAvatarInisial1.setManaged(true);

            lblAvatarInisial.setText(
                    nama.length() > 0
                            ? String.valueOf(nama.charAt(0)).toUpperCase()
                            : "A");
        }
    }

    // ══════════════════════════════════════
    // TAB HANDLERS
    // ══════════════════════════════════════
    private void setupForm() {
        formEditAkun.setVisible(false);
        formEditAkun.setManaged(false);
        backupProgressBox.setVisible(false);
        backupProgressBox.setManaged(false);
        Circle clip1 = new Circle(45, 45, 45);
        Circle clip2 = new Circle(20, 20, 20);

        imgAvatarGoogle.setClip(clip1);
        imgAvatarGoogle1.setClip(clip2);
        lblEmailGoogle.setText(session.email);
        lblEmailGoogle2.setText(session.email);
        if ("Admin".equalsIgnoreCase(session.role)) {
            EditProfil.setVisible(true);
            EditProfil.setManaged(true);

            cardAkunGoogle.setVisible(true);
            cardAkunGoogle.setManaged(true);
        } else {
            EditProfil.setVisible(false);
            EditProfil.setManaged(false);

            cardAkunGoogle.setVisible(false);
            cardAkunGoogle.setManaged(false);
        }
    }

    // ══════════════════════════════════════
    // AKUN HANDLERS
    // ══════════════════════════════════════
    @FXML
    private void onEditProfil() {
        formEditAkun.setVisible(true);
        formEditAkun.setManaged(true);
    }

    @FXML
    private void onBatalEdit() {
        formEditAkun.setVisible(false);
        formEditAkun.setManaged(false);
    }

    @FXML
    private void onSimpanProfil() {
        String nama = tfEditNama.getText().trim();
        String username = tfEditUsername.getText().trim();
        String password = pfEditPassword.getText().trim();

        if (nama.isEmpty() || username.isEmpty()) {
            showAlert("Peringatan", "Nama dan username tidak boleh kosong!");
            return;
        }

        try {
            if (!password.isEmpty()) {
                String hashed = hashSHA256(password);
                koneksi.eksekusiQuery(
                        "UPDATE tb_user SET nama_lengkap=?, username=?, password=? WHERE id_user=1",
                        nama, username, hashed);
            } else {
                koneksi.eksekusiQuery(
                        "UPDATE tb_user SET nama_lengkap=?, username=? WHERE id_user=1",
                        nama, username);
            }
            loadInfoAkun();
            onBatalEdit();
            showAlert("Berhasil", "Profil berhasil diperbarui!");

        } catch (Exception e) {
            showAlert("Gagal", "Terjadi kesalahan saat menyimpan!");
            e.printStackTrace();
        }
    }

    @FXML
    private void onDisconnectGoogle() {
        Popup popup = new Popup();
        popup.showConfirmPopup("Log Out", "Apakah Anda Yakin Keluar Dari Akun " + session.email, 
        ()->{
                    try {
                        prefs.clear();
                    } catch (BackingStoreException e) {
                        e.printStackTrace();
                    }
                    navigation nav = new navigation();
                    nav.navigateToLogin();
                    Stage stage = (Stage) navLblPengaturan.getScene().getWindow();
                    stage.close();
        } );        
    }

    // ══════════════════════════════════════
    // BACKUP HANDLERS
    // ══════════════════════════════════════
    private Timeline pbAnimTimeline; // tambah field ini di atas

    @FXML
    private void onBackup() {
        backupProgressBox.setVisible(true);
        backupProgressBox.setManaged(true);
        lblBackupStatus.setText("Mengupload ke Google Drive...");
        pbBackup.setStyle("-fx-accent: #6C63FF;");
        pbBackup.setProgress(0.0);

        // Animasi: naik pelan ke 0.85, lalu pulse opacity
        pbAnimTimeline = new Timeline(
                // Fase 1: naik cepat ke 30%
                new KeyFrame(Duration.millis(0),
                        new KeyValue(pbBackup.progressProperty(), 0.0)),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(pbBackup.progressProperty(), 0.3)),
                // Fase 2: naik lambat ke 85%, berhenti di sini sampai upload selesai
                new KeyFrame(Duration.millis(3000),
                        new KeyValue(pbBackup.progressProperty(), 0.85)));
        pbAnimTimeline.setCycleCount(1); // cukup 1x, tidak bolak-balik
        pbAnimTimeline.play();

        Thread t = new Thread(() -> {
            GoogleDriveService driveService = new GoogleDriveService();
            boolean berhasil = driveService.uploadBackup();

            Platform.runLater(() -> {
                pbAnimTimeline.stop();

                if (berhasil) {
                    Timeline selesai = new Timeline(
                            new KeyFrame(Duration.ZERO,
                                    new KeyValue(pbBackup.progressProperty(), pbBackup.getProgress())),
                            new KeyFrame(Duration.millis(400),
                                    new KeyValue(pbBackup.progressProperty(), 1.0)));

                    selesai.setOnFinished(ev -> {
                        Platform.runLater(() -> {
                            pbBackup.setStyle("-fx-accent: #00E5A0;");
                            pbBackup.applyCss();
                        });
                    });

                    selesai.play();
                    lblBackupStatus.setText("✅ Backup berhasil ke Google Drive");
                    loadLastBackupTime();

                } else {
                    Timeline selesai = new Timeline(
                            new KeyFrame(Duration.ZERO,
                                    new KeyValue(pbBackup.progressProperty(), pbBackup.getProgress())),
                            new KeyFrame(Duration.millis(400),
                                    new KeyValue(pbBackup.progressProperty(), 1.0)));

                    selesai.setOnFinished(ev -> {
                        Platform.runLater(() -> {
                            pbBackup.setStyle("-fx-accent: #FF5C7C;");
                            pbBackup.applyCss();
                        });
                    });

                    selesai.play();
                    lblBackupStatus.setText("❌ Upload Google Drive gagal");
                }
            });
        }, "backup-thread");
        t.setDaemon(true);
        t.start();
    }

    @FXML
    private void onToggleBackupOtomatis() {
        boolean aktif = cbBackupOtomatis.isSelected();
        cbInterval.setDisable(!aktif);
    }

    @FXML
    private void onSimpanBackupSetting() {
        prefs.putBoolean("backup_otomatis", cbBackupOtomatis.isSelected());
        prefs.put("backup_interval", cbInterval.getValue());
        Stage stage = (Stage) btnSimpanSeting.getScene().getWindow();
        Popup popup = new Popup();
        popup.showModernPopup("Simpan Pengaturan", "Simpan Pengaturan. Backup Otomatis Akan Di Lakukan Setiap: " + prefs.get("backup_interval", "") + " Sekali", Popup.PopupType.SUCCESS, stage);
    }

    @FXML
    private void onBrowseFolder() {
        javafx.stage.DirectoryChooser dc = new javafx.stage.DirectoryChooser();
        dc.setTitle("Pilih Folder Backup");
        java.io.File folder = dc.showDialog(
                tfLokasiBackup.getScene().getWindow());
        if (folder != null) {
            tfLokasiBackup.setText(folder.getAbsolutePath());
        }
    }

    @FXML
    private void onBrowseRestore() {
        javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
        fc.setTitle("Pilih File Backup");
        fc.getExtensionFilters().add(
                new javafx.stage.FileChooser.ExtensionFilter("SQL File", "*.sql"));
        java.io.File file = fc.showOpenDialog(
                tfFileRestore.getScene().getWindow());
        if (file != null) {
            tfFileRestore.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void onRestore() {
        backupProgressBox.setVisible(true);
        backupProgressBox.setManaged(true);
        lblBackupStatus.setText("Mengunduh backup dari Google Drive...");
        pbBackup.setStyle("-fx-accent: #6C63FF;");
        pbBackup.setProgress(0.0);

        // Animasi loading
        pbAnimTimeline = new Timeline(
                new KeyFrame(Duration.millis(0),
                        new KeyValue(pbBackup.progressProperty(), 0.0)),
                new KeyFrame(Duration.millis(600),
                        new KeyValue(pbBackup.progressProperty(), 0.3)),
                new KeyFrame(Duration.millis(3000),
                        new KeyValue(pbBackup.progressProperty(), 0.85)));
        pbAnimTimeline.setCycleCount(1);
        pbAnimTimeline.play();

        Thread t = new Thread(() -> {
            GoogleDriveService driveService = new GoogleDriveService();
            boolean berhasil = driveService.restoreBackup();

            Platform.runLater(() -> {
                pbAnimTimeline.stop();

                Timeline selesai = new Timeline(
                        new KeyFrame(Duration.ZERO,
                                new KeyValue(pbBackup.progressProperty(), pbBackup.getProgress())),
                        new KeyFrame(Duration.millis(400),
                                new KeyValue(pbBackup.progressProperty(), 1.0)));

                if (berhasil) {
                    selesai.setOnFinished(ev -> Platform.runLater(() -> {
                        pbBackup.setStyle("-fx-accent: #00E5A0;");
                        pbBackup.applyCss();
                    }));
                    selesai.play();
                    lblBackupStatus.setText("✅ Restore berhasil dari Google Drive");
                    Popup popup = new Popup();
                    popup.showSuccessPopup("Berhasil","Restore Data Dari Googl Drive Berhasil Di Lakukan");
                } else {
                    selesai.setOnFinished(ev -> Platform.runLater(() -> {
                        pbBackup.setStyle("-fx-accent: #FF5C7C;");
                        pbBackup.applyCss();
                    }));
                    selesai.play();
                    Stage stage = (Stage) btnRestore.getScene().getWindow();
                    Popup popup = new Popup();
                    popup.showModernPopup("EROR", "Gagal Melakukan Restore Database", Popup.PopupType.ERROR,stage);
                    lblBackupStatus.setText("❌ Restore dari Google Drive gagal");
                }
            });
        }, "restore-thread");
        t.setDaemon(true);
        t.start();
    }

    // ══════════════════════════════════════
    // DATABASE HANDLERS
    // ══════════════════════════════════════
    @FXML
    private void onTestKoneksi() {
        try {
            koneksi.getConnection();
            lblDbDot.getStyleClass().setAll("db-dot-online");
            lblDbStatus.setText("Terhubung");
            lblDbStatus.setStyle("-fx-text-fill: #00E5A0;");
            showAlert("Berhasil", "✅ Koneksi database berhasil!");
        } catch (Exception e) {
            lblDbDot.getStyleClass().setAll("db-dot-offline");
            lblDbStatus.setText("Gagal");
            lblDbStatus.setStyle("-fx-text-fill: #FF5C7C;");
            showAlert("Gagal", "❌ Koneksi database gagal!\n" + e.getMessage());
        }
    }

    @FXML
    private void onResetDb() {
        tfDbHost.setText("localhost");
        tfDbPort.setText("3306");
        tfDbName.setText("db_enjoy_cave");
        tfDbUser.setText("root");
        pfDbPassword.clear();
    }

    @FXML
    private void onSimpanDb() {
        showAlert("Info", "Pengaturan database disimpan.\nRestart aplikasi untuk menerapkan perubahan.");
    }

    // ══════════════════════════════════════
    // HELPERS
    // ══════════════════════════════════════
    private void showAlert(String title, String msg) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private String hashSHA256(String input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            return input;
        }
    }

    private void loadLastBackupTime() {

        Thread t = new Thread(() -> {

            GoogleDriveService driveService = new GoogleDriveService();

            String waktuBackup = driveService.getLastBackupTime();

            Platform.runLater(() -> {
                lblBackupTerakhir.setText(waktuBackup);
            });

        });

        t.setDaemon(true);
        t.start();
    }

    // ======================
    // OTHER HANDLES
    @FXML
    private void onNotif() {
        System.out.println("Notifikasi dibuka");
    }

    @FXML
    private void onLihatSemua() {
        System.out.println("Lihat semua transaksi");
    }

}
