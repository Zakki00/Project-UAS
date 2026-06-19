package com.mycompany.projectuas;

import java.net.URL;
import java.security.MessageDigest;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import com.mycompany.Model.AbsensiModel;
import com.mycompany.Model.KaryawanModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.scene.image.ImageView;
import javafx.animation.Timeline;
import javafx.util.Callback;
import javafx.util.Duration;

public class KaryawanController implements Initializable {
    // ======================================================
    // FOTO PROFILE
    // =======================================================
    @FXML
    private Label lblAvatarnavbar;
    @FXML
    private Label lblAvatartopbar;
    @FXML
    private ImageView imgAvatarGooglenavbar;
    @FXML
    private ImageView imgAvatarGoogletopbar;

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

    // ══════════════════════════════════════════════════════
    // TAB KARYAWAN — Form
    // ══════════════════════════════════════════════════════
    @FXML
    private Tab tabKaryawan; // pastikan sudah di-inject

    @FXML
    private TabPane tabPane; // inject TabPane-nya juga
    @FXML
    private TextField txtIdKaryawan;
    @FXML
    private TextField txtNamaKaryawan;
    @FXML
    private ComboBox<String> cmbJenisKelamin;
    @FXML
    private TextField txtNoHp;
    @FXML
    private DatePicker dpTanggalMasuk;
    @FXML
    private ComboBox<String> cmbStatusKerja;
    @FXML
    private TextArea txtAlamat;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;

    // ══════════════════════════════════════════════════════
    // TAB KARYAWAN — TableView
    // ══════════════════════════════════════════════════════
    @FXML
    private TableView<KaryawanModel> tableKaryawan;
    @FXML
    private TableColumn<KaryawanModel, String> colId;
    @FXML
    private TableColumn<KaryawanModel, String> colNama;
    @FXML
    private TableColumn<KaryawanModel, String> colUsername; // baru
    @FXML
    private TableColumn<KaryawanModel, String> colJenisKelamin; // baru
    @FXML
    private TableColumn<KaryawanModel, String> colNoHp;
    @FXML
    private TableColumn<KaryawanModel, String> colTglMasuk;
    @FXML
    private TableColumn<KaryawanModel, String> colAlamat;
    @FXML
    private TableColumn<KaryawanModel, String> colStatus;
    @FXML
    private TableColumn<KaryawanModel, String> colRole; // baru

    // ══════════════════════════════════════════════════════
    // TAB ABSENSI — Form
    // ══════════════════════════════════════════════════════
    @FXML
    private DatePicker dpTanggalAbsensi;
    @FXML
    private ComboBox<String> cmbPilihKaryawan;
    @FXML
    private TextField txtNamaAbsensi;
    @FXML
    private ComboBox<String> cmbShift;
    @FXML
    private ComboBox<String> cmbStatusKehadiran;

    // ══════════════════════════════════════════════════════
    // TAB ABSENSI — TableView
    // ══════════════════════════════════════════════════════
    @FXML
    private TableView<AbsensiModel> tableAbsensi;
    @FXML
    private TableColumn<AbsensiModel, String> colAbsTanggal;
    @FXML
    private TableColumn<AbsensiModel, String> colAbsId;
    @FXML
    private TableColumn<AbsensiModel, String> colAbsNama;
    @FXML
    private TableColumn<AbsensiModel, String> colAbsShift;
    @FXML
    private TableColumn<AbsensiModel, String> colAbsStatus;

    // ══════════════════════════════════════════════════════
    // OBSERVABLE LISTS
    // ══════════════════════════════════════════════════════
    private final ObservableList<KaryawanModel> karyawanList = FXCollections.observableArrayList();
    private final ObservableList<AbsensiModel> absensiList = FXCollections.observableArrayList();

    private boolean sidebarCollapsed = false;
    private static final double SIDEBAR_FULL = 220;
    private static final double SIDEBAR_MINI = 60;

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // ══════════════════════════════════════════════════════
    // INITIALIZE
    // ══════════════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupNavHover();
        setupLogoutHover();
        setActiveNav(navKaryawan);
        setupComboBoxes();
        setuptxNoHp();
        setupTableKaryawan();
        setupTableAbsensi();
        setupTableClickKaryawan();
        setupTableClickAbsensi();
        loadDataKaryawan();
        loadDataAbsensi();
        generateNextId();
        setupForm();
    }

    //=======================================================
    //SETUP FORM
    //=======================================================
    private void setupForm(){
        session.applyFotoProfile(lblAvatartopbar, lblAvatarnavbar,
                imgAvatarGoogletopbar, imgAvatarGooglenavbar);
        if (session.email == "") {
            navllblakun.setText(session.role);
            navlblnama.setText(session.nama);
            tabPane.getTabs().remove(tabKaryawan);
        } else {
            navllblakun.setText(session.role);
            navlblnama.setText(session.nama);
        }

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
        new navigation().navigataeToPengaturan();
        ((Stage) navPengaturan.getScene().getWindow()).close();
    }

    // ══════════════════════════════════════════════════════
    // COMBO BOXES
    // ══════════════════════════════════════════════════════
    private void setupComboBoxes() {
        cmbJenisKelamin.setItems(FXCollections.observableArrayList("Laki-laki", "Perempuan"));
        cmbStatusKerja.setItems(FXCollections.observableArrayList("Aktif", "Non Aktif"));
        cmbShift.setItems(FXCollections.observableArrayList("Siang", "Malam"));
        cmbStatusKehadiran.setItems(FXCollections.observableArrayList("Hadir", "Izin", "Sakit", "Alpha"));

        cmbPilihKaryawan.setOnAction(e -> {
            String selectedId = cmbPilihKaryawan.getValue();
            if (selectedId != null) {
                for (KaryawanModel k : karyawanList) {
                    if (k.getIdKaryawan().equals(selectedId)) {
                        txtNamaAbsensi.setText(k.getNamaLengkap());
                        break;
                    }
                }
            }
        });
    }

    // ══════════════════════════════════════════════════════
    // SETUP TABLE KARYAWAN
    // FIX: sesuaikan property name dengan KaryawanModel
    // ══════════════════════════════════════════════════════
    private void setupTableKaryawan() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idKaryawan"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaLengkap"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colJenisKelamin.setCellValueFactory(new PropertyValueFactory<>("jenisKelamin"));
        colNoHp.setCellValueFactory(new PropertyValueFactory<>("noHp"));
        colTglMasuk.setCellValueFactory(new PropertyValueFactory<>("tanggalMasuk"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusKerja"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        colStatus.setCellFactory(new Callback<TableColumn<KaryawanModel, String>, TableCell<KaryawanModel, String>>() {
            @Override
            public TableCell<KaryawanModel, String> call(TableColumn<KaryawanModel, String> param) {
                return new TableCell<KaryawanModel, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                            setText(null);
                            return;
                        }
                        Label badge = new Label(item);
                        badge.getStyleClass().add("status-badge");
                        badge.getStyleClass().add(
                                item.equalsIgnoreCase("Aktif") ? "badge-aktif" : "badge-nonaktif");
                        setGraphic(badge);
                        setText(null);
                    }
                };
            }
        });

        tableKaryawan.setItems(karyawanList);
        tableKaryawan.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // ══════════════════════════════════════════════════════
    // SETUP TABLE ABSENSI
    // FIX: hapus colAbsJabatan karena tidak ada di model baru
    // ══════════════════════════════════════════════════════
    private void setupTableAbsensi() {
        colAbsTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colAbsId.setCellValueFactory(new PropertyValueFactory<>("idKaryawan"));
        colAbsNama.setCellValueFactory(new PropertyValueFactory<>("namaKaryawan"));
        colAbsShift.setCellValueFactory(new PropertyValueFactory<>("shiftMasuk"));
        colAbsStatus.setCellValueFactory(new PropertyValueFactory<>("statusKehadiran"));

        colAbsStatus.setCellFactory(new Callback<TableColumn<AbsensiModel, String>, TableCell<AbsensiModel, String>>() {
            @Override
            public TableCell<AbsensiModel, String> call(TableColumn<AbsensiModel, String> param) {
                return new TableCell<AbsensiModel, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                            setText(null);
                            return;
                        }
                        Label badge = new Label(item);
                        badge.getStyleClass().add("status-badge");
                        switch (item.toLowerCase()) {
                            case "hadir" -> badge.getStyleClass().add("badge-hadir");
                            case "izin" -> badge.getStyleClass().add("badge-izin");
                            case "sakit" -> badge.getStyleClass().add("badge-sakit");
                            default -> badge.getStyleClass().add("badge-alpha");
                        }
                        setGraphic(badge);
                        setText(null);
                    }
                };
            }
        });

        tableAbsensi.setItems(absensiList);
        tableAbsensi.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // ══════════════════════════════════════════════════════
    // SETUP TEXTFIELD NO HP
    // ══════════════════════════════════════════════════════
    private void setuptxNoHp() {
        txtNoHp.setTextFormatter(new TextFormatter<>(change -> {
            String text = change.getControlNewText();
            return text.matches("\\d{0,13}") ? change : null;
        }));
    }

    // ══════════════════════════════════════════════════════
    // KLIK BARIS → ISI FORM
    // ══════════════════════════════════════════════════════
    private void setupTableClickKaryawan() {
        tableKaryawan.setOnMouseClicked(e -> {
            KaryawanModel selected = tableKaryawan.getSelectionModel().getSelectedItem();
            if (selected == null)
                return;
            txtIdKaryawan.setText(selected.getIdKaryawan());
            txtNamaKaryawan.setText(selected.getNamaLengkap());
            txtUsername.setText(selected.getUsername());
            txtPassword.setText(selected.getPassword());
            cmbJenisKelamin.setValue(selected.getJenisKelamin());
            txtNoHp.setText(selected.getNoHp());
            cmbStatusKerja.setValue(selected.getStatusKerja());
            txtAlamat.setText(selected.getAlamat());
            try {
                dpTanggalMasuk.setValue(
                        LocalDate.parse(selected.getTanggalMasuk(),
                                DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            } catch (Exception ex) {
                dpTanggalMasuk.setValue(null);
            }
        });
    }

    private void setupTableClickAbsensi() {
        tableAbsensi.setOnMouseClicked(e -> {
            AbsensiModel selected = tableAbsensi.getSelectionModel().getSelectedItem();
            if (selected == null)
                return;
            cmbPilihKaryawan.setValue(selected.getIdKaryawan());
            txtNamaAbsensi.setText(selected.getNamaKaryawan());
            cmbShift.setValue(selected.getShiftMasuk());
            cmbStatusKehadiran.setValue(selected.getStatusKehadiran());
            try {
                dpTanggalAbsensi.setValue(
                        LocalDate.parse(selected.getTanggal(),
                                DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            } catch (Exception ex) {
                dpTanggalAbsensi.setValue(null);
            }
        });
    }

    // ══════════════════════════════════════════════════════
    // LOAD DATA KARYAWAN
    // FIX: hapus kolom jabatan, sesuaikan index & constructor
    // ══════════════════════════════════════════════════════
    @FXML
    public void loadDataKaryawan() {
        karyawanList.clear();
        try {
            // Kolom: 0=id, 1=username, 2=password, 3=nama_lengkap,
            // 4=jenis_kelamin, 5=no_hp, 6=tanggal_masuk,
            // 7=status_kerja, 8=alamat, 9=role
            List<Object[]> data = koneksi.ambilData(
                    "SELECT id_karyawan, username, password, nama_lengkap, " +
                            "jenis_kelamin, no_hp, tanggal_masuk, status_kerja, alamat, role " +
                            "FROM tb_karyawan ORDER BY id_karyawan");

            ObservableList<String> idList = FXCollections.observableArrayList();

            for (Object[] row : data) {
                String id = row[0] != null ? row[0].toString() : "";
                String username = row[1] != null ? row[1].toString() : "";
                String password = row[2] != null ? row[2].toString() : "";
                String nama = row[3] != null ? row[3].toString() : "";
                String jenisKelamin = row[4] != null ? row[4].toString() : "";
                String noHp = row[5] != null ? row[5].toString() : "";
                String tanggalMasuk = row[6] != null ? row[6].toString() : "";
                String statusKerja = row[7] != null ? row[7].toString() : "";
                String alamat = row[8] != null ? row[8].toString() : "";
                String role = row[9] != null ? row[9].toString() : "";

                karyawanList.add(new KaryawanModel(
                        id, username, password, nama,
                        jenisKelamin, noHp, tanggalMasuk,
                        statusKerja, alamat, role));

                idList.add(id);
            }
            cmbPilihKaryawan.setItems(idList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ══════════════════════════════════════════════════════
    // LOAD DATA ABSENSI
    // FIX: pakai nama_lengkap, hapus jabatan dari SELECT & constructor
    // ══════════════════════════════════════════════════════
    @FXML
    public void loadDataAbsensi() {
        absensiList.clear();
        try {
            List<Object[]> data = koneksi.ambilData(
                    "SELECT a.id_absensi, a.id_karyawan, k.nama_lengkap, " + // FIX: nama_lengkap
                            "a.tanggal, a.jam_masuk, a.status_kehadiran " +
                            "FROM tb_absensi a " +
                            "JOIN tb_karyawan k ON a.id_karyawan = k.id_karyawan " +
                            "ORDER BY a.tanggal DESC");

            for (Object[] row : data) {
                int idAbs = row[0] != null ? ((Number) row[0]).intValue() : 0;
                String idKary = row[1] != null ? row[1].toString() : "";
                String nama = row[2] != null ? row[2].toString() : "";
                String tanggal = row[3] != null ? row[3].toString() : "";
                String shift = row[4] != null ? row[4].toString() : "";
                String status = row[5] != null ? row[5].toString() : "";

                // Constructor AbsensiModel: (idAbsensi, idKaryawan, namaKaryawan,
                // jabatan[unused], tanggal, shiftMasuk, statusKehadiran)
                absensiList.add(new AbsensiModel(idAbs, idKary, nama, "", tanggal, shift, status));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // ══════════════════════════════════════════════════════
    // GENERATE ID
    // ══════════════════════════════════════════════════════
    private void generateNextId() {
        if (karyawanList.isEmpty()) {
            txtIdKaryawan.setText("KRY001");
        } else {
            int max = 0;
            for (KaryawanModel k : karyawanList) {
                try {
                    int num = Integer.parseInt(k.getIdKaryawan().replace("KRY", ""));
                    if (num > max)
                        max = num;
                } catch (Exception ignored) {
                }
            }
            txtIdKaryawan.setText(String.format("KRY%03d", max + 1));
        }
        txtIdKaryawan.setEditable(false);
    }

    // ══════════════════════════════════════════════════════
    // VALIDASI KARYAWAN
    // ══════════════════════════════════════════════════════
    private boolean validasiKaryawan() {
        if (txtNamaKaryawan.getText().isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Nama karyawan wajib diisi!");
            return false;
        }
        if (cmbJenisKelamin.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih jenis kelamin!");
            return false;
        }
        if (txtNoHp.getText().isBlank()) {
            showAlert(Alert.AlertType.WARNING, "No HP wajib diisi!");
            return false;
        }
        if (!txtNoHp.getText().matches("\\d{10,13}")) {
            showAlert(Alert.AlertType.WARNING, "No HP harus terdiri dari 10 sampai 13 digit!");
            return false;
        }
        if (dpTanggalMasuk.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih tanggal masuk!");
            return false;
        }
        if (cmbStatusKerja.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih status kerja!");
            return false;
        }
        if (txtUsername.getText().isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Username wajib diisi!");
            return false;
        }
        if (txtPassword.getText().isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Password wajib diisi!");
            return false;
        }
        return true;
    }
    
    // ══════════════════════════════════════════════════════
    // CEK DUPLIKAT USERNAME & PASSWORD
    // ══════════════════════════════════════════════════════
    private boolean isDuplicateUsername(String username, String excludeId) {
        List<Object[]> data = koneksi.ambilData(
                "SELECT id_karyawan FROM tb_karyawan WHERE username=? AND id_karyawan != ?",
                username, excludeId);
        return !data.isEmpty();
    }

    // ══════════════════════════════════════════════════════
    // CRUD KARYAWAN
    // FIX: hapus jabatan dari INSERT/UPDATE, sesuaikan constructor
    // ══════════════════════════════════════════════════════
    @FXML
    public void tambahKaryawan() {
        if (!validasiKaryawan())
            return;

        // Cek duplikat username
        if (isDuplicateUsername(txtUsername.getText(), "")) {
            new Popup().showModernPopup(
                    "ERROR",
                    "Username '" + txtUsername.getText() + "' sudah digunakan karyawan lain!",
                    Popup.PopupType.ERROR,
                    (Stage) txtIdKaryawan.getScene().getWindow());
            return;
        }

        String id = txtIdKaryawan.getText();
        String tgl = dpTanggalMasuk.getValue().format(fmt);

        String sql = """
                INSERT INTO tb_karyawan
                (id_karyawan, username, password, nama_lengkap, jenis_kelamin,
                 no_hp, tanggal_masuk, status_kerja, alamat, role)
                VALUES (?,?,?,?,?,?,?,?,?,?)
                """;

        koneksi.eksekusiQuery(
                sql,
                id,
                txtUsername.getText(),
                txtPassword.getText(),
                txtNamaKaryawan.getText(),
                cmbJenisKelamin.getValue(),
                txtNoHp.getText(),
                tgl,
                cmbStatusKerja.getValue(),
                txtAlamat.getText(),
                "Karyawan");

        karyawanList.add(new KaryawanModel(
                id,
                txtUsername.getText(),
                txtPassword.getText(),
                txtNamaKaryawan.getText(),
                cmbJenisKelamin.getValue(),
                txtNoHp.getText(),
                tgl,
                cmbStatusKerja.getValue(),
                txtAlamat.getText(),
                "Karyawan"));

        cmbPilihKaryawan.getItems().add(id);

        // Khusus tambah → pakai showSuccessPopup
        new Popup().showSuccessPopup(
                "Berhasil Ditambahkan!",
                "Karyawan " + txtNamaKaryawan.getText() + " berhasil ditambahkan.");

        resetKaryawan();
    }

    @FXML
    public void simpanKaryawan() {
        KaryawanModel selected = tableKaryawan.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Popup().showModernPopup(
                    "WARNING",
                    "Pilih karyawan di tabel terlebih dahulu",
                    Popup.PopupType.WARNING,
                    (Stage) txtIdKaryawan.getScene().getWindow());
            return;
        }
        if (!validasiKaryawan())
            return;

        // Cek duplikat username, kecualikan ID karyawan yang sedang diedit
        if (isDuplicateUsername(txtUsername.getText(), selected.getIdKaryawan())) {
            new Popup().showModernPopup(
                    "ERROR",
                    "Username '" + txtUsername.getText() + "' sudah digunakan karyawan lain!",
                    Popup.PopupType.ERROR,
                    (Stage) txtIdKaryawan.getScene().getWindow());
            return;
        }

        String tgl = dpTanggalMasuk.getValue().format(fmt);

        String sql = """
                UPDATE tb_karyawan
                SET username=?, password=?, nama_lengkap=?, jenis_kelamin=?,
                    no_hp=?, tanggal_masuk=?, status_kerja=?, alamat=?, role=?
                WHERE id_karyawan=?
                """;

        koneksi.eksekusiQuery(
                sql,
                txtUsername.getText(),
                txtPassword.getText(),
                txtNamaKaryawan.getText(),
                cmbJenisKelamin.getValue(),
                txtNoHp.getText(),
                tgl,
                cmbStatusKerja.getValue(),
                txtAlamat.getText(),
                "Karyawan",
                selected.getIdKaryawan());

        selected.setUsername(txtUsername.getText());
        selected.setPassword(txtPassword.getText());
        selected.setNamaLengkap(txtNamaKaryawan.getText());
        selected.setJenisKelamin(cmbJenisKelamin.getValue());
        selected.setNoHp(txtNoHp.getText());
        selected.setTanggalMasuk(tgl);
        selected.setStatusKerja(cmbStatusKerja.getValue());
        selected.setAlamat(txtAlamat.getText());

        tableKaryawan.refresh();

        new Popup().showModernPopup(
                "SUCCESS",
                "Data karyawan berhasil diperbarui",
                Popup.PopupType.SUCCESS,
                (Stage) txtIdKaryawan.getScene().getWindow());

        resetKaryawan();
    }

    @FXML
    public void ubahKaryawan() {
        simpanKaryawan();
    }

    @FXML
    public void hapusKaryawan() {
        KaryawanModel selected = tableKaryawan.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Popup().showModernPopup(
                    "WARNING",
                    "Pilih karyawan di tabel terlebih dahulu!",
                    Popup.PopupType.WARNING,
                    (Stage) txtIdKaryawan.getScene().getWindow());
            return;
        }

        new Popup().showConfirmPopup(
                "Hapus Karyawan",
                "Yakin ingin menghapus karyawan " + selected.getNamaLengkap() + "?",
                () -> {
                    koneksi.eksekusiQuery(
                            "DELETE FROM tb_karyawan WHERE id_karyawan=?",
                            selected.getIdKaryawan());
                    karyawanList.remove(selected);
                    cmbPilihKaryawan.getItems().remove(selected.getIdKaryawan());
                    resetKaryawan();

                    new Popup().showModernPopup(
                            "SUCCESS",
                            "Karyawan " + selected.getNamaLengkap() + " berhasil dihapus",
                            Popup.PopupType.SUCCESS,
                            (Stage) tableKaryawan.getScene().getWindow());
                });
    }

    @FXML
    public void resetKaryawan() {
        txtNamaKaryawan.clear();
        txtUsername.clear();
        txtPassword.clear();
        cmbJenisKelamin.setValue(null);
        txtNoHp.clear();
        dpTanggalMasuk.setValue(null);
        cmbStatusKerja.setValue(null);
        txtAlamat.clear();
        tableKaryawan.getSelectionModel().clearSelection();
        generateNextId();
    }

    // ══════════════════════════════════════════════════════
    // CRUD ABSENSI
    // FIX: nama tabel tb_absensi, hapus jabatan dari query
    // ══════════════════════════════════════════════════════
    @FXML
    public void simpanAbsensi() {
        if (dpTanggalAbsensi.getValue() == null) {
            new Popup().showModernPopup(
                    "WARNING", "Pilih tanggal absensi!",
                    Popup.PopupType.WARNING,
                    (Stage) tableAbsensi.getScene().getWindow());
            return;
        }
        if (cmbPilihKaryawan.getValue() == null) {
            new Popup().showModernPopup(
                    "WARNING", "Pilih karyawan!",
                    Popup.PopupType.WARNING,
                    (Stage) tableAbsensi.getScene().getWindow());
            return;
        }
        if (cmbShift.getValue() == null) {
            new Popup().showModernPopup(
                    "WARNING", "Pilih shift!",
                    Popup.PopupType.WARNING,
                    (Stage) tableAbsensi.getScene().getWindow());
            return;
        }
        if (cmbStatusKehadiran.getValue() == null) {
            new Popup().showModernPopup(
                    "WARNING", "Pilih status kehadiran!",
                    Popup.PopupType.WARNING,
                    (Stage) tableAbsensi.getScene().getWindow());
            return;
        }

        String tgl = dpTanggalAbsensi.getValue().format(fmt);
        String idKary = cmbPilihKaryawan.getValue();
        String nama = txtNamaAbsensi.getText();

        koneksi.eksekusiQuery(
                "INSERT INTO tb_absensi (id_karyawan, tanggal, jam_masuk, status_kehadiran) " +
                        "VALUES (?,?,?,?)",
                idKary,
                tgl,
                cmbShift.getValue(),
                cmbStatusKehadiran.getValue());

        int newId = absensiList.isEmpty() ? 1
                : absensiList.stream()
                        .mapToInt(AbsensiModel::getIdAbsensi)
                        .max().orElse(0) + 1;

        absensiList.add(new AbsensiModel(
                newId, idKary, nama, "",
                tgl, cmbShift.getValue(), cmbStatusKehadiran.getValue()));

        new Popup().showSuccessPopup(
                "Absensi Tersimpan!",
                "Absensi karyawan " + nama + " berhasil disimpan.");

        resetAbsensi();
    }

    @FXML
    public void ubahAbsensi() {
        AbsensiModel selected = tableAbsensi.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Popup().showModernPopup(
                    "WARNING", "Pilih absensi di tabel terlebih dahulu!",
                    Popup.PopupType.WARNING,
                    (Stage) tableAbsensi.getScene().getWindow());
            return;
        }
        if (dpTanggalAbsensi.getValue() == null) {
            new Popup().showModernPopup(
                    "WARNING", "Pilih tanggal absensi!",
                    Popup.PopupType.WARNING,
                    (Stage) tableAbsensi.getScene().getWindow());
            return;
        }
        if (cmbShift.getValue() == null) {
            new Popup().showModernPopup(
                    "WARNING", "Pilih shift!",
                    Popup.PopupType.WARNING,
                    (Stage) tableAbsensi.getScene().getWindow());
            return;
        }
        if (cmbStatusKehadiran.getValue() == null) {
            new Popup().showModernPopup(
                    "WARNING", "Pilih status kehadiran!",
                    Popup.PopupType.WARNING,
                    (Stage) tableAbsensi.getScene().getWindow());
            return;
        }

        String tgl = dpTanggalAbsensi.getValue().format(fmt);

        koneksi.eksekusiQuery(
                "UPDATE tb_absensi SET tanggal=?, jam_masuk=?, status_kehadiran=? " +
                        "WHERE id_absensi=?",
                tgl,
                cmbShift.getValue(),
                cmbStatusKehadiran.getValue(),
                selected.getIdAbsensi());

        selected.setTanggal(tgl);
        selected.setShiftMasuk(cmbShift.getValue());
        selected.setStatusKehadiran(cmbStatusKehadiran.getValue());
        tableAbsensi.refresh();

        new Popup().showModernPopup(
                "SUCCESS", "Data absensi berhasil diperbarui",
                Popup.PopupType.SUCCESS,
                (Stage) tableAbsensi.getScene().getWindow());

        resetAbsensi();
    }

    @FXML
    public void hapusAbsensi() {
        AbsensiModel selected = tableAbsensi.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Popup().showModernPopup(
                    "WARNING",
                    "Pilih absensi di tabel terlebih dahulu!",
                    Popup.PopupType.WARNING,
                    (Stage) tableAbsensi.getScene().getWindow());
            return;
        }

        new Popup().showConfirmPopup(
                "Hapus Absensi",
                "Yakin ingin menghapus data absensi ini?",
                () -> {
                    koneksi.eksekusiQuery(
                            "DELETE FROM tb_absensi WHERE id_absensi=?",
                            selected.getIdAbsensi());
                    absensiList.remove(selected);
                    resetAbsensi();

                    new Popup().showModernPopup(
                            "SUCCESS",
                            "Data absensi berhasil dihapus",
                            Popup.PopupType.SUCCESS,
                            (Stage) tableAbsensi.getScene().getWindow());
                });
    }

    @FXML
    public void resetAbsensi() {
        dpTanggalAbsensi.setValue(null);
        cmbPilihKaryawan.setValue(null);
        txtNamaAbsensi.clear();
        cmbShift.setValue(null);
        cmbStatusKehadiran.setValue(null);
        tableAbsensi.getSelectionModel().clearSelection();
    }

    // ══════════════════════════════════════════════════════
    // HELPER
    // ══════════════════════════════════════════════════════
    private void showAlert(Alert.AlertType type, String msg) {
        new Alert(type, msg, ButtonType.OK).showAndWait();
    }


    //===========================================
     // Hash password pakai SHA-256
     //=========================================
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