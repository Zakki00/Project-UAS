package com.mycompany.projectuas;

import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.mycompany.Model.PiutangModel;
import com.mycompany.Model.PiutangModel.DaftarPaketPS;
import com.mycompany.Model.PiutangModel.DataHutang;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PiutangController implements Initializable {
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

    // ═══════════════════════════════════════════════════════
    // FXML — KPI TOPBAR
    // ═══════════════════════════════════════════════════════

    @FXML
    private Label kpiTotalPiutang;
    @FXML
    private Label kpiJumlahTransaksi;
    @FXML
    private Label kpiHariIni;
    @FXML
    private Label lblDeltaPiutang;
    @FXML
    private Label lblDeltaTransaksi;
    @FXML
    private Label lblDeltaHariIni;

    // ═══════════════════════════════════════════════════════
    // FXML — TABEL HUTANG
    // ═══════════════════════════════════════════════════════

    @FXML
    private TextField tfPelanggan;
    @FXML
    private Button btnClearSearch;
    @FXML
    private TableView<DataHutang> tableHutang;
    @FXML
    private TableColumn<DataHutang, String> colNo;
    @FXML
    private TableColumn<DataHutang, String> colNama;
    @FXML
    private TableColumn<DataHutang, String> colTotalPembayaran;
    @FXML
    private TableColumn<DataHutang, String> colUangPembayaran;
    @FXML
    private TableColumn<DataHutang, String> colKekurangan;
    @FXML
    private TableColumn<DataHutang, String> colTanggal_Transaksi;

    // ═══════════════════════════════════════════════════════
    // FXML — FORM PEMBAYARAN
    // ═══════════════════════════════════════════════════════

    @FXML
    private TextField tfTunai;
    @FXML
    private Label lblKembalian;
    @FXML
    private Label lblNamaPelanggan;
    @FXML
    private Label lblIdTransaksi;
    @FXML
    private Label lblJumlahHutang;
    @FXML
    private Button btnLunas;
    @FXML
    private Button btnBatal;
    @FXML
    private Button btnQuick5;
    @FXML
    private Button btnQuick10;
    @FXML
    private Button btnQuick20;
    @FXML
    private Button btnQuick50;
    @FXML
    private ScrollPane detailScroll;

    // ═══════════════════════════════════════════════════════
    // KONSTANTA
    // ═══════════════════════════════════════════════════════

    private static final NumberFormat FMT = NumberFormat.getInstance(new Locale("id", "ID"));
    private static final double SIDEBAR_FULL = 220;
    private static final double SIDEBAR_MINI = 60;

    // ═══════════════════════════════════════════════════════
    // STATE / VARIABEL
    // ═══════════════════════════════════════════════════════

    private Stage myStage;
    private VBox detailList;
    private boolean sidebarCollapsed = false;
    private boolean isUpdating = false;
    private long kembalian;
    private long tunai;

    // ═══════════════════════════════════════════════════════
    // INITIALIZE
    // ═══════════════════════════════════════════════════════

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTableHutang();
        setupFormPembayaran();
        setupDetailList();
        loadSemuaData();
        setActiveNav(navPiutang);
        setupNavHover();
        setupLogoutHover();
        setupForm();
    }

    public void setStage(Stage stage) {
        this.myStage = stage;
    }


    private void setupForm(){
        // notif---------------
        Notifikasi.updateBadge(notifBadge);

        // ====tanggal====
        Locale localeID = new Locale("id", "ID");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", localeID);
        tanggal.setText(LocalDate.now().format(formatter));

        session.applyFotoProfile(lblAvatartopbar, lblAvatarnavbar,
                imgAvatarGoogletopbar, imgAvatarGooglenavbar);
        if (session.email == "") {
            navllblakun.setText(session.role);
            navlblnama.setText(session.nama);
        } else {
            navllblakun.setText(session.role);
            navlblnama.setText(session.nama);
        }

          //===logo
        Platform.runLater(() -> {
            Stage stage = (Stage) navMenu.getScene().getWindow();
            Image icon = new Image(getClass().getResourceAsStream("/image/Logo.png"));
            stage.getIcons().add(icon);
        });
    }

    // ── Kelompok setup saat initialize ──────────────────────

    /** Setup tabel, search, dan row click */
    private void setupTableHutang() {
        setupTable();
        setupSearch();
        setupRowClick();
        tableHutang.setItems(PiutangModel.dataHutang);
    }

    /** Setup semua komponen form pembayaran */
    private void setupFormPembayaran() {
        tfTunai.setDisable(true);
        lblKembalian.setText("Rp 0");
        lblNamaPelanggan.setText("-");
        lblJumlahHutang.setText("Rp 0");
        setQuickButtonsDisable(true);
    }

    /** Setup scroll dan VBox untuk list detail barang */
    private void setupDetailList() {
        detailList = new VBox(10);
        detailList.getStyleClass().add("detail-list");
        detailList.setPadding(new Insets(12, 0, 12, 0));

        detailScroll.setContent(detailList);
        detailScroll.getStyleClass().add("detail-scroll");
        detailScroll.setFitToWidth(true);
    }

    /** Load semua data awal ke UI */
    private void loadSemuaData() {
        load_data_hutang("");
        renderList();
        renderListKPI();
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
       
    }

    @FXML
    private void onNavPengaturan() {
        setActiveNav(navPengaturan);
        new navigation().navigataeToPengaturan();
        ((Stage) navPengaturan.getScene().getWindow()).close();
    }

    // ═══════════════════════════════════════════════════════
    // DATABASE — LOAD DATA
    // ═══════════════════════════════════════════════════════

    private void load_data_hutang(String namaPelanggan) {
        PiutangModel.dataHutang.clear();

        String sql = """
                SELECT t.id_transaksi,
                       t.pelanggan        AS nama_pelanggan,
                       t.total_pembayaran,
                       t.uang_pembayaran,
                       t.kekurangan,
                       t.status_pembayaran,
                       t.tanggal_transaksi
                FROM tb_transaksi t
                WHERE t.status_pembayaran = 'Belum Lunas'
                  AND t.pelanggan LIKE ?
                """;

        List<Object[]> results = koneksi.ambilData(sql, "%" + namaPelanggan + "%");
        System.out.println("Data hutang ditemukan: " + results.size() + " baris");

        int rowNo = 1;
        for (Object[] row : results) {
            String idTransaksi = String.valueOf(row[0]);
            String namaPelangganR = String.valueOf(row[1]);
            long totalPembayaran = ((Number) row[2]).longValue();
            long uangPembayaran = ((Number) row[3]).longValue();
            long kekurangan = ((Number) row[4]).longValue();
            String status = String.valueOf(row[5]);
            String tanggal = String.valueOf(row[6]);

            PiutangModel.dataHutang.add(new DataHutang(
                    rowNo++, idTransaksi, namaPelangganR,
                    totalPembayaran, uangPembayaran, kekurangan, status, tanggal));
        }
    }

    private void load_data_barang(DataHutang dataHutang) {
        PiutangModel.dataBarang.clear();

        String sql = """
                SELECT b.nama_barang,
                       b.harga,
                       td.jumlah
                FROM tb_detail_transaksi td
                JOIN tb_barang b ON td.id_barang = b.id_barang
                WHERE td.id_transaksi = ?
                """;

        List<Object[]> results = koneksi.ambilData(sql, dataHutang.idTransaksi);
        for (Object[] row : results) {
            String nama = String.valueOf(row[0]);
            long harga = ((Number) row[1]).longValue();
            int qty = ((Number) row[2]).intValue();
            PiutangModel.dataBarang.add(new PiutangModel.DataBarangHutang(nama, harga, qty));
        }
    }

    private void loadDataPaketPS(DataHutang dataHutang) {
        PiutangModel.daftarpaketps = null;

        String sql = """
                SELECT durasi, harga
                FROM tb_paket_ps
                WHERE id_transaksi = ?
                """;

        List<Object[]> results = koneksi.ambilData(sql, dataHutang.idTransaksi);
        if (!results.isEmpty()) {
            Object[] row = results.get(0);
            int durasi = ((Number) row[0]).intValue();
            long harga = ((Number) row[1]).longValue();
            PiutangModel.daftarpaketps = new DaftarPaketPS(durasi, harga);
        }
    }

    // ═══════════════════════════════════════════════════════
    // TABEL — SETUP & SEARCH
    // ═══════════════════════════════════════════════════════

    private void setupTable() {
        colNo.setCellValueFactory(d -> new SimpleStringProperty(String.valueOf(d.getValue().no)));
        colNama.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().namaPelanggan));
        colTotalPembayaran
                .setCellValueFactory(d -> new SimpleStringProperty("Rp " + FMT.format(d.getValue().total_pembayaran)));
        colUangPembayaran
                .setCellValueFactory(d -> new SimpleStringProperty("Rp " + FMT.format(d.getValue().uang_pembayaran)));
        colKekurangan.setCellValueFactory(d -> new SimpleStringProperty("Rp " + FMT.format(d.getValue().kekurangan)));
        colTanggal_Transaksi.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().tanggal_transaksi));

        tableHutang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupSearch() {
        tfPelanggan.textProperty().addListener((obs, oldVal, newVal) -> {
            load_data_hutang(newVal);
            tableHutang.setItems(PiutangModel.dataHutang);
        });
    }

    private void setupRowClick() {
        tableHutang.setOnMouseClicked(e -> {
            DataHutang selected = tableHutang.getSelectionModel().getSelectedItem();
            if (selected == null)
                return;

            lblIdTransaksi.setText(selected.idTransaksi);
            lblNamaPelanggan.setText(selected.namaPelanggan);
            lblJumlahHutang.setText("Rp " + FMT.format(selected.kekurangan));

            load_data_barang(selected);
            loadDataPaketPS(selected);
            renderList();

            tfPelanggan.setDisable(true);
            tfTunai.setDisable(false);
            setQuickButtonsDisable(false);

            System.out.println("Transaksi dipilih: " + selected.idTransaksi
                    + " | Barang: " + PiutangModel.dataBarang.size());
        });
    }

    @FXML
    private void onClearSearch() {
        tfPelanggan.setText("Cari Nama Pelanggan");
        load_data_hutang("");
        tableHutang.setItems(PiutangModel.dataHutang);
        tableHutang.refresh();
    }

    // ═══════════════════════════════════════════════════════
    // KPI — LOAD & RENDER
    // ═══════════════════════════════════════════════════════

    void renderListKPI() {
        loadTotalPiutang();
        loadJumlahTransaksiBelumLunas();
        loadPiutangHariIni();
    }

    private void loadTotalPiutang() {
        double today = querySum("""
                SELECT COALESCE(SUM(kekurangan),0) FROM tb_transaksi
                WHERE status_pembayaran='Belum Lunas'
                  AND DATE(tanggal_transaksi)=DATE('now','localtime')
                """);
        double yesterday = querySum("""
                SELECT COALESCE(SUM(kekurangan),0) FROM tb_transaksi
                WHERE status_pembayaran='Belum Lunas'
                  AND DATE(tanggal_transaksi)=DATE('now','localtime','-1 day')
                """);

        kpiTotalPiutang.setText("Rp " + String.format("%,.0f", today));
        setPersenKPI(lblDeltaPiutang, today, yesterday);
    }

    private void loadJumlahTransaksiBelumLunas() {
        double today = querySum("""
                SELECT COUNT(*) FROM tb_transaksi
                WHERE status_pembayaran='Belum Lunas'
                  AND DATE(tanggal_transaksi)=DATE('now','localtime')
                """);
        double yesterday = querySum("""
                SELECT COUNT(*) FROM tb_transaksi
                WHERE status_pembayaran='Belum Lunas'
                  AND DATE(tanggal_transaksi)=DATE('now','localtime','-1 day')
                """);

        kpiJumlahTransaksi.setText(String.valueOf((int) today));
        setPersenKPI(lblDeltaTransaksi, today, yesterday);
    }

    private void loadPiutangHariIni() {
        double today = querySum("""
                SELECT COALESCE(SUM(kekurangan),0) FROM tb_transaksi
                WHERE status_pembayaran='Belum Lunas'
                  AND DATE(tanggal_transaksi)=DATE('now','localtime')
                """);
        double yesterday = querySum("""
                SELECT COALESCE(SUM(kekurangan),0) FROM tb_transaksi
                WHERE status_pembayaran='Belum Lunas'
                  AND DATE(tanggal_transaksi)=DATE('now','localtime','-1 day')
                """);

        kpiHariIni.setText("Rp " + String.format("%,.0f", today));
        setPersenKPI(lblDeltaHariIni, today, yesterday);
    }

    /**
     * Helper: jalankan query single-value dan kembalikan hasilnya sebagai double
     */
    private double querySum(String sql) {
        List<Object[]> rows = koneksi.ambilData(sql);
        if (!rows.isEmpty() && rows.get(0)[0] != null)
            return ((Number) rows.get(0)[0]).doubleValue();
        return 0;
    }

    private void setPersenKPI(Label label, double today, double yesterday) {
        double persen;
        if (yesterday == 0) {
            persen = today > 0 ? 100 : 0;
        } else {
            persen = ((today - yesterday) / yesterday) * 100;
        }

        String icon, status, style;
        if (persen > 0) {
            icon = "▲ +";
            status = "lebih besar dari kemarin";
            style = "-fx-font-weight: bold; -fx-text-fill: #ef4444;";
        } else if (persen < 0) {
            icon = "▼ -";
            status = "lebih kecil dari kemarin";
            style = "-fx-font-weight: bold; -fx-text-fill: #22c55e;";
        } else {
            icon = "■ ";
            status = "tidak berubah dari kemarin";
            style = "-fx-font-weight: bold; -fx-text-fill: #9ca3af;";
        }

        label.setText(icon + String.format("%.1f%% %s", Math.abs(persen), status));
        label.setStyle(style);
    }

    // ═══════════════════════════════════════════════════════
    // DETAIL LIST — RENDER
    // ═══════════════════════════════════════════════════════

    private void renderList() {
        detailList.getChildren().clear();

        if (PiutangModel.dataBarang.isEmpty()) {
            detailList.getChildren().add(buildEmptyState());
            btnLunas.setDisable(true);
            return;
        }

        detailList.getChildren().add(buildTableHeader());

        int no = 1;
        for (PiutangModel.DataBarangHutang barang : PiutangModel.dataBarang) {
            detailList.getChildren().add(buildRowBarang(barang, no++));
        }
        if (PiutangModel.daftarpaketps != null) {
            detailList.getChildren().add(buildRowPaketPS(PiutangModel.daftarpaketps, no));
        }

        updateSummary();
        btnLunas.setDisable(false);
        System.out.println("Detail barang ditampilkan: " + PiutangModel.dataBarang.size() + " item");
    }

    private VBox buildEmptyState() {
        Label icon = new Label("💰");
        icon.getStyleClass().add("empty-icon");

        Label text = new Label("Belum ada item");
        text.getStyleClass().add("empty-text");

        Label sub = new Label("Pilih Data Hutang untuk melihat detail barang");
        sub.getStyleClass().add("empty-sub");

        VBox empty = new VBox(8, icon, text, sub);
        empty.setAlignment(Pos.CENTER);
        empty.setPadding(new Insets(60, 0, 60, 0));
        return empty;
    }

    private HBox buildTableHeader() {
        Label thNo = buildHeaderLabel("No", 30, Pos.CENTER);
        Label thNama = buildHeaderLabel("Nama Produk", 100, Pos.CENTER_LEFT);
        Label thHarga = buildHeaderLabel("Harga", 90, Pos.CENTER);
        Label thQty = buildHeaderLabel("Qty", 50, Pos.CENTER);

        HBox header = new HBox(5, thNo, thNama, thHarga, thQty);
        header.getStyleClass().add("table-header");
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    private HBox buildRowBarang(PiutangModel.DataBarangHutang barang, int no) {
        Label lblNo = buildCellLabel(String.valueOf(no), "item-no", 30, Pos.CENTER);
        Label lblNama = buildCellLabel(barang.nama_barang, "item-nama", 100, Pos.CENTER_LEFT);
        Label lblHarga = buildCellLabel("Rp " + FMT.format(barang.harga_barang), "item-harga", 90, Pos.CENTER);
        Label lblQty = buildCellLabel("x" + barang.qty, "item-qty", 50, Pos.CENTER);

        HBox row = new HBox(5, lblNo, lblNama, lblHarga, lblQty);
        row.getStyleClass().add("item-row");
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private HBox buildRowPaketPS(PiutangModel.DaftarPaketPS paketPS, int no) {
        Label lblNo = buildCellLabel(String.valueOf(no), "item-no", 30, Pos.CENTER);
        Label lblNama = buildCellLabel("Paket Play Station", "item-nama", 100, Pos.CENTER_LEFT);
        Label lblHarga = buildCellLabel("Rp " + FMT.format(paketPS.harga), "item-harga", 90, Pos.CENTER);

        HBox row = new HBox(5, lblNo, lblNama, lblHarga);
        row.getStyleClass().add("item-row");
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    /** Helper: buat label untuk header tabel */
    private Label buildHeaderLabel(String text, double width, Pos alignment) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("th-label");
        lbl.setPrefWidth(width);
        lbl.setMinWidth(width);
        lbl.setMaxWidth(width);
        lbl.setAlignment(alignment);
        return lbl;
    }

    /** Helper: buat label untuk cell baris */
    private Label buildCellLabel(String text, String styleClass, double width, Pos alignment) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add(styleClass);
        lbl.setPrefWidth(width);
        lbl.setMinWidth(width);
        lbl.setMaxWidth(width);
        lbl.setAlignment(alignment);
        return lbl;
    }

    // ═══════════════════════════════════════════════════════
    // FORM PEMBAYARAN — HANDLER
    // ═══════════════════════════════════════════════════════

    @FXML
    private void onLunas() {
        Stage ownerStage = (Stage) btnLunas.getScene().getWindow();

        if (tfTunai.getText().isEmpty() || kembalian < 0) {
            new Popup().showModernPopup("ERROR",
                    "Pembayaran tidak valid. Pastikan jumlah tunai mencukupi untuk melunasi hutang.",
                    Popup.PopupType.ERROR, ownerStage);
            return;
        }

        new Popup().showConfirmPopup("KONFIRMASI",
                "Apakah Anda yakin ingin menandai transaksi ini sebagai LUNAS?",
                () -> prosesLunas(ownerStage));
    }

    private void prosesLunas(Stage ownerStage) {
        String idTransaksi = lblIdTransaksi.getText();
        long tunaiMasuk = parseLong(tfTunai.getText().replaceAll("[^0-9]", ""));
        long kembalianNum = Math.max(kembalian, 0);

        String sql = """
                UPDATE tb_transaksi SET
                    uang_pembayaran   = uang_pembayaran + ?,
                    status_pembayaran = 'Lunas',
                    kembalian         = ?,
                    kekurangan        = 0
                WHERE id_transaksi = ?
                """;
        koneksi.eksekusiQuery(sql, tunaiMasuk, kembalianNum, idTransaksi);
        System.out.println("Transaksi " + idTransaksi + " ditandai LUNAS");

        // Reset dan refresh UI
        PiutangModel.dataBarang.clear();
        renderList();
        load_data_hutang(tfPelanggan.getText());
        tableHutang.setItems(PiutangModel.dataHutang);
        tableHutang.refresh();
        renderListKPI();

        resetFormSetelahLunas();
        new Popup().showModernPopup("SUKSES", "Transaksi berhasil ditandai sebagai LUNAS.",
                Popup.PopupType.SUCCESS, ownerStage);
    }

    private void resetFormSetelahLunas() {
        clearForm();
        onClearSearch();
        tfPelanggan.setDisable(false);
    }

    @FXML
    private void onBatal() {
        PiutangModel.dataBarang.clear();
        renderList();
        clearForm();
        setupFormPembayaran();
        tfPelanggan.setDisable(false);
    }

    private void clearForm() {
        lblIdTransaksi.setText("");
        lblNamaPelanggan.setText("");
        lblJumlahHutang.setText("Rp 0");
        tfTunai.setText("");
        lblKembalian.setText("Rp 0");
    }

    @FXML
    private void onTunaiChanged() {
        if (isUpdating)
            return;
        isUpdating = true;

        String raw = tfTunai.getText().replaceAll("[^0-9]", "");
        if (raw.isEmpty()) {
            tfTunai.setText("");
        } else {
            long value = Long.parseLong(raw);
            tfTunai.setText("Rp " + FMT.format(value));
            tfTunai.positionCaret(tfTunai.getText().length());
        }

        isUpdating = false;
        updateSummary();
    }

    public void updateSummary() {
        DataHutang selected = tableHutang.getSelectionModel().getSelectedItem();
        tunai = parseLong(tfTunai.getText().replaceAll("[^0-9]", ""));
        kembalian = tunai - (selected != null ? selected.kekurangan : 0);

        lblKembalian.setText(kembalian >= 0
                ? "Kembalian Rp " + FMT.format(kembalian)
                : "Kurang Rp " + FMT.format(Math.abs(kembalian)));
        lblKembalian.setStyle(kembalian >= 0
                ? "-fx-text-fill: #00E5A0;"
                : "-fx-text-fill: #FF5C7C;");
    }

    // ═══════════════════════════════════════════════════════
    // TOMBOL QUICK AMOUNT
    // ═══════════════════════════════════════════════════════

    @FXML
    private void onQuick5() {
        setQuickAmount("5.000");
    }

    @FXML
    private void onQuick10() {
        setQuickAmount("10.000");
    }

    @FXML
    private void onQuick20() {
        setQuickAmount("20.000");
    }

    @FXML
    private void onQuick50() {
        setQuickAmount("50.000");
    }

    private void setQuickAmount(String amount) {
        tfTunai.setText(amount);
        updateSummary();
    }

    private void setQuickButtonsDisable(boolean disable) {
        btnQuick5.setDisable(disable);
        btnQuick10.setDisable(disable);
        btnQuick20.setDisable(disable);
        btnQuick50.setDisable(disable);
    }

    // ═══════════════════════════════════════════════════════
    // NOTIFIKASI
    // ═══════════════════════════════════════════════════════

    @FXML
    private void onNotif() {
        Stage stage = (Stage) notifBadge.getScene().getWindow();
        Notifikasi.show(stage);
    }

    // ═══════════════════════════════════════════════════════
    // UTILITY
    // ═══════════════════════════════════════════════════════

    private long parseLong(String s) {
        try {
            return (s == null || s.isBlank()) ? 0 : Long.parseLong(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}