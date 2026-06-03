package com.mycompany.projectuas;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.animation.Timeline;
import javafx.util.Callback;
import javafx.util.Duration;

public class KaryawanController implements Initializable {

    // ══════════════════════════════════════════════════════
    // SIDEBAR
    // ══════════════════════════════════════════════════════
    @FXML private VBox   sidebar;
    @FXML private HBox   logoRow;
    @FXML private VBox   logoBrand;
    @FXML private VBox   userInfo;
    @FXML private HBox   userRow;
    @FXML private Button toggleBtn;
    @FXML private VBox   navMenu;

    @FXML private HBox navDashboard;
    @FXML private HBox navProduk;
    @FXML private HBox navKasir;
    @FXML private HBox navPelanggan;
    @FXML private HBox navLaporan;
    @FXML private HBox navPiutang;
    @FXML private HBox navPengaturan;
    @FXML private HBox navKaryawan;

    @FXML private Label navLblDashboard;
    @FXML private Label navLblProduk;
    @FXML private Label navLblKasir;
    @FXML private Label navLblPelanggan;
    @FXML private Label navLblLaporan;
    @FXML private Label navLblPengaturan;
    @FXML private Label navLblKaryawan;

    // ══════════════════════════════════════════════════════
    // TAB KARYAWAN — Form
    // ══════════════════════════════════════════════════════
    @FXML private TextField        txtIdKaryawan;
    @FXML private TextField        txtNamaKaryawan;
    @FXML private ComboBox<String> cmbJenisKelamin;
    @FXML private ComboBox<String> cmbJabatan;
    @FXML private TextField        txtNoHp;
    @FXML private DatePicker       dpTanggalMasuk;
    @FXML private ComboBox<String> cmbStatusKerja;
    @FXML private TextArea         txtAlamat;

    // ══════════════════════════════════════════════════════
    // TAB KARYAWAN — TableView
    // ══════════════════════════════════════════════════════
    @FXML private TableView<KaryawanModel>          tableKaryawan;
    @FXML private TableColumn<KaryawanModel,String> colId;
    @FXML private TableColumn<KaryawanModel,String> colNama;
    @FXML private TableColumn<KaryawanModel,String> colNoHp;
    @FXML private TableColumn<KaryawanModel,String> colTglMasuk;
    @FXML private TableColumn<KaryawanModel,String> colAlamat;
    @FXML private TableColumn<KaryawanModel,String> colStatus;

    // ══════════════════════════════════════════════════════
    // TAB ABSENSI — Form
    // ══════════════════════════════════════════════════════
    @FXML private DatePicker       dpTanggalAbsensi;
    @FXML private ComboBox<String> cmbPilihKaryawan;
    @FXML private TextField        txtNamaAbsensi;
    @FXML private ComboBox<String> cmbShift;
    @FXML private ComboBox<String> cmbStatusKehadiran;

    // ══════════════════════════════════════════════════════
    // TAB ABSENSI — TableView
    // ══════════════════════════════════════════════════════
    @FXML private TableView<AbsensiModel>           tableAbsensi;
    @FXML private TableColumn<AbsensiModel,String>  colAbsTanggal;
    @FXML private TableColumn<AbsensiModel,String>  colAbsId;
    @FXML private TableColumn<AbsensiModel,String>  colAbsNama;
    @FXML private TableColumn<AbsensiModel,String>  colAbsJabatan;
    @FXML private TableColumn<AbsensiModel,String>  colAbsShift;
    @FXML private TableColumn<AbsensiModel,String>  colAbsStatus;

    // ══════════════════════════════════════════════════════
    // OBSERVABLE LISTS
    // ══════════════════════════════════════════════════════
    private final ObservableList<KaryawanModel> karyawanList = FXCollections.observableArrayList();
    private final ObservableList<AbsensiModel>  absensiList  = FXCollections.observableArrayList();

    private boolean sidebarCollapsed = false;
    private static final double SIDEBAR_FULL = 220;
    private static final double SIDEBAR_MINI = 60;

    private static final String DB_URL  = "jdbc:mysql://localhost:3306/cafe_db";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // ══════════════════════════════════════════════════════
    // INITIALIZE
    // ══════════════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupNavHover();
        setActiveNav(navKaryawan);
        setupComboBoxes();
        setupTableKaryawan();
        setupTableAbsensi();
        setupTableClickKaryawan();
        setupTableClickAbsensi();
        loadDataKaryawan();
        loadDataAbsensi();
        generateNextId();
    }

    // ══════════════════════════════════════════════════════
    // SIDEBAR TOGGLE
    // ══════════════════════════════════════════════════════
    @FXML
    private void onToggleSidebar() {
        sidebarCollapsed = !sidebarCollapsed;
        double targetWidth = sidebarCollapsed ? SIDEBAR_MINI : SIDEBAR_FULL;
        Timeline tl = new Timeline(
            new KeyFrame(Duration.ZERO,
                new KeyValue(sidebar.prefWidthProperty(), sidebar.getPrefWidth()),
                new KeyValue(sidebar.minWidthProperty(), sidebar.getMinWidth())),
            new KeyFrame(Duration.millis(350),
                new KeyValue(sidebar.prefWidthProperty(), targetWidth),
                new KeyValue(sidebar.minWidthProperty(), targetWidth))
        );
        if (sidebarCollapsed) {
            hideSidebarText();
            toggleBtn.setText("▶");
            logoRow.setAlignment(Pos.CENTER);
            logoRow.setPadding(new Insets(18, 0, 18, 0));
            userRow.setAlignment(Pos.CENTER);
            userRow.setPadding(new Insets(12, 0, 12, 0));
        } else {
            tl.setOnFinished(e -> {
                showSidebarText();
                logoRow.setAlignment(Pos.CENTER_LEFT);
                logoRow.setPadding(new Insets(18, 16, 18, 16));
                userRow.setAlignment(Pos.CENTER_LEFT);
                userRow.setPadding(new Insets(12, 16, 12, 16));
            });
            toggleBtn.setText("◀");
        }
        updateNavPadding(sidebarCollapsed);
        tl.play();
    }

    private void hideSidebarText() {
        logoBrand.setVisible(false); logoBrand.setManaged(false);
        userInfo.setVisible(false);  userInfo.setManaged(false);
        setNavLabelsVisible(false);
    }
    private void showSidebarText() {
        logoBrand.setVisible(true); logoBrand.setManaged(true);
        userInfo.setVisible(true);  userInfo.setManaged(true);
        setNavLabelsVisible(true);
    }
    private void setNavLabelsVisible(boolean v) {
        for (Label lbl : List.of(navLblDashboard, navLblProduk, navLblKasir,
                navLblPelanggan, navLblLaporan, navLblPengaturan, navLblKaryawan)) {
            lbl.setVisible(v); lbl.setManaged(v);
        }
    }
    private void updateNavPadding(boolean collapsed) {
        Insets pad = collapsed ? new Insets(10,0,10,0) : new Insets(10,14,10,0);
        for (HBox item : List.of(navDashboard, navProduk, navKasir,
                navPelanggan, navLaporan, navPengaturan, navKaryawan)) {
            item.setAlignment(collapsed ? Pos.CENTER : Pos.CENTER_LEFT);
            item.setPadding(pad);
        }
    }

    // ══════════════════════════════════════════════════════
    // NAV HANDLERS
    // ══════════════════════════════════════════════════════
    @FXML private void onNavDashboard()  { setActiveNav(navDashboard); }
    @FXML private void onNavProduk()     { setActiveNav(navProduk); }
    @FXML private void onNavKasir() {
        setActiveNav(navKasir);
        navigation nav = new navigation();
        nav.navigateToTransaksi();
        ((Stage) navKasir.getScene().getWindow()).close();
    }
    @FXML private void onNavPelanggan()  { setActiveNav(navPelanggan); }
    @FXML private void onNavLaporan() {
        setActiveNav(navLaporan);
        navigation nav = new navigation();
        nav.navigateToLaporan();
        ((Stage) navLaporan.getScene().getWindow()).close();
    }
    @FXML private void onNavPiutang() {
        setActiveNav(navPiutang);
        navigation nav = new navigation();
        nav.navigateToPiutang();
        ((Stage) navPiutang.getScene().getWindow()).close();
    }
    @FXML private void onNavPengaturan() { setActiveNav(navPengaturan); }
    @FXML private void onNavKaryawan()   { setActiveNav(navKaryawan); }

    private void setActiveNav(HBox selected) {
        for (HBox item : List.of(navDashboard, navProduk, navKasir,
                navPelanggan, navLaporan, navPengaturan, navKaryawan)) {
            item.getStyleClass().removeAll("nav-active");
            if (!item.getStyleClass().contains("nav-item"))
                item.getStyleClass().add("nav-item");
        }
        selected.getStyleClass().add("nav-active");
    }
    private void setupNavHover() {
        for (HBox item : List.of(navDashboard, navProduk, navKasir,
                navPelanggan, navLaporan, navPengaturan, navKaryawan)) {
            item.setOnMouseEntered(e -> item.setStyle("-fx-background-color:#252840;-fx-background-radius:10;"));
            item.setOnMouseExited(e  -> item.setStyle(""));
        }
    }

    // ══════════════════════════════════════════════════════
    // COMBO BOXES
    // ══════════════════════════════════════════════════════
    private void setupComboBoxes() {
        cmbJenisKelamin.setItems(FXCollections.observableArrayList("Laki-laki", "Perempuan"));
        cmbJabatan.setItems(FXCollections.observableArrayList("Kasir", "Admin", "Barista", "Manager", "Security", "Cleaning Service"));
        cmbStatusKerja.setItems(FXCollections.observableArrayList("Aktif", "Non Aktif"));
        cmbShift.setItems(FXCollections.observableArrayList("Siang", "Malam"));
        cmbStatusKehadiran.setItems(FXCollections.observableArrayList("Hadir", "Izin", "Sakit", "Alpha"));

        cmbPilihKaryawan.setOnAction(e -> {
            String selectedId = cmbPilihKaryawan.getValue();
            if (selectedId != null) {
                for (KaryawanModel k : karyawanList) {
                    if (k.getIdKaryawan().equals(selectedId)) {
                        txtNamaAbsensi.setText(k.getNamaKaryawan());
                        break;
                    }
                }
            }
        });
    }

    // ══════════════════════════════════════════════════════
    // SETUP TABLE KARYAWAN
    // ══════════════════════════════════════════════════════
    private void setupTableKaryawan() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idKaryawan"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("namaKaryawan"));
        colNoHp.setCellValueFactory(new PropertyValueFactory<>("noHp"));
        colTglMasuk.setCellValueFactory(new PropertyValueFactory<>("tanggalMasuk"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusKerja"));

        // FIX: gunakan tipe eksplisit pada CellFactory agar @Override benar
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
                        if (item.equalsIgnoreCase("Aktif")) {
                            badge.getStyleClass().add("badge-aktif");
                        } else {
                            badge.getStyleClass().add("badge-nonaktif");
                        }
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
    // ══════════════════════════════════════════════════════
    private void setupTableAbsensi() {
        colAbsTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        colAbsId.setCellValueFactory(new PropertyValueFactory<>("idKaryawan"));
        colAbsNama.setCellValueFactory(new PropertyValueFactory<>("namaKaryawan"));
        colAbsJabatan.setCellValueFactory(new PropertyValueFactory<>("jabatan"));
        colAbsShift.setCellValueFactory(new PropertyValueFactory<>("shiftMasuk"));
        colAbsStatus.setCellValueFactory(new PropertyValueFactory<>("statusKehadiran"));

        // FIX: gunakan tipe eksplisit pada CellFactory agar @Override benar
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
                            case "hadir": badge.getStyleClass().add("badge-hadir"); break;
                            case "izin":  badge.getStyleClass().add("badge-izin");  break;
                            case "sakit": badge.getStyleClass().add("badge-sakit"); break;
                            default:      badge.getStyleClass().add("badge-alpha"); break;
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
    // KLIK BARIS → ISI FORM
    // ══════════════════════════════════════════════════════
    private void setupTableClickKaryawan() {
        tableKaryawan.setOnMouseClicked(e -> {
            KaryawanModel selected = tableKaryawan.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            txtIdKaryawan.setText(selected.getIdKaryawan());
            txtNamaKaryawan.setText(selected.getNamaKaryawan());
            cmbJenisKelamin.setValue(selected.getJenisKelamin());
            cmbJabatan.setValue(selected.getJabatan());
            txtNoHp.setText(selected.getNoHp());
            cmbStatusKerja.setValue(selected.getStatusKerja());
            txtAlamat.setText(selected.getAlamat());
            try {
                dpTanggalMasuk.setValue(
                    LocalDate.parse(selected.getTanggalMasuk(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            } catch (Exception ex) {
                dpTanggalMasuk.setValue(null);
            }
        });
    }

    private void setupTableClickAbsensi() {
        tableAbsensi.setOnMouseClicked(e -> {
            AbsensiModel selected = tableAbsensi.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            cmbPilihKaryawan.setValue(selected.getIdKaryawan());
            txtNamaAbsensi.setText(selected.getNamaKaryawan());
            cmbShift.setValue(selected.getShiftMasuk());
            cmbStatusKehadiran.setValue(selected.getStatusKehadiran());
            try {
                dpTanggalAbsensi.setValue(
                    LocalDate.parse(selected.getTanggal(), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            } catch (Exception ex) {
                dpTanggalAbsensi.setValue(null);
            }
        });
    }

    // ══════════════════════════════════════════════════════
    // LOAD DATA KARYAWAN
    // ══════════════════════════════════════════════════════
    @FXML
    public void loadDataKaryawan() {
        karyawanList.clear();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement  stmt = conn.createStatement();
             ResultSet  rs   = stmt.executeQuery("SELECT * FROM karyawan ORDER BY id_karyawan")) {

            ObservableList<String> idList = FXCollections.observableArrayList();
            while (rs.next()) {
                String id  = rs.getString("id_karyawan");
                String tgl = rs.getDate("tanggal_masuk") != null
                    ? rs.getDate("tanggal_masuk").toLocalDate().format(fmt) : "";
                karyawanList.add(new KaryawanModel(
                    id,
                    rs.getString("nama_karyawan"),
                    rs.getString("jenis_kelamin"),
                    rs.getString("jabatan"),
                    rs.getString("no_hp"),
                    tgl,
                    rs.getString("status_kerja"),
                    rs.getString("alamat") != null ? rs.getString("alamat") : ""
                ));
                idList.add(id);
            }
            cmbPilihKaryawan.setItems(idList);

        } catch (SQLException ex) {
            loadDummyKaryawan();
        }
    }

    private void loadDummyKaryawan() {
        karyawanList.addAll(
            new KaryawanModel("KRY001","Ahmad Fauzi","Laki-laki","Barista","08123456789","01-01-2026","Aktif","Jl. Kabon Jeruk No. 12, RT 03 RW 05"),
            new KaryawanModel("KRY002","Budi Santoso","Laki-laki","Kasir","08122000001","03-01-2026","Aktif","Jl. Melati No. 5"),
            new KaryawanModel("KRY003","Siti Aulia","Perempuan","Manager","08133000002","05-01-2026","Aktif","Jl. Mawar No. 3"),
            new KaryawanModel("KRY004","Anan Ting","Laki-laki","Manager","08124000003","09-01-2026","Aktif","Jl. Kenanga No. 7"),
            new KaryawanModel("KRY005","Siti Nadinli","Perempuan","Kasir","08122000004","10-01-2026","Non Aktif","Jl. Dahlia No. 2"),
            new KaryawanModel("KRY006","Dinda Ayu","Perempuan","Admin","08135000005","12-01-2026","Aktif","Jl. Anggrek No. 9")
        );
        ObservableList<String> ids = FXCollections.observableArrayList();
        for (KaryawanModel k : karyawanList) ids.add(k.getIdKaryawan());
        cmbPilihKaryawan.setItems(ids);
    }

    // ══════════════════════════════════════════════════════
    // LOAD DATA ABSENSI
    // ══════════════════════════════════════════════════════
    @FXML
    public void loadDataAbsensi() {
        absensiList.clear();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement  stmt = conn.createStatement();
             ResultSet  rs   = stmt.executeQuery(
                 "SELECT a.*, k.nama_karyawan, k.jabatan FROM absensi a " +
                 "JOIN karyawan k ON a.id_karyawan = k.id_karyawan ORDER BY a.tanggal DESC")) {

            while (rs.next()) {
                String tgl = rs.getDate("tanggal") != null
                    ? rs.getDate("tanggal").toLocalDate().format(fmt) : "";
                absensiList.add(new AbsensiModel(
                    rs.getInt("id_absensi"),
                    rs.getString("id_karyawan"),
                    rs.getString("nama_karyawan"),
                    rs.getString("jabatan"),
                    tgl,
                    rs.getString("jam_masuk") != null ? rs.getString("jam_masuk") : "Siang",
                    rs.getString("status_kehadiran")
                ));
            }
        } catch (SQLException ex) {
            loadDummyAbsensi();
        }
    }

    private void loadDummyAbsensi() {
        absensiList.addAll(
            new AbsensiModel(1,"KRY001","Ahmad Fauzi","Barista","03-06-2026","Siang","Hadir"),
            new AbsensiModel(2,"KRY002","Budi Santoso","Kasir","03-06-2026","Malam","Hadir"),
            new AbsensiModel(3,"KRY003","Siti Aulia","Manager","03-06-2026","Siang","Izin"),
            new AbsensiModel(4,"KRY004","Anan Ting","Manager","03-06-2026","Malam","Sakit"),
            new AbsensiModel(5,"KRY005","Siti Nadinli","Kasir","03-06-2026","Siang","Alpha")
        );
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
                    if (num > max) max = num;
                } catch (Exception ignored) {}
            }
            txtIdKaryawan.setText(String.format("KRY%03d", max + 1));
        }
        txtIdKaryawan.setEditable(false);
    }

    // ══════════════════════════════════════════════════════
    // VALIDASI
    // ══════════════════════════════════════════════════════
    private boolean validasiKaryawan() {
        if (txtNamaKaryawan.getText().isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Nama karyawan wajib diisi!"); return false;
        }
        if (cmbJenisKelamin.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih jenis kelamin!"); return false;
        }
        if (cmbJabatan.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih jabatan!"); return false;
        }
        if (txtNoHp.getText().isBlank()) {
            showAlert(Alert.AlertType.WARNING, "No HP wajib diisi!"); return false;
        }
        if (dpTanggalMasuk.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih tanggal masuk!"); return false;
        }
        if (cmbStatusKerja.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Pilih status kerja!"); return false;
        }
        return true;
    }

    // ══════════════════════════════════════════════════════
    // CRUD KARYAWAN
    // ══════════════════════════════════════════════════════
    @FXML
    public void tambahKaryawan() {
        if (!validasiKaryawan()) return;
        String id     = txtIdKaryawan.getText();
        String tgl    = dpTanggalMasuk.getValue().format(fmt);
        String alamat = txtAlamat.getText();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO karyawan(id_karyawan,nama_karyawan,jenis_kelamin,jabatan,no_hp,tanggal_masuk,status_kerja,alamat) VALUES(?,?,?,?,?,?,?,?)")) {
            ps.setString(1, id);
            ps.setString(2, txtNamaKaryawan.getText());
            ps.setString(3, cmbJenisKelamin.getValue());
            ps.setString(4, cmbJabatan.getValue());
            ps.setString(5, txtNoHp.getText());
            ps.setDate  (6, Date.valueOf(dpTanggalMasuk.getValue()));
            ps.setString(7, cmbStatusKerja.getValue());
            ps.setString(8, alamat);
            ps.executeUpdate();
        } catch (SQLException ex) { /* simpan ke list saja */ }

        karyawanList.add(new KaryawanModel(id, txtNamaKaryawan.getText(),
            cmbJenisKelamin.getValue(), cmbJabatan.getValue(),
            txtNoHp.getText(), tgl, cmbStatusKerja.getValue(), alamat));
        cmbPilihKaryawan.getItems().add(id);
        showAlert(Alert.AlertType.INFORMATION, "Karyawan berhasil ditambahkan!");
        resetKaryawan();
    }

    @FXML
    public void simpanKaryawan() {
        KaryawanModel selected = tableKaryawan.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert(Alert.AlertType.WARNING,"Pilih karyawan di tabel terlebih dahulu!"); return; }
        if (!validasiKaryawan()) return;

        String tgl = dpTanggalMasuk.getValue().format(fmt);
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(
                 "UPDATE karyawan SET nama_karyawan=?,jenis_kelamin=?,jabatan=?,no_hp=?,tanggal_masuk=?,status_kerja=?,alamat=? WHERE id_karyawan=?")) {
            ps.setString(1, txtNamaKaryawan.getText());
            ps.setString(2, cmbJenisKelamin.getValue());
            ps.setString(3, cmbJabatan.getValue());
            ps.setString(4, txtNoHp.getText());
            ps.setDate  (5, Date.valueOf(dpTanggalMasuk.getValue()));
            ps.setString(6, cmbStatusKerja.getValue());
            ps.setString(7, txtAlamat.getText());
            ps.setString(8, selected.getIdKaryawan());
            ps.executeUpdate();
        } catch (SQLException ex) { /* update list saja */ }

        selected.setNamaKaryawan(txtNamaKaryawan.getText());
        selected.setJenisKelamin(cmbJenisKelamin.getValue());
        selected.setJabatan(cmbJabatan.getValue());
        selected.setNoHp(txtNoHp.getText());
        selected.setTanggalMasuk(tgl);
        selected.setStatusKerja(cmbStatusKerja.getValue());
        selected.setAlamat(txtAlamat.getText());
        tableKaryawan.refresh();
        showAlert(Alert.AlertType.INFORMATION,"Data karyawan berhasil diperbarui!");
        resetKaryawan();
    }

    @FXML
    public void ubahKaryawan() { simpanKaryawan(); }

    @FXML
    public void hapusKaryawan() {
        KaryawanModel selected = tableKaryawan.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert(Alert.AlertType.WARNING,"Pilih karyawan di tabel terlebih dahulu!"); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "Hapus karyawan " + selected.getNamaKaryawan() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                     PreparedStatement ps = conn.prepareStatement("DELETE FROM karyawan WHERE id_karyawan=?")) {
                    ps.setString(1, selected.getIdKaryawan());
                    ps.executeUpdate();
                } catch (SQLException ex) { /* hapus dari list saja */ }
                karyawanList.remove(selected);
                cmbPilihKaryawan.getItems().remove(selected.getIdKaryawan());
                resetKaryawan();
            }
        });
    }

    @FXML
    public void resetKaryawan() {
        txtNamaKaryawan.clear();
        cmbJenisKelamin.setValue(null);
        cmbJabatan.setValue(null);
        txtNoHp.clear();
        dpTanggalMasuk.setValue(null);
        cmbStatusKerja.setValue(null);
        txtAlamat.clear();
        tableKaryawan.getSelectionModel().clearSelection();
        generateNextId();
    }

    // ══════════════════════════════════════════════════════
    // CRUD ABSENSI
    // ══════════════════════════════════════════════════════
    @FXML
    public void simpanAbsensi() {
        if (dpTanggalAbsensi.getValue() == null)  { showAlert(Alert.AlertType.WARNING,"Pilih tanggal absensi!"); return; }
        if (cmbPilihKaryawan.getValue()  == null)  { showAlert(Alert.AlertType.WARNING,"Pilih karyawan!"); return; }
        if (cmbShift.getValue()          == null)  { showAlert(Alert.AlertType.WARNING,"Pilih shift!"); return; }
        if (cmbStatusKehadiran.getValue()== null)  { showAlert(Alert.AlertType.WARNING,"Pilih status kehadiran!"); return; }

        String tgl    = dpTanggalAbsensi.getValue().format(fmt);
        String idKary = cmbPilihKaryawan.getValue();
        String nama   = txtNamaAbsensi.getText();
        String jabatan = "-";
        for (KaryawanModel k : karyawanList) {
            if (k.getIdKaryawan().equals(idKary)) { jabatan = k.getJabatan(); break; }
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(
                 "INSERT INTO absensi(id_karyawan,tanggal,jam_masuk,status_kehadiran) VALUES(?,?,?,?)")) {
            ps.setString(1, idKary);
            ps.setDate  (2, Date.valueOf(dpTanggalAbsensi.getValue()));
            ps.setString(3, cmbShift.getValue());
            ps.setString(4, cmbStatusKehadiran.getValue());
            ps.executeUpdate();
        } catch (SQLException ex) { /* simpan ke list saja */ }

        int newId = absensiList.isEmpty() ? 1 : absensiList.get(absensiList.size()-1).getIdAbsensi() + 1;
        absensiList.add(new AbsensiModel(newId, idKary, nama, jabatan, tgl,
            cmbShift.getValue(), cmbStatusKehadiran.getValue()));
        showAlert(Alert.AlertType.INFORMATION,"Absensi berhasil disimpan!");
        resetAbsensi();
    }

    @FXML
    public void ubahAbsensi() {
        AbsensiModel selected = tableAbsensi.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert(Alert.AlertType.WARNING,"Pilih absensi di tabel terlebih dahulu!"); return; }
        if (dpTanggalAbsensi.getValue() == null || cmbShift.getValue() == null || cmbStatusKehadiran.getValue() == null) {
            showAlert(Alert.AlertType.WARNING,"Lengkapi form absensi!"); return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(
                 "UPDATE absensi SET tanggal=?,jam_masuk=?,status_kehadiran=? WHERE id_absensi=?")) {
            ps.setDate  (1, Date.valueOf(dpTanggalAbsensi.getValue()));
            ps.setString(2, cmbShift.getValue());
            ps.setString(3, cmbStatusKehadiran.getValue());
            ps.setInt   (4, selected.getIdAbsensi());
            ps.executeUpdate();
        } catch (SQLException ex) { /* update list saja */ }

        selected.setTanggal(dpTanggalAbsensi.getValue().format(fmt));
        selected.setShiftMasuk(cmbShift.getValue());
        selected.setStatusKehadiran(cmbStatusKehadiran.getValue());
        tableAbsensi.refresh();
        showAlert(Alert.AlertType.INFORMATION,"Absensi berhasil diperbarui!");
        resetAbsensi();
    }

    @FXML
    public void hapusAbsensi() {
        AbsensiModel selected = tableAbsensi.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert(Alert.AlertType.WARNING,"Pilih absensi di tabel terlebih dahulu!"); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "Hapus data absensi ini?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                     PreparedStatement ps = conn.prepareStatement("DELETE FROM absensi WHERE id_absensi=?")) {
                    ps.setInt(1, selected.getIdAbsensi());
                    ps.executeUpdate();
                } catch (SQLException ex) { /* hapus list saja */ }
                absensiList.remove(selected);
                resetAbsensi();
            }
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
}