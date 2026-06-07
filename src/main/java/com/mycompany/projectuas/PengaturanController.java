/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectuas;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.mycompany.services.BackupService;
import com.mycompany.services.GoogleDriveService;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private Label lblNamaAkun;
    @FXML
    private Label lblUsernameAkun;
    @FXML
    private Label lblRoleAkun;
    @FXML
    private Label lblEmailGoogle;
    @FXML
    private VBox formEditAkun;
    @FXML
    private TextField tfEditNama;
    @FXML
    private TextField tfEditUsername;
    @FXML
    private javafx.scene.control.PasswordField pfEditPassword;

    // Backup
    @FXML
    private Label lblBackupTerakhir;
    @FXML
    private VBox backupProgressBox;
    @FXML
    private Label lblBackupStatus;
    @FXML
    private javafx.scene.control.ProgressBar pbBackup;
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
    private VBox logoBrand;
    @FXML
    private VBox userInfo;
    @FXML
    private HBox userRow;
    @FXML
    private Button toggleBtn;
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
    private HBox navPelanggan;
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

    // ── KPI ───────────────────────────────────────────────
    @FXML
    private Label kpiPenjualan;
    @FXML
    private Label kpiTransaksi;
    @FXML
    private Label kpiProduk;
    @FXML
    private Label kpiStok;

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

    // ═════════════════════════════════════════════════════
    // INITIALIZE
    // ═════════════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupPengaturan();
        setupNavHover();
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

        List<HBox> items = List.of(navDashboard, navProduk, navKasir, navPelanggan, navLaporan, navPengaturan);
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
    private void onNavPelanggan() {
        setActiveNav(navPelanggan);
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
        List<HBox> all = List.of(navDashboard, navProduk, navKasir, navPelanggan, navLaporan, navPengaturan);
        for (HBox item : all) {
            item.getStyleClass().removeAll("nav-active");
            if (!item.getStyleClass().contains("nav-item"))
                item.getStyleClass().add("nav-item");
        }
        selected.getStyleClass().add("nav-active");
    }

    private void setupNavHover() {
        List<HBox> all = List.of(navDashboard, navProduk, navKasir, navPelanggan, navLaporan, navPengaturan);
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
        // interval combo
        cbInterval.setItems(FXCollections.observableArrayList(
                "1 Jam", "6 Jam", "12 Jam", "24 Jam", "3 Hari", "7 Hari"));
        cbInterval.setValue("24 Jam");

        // load info akun dari session/db
        loadInfoAkun();
    }

    // ── Load info akun ──────────────────────
    private void loadInfoAkun() {
        String sql = "SELECT username, nama_lengkap FROM tb_user LIMIT 1";
        java.util.List<Object[]> data = koneksi.ambilData(sql);
        if (!data.isEmpty()) {
            String username = String.valueOf(data.get(0)[0]);
            String nama = String.valueOf(data.get(0)[1]);
            lblNamaAkun.setText(nama);
            lblUsernameAkun.setText("@" + username);
            lblAvatarInisial.setText(
                    nama.length() > 0
                            ? String.valueOf(nama.charAt(0)).toUpperCase()
                            : "A");
            tfEditNama.setText(nama);
            tfEditUsername.setText(username);
        }
    }

    // ══════════════════════════════════════
    // TAB HANDLERS
    // ══════════════════════════════════════
    @FXML
    private void onTabAkun() {
        showPanel(panelAkun);
        setActiveTab(tabAkun);
    }

    @FXML
    private void onTabBackup() {
        showPanel(panelBackup);
        setActiveTab(tabBackup);
    }

    @FXML
    private void onTabDatabase() {
        showPanel(panelDatabase);
        setActiveTab(tabDatabase);
    }

    @FXML
    private void onTabTentang() {
        showPanel(panelTentang);
        setActiveTab(tabTentang);
    }

    private void showPanel(VBox panel) {
        VBox[] panels = { panelAkun, panelBackup, panelDatabase, panelTentang };
        for (VBox p : panels) {
            p.setVisible(false);
            p.setManaged(false);
        }
        panel.setVisible(true);
        panel.setManaged(true);
    }

    private void setActiveTab(Button active) {
        Button[] tabs = { tabAkun, tabBackup, tabDatabase, tabTentang };
        for (Button t : tabs) {
            t.getStyleClass().setAll("tab-btn");
        }
        active.getStyleClass().setAll("tab-btn-active");
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
        // String nama = tfEditNama.getText().trim();
        // String username = tfEditUsername.getText().trim();
        // String password = pfEditPassword.getText().trim();

        // if (nama.isEmpty() || username.isEmpty()) {
        // showAlert("Peringatan", "Nama dan username tidak boleh kosong!");
        // return;
        // }

        // String sql;
        // if (!password.isEmpty()) {
        // String hashed = hashSHA256(password);
        // sql = "UPDATE tb_user SET nama_lengkap='" + nama
        // + "', username='" + username
        // + "', password='" + hashed + "' WHERE id_user=1";
        // } else {
        // sql = "UPDATE tb_user SET nama_lengkap='" + nama
        // + "', username='" + username + "' WHERE id_user=1";
        // }

        // if (koneksi.eksekusi(sql)) {
        // loadInfoAkun();
        // onBatalEdit();
        // showAlert("Berhasil", "Profil berhasil diperbarui!");
        // } else {
        // showAlert("Gagal", "Terjadi kesalahan saat menyimpan!");
        // }
    }

    @FXML
    private void onDisconnectGoogle() {
        showAlert("Info", "Akun Google berhasil diputus.");
        try {
            prefs.clear();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
        navigation nav = new navigation();
        nav.navigateToLogin();
        Stage stage = (Stage) navLblPengaturan.getScene().getWindow();
        stage.close();
        
    }

    // ══════════════════════════════════════
    // BACKUP HANDLERS
    // ══════════════════════════════════════
    @FXML
    private void onBackup() {

        backupProgressBox.setVisible(true);
        backupProgressBox.setManaged(true);

        lblBackupStatus.setText("Membuat salinan database...");
        pbBackup.setProgress(-1);

        BackupService backupService = new BackupService();

        boolean berhasil = backupService.backupLocal();

        if (berhasil) {

            lblBackupStatus.setText("Mengupload ke Google Drive...");

            GoogleDriveService driveService = new GoogleDriveService();

            boolean berhasilBackupDrive = driveService.uploadBackup();

            if (berhasilBackupDrive) {

                pbBackup.setProgress(1);

                lblBackupStatus.setText("✅ Backup berhasil ke Google Drive");

                String now = java.time.LocalDateTime.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern(
                                "dd-MM-yyyy HH:mm"));

                lblBackupTerakhir.setText(now);

            } else {

                pbBackup.setProgress(0);

                lblBackupStatus.setText("❌ Upload Google Drive gagal");
            }

        } else {

            pbBackup.setProgress(0);

            lblBackupStatus.setText("❌ Backup lokal gagal");
        }
    }

    @FXML
    private void onToggleBackupOtomatis() {
        boolean aktif = cbBackupOtomatis.isSelected();
        cbInterval.setDisable(!aktif);
        System.out.println("Backup otomatis: " + (aktif ? "Aktif" : "Nonaktif"));
    }

    @FXML
    private void onSimpanBackupSetting() {
        showAlert("Berhasil", "Pengaturan backup disimpan!");
    }

    @FXML
    private void onBrowseFolder() {
        javafx.stage.DirectoryChooser dc = new javafx.stage.DirectoryChooser();
        dc.setTitle("Pilih Folder Backup");
        java.io.File folder = dc.showDialog(
                tfLokasiBackup.getScene().getWindow());
        if (folder != null)
            tfLokasiBackup.setText(folder.getAbsolutePath());
    }

    @FXML
    private void onBrowseRestore() {
        javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
        fc.setTitle("Pilih File Backup");
        fc.getExtensionFilters().add(
                new javafx.stage.FileChooser.ExtensionFilter("SQL File", "*.sql"));
        java.io.File file = fc.showOpenDialog(
                tfFileRestore.getScene().getWindow());
        if (file != null)
            tfFileRestore.setText(file.getAbsolutePath());
    }

    @FXML
    private void onRestore() {
        if (tfFileRestore.getText().isEmpty()) {
            showAlert("Peringatan", "Pilih file backup terlebih dahulu!");
            return;
        }
        showAlert("Info", "Fitur restore akan segera diimplementasikan.");
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
            for (byte b : hash)
                hex.append(String.format("%02x", b));
            return hex.toString();
        } catch (Exception e) {
            return input;
        }
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
