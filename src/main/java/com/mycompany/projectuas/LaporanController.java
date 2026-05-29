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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.VBox;
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
    private Label kpiPenjualanDelta;

    @FXML
    private Label kpiTransaksi;
    @FXML
    private Label kpiTransaksiDelta;

    @FXML
    private Label kpiProduk;
    @FXML
    private Label kpiProdukDelta;

    @FXML
    private Label kpiStok;
    @FXML
    private Label kpiStokDelta;

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

    // ── Table ─────────────────────────────────────────────
    @FXML
    private TableView<TransaksiItem> trxTable;
    @FXML
    private TableColumn<TransaksiItem, String> colId;
    @FXML
    private TableColumn<TransaksiItem, String> colItem;
    @FXML
    private TableColumn<TransaksiItem, String> colKasir;
    @FXML
    private TableColumn<TransaksiItem, String> colWaktu;
    @FXML
    private TableColumn<TransaksiItem, String> colTotal;

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
        if (!tfNominal.getText().trim().isEmpty()) {

            sql.append(" AND t.total_pembayaran >= ")
                    .append(tfNominal.getText().trim());
        }

        // =========================
        // ORDER
        // =========================
        sql.append(" ORDER BY t.tanggal_transaksi DESC");

        // DEBUG QUERY
        System.out.println(sql);

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
        setupTable();
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
    }

    // ═════════════════════════════════════════════════════
    // setup tabel laporan
    private void setupTableLaporan() {
        colNo.setCellValueFactory(
                data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().no).asObject());
        colIdTransaksi.setCellValueFactory(
                data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().idTransaksi).asObject());
        colNama.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().namaLengkap));
        colUser.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().username));
        colTotalPembayaran.setCellValueFactory(
                data -> new SimpleStringProperty("Rp " + FMT.format(data.getValue().totalPembayaran)));
        colUangPembayaran.setCellValueFactory(
                data -> new SimpleStringProperty("Rp " + FMT.format(data.getValue().uangPembayaran)));
        colKembalian.setCellValueFactory(
                data -> new SimpleStringProperty("Rp " + FMT.format(data.getValue().kembalian)));
        colKekurangan.setCellValueFactory(
                data -> new SimpleStringProperty("Rp " + FMT.format(data.getValue().kekurangan)));
        colStatus.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().statusPembayaran));
        colTanggalTransaksi.setCellValueFactory(
                data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().tanggalTransaksi));
        TableLaporan.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // ═════════════════════════════════════════════════════
    // CHARTS
    // ═════════════════════════════════════════════════════
    private void setupCharts() {

        // ── Area chart: Penjualan 7 hari ─────────────────
        XYChart.Series<String, Number> salesSeries = new XYChart.Series<>();
        salesSeries.setName("Penjualan");
        String[] days = { "Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min" };
        long[] vals = { 2100000L, 3400000L, 2800000L, 4100000L, 3600000L, 4800000L, 4280000L };
        for (int i = 0; i < days.length; i++)
            salesSeries.getData().add(new XYChart.Data<>(days[i], vals[i]));

        salesChart.getData().add(salesSeries);
        salesChart.setLegendVisible(false);
        salesChart.setAnimated(true);

        // ── Bar chart: Transaksi per hari ─────────────────
        XYChart.Series<String, Number> trxSeries = new XYChart.Series<>();
        trxSeries.setName("Transaksi");
        int[] trxVals = { 42, 68, 54, 82, 71, 95, 128 };
        for (int i = 0; i < days.length; i++)
            trxSeries.getData().add(new XYChart.Data<>(days[i], trxVals[i]));

        trxChart.getData().add(trxSeries);
        trxChart.setLegendVisible(false);
        trxChart.setAnimated(true);

        // ── Line chart: Tren 6 bulan ──────────────────────
        XYChart.Series<String, Number> monthSeries = new XYChart.Series<>();
        monthSeries.setName("Omzet");
        String[] months = { "Jan", "Feb", "Mar", "Apr", "Mei", "Jun" };
        int[] monthVals = { 52, 61, 58, 74, 69, 87 };
        for (int i = 0; i < months.length; i++)
            monthSeries.getData().add(new XYChart.Data<>(months[i], monthVals[i]));

        monthChart.getData().add(monthSeries);
        monthChart.setLegendVisible(false);
        monthChart.setAnimated(true);
        monthChart.setCreateSymbols(false);
    }

    // ═════════════════════════════════════════════════════
    // TABLE
    // ═════════════════════════════════════════════════════
    public static class TransaksiItem {
        private final SimpleStringProperty id, item, kasir, waktu, total;

        public TransaksiItem(String id, String item, String kasir, String waktu, String total) {
            this.id = new SimpleStringProperty(id);
            this.item = new SimpleStringProperty(item);
            this.kasir = new SimpleStringProperty(kasir);
            this.waktu = new SimpleStringProperty(waktu);
            this.total = new SimpleStringProperty(total);
        }

        public String getId() {
            return id.get();
        }

        public String getItem() {
            return item.get();
        }

        public String getKasir() {
            return kasir.get();
        }

        public String getWaktu() {
            return waktu.get();
        }

        public String getTotal() {
            return total.get();
        }
    }

    private void setupTable() {
        colId.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getId()));
        colItem.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getItem()));
        colKasir.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getKasir()));
        colWaktu.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getWaktu()));
        colTotal.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTotal()));

        // Warna khusus kolom ID (ungu) dan Total (hijau)
        colId.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                setText(empty ? null : s);
                setStyle(empty ? "" : "-fx-text-fill: #6C63FF; -fx-font-weight: bold;");
            }
        });
        colTotal.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String s, boolean empty) {
                super.updateItem(s, empty);
                setText(empty ? null : s);
                setStyle(empty ? "" : "-fx-text-fill: #00E5A0; -fx-font-weight: bold; -fx-alignment: CENTER-RIGHT;");
            }
        });

        ObservableList<TransaksiItem> data = FXCollections.observableArrayList(
                new TransaksiItem("#TRX-0128", "Indomie Goreng x3", "Budi S.", "14:22", "Rp 10.500"),
                new TransaksiItem("#TRX-0127", "Aqua 600ml x5", "Siti R.", "13:51", "Rp 20.000"),
                new TransaksiItem("#TRX-0126", "Teh Sosro x2", "Budi S.", "12:37", "Rp 10.000"),
                new TransaksiItem("#TRX-0125", "Sabun Lifebuoy x1", "Rafi A.", "11:09", "Rp 12.500"),
                new TransaksiItem("#TRX-0124", "Beng-Beng x4", "Siti R.", "10:33", "Rp 16.000"));

        trxTable.setItems(data);
        trxTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // ═════════════════════════════════════════════════════
    // STOCK LIST (progress bars dinamis)
    // ═════════════════════════════════════════════════════

    // Inner class pengganti record (kompatibel Java 11+)
    private static class StockItem {
        final String name;
        final int stock;
        final int max;
        final String status;

        StockItem(String name, int stock, int max, String status) {
            this.name = name;
            this.stock = stock;
            this.max = max;
            this.status = status;
        }
    }

    private void setupStockList() {
        List<StockItem> items = List.of(new StockItem("Minyak Goreng", 3, 50, "Kritis"),
                new StockItem("Teh Botol Sosro", 12, 100, "Menipis"), new StockItem("Gula Pasir", 8, 40, "Kritis"),
                new StockItem("Kopi Kapal Api", 18, 80, "Menipis"));

        for (StockItem si : items) {
            double pct = (double) si.stock / si.max;
            boolean kritis = si.status.equals("Kritis");

            // Row atas: nama | count + badge
            Label nameLabel = new Label(si.name);
            nameLabel.getStyleClass().add("stock-item-name");

            Label countLabel = new Label(si.stock + " unit");
            countLabel.getStyleClass().add("stock-item-count");

            Label badge = new Label(si.status);
            badge.getStyleClass().add(kritis ? "badge-kritis" : "badge-menipis");

            HBox rightBox = new HBox(6, countLabel, badge);
            rightBox.setAlignment(Pos.CENTER_RIGHT);

            HBox topRow = new HBox(nameLabel, rightBox);
            HBox.setHgrow(nameLabel, Priority.ALWAYS);
            topRow.setAlignment(Pos.CENTER_LEFT);

            // Progress bar
            ProgressBar pb = new ProgressBar(pct);
            pb.setMaxWidth(Double.MAX_VALUE);
            pb.setPrefHeight(5);
            pb.getStyleClass().add(kritis ? "progress-kritis" : "progress-menipis");

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
        }
        long value = Long.parseLong(raw);
        tfNominal.setText("Rp " + FMT.format(value));
        tfNominal.positionCaret(tfNominal.getText().length());
        isUpdating = false;
    }

    // ═══════════════════════════════════════════════
    // HANDLERS
    // ═══════════════════════════════════════════════
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

    // ====================================================
    // KPI
    // ====================================================
    private void loadKPI() {
        loadTotalPenjualan();
        loadTotalTransaksi();
        loadProdukTerjual();
        loadStokMenipis();
    }

    private void loadTotalPenjualan() {

        String query = """
                    SELECT COALESCE(SUM(total_pembayaran), 0)
                    FROM tb_transaksi
                    WHERE DATE(tanggal_transaksi) = CURDATE()
                """;

        List<Object[]> data = koneksi.ambilData(query);

        if (!data.isEmpty()) {

            Object[] row = data.get(0);

            double total = ((Number) row[0]).doubleValue();

            kpiPenjualan.setText("Rp " + String.format("%,.0f", total));
            kpiPenjualanDelta.setText("Penjualan hari ini");
        }
    }

    private void loadTotalTransaksi() {

        String query = """
                    SELECT COUNT(*)
                    FROM tb_transaksi
                    WHERE DATE(tanggal_transaksi) = CURDATE()
                """;

        List<Object[]> data = koneksi.ambilData(query);

        if (!data.isEmpty()) {

            Object[] row = data.get(0);

            int total = ((Number) row[0]).intValue();

            kpiTransaksi.setText(String.valueOf(total));
            kpiTransaksiDelta.setText("Transaksi hari ini");
        }
    }

    private void loadProdukTerjual() {

        String query = """
                    SELECT COALESCE(SUM(jumlah), 0)
                    FROM tb_detail_transaksi dt
                    JOIN tb_transaksi t
                    ON dt.id_transaksi = t.id_transaksi
                    WHERE DATE(t.tanggal_transaksi) = CURDATE()
                """;

        List<Object[]> data = koneksi.ambilData(query);

        if (!data.isEmpty()) {

            Object[] row = data.get(0);

            int total = ((Number) row[0]).intValue();

            kpiProduk.setText(total + " Unit");
            kpiProdukDelta.setText("Produk terjual hari ini");
        }
    }

    private void loadStokMenipis() {

        String query = """
                    SELECT COUNT(*)
                    FROM tb_barang
                    WHERE stok <= 5
                """;

        List<Object[]> data = koneksi.ambilData(query);

        if (!data.isEmpty()) {

            Object[] row = data.get(0);

            int total = ((Number) row[0]).intValue();

            kpiStok.setText(total + " Item");
            kpiStokDelta.setText("Perlu restock");
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

}
