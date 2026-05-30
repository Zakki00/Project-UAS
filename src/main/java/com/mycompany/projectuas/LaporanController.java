package com.mycompany.projectuas;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mycompany.Model.LaporanModel;
import com.mycompany.Model.LaporanModel.LaporanTransaksiItem;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author zakki mubarroq
 */
public class LaporanController implements Initializable {
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
    private Label kpiPenjulanPersen;

    @FXML
    private Label kpiTransaksi;
    @FXML
    private Label kpiTransaksiPersen;

    @FXML
    private Label kpiProduk;
    @FXML
    private Label kpiProdukPersen;

    @FXML
    private Label kpiStok;
    @FXML
    private Label kpiStokInfo;

    @FXML
    private Label chartslbstok;
    @FXML
    private Label chartslbSegeraStok;
    // ── FXML table laporan ──────────────────────────────────
    @FXML
    private DatePicker dpTanggal;
    @FXML
    private ComboBox<String> cbStatusPembayaran;
    @FXML
    private TextField tfNamaPelanggan;
    @FXML
    private TextField tfNominal;
    @FXML
    private Button btnExport;
    @FXML
    private TableView<LaporanModel.LaporanTransaksiItem> TableLaporan;
    @FXML
    private TableColumn<LaporanModel.LaporanTransaksiItem, Integer> colNo;
    @FXML
    private TableColumn<LaporanModel.LaporanTransaksiItem, Integer> colIdTransaksi;
    @FXML
    private TableColumn<LaporanModel.LaporanTransaksiItem, String> colNama;
    @FXML
    private TableColumn<LaporanModel.LaporanTransaksiItem, String> colUser;
    @FXML
    private TableColumn<LaporanModel.LaporanTransaksiItem, String> colNamalengkap;
    @FXML
    private TableColumn<LaporanTransaksiItem, String> colUangPembayaran;
    @FXML
    private TableColumn<LaporanTransaksiItem, String> colKembalian;
    @FXML
    private TableColumn<LaporanTransaksiItem, String> colKekurangan;
    @FXML
    private TableColumn<LaporanTransaksiItem, String> colTotalPembayaran;
    @FXML
    private TableColumn<LaporanModel.LaporanTransaksiItem, String> colStatus;
    @FXML
    private TableColumn<LaporanModel.LaporanTransaksiItem, LocalDate> colTanggalTransaksi;
    // ── Charts ────────────────────────────────────────────
    @FXML
    private AreaChart<String, Number> salesChart;
    @FXML
    private BarChart<String, Number> trxChart;
    @FXML
    private LineChart<String, Number> monthChart;

    //=======Card View Shift
        @FXML
    private VBox cardPagi;
    @FXML
    private VBox cardMalam;
    @FXML
    private Label lblUsernamePagi;
    @FXML
    private Label lblNamaPagi;
    @FXML
    private Label lblTrxPagi;
    @FXML
    private Label lblItemPagi;
    @FXML
    private Label lblPendapatanPagi;
    @FXML
    private Label lblStatusPagi;
    @FXML
    private Label lblJamPagi;

    @FXML
    private Label lblUsernameMalam;
    @FXML
    private Label lblNamaMalam;
    @FXML
    private Label lblTrxMalam;
    @FXML
    private Label lblItemMalam;
    @FXML
    private Label lblPendapatanMalam;
    @FXML
    private Label lblStatusMalam;
    @FXML
    private Label lblJamMalam;
    @FXML
    private StackPane wrapperPagi;
    @FXML 
    private StackPane wrapperMalam;

    // @FXML
    // private StackPane wrapperMalam;

    // ----caharts--------------------------------------------
    @FXML
    private VBox vboxChart;
    // ── Stock list ────────────────────────────────────────
    @FXML
    private VBox stockList;

    // ── State ─────────────────────────────────────────────
    private boolean sidebarCollapsed = false;
    private static final double SIDEBAR_FULL = 220;
    private static final double SIDEBAR_MINI = 60;

    // ── Data ───────────────────────────────────────
    private static final NumberFormat FMT = NumberFormat.getInstance(
            new Locale("id", "ID"));

    private void loadLaporanTransaksi() {

        StringBuilder sql = new StringBuilder("""
                    SELECT
                        t.id_transaksi,
                        t.tanggal_transaksi,
                        t.pelanggan,
                        t.status_pembayaran,
                        t.total_pembayaran,
                        t.uang_pembayaran,
                        t.kembalian,
                        t.kekurangan,
                        u.username,
                        u.nama_lengkap
                    FROM tb_transaksi t
                    JOIN tb_user u ON t.id_user = u.id_user
                    WHERE 1=1
                """);

        // =========================
        // FILTER STATUS PEMBAYARAN
        // =========================
        if (cbStatusPembayaran.getValue() != null
                && !cbStatusPembayaran.getValue().isEmpty()
                && !cbStatusPembayaran.getValue().equals("Semua")) {

            sql.append(" AND t.status_pembayaran = '")
                    .append(cbStatusPembayaran.getValue())
                    .append("'");
        }

        // =========================
        // FILTER TANGGAL
        // =========================
        if (dpTanggal.getValue() != null) {

            sql.append(" AND DATE(t.tanggal_transaksi) = '")
                    .append(dpTanggal.getValue())
                    .append("'");
        }

        // =========================
        // FILTER NAMA PELANGGAN
        // =========================
        if (!tfNamaPelanggan.getText().trim().isEmpty()) {

            sql.append(" AND t.pelanggan LIKE '%")
                    .append(tfNamaPelanggan.getText().trim())
                    .append("%'");
        }

        // =========================
        // FILTER NOMINAL
        // =========================
        String raw = tfNominal.getText().replaceAll("[^0-9]", "");

        if (!raw.isEmpty()) {
            sql.append(" AND t.total_pembayaran >= ")
                    .append(Long.parseLong(raw));
        }

        // =========================
        // ORDER
        // =========================
        sql.append(" ORDER BY t.tanggal_transaksi DESC");

        List<Object[]> results = koneksi.ambilData(sql.toString());

        LaporanModel.dataLaporanTransaksi.clear();
        int no = 1;
        for (Object[] row : results) {
            int idTransaksi = ((Number) row[0]).intValue();
            LocalDate tanggalTransaksi = ((java.util.Date) row[1]).toInstant()
                    .atZone(java.time.ZoneId.systemDefault())
                    .toLocalDate();
            String pelanggan = (String) row[2];
            String statusPembayaran = (String) row[3];
            long totalPembayaran = ((Number) row[4]).longValue();
            long uangPembayaran = ((Number) row[5]).longValue();
            long kembalian = ((Number) row[6]).longValue();
            long kekurangan = ((Number) row[7]).longValue();
            String username = (String) row[8];
            String namaLengkap = (String) row[9];
            LaporanModel.dataLaporanTransaksi.add(
                    new LaporanTransaksiItem(
                            no++,
                            idTransaksi,
                            username,
                            namaLengkap,
                            pelanggan,
                            totalPembayaran,
                            uangPembayaran,
                            kembalian,
                            kekurangan,
                            statusPembayaran,
                            tanggalTransaksi));
        }
        TableLaporan.setItems(LaporanModel.dataLaporanTransaksi);
        // =========================
        // LOG
        // =========================
        if (results.isEmpty()) {

            System.out.println("Tidak ada data laporan transaksi.");

        } else {

            System.out.println(
                    "Laporan transaksi berhasil dimuat: "
                            + results.size()
                            + " data");
        }
    }

    // ═════════════════════════════════════════════════════
    // INITIALIZE
    // ══════════════════════╗
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadLaporanTransaksi();
        loadKPI();
        setupCharts();
        setupStockList();
        setupNavHover();
        SetupFrome();
        setActiveNav(navLaporan);
        setupTableLaporan();
        

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
        navigation nav = new navigation();
        nav.navigateToDashboard();
        Stage stage = (Stage) navDashboard.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void onNavProduk() {
        setActiveNav(navProduk);
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

    private void SetupFrome() {
        cbStatusPembayaran.getItems().addAll("Semua", "Lunas", "Belum Lunas");
        cbStatusPembayaran.setOnAction(e -> {
            loadLaporanTransaksi();
        });
        dpTanggal.setOnAction(e -> {
            loadLaporanTransaksi();
        });
        tfNamaPelanggan.textProperty().addListener((obs, old, niu) -> {
            loadLaporanTransaksi();
        });
        tfNominal.textProperty().addListener((obs, old, niu) -> {
            loadLaporanTransaksi();
            onNominalChanged();
        });
        if (LaporanModel.dataLaporanTransaksi.isEmpty()) {
            btnExport.setDisable(true);
        } else {
            btnExport.setDisable(false);
        }
        applyRoundedClip(wrapperPagi);
        applyRoundedClip(wrapperMalam);
    }
     // ====================================================
    // KPI
    // ====================================================
    private void loadKPI() {
        loadTotalPenjualan();
        loadTotalTransaksi();
        loadProdukTerjual();
        loadStokMenipis();
        loadShiftData();
    }

    private void setPersenKPI(Label label, double today, double yesterday) {

        double persen;

        if (yesterday == 0) {
            persen = today > 0 ? 100 : 0;
        } else {
            persen = ((today - yesterday) / yesterday) * 100;
        }

        String icon = persen >= 0 ? "▲ +" : "▼ -";
        String text = icon + String.format("%.1f", Math.abs(persen));

        label.setText(text);

        // warna langsung di sini
        if (persen > 0) {
            label.setStyle("-fx-text-fill: #22c55e;"); // hijau
        } else if (persen < 0) {
            label.setStyle("-fx-text-fill: #ef4444;"); // merah
        } else {
            label.setStyle("-fx-text-fill: #9ca3af;"); // abu-abu
        }
    }

    private void loadTotalPenjualan() {

        List<Object[]> todayData = koneksi.ambilData("""
                    SELECT COALESCE(SUM(total_pembayaran), 0)
                    FROM tb_transaksi
                    WHERE DATE(tanggal_transaksi) = CURDATE()
                """);

        List<Object[]> yesterdayData = koneksi.ambilData("""
                    SELECT COALESCE(SUM(total_pembayaran), 0)
                    FROM tb_transaksi
                    WHERE DATE(tanggal_transaksi) = CURDATE() - INTERVAL 1 DAY
                """);

        double today = ((Number) todayData.get(0)[0]).doubleValue();
        double yesterday = ((Number) yesterdayData.get(0)[0]).doubleValue();

        kpiPenjualan.setText("Rp " + String.format("%,.0f", today));
        setPersenKPI(kpiPenjulanPersen, today, yesterday);
    }

    private void loadTotalTransaksi() {

        double today = ((Number) koneksi.ambilData("""
                    SELECT COUNT(*)
                    FROM tb_transaksi
                    WHERE DATE(tanggal_transaksi) = CURDATE()
                """).get(0)[0]).doubleValue();

        double yesterday = ((Number) koneksi.ambilData("""
                    SELECT COUNT(*)
                    FROM tb_transaksi
                    WHERE DATE(tanggal_transaksi) = CURDATE() - INTERVAL 1 DAY
                """).get(0)[0]).doubleValue();

        kpiTransaksi.setText(String.valueOf((int) today));
        setPersenKPI(kpiTransaksiPersen, today, yesterday);
    }

    private void loadProdukTerjual() {

        double today = ((Number) koneksi.ambilData("""
                    SELECT COALESCE(SUM(jumlah), 0)
                    FROM tb_detail_transaksi dt
                    JOIN tb_transaksi t ON dt.id_transaksi = t.id_transaksi
                    WHERE DATE(t.tanggal_transaksi) = CURDATE()
                """).get(0)[0]).doubleValue();

        double yesterday = ((Number) koneksi.ambilData("""
                    SELECT COALESCE(SUM(jumlah), 0)
                    FROM tb_detail_transaksi dt
                    JOIN tb_transaksi t ON dt.id_transaksi = t.id_transaksi
                    WHERE DATE(t.tanggal_transaksi) = CURDATE() - INTERVAL 1 DAY
                """).get(0)[0]).doubleValue();

        kpiProduk.setText((int) today + " Unit");
        setPersenKPI(kpiProdukPersen, today, yesterday);
    }

    private void loadStokMenipis() {
        int stokMenipis = ((Number) koneksi.ambilData("""
                    SELECT COUNT(*)
                    FROM tb_barang
                    WHERE stok <= 5
                """).get(0)[0]).intValue();

        kpiStok.setText(stokMenipis + " Item");
        if (stokMenipis == 0) {
            kpiStokInfo.setText("Semua stok aman");
            kpiStokInfo.setStyle("""
                            -fx-text-fill: #00C853;
                            -fx-font-weight: bold;
                    """);
        } else if (stokMenipis <= 5) {
            kpiStokInfo.setText("⚠ Perlu restock segera");
            kpiStokInfo.setStyle("""
                            -fx-text-fill: #D50000;
                            -fx-font-weight: bold;
                    """);
        } else {
            kpiStokInfo.setText("Stok kritis");
        }
    }
    // ══════════════════════════════════════════════
    // LOAD SHIFT DATA
    // ══════════════════════════════════════════════
    private void loadShiftData() {
        loadShift(
                "06:00:00", "12:00:00", // jam mulai-selesai pagi
                lblUsernamePagi, lblNamaPagi,
                lblTrxPagi, lblItemPagi,
                lblPendapatanPagi, lblStatusPagi,
                lblJamPagi, "06:00 — 12:00");

        loadShift(
                "12:00:00", "21:00:00", // jam mulai-selesai malam
                lblUsernameMalam, lblNamaMalam,
                lblTrxMalam, lblItemMalam,
                lblPendapatanMalam, lblStatusMalam,
                lblJamMalam, "12:00 — 21:00");

        // tandai shift yang sedang aktif
        updateStatusDot();
    }

    private void loadShift(
            String jamMulai, String jamSelesai,
            Label lblUsername, Label lblNama,
            Label lblTrx, Label lblItem,
            Label lblPendapatan, Label lblStatus,
            Label lblJam, String jamText) {

        String sql = """
                SELECT
                    u.username,
                    u.nama_lengkap,
                    COUNT(t.id_transaksi)        AS total_trx,
                    SUM(dt.jumlah)               AS total_item,
                    SUM(t.total_pembayaran)      AS total_pendapatan
                FROM tb_transaksi t
                JOIN tb_user u ON t.id_user = u.id_user
                JOIN tb_detail_transaksi dt ON t.id_transaksi = dt.id_transaksi
                WHERE DATE(t.tanggal_transaksi) = CURDATE()
                AND TIME(t.tanggal_transaksi) BETWEEN '""" + jamMulai + "' AND '" + jamSelesai + """
                '
                GROUP BY u.id_user, u.username, u.nama_lengkap
                ORDER BY total_pendapatan DESC
                LIMIT 1
                """;

        List<Object[]> data = koneksi.ambilData(sql);

        lblJam.setText(jamText);

        if (data.isEmpty()) {
            lblUsername.setText("Tidak ada kasir");
            lblNama.setText("-");
            lblTrx.setText("0");
            lblItem.setText("0 unit");
            lblPendapatan.setText("Rp 0");
        } else {
            Object[] row = data.get(0);
            lblUsername.setText(String.valueOf(row[0]));
            lblNama.setText(String.valueOf(row[1]));
            lblTrx.setText(String.valueOf(((Number) row[2]).intValue()));
            lblItem.setText(((Number) row[3]).intValue() + " unit");
            lblPendapatan.setText("Rp " + FMT.format(((Number) row[4]).longValue()));
        }
    }

    // ── Tandai shift aktif berdasarkan jam sekarang ──
    private void updateStatusDot() {
        int jamSekarang = java.time.LocalTime.now().getHour();

        boolean pagiAktif = jamSekarang >= 6 && jamSekarang < 12;
        boolean malamAktif = jamSekarang >= 12 && jamSekarang < 21;

        lblStatusPagi.getStyleClass().removeAll(
                "shift-status-aktif", "shift-status-nonaktif");
        lblStatusPagi.getStyleClass().add(
                pagiAktif ? "shift-status-aktif" : "shift-status-nonaktif");

        lblStatusMalam.getStyleClass().removeAll(
                "shift-status-aktif", "shift-status-nonaktif");
        lblStatusMalam.getStyleClass().add(
                malamAktif ? "shift-status-aktif" : "shift-status-nonaktif");
    }

    // ═════════════════════════════════════════════════════
    // setup tabel laporan
    //======================================================

    private void setupTableLaporan() {
        colNo.setCellValueFactory(
                data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().no).asObject());
        colIdTransaksi.setCellValueFactory(
                data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().idTransaksi).asObject());
        colNama.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().pelanggan));
        colTotalPembayaran.setCellValueFactory(
                data -> new SimpleStringProperty("Rp " + FMT.format(data.getValue().totalPembayaran)));
        colUangPembayaran.setCellValueFactory(
                data -> new SimpleStringProperty("Rp " + FMT.format(data.getValue().uangPembayaran)));
        colKembalian.setCellValueFactory(
                data -> new SimpleStringProperty("Rp " + FMT.format(data.getValue().kembalian)));
        colKekurangan.setCellValueFactory(
                data -> new SimpleStringProperty("Rp " + FMT.format(data.getValue().kekurangan)));
        // 1. Value factory dulu
        colStatus.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().statusPembayaran));
        colStatus.setCellFactory(column -> new TableCell<LaporanModel.LaporanTransaksiItem, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);

                if (empty || status == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                    return;
                }

                setText(null);

                Label label = new Label(status);
                label.setMaxWidth(Double.MAX_VALUE);
                label.setAlignment(javafx.geometry.Pos.CENTER);
                label.setPadding(new javafx.geometry.Insets(4, 10, 4, 10));

                if (status.equalsIgnoreCase("Lunas")) {
                    label.setStyle("""
                                -fx-text-fill: #00C853;
                                -fx-font-weight: bold;
                                -fx-background-color: rgba(0, 200, 83, 0.15);
                                -fx-background-radius: 6;
                            """);
                } else if (status.equalsIgnoreCase("Belum Lunas")) {
                    label.setStyle("""
                                -fx-text-fill: #D50000;
                                -fx-font-weight: bold;
                                -fx-background-color: rgba(213, 0, 0, 0.12);
                                -fx-background-radius: 6;
                            """);
                } else {
                    label.setStyle("");
                }

                setGraphic(label);
                setStyle("");
            }
        });
        colTanggalTransaksi.setCellValueFactory(
                data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().tanggalTransaksi));
        colUser.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().username));
        colNamalengkap.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().namaLengkap));

        TableLaporan.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // ═════════════════════════════════════════════════════
    // CHARTS —Table data dari database
    // ═════════════════════════════════════════════════════
    private void setupCharts() {
        setupSalesChart();
        setupTrxChart();
        setupMonthChart();
        loadBarChart();
    }

    // ── Area chart: Penjualan 7 hari terakhir ────────────
    private void setupSalesChart() {
        String sql = """
                SELECT
                    DAYNAME(tanggal_transaksi) AS hari,
                    SUM(total_pembayaran) AS total
                FROM tb_transaksi
                WHERE tanggal_transaksi >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
                AND status_pembayaran = 'Lunas'
                GROUP BY DATE(tanggal_transaksi), DAYNAME(tanggal_transaksi)
                ORDER BY DATE(tanggal_transaksi)
                """;

        List<Object[]> data = koneksi.ambilData(sql);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Penjualan");

        // fallback kalau data kosong
        if (data.isEmpty()) {
            String[] days = { "Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min" };
            for (String d : days)
                series.getData().add(new XYChart.Data<>(d, 0));
        } else {
            for (Object[] row : data) {
                String hari = String.valueOf(row[0]);
                long total = ((Number) row[1]).longValue();
                series.getData().add(new XYChart.Data<>(hari, total));
            }
        }

        salesChart.getData().clear();
        salesChart.getData().add(series);
        salesChart.setLegendVisible(false);
        salesChart.setAnimated(true);
    }

    private void loadBarChart() {
        String sql = """
                SELECT b.nama_barang, SUM(dt.jumlah) AS total
                FROM tb_detail_transaksi dt
                JOIN tb_barang b ON dt.id_barang = b.id_barang
                GROUP BY b.nama_barang
                ORDER BY total DESC
                LIMIT 5
                """;

        List<Object[]> data = koneksi.ambilData(sql);

        long max = 1;
        for (Object[] row : data) {
            long val = ((Number) row[1]).longValue();
            if (val > max)
                max = val;
        }

        vboxChart.getChildren().clear();

        String[] colors = { "#6C63FF", "#00D4FF", "#00E5A0", "#FFD166", "#FF5C7C" };

        for (int i = 0; i < data.size(); i++) {
            Object[] row = data.get(i);
            String nama = row[0].toString();
            long total = ((Number) row[1]).longValue();
            double pct = (double) total / max;

            // ── Nama + angka ──────────────────────────
            Label lblNama = new Label(nama);
            lblNama.getStyleClass().add("bar-nama");
            lblNama.setPrefWidth(140);
            lblNama.setMinWidth(140);
            lblNama.setMaxWidth(140);

            Label lblTotal = new Label(total + " unit");
            lblTotal.getStyleClass().add("bar-total");

            // ── Progress bar ──────────────────────────
            ProgressBar pb = new ProgressBar(pct);
            pb.getStyleClass().add("bar-progress");
            pb.setPrefHeight(12);
            pb.setMaxWidth(Double.MAX_VALUE);
            pb.setStyle("-fx-accent: " + colors[i % colors.length] + ";");
            HBox.setHgrow(pb, Priority.ALWAYS);

            // ── Row ───────────────────────────────────
            HBox row2 = new HBox(10, lblNama, pb, lblTotal);
            row2.setAlignment(Pos.CENTER_LEFT);
            row2.getStyleClass().add("bar-row");

            vboxChart.getChildren().add(row2);
        }
    }

    // ── Bar chart: Jumlah transaksi per hari ─────────────
    private void setupTrxChart() {
        String sql = """
                SELECT
                    DAYNAME(tanggal_transaksi) AS hari,
                    COUNT(*) AS jumlah
                FROM tb_transaksi
                WHERE tanggal_transaksi >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)
                GROUP BY DATE(tanggal_transaksi), DAYNAME(tanggal_transaksi)
                ORDER BY DATE(tanggal_transaksi)
                """;

        List<Object[]> data = koneksi.ambilData(sql);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Transaksi");

        if (data.isEmpty()) {
            String[] days = { "Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min" };
            for (String d : days)
                series.getData().add(new XYChart.Data<>(d, 0));
        } else {
            for (Object[] row : data) {
                String hari = String.valueOf(row[0]);
                int jumlah = ((Number) row[1]).intValue();
                series.getData().add(new XYChart.Data<>(hari, jumlah));
            }
        }

        trxChart.getData().clear();
        trxChart.getData().add(series);
        trxChart.setLegendVisible(false);
        trxChart.setAnimated(true);
    }

    // ── Line chart: Tren omzet 6 bulan ───────────────────
    private void setupMonthChart() {
        String sql = """
                SELECT
                    DATE_FORMAT(tanggal_transaksi, '%b') AS bulan,
                    MONTH(tanggal_transaksi) AS no_bulan,
                    SUM(total_pembayaran) AS total
                FROM tb_transaksi
                WHERE tanggal_transaksi >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)
                AND status_pembayaran = 'Lunas'
                GROUP BY MONTH(tanggal_transaksi), DATE_FORMAT(tanggal_transaksi, '%b')
                ORDER BY no_bulan
                """;

        List<Object[]> data = koneksi.ambilData(sql);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Omzet");

        if (data.isEmpty()) {
            String[] months = { "Jan", "Feb", "Mar", "Apr", "Mei", "Jun" };
            for (String m : months)
                series.getData().add(new XYChart.Data<>(m, 0));
        } else {
            for (Object[] row : data) {
                String bulan = String.valueOf(row[0]);
                long total = ((Number) row[2]).longValue();
                series.getData().add(new XYChart.Data<>(bulan, total));
            }
        }

        monthChart.getData().clear();
        monthChart.getData().add(series);
        monthChart.setLegendVisible(false);
        monthChart.setAnimated(true);
        monthChart.setCreateSymbols(false);
    }

    // ═════════════════════════════════════════════════════
    // STOCK LIST — dari database
    // ═════════════════════════════════════════════════════
    private void setupStockList() {
        String sql = """
                SELECT
                    nama_barang,
                    stok,
                    CASE
                        WHEN stok = 0    THEN 'Habis'
                        WHEN stok <= 5   THEN 'Kritis'
                        WHEN stok <= 20  THEN 'Menipis'
                        ELSE 'Aman'
                    END AS status
                FROM tb_barang
                WHERE stok <= 20
                ORDER BY stok ASC
                LIMIT 4
                """;

        List<Object[]> data = koneksi.ambilData(sql);

        // pakai dummy kalau db kosong
        if (data.isEmpty()) {
            chartslbstok.setText("Tidak Ada Stok Barang Yang Habis");
            chartslbSegeraStok.setVisible(false);
        }

        stockList.getChildren().clear();

        for (Object[] row : data) {

            String name = String.valueOf(row[0]);
            int stock = ((Number) row[1]).intValue();
            String status = String.valueOf(row[2]);

            int max = 50;
            double pct = Math.min(1.0, (double) stock / max);

            boolean kritis = status.equals("Kritis") || status.equals("Habis");

            // =========================
            // LABEL KIRI (NAMA)
            // =========================
            Label nameLabel = new Label(name);
            nameLabel.getStyleClass().add("stock-item-name");

            // =========================
            // COUNT
            // =========================
            Label countLabel = new Label(stock + " unit");
            countLabel.getStyleClass().add("stock-item-count");

            // =========================
            // BADGE STATUS
            // =========================
            Label badge = new Label(status);
            badge.getStyleClass().add(kritis ? "badge-kritis" : "badge-menipis");

            // =========================
            // RIGHT BOX (COUNT + BADGE)
            // =========================
            HBox rightBox = new HBox(6, countLabel, badge);
            rightBox.setAlignment(Pos.CENTER_RIGHT);
            rightBox.setMinWidth(Region.USE_PREF_SIZE);

            // =========================
            // SPACER (INI KUNCI BIAR TIDAK NEMPEL)
            // =========================
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            // =========================
            // TOP ROW (NAMA | spacer | RIGHT)
            // =========================
            HBox topRow = new HBox(8, nameLabel, spacer, rightBox);
            topRow.setAlignment(Pos.CENTER_LEFT);
            topRow.setMaxWidth(Double.MAX_VALUE);

            // =========================
            // PROGRESS BAR
            // =========================
            ProgressBar pb = new ProgressBar(pct);
            pb.setMaxWidth(Double.MAX_VALUE);
            pb.setPrefHeight(5);
            pb.getStyleClass().add(kritis ? "progress-kritis" : "progress-menipis");

            // =========================
            // ITEM BOX
            // =========================
            VBox itemBox = new VBox(4, topRow, pb);

            stockList.getChildren().add(itemBox);
        }
    }

    private boolean isUpdating = false;

    // ═════════════════════════════════════════════════════
    // NOMINAL TEXTFIELD: format otomatis saat input
    private void onNominalChanged() {

        if (isUpdating)
            return;
        isUpdating = true;
        String raw = tfNominal.getText().replaceAll("[^0-9]", "");
        if (raw.isEmpty()) {
            tfNominal.setText("");
            isUpdating = false;
            return;
        }
        long value;
        try {
            value = Long.parseLong(raw);
        } catch (NumberFormatException e) {
            isUpdating = false;
            return;
        }
        tfNominal.setText("Rp " + FMT.format(value));
        tfNominal.positionCaret(tfNominal.getText().length());
        isUpdating = false;
    }

    // ================================
    // Exxport To Excel
    // ================================
    @FXML
    private void onExport() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Simpan Laporan Excel");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            fileChooser.setInitialFileName("laporan-transaksi.xlsx");
            File file = fileChooser.showSaveDialog(TableLaporan.getScene().getWindow());
            if (file == null) {
                return;
            }
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Laporan Transaksi");
            // =========================
            // STYLE HEADER
            // =========================
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            // =========================
            // HEADER
            // =========================
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "No",
                    "ID Transaksi",
                    "Nama Lengkap",
                    "Username",
                    "Total Pembayaran",
                    "Uang Pembayaran",
                    "Kembalian",
                    "Kekurangan",
                    "Status",
                    "Tanggal"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // =========================
            // DATA
            // =========================
            int rowIndex = 1;

            for (LaporanTransaksiItem item : TableLaporan.getItems()) {

                Row row = sheet.createRow(rowIndex++);

                row.createCell(0).setCellValue(item.no);
                row.createCell(1).setCellValue(item.idTransaksi);
                row.createCell(2).setCellValue(item.namaLengkap);
                row.createCell(3).setCellValue(item.username);

                row.createCell(4).setCellValue(item.totalPembayaran);
                row.createCell(5).setCellValue(item.uangPembayaran);
                row.createCell(6).setCellValue(item.kembalian);
                row.createCell(7).setCellValue(item.kekurangan);

                row.createCell(8).setCellValue(item.statusPembayaran);

                row.createCell(9).setCellValue(
                        item.tanggalTransaksi.toString());
            }

            // =========================
            // AUTO SIZE
            // =========================
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            // =========================
            // SAVE
            // =========================
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            workbook.close();
            System.out.println("Export Excel berhasil!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   

    // ═════════════════════════════════════════════════════
    // OTHER HANDLERS
    // ═════════════════════════════════════════════════════
    @FXML
    private void onNotif() {
        System.out.println("Notifikasi dibuka");
    }

    @FXML
    private void onLihatSemua() {
        System.out.println("Lihat semua transaksi");
    }
    
    private void applyRoundedClip(StackPane pane) {
        Rectangle clip = new Rectangle();

        clip.widthProperty().bind(pane.widthProperty());
        clip.heightProperty().bind(pane.heightProperty());

        clip.setArcWidth(20);
        clip.setArcHeight(20);

        pane.setClip(clip);
    }




    

}
