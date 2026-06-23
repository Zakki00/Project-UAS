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

import com.mycompany.Model.GoogleUser;
import com.mycompany.projectuas.Popup.PopupType;
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
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.shape.Circle;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * FXML Controller class
 *
 * @author zakki mubarroq
 */
public class PengaturanController implements Initializable {
    @FXML
    private Label tanggal;
    // ======================================================
    // FOTO PROFILE
    // =======================================================
    @FXML
    private Label notifBadge;
    @FXML
    private Label lblAvatarnavbar;
    @FXML
    private Label lblAvatartopbar;
    @FXML
    private ImageView imgAvatarGooglenavbar;
    @FXML
    private ImageView imgAvatarGoogletopbar;

    @FXML
    private VBox panelAkun;
    @FXML
    private VBox panelBackup;
    @FXML
    private VBox panelDatabase;
    @FXML
    private Button btnBackup;
    @FXML
    private Button btnSimpanSeting;

    @FXML
    private VBox panelTentang;
    @FXML
    private ImageView imgAvatarGoogle1;
    @FXML
    private ImageView imgAvatarGoogle;

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
    private javafx.scene.control.CheckBox cbBackupOtomatis;
    @FXML
    private javafx.scene.control.ComboBox<String> cbInterval;

    // Database
    @FXML
    private Label lblDbDot;
    @FXML
    private Label lblDbStatus;

    // ═══════════════════════════════════════════════════════
    // FXML — SIDEBAR
    // ═══════════════════════════════════════════════════════
    @FXML
    private HBox navLogout;
    @FXML
    private Label navLblLogout;
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
    private HBox navKaryawan;
    @FXML
    private HBox navKasir;
    @FXML
    private HBox navLaporan;
    @FXML
    private HBox navPiutang;
    @FXML
    private HBox navPengaturan;

    // Nav labels
    @FXML
    private Label navLblDashboard;
    @FXML
    private Label navLblProduk;
    @FXML
    private Label navLblKaryawan;
    @FXML
    private Label navLblKasir;

    @FXML
    private Label navLblPiutang;

    @FXML
    private Label navLblLaporan;
    @FXML
    private Label navLblPengaturan;
    @FXML
    private Label navllblakun;
    @FXML
    private Label navlblnama;

    // ── Charts ────────────────────────────────────────────
    @FXML
    private AreaChart<String, Number> salesChart;
    @FXML
    private BarChart<String, Number> trxChart;
    @FXML
    private LineChart<String, Number> monthChart;

    // ── State ─────────────────────────────────────────────
    private boolean sidebarCollapsed = false;
    private static final double SIDEBAR_FULL = 220;
    private static final double SIDEBAR_MINI = 60;

    Preferences prefs = Preferences.userNodeForPackage(session.class);
    // private Timeline autoBackupTimeline;

    // ═════════════════════════════════════════════════════
    // INITIALIZE
    // ═════════════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupPengaturan();
        setupNavHover();
        setupLogoutHover();
        setActiveNav(navPengaturan);
        setupForm();
        loadLastBackupTime();
    }

    // ═══════════════════════════════════════════════════════
    // SIDEBAR TOGGLE
    // ═══════════════════════════════════════════════════════
    @FXML
    private void onToggleSidebar() {
        sidebarCollapsed = !sidebarCollapsed;
        double targetWidth = sidebarCollapsed ? SIDEBAR_MINI : SIDEBAR_FULL;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(sidebar.prefWidthProperty(), sidebar.getPrefWidth()),
                        new KeyValue(sidebar.minWidthProperty(), sidebar.getMinWidth())),
                new KeyFrame(Duration.millis(350),
                        new KeyValue(sidebar.prefWidthProperty(), targetWidth),
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
        List<Label> labels = List.of(
                navLblDashboard, navLblProduk, navLblKaryawan,
                navLblKasir, navLblPiutang, navLblLaporan,
                navLblPengaturan, navLblLogout);
        for (Label lbl : labels) {
            lbl.setVisible(visible);
            lbl.setManaged(visible);
        }
    }

    private void updateNavPadding(boolean collapsed) {
        Insets pad = collapsed ? new Insets(10, 0, 10, 0) : new Insets(10, 14, 10, 0);
        List<HBox> items = List.of(navDashboard, navProduk, navKaryawan,
                navKasir, navPiutang, navLaporan, navPengaturan); // navLogout DIKELUARKAN
        for (HBox item : items) {
            item.setAlignment(collapsed ? Pos.CENTER : Pos.CENTER_LEFT);
            item.setPadding(pad);
        }

        // navLogout dihandle terpisah — hanya alignment & padding, tanpa setStyle()
        navLogout.setAlignment(collapsed ? Pos.CENTER : Pos.CENTER_LEFT);
        navLogout.setPadding(pad);
    }

    private void setActiveNav(HBox selected) {
        List<HBox> all = List.of(navDashboard, navProduk, navKasir,
                navLaporan, navPengaturan); // navLogout DIKELUARKAN
        for (HBox item : all) {
            item.getStyleClass().removeAll("nav-active");
            if (!item.getStyleClass().contains("nav-item"))
                item.getStyleClass().add("nav-item");
        }
        selected.getStyleClass().add("nav-active");
    }

    private void setupNavHover() {
        List<HBox> all = List.of(navDashboard, navProduk, navKasir,
                navLaporan, navPengaturan); // navLogout DIKELUARKAN
        for (HBox item : all) {
            item.setOnMouseEntered(e -> item.setStyle(
                    "-fx-background-color: #252840; -fx-background-radius: 10;"));
            item.setOnMouseExited(e -> item.setStyle(""));
        }
    }

    // Tambahkan method baru ini
    private void setupLogoutHover() {
        String styleNormal = "-fx-background-color: transparent;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;";

        String styleHover = "-fx-background-color: #FF5C7C26;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;";

        navLogout.setStyle(styleNormal);
        navLogout.setOnMouseEntered(e -> navLogout.setStyle(styleHover));
        navLogout.setOnMouseExited(e -> navLogout.setStyle(styleNormal));
    }

    // =====================================
    // LOG OUT
    // =====================================
    @FXML
    private void onNavLogout() {
        new Popup().showConfirmPopup(
                "Konfirmasi Logout",
                "Yakin ingin keluar dari aplikasi?",
                () -> {
                    new navigation().navigateToLogin();
                    ((Stage) navLogout.getScene().getWindow()).close();
                });
    }

    @FXML
    private void onNavLogoutHover() {
        navLogout.setStyle(
                "-fx-background-color: #FF5C7C26;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 10 14 10 0;");
    }

    @FXML
    private void onNavLogoutExit() {
        navLogout.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 10 14 10 0;");
    }

    // ═══════════════════════════════════════════════════════
    // NAV HANDLERS
    // ═══════════════════════════════════════════════════════
    @FXML
    private void onNavDashboard() {
        setActiveNav(navDashboard);
        new navigation().navigateToDashboard();
        ((Stage) navDashboard.getScene().getWindow()).close();
    }

    @FXML
    private void onNavProduk() {
        setActiveNav(navProduk);
        new navigation().navigateToProduk();
        ((Stage) navProduk.getScene().getWindow()).close();
    }

    @FXML
    private void onNavKaryawan() {
        setActiveNav(navKaryawan);
        new navigation().navigationToKaryawan();
        ((Stage) navKaryawan.getScene().getWindow()).close();
    }

    @FXML
    private void onNavKasir() {
        setActiveNav(navKasir);
        new navigation().navigateToTransaksi();
        ((Stage) navKasir.getScene().getWindow()).close();
    }

    @FXML
    private void onNavLaporan() {
        setActiveNav(navLaporan);
        new navigation().navigateToLaporan();
        ((Stage) navLaporan.getScene().getWindow()).close();
    }

    @FXML
    private void onNavPiutang() {
        setActiveNav(navPiutang);
        new navigation().navigateToPiutang();
        ((Stage) navPiutang.getScene().getWindow()).close();
    }

    @FXML
    private void onNavPengaturan() {
        setActiveNav(navPengaturan);

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
        String role = session.role;
        System.out.println(session.role);

        if ("Admin".equalsIgnoreCase(role)) {
            lblRoleAkun.setText(role);
            String photoUrl = session.googleUser.getProfilePictureUrl();

            if (photoUrl != null && !photoUrl.isEmpty()) {
                Image img1 = new Image(photoUrl, true);
                Image img2 = new Image(photoUrl, true);

                // Fallback ke inisial kalau gambar gagal load (tidak ada internet)
                img1.errorProperty().addListener((obs, old, isError) -> {
                    if (isError) {
                        javafx.application.Platform.runLater(() -> tampilInisial());
                    }
                });

                imgAvatarGoogle.setImage(img1);
                imgAvatarGoogle1.setImage(img2);

                imgAvatarGoogle.setVisible(true);
                imgAvatarGoogle.setManaged(true);
                imgAvatarGoogle1.setVisible(true);
                imgAvatarGoogle1.setManaged(true);

                lblAvatarInisial.setVisible(false);
                lblAvatarInisial.setManaged(false);
                lblAvatarInisial1.setVisible(false);
                lblAvatarInisial1.setManaged(false);

            } else {
                tampilInisial();
            }

        } else {
            lblRoleAkun.setText(role);

            imgAvatarGoogle.setVisible(false);
            imgAvatarGoogle.setVisible(false);
            imgAvatarGoogle1.setVisible(false);
            imgAvatarGoogle1.setManaged(false);

            lblAvatarInisial.setVisible(true);
            lblAvatarInisial.setManaged(true);
            lblAvatarInisial1.setVisible(true);
            lblAvatarInisial1.setManaged(true);
            if (session.nama != null && !session.nama.isBlank()) {
                lblAvatarInisial.setText(
                        String.valueOf(session.nama.charAt(0)).toUpperCase());
            } else {
                lblAvatarInisial.setText("A");
            }
        }
    }
    
    private void tampilInisial() {
        imgAvatarGoogle.setVisible(false);
        imgAvatarGoogle.setManaged(false);
        imgAvatarGoogle1.setVisible(false);
        imgAvatarGoogle1.setManaged(false);

        lblAvatarInisial.setVisible(true);
        lblAvatarInisial.setManaged(true);
        lblAvatarInisial1.setVisible(true);
        lblAvatarInisial1.setManaged(true);

        String inisial = (session.nama != null && !session.nama.isBlank())
                ? String.valueOf(session.nama.charAt(0)).toUpperCase()
                : "A";

        lblAvatarInisial.setText(inisial);
        lblAvatarInisial1.setText(inisial);
    }

    // ══════════════════════════════════════
    // TAB HANDLERS
    // ══════════════════════════════════════
    private void setupForm() {
        // notif---------------
        Notifikasi.updateBadge(notifBadge);

        // ====tanggal====
        Locale localeID = new Locale("id", "ID");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", localeID);
        tanggal.setText(LocalDate.now().format(formatter));


        session.applyFotoProfile(lblAvatartopbar, lblAvatarnavbar,
                imgAvatarGoogletopbar, imgAvatarGooglenavbar);

        if (session.email == "") {
            lblNamaAkun.setText(session.nama);
            lblEmailGoogle2.setVisible(false);
            lblEmailGoogle2.setManaged(false);
            lblUsernameAkun.setVisible(false);
            lblUsernameAkun.setManaged(false);
            navllblakun.setText(session.role);
            navlblnama.setText(session.nama);
        } else {
            lblNamaAkun.setText(session.nama);
            navllblakun.setText(session.role);
            navlblnama.setText(session.nama);
        }

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
        lblUsernameAkun.setText("@" + session.email);
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

        // ===logo
        Platform.runLater(() -> {
            Stage stage = (Stage) navMenu.getScene().getWindow();
            Image icon = new Image(getClass().getResourceAsStream("/image/Logo.png"));
            stage.getIcons().add(icon);
        });
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
        // VALIDASI NAMA
        Stage stage = (Stage) btnSimpanSeting.getScene().getWindow();
        
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
                () -> {
                    try {
                        prefs.clear();
                    } catch (BackingStoreException e) {
                        e.printStackTrace();
                    }
                    navigation nav = new navigation();
                    nav.navigateToLogin();
                    Stage stage = (Stage) navLblPengaturan.getScene().getWindow();
                    stage.close();
                });
        session.id = 0;
        session.googleUser = null;
        session.username ="";
        session.nama = "";
        session.role = "";
        session.email = "";
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
            GoogleDriveService service = new GoogleDriveService();

            boolean backup = service.uploadBackupAll(errorKode -> {
                Platform.runLater(() -> {
                    Stage stage = (Stage) btnBackup.getScene().getWindow();

                    if (errorKode.equals("IZIN_DITOLAK") || errorKode.equals("TOKEN_EXPIRED")) {
                        new Popup().showConfirmPopup(
                                "Akses Google Drive Ditolak",
                                "Izin akses Google Drive ditolak atau sesi telah kedaluwarsa.\nSilakan login ulang dan berikan izin akses Drive.",
                                () -> {

                                    try {
                                        prefs.clear();
                                    } catch (BackingStoreException e) {
                                        e.printStackTrace();
                                    }
                                    
                                    navigation nav = new navigation();
                                    nav.navigateToLogin();
                                    stage.close();

                                    session.id = 0;
                                    session.googleUser = null;
                                    session.username = "";
                                    session.nama = "";
                                    session.role = "";
                                    session.email = "";
                                });
                               

                    } else if (errorKode.equals("TIDAK_ADA_INTERNET")) {
                        new Popup().showModernPopup(
                                "Tidak Ada Internet",
                                "Koneksi internet tidak tersedia. Periksa koneksi dan coba lagi.",
                                Popup.PopupType.WARNING, stage);

                    } else if (errorKode.equals("BELUM_LOGIN")) {
                        new Popup().showModernPopup(
                                "Belum Login Google",
                                "Silakan login dengan akun Google terlebih dahulu.",
                                Popup.PopupType.WARNING, stage);
                    }
                });
            });

            Platform.runLater(() -> {
                pbAnimTimeline.stop();

                if (backup) {
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
                    new Popup().showSuccessPopup("Berhasil", "Backup data ke Google Drive berhasil dilakukan.");

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
        popup.showModernPopup("Simpan Pengaturan", "Simpan Pengaturan. Backup Otomatis Akan Di Lakukan Setiap: "
                + prefs.get("backup_interval", "") + " Sekali", Popup.PopupType.SUCCESS, stage);
    }

    @FXML
    private void onRestore() {
        backupProgressBox.setVisible(true);
        backupProgressBox.setManaged(true);
        lblBackupStatus.setText("Mengunduh backup dari Google Drive...");
        pbBackup.setStyle("-fx-accent: #6C63FF;");
        pbBackup.setProgress(0.0);

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

            boolean restore = driveService.restoreBackupAll(errorKode -> {
                Platform.runLater(() -> {
                    Stage stage = (Stage) btnRestore.getScene().getWindow();

                    if (errorKode.equals("IZIN_DITOLAK") || errorKode.equals("TOKEN_EXPIRED")) {
                        new Popup().showConfirmPopup(
                                "Akses Google Drive Ditolak",
                                "Izin akses Google Drive ditolak atau sesi telah kedaluwarsa.\nSilakan login ulang dan berikan izin akses Drive.",
                                () -> {
                                    try {
                                        prefs.clear();
                                    } catch (BackingStoreException e) {
                                        e.printStackTrace();
                                    }

                                    navigation nav = new navigation();
                                    nav.navigateToLogin();
                                    stage.close();

                                    session.id = 0;
                                    session.googleUser = null;
                                    session.username = "";
                                    session.nama = "";
                                    session.role = "";
                                    session.email = "";
                                });

                    } else if (errorKode.equals("TIDAK_ADA_INTERNET")) {
                        new Popup().showModernPopup(
                                "Tidak Ada Internet",
                                "Koneksi internet tidak tersedia. Periksa koneksi dan coba lagi.",
                                Popup.PopupType.WARNING, stage);

                    } else if (errorKode.equals("BELUM_LOGIN")) {
                        new Popup().showModernPopup(
                                "Belum Login Google",
                                "Silakan login dengan akun Google terlebih dahulu.",
                                Popup.PopupType.WARNING, stage);
                    }
                });
            });

            Platform.runLater(() -> {
                pbAnimTimeline.stop();

                Timeline selesai = new Timeline(
                        new KeyFrame(Duration.ZERO,
                                new KeyValue(pbBackup.progressProperty(), pbBackup.getProgress())),
                        new KeyFrame(Duration.millis(400),
                                new KeyValue(pbBackup.progressProperty(), 1.0)));

                if (restore) {
                    selesai.setOnFinished(ev -> Platform.runLater(() -> {
                        pbBackup.setStyle("-fx-accent: #00E5A0;");
                        pbBackup.applyCss();
                    }));
                    selesai.play();
                    lblBackupStatus.setText("✅ Restore berhasil dari Google Drive");
                    new Popup().showSuccessPopup("Berhasil", "Restore data dari Google Drive berhasil dilakukan.");

                } else {
                    selesai.setOnFinished(ev -> Platform.runLater(() -> {
                        pbBackup.setStyle("-fx-accent: #FF5C7C;");
                        pbBackup.applyCss();
                    }));
                    selesai.play();
                    Stage stage = (Stage) btnRestore.getScene().getWindow();
                    new Popup().showModernPopup("Error", "Gagal melakukan restore database.", Popup.PopupType.ERROR,
                            stage);
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
            new Popup().showSuccessPopup("KONEKSI", "Koneksi Ke Database Berjalan Dengan Baik");
        } catch (Exception e) {
            Stage stage = (Stage) lblAvatarInisial.getScene().getWindow();
            lblDbDot.getStyleClass().setAll("db-dot-offline");
            lblDbStatus.setText("Gagal");
            lblDbStatus.setStyle("-fx-text-fill: #FF5C7C;");
            new Popup().showModernPopup("EROR","Koneksi Ke Database Bermasalah. Silahkan Lakukan Backup Manual" ,Popup.PopupType.ERROR,stage);
        }
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

    // ═══════════════════════════════════════════════════════
    // NOTIFIKASI
    // ═══════════════════════════════════════════════════════

    @FXML
    private void onNotif() {
        Stage stage = (Stage) notifBadge.getScene().getWindow();
        Notifikasi.show(stage);
    }

}
