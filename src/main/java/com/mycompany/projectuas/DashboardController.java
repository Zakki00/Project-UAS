package com.mycompany.projectuas;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.layout.Region;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DashboardController implements Initializable {

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
    // ── Chart baru ─────────────────────────────────────────
    @FXML
    private PieChart pieKomposisi;
    @FXML
    private VBox vboxKategori;
    @FXML
    private BarChart<String, Number> jamChart;
    @FXML
    private Label kpiPiutangJumlah;
    @FXML
    private Label kpiPiutangNominal;
    @FXML
    private VBox vboxTerlarisMinggu;
    @FXML
    private Label kpiRataRata;
    @FXML
    private Label kpiRataRataInfo;
    @FXML
    private PieChart piePiutang;
    @FXML
    private LineChart<String, Number> lineRataRata;

    // ── Charts ────────────────────────────────────────────
    @FXML
    private AreaChart<String, Number> salesChart;
    @FXML
    private BarChart<String, Number> trxChart;
    @FXML
    private LineChart<String, Number> monthChart;

    @FXML
    private VBox vboxChart;
    @FXML
    private VBox vboxChartPs;
    @FXML
    private Label chartslbstok;
    @FXML
    private Label chartslbSegeraStok;

    // ── Stock list ────────────────────────────────────────
    @FXML
    private VBox stockList;

    // ── State ─────────────────────────────────────────────
    private boolean sidebarCollapsed = false;
    private static final double SIDEBAR_FULL = 220;
    private static final double SIDEBAR_MINI = 60;

    // ═════════════════════════════════════════════════════
    // INITIALIZE
    // ═════════════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupCharts();
        setupStockList();
        setupNavHover();
        loadKPI();
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
        navigation nav = new navigation();
        nav.navigataeToPengaturan();
        Stage stage = (Stage) navPengaturan.getScene().getWindow();
        stage.close();

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

    // ====================================================
    // KPI
    // ====================================================
    private void loadKPI() {
        loadTotalPenjualan();
        loadTotalTransaksi();
        loadProdukTerjual();
        loadStokMenipis();
        loadPiutangHariIni();
        loadRataRataTransaksi();
        // loadShiftData();
    }

    private void setPersenKPI(Label label, double today, double yesterday) {

        double persen;

        if (yesterday == 0) {
            persen = today > 0 ? 100 : 0;
        } else {
            persen = ((today - yesterday) / yesterday) * 100;
        }

        String icon;
        String status;

        if (persen > 0) {
            icon = "▲ +";
            status = "lebih besar dari kemarin";
            label.setStyle("""
                    -fx-font-weight: bold;
                    -fx-text-fill: #22c55e;
                    """);
            // merah
        } else if (persen < 0) {
            icon = "▼ -";
            status = "lebih kecil dari kemarin";
            label.setStyle("""
                    -fx-font-weight: bold;
                    -fx-text-fill: #ef4444;
                    """);
            // hijau
        } else {
            icon = "■ ";
            status = "tidak berubah dari kemarin";
            label.setStyle("""
                    -fx-font-weight: bold;
                     -fx-text-fill: #9ca3af;
                    """);
            // abu-abu
        }

        String text = icon + String.format("%.1f%% %s", Math.abs(persen), status);

        label.setText(text);
    }

    private void loadTotalPenjualan() {

        List<Object[]> todayData = koneksi.ambilData("""
                    SELECT COALESCE(SUM(total_pembayaran - kekurangan), 0)
                    FROM tb_transaksi
                    WHERE DATE(tanggal_transaksi) = DATE('now','localtime')
                """);

        List<Object[]> yesterdayData = koneksi.ambilData("""
                    SELECT COALESCE(SUM(uang_pembayaran), 0)
                    FROM tb_transaksi
                    WHERE DATE(tanggal_transaksi) = DATE('now', '-1 day')
                """);

        double today = 0;
        if (!todayData.isEmpty() && todayData.get(0)[0] != null) {
            today = ((Number) todayData.get(0)[0]).doubleValue();
        }

        double yesterday = 0;
        if (!yesterdayData.isEmpty() && yesterdayData.get(0)[0] != null) {
            yesterday = ((Number) yesterdayData.get(0)[0]).doubleValue();
        }

        kpiPenjualan.setText("Rp " + String.format("%,.0f", today));
        setPersenKPI(kpiPenjulanPersen, today, yesterday);
    }

    private void loadTotalTransaksi() {
        List<Object[]> todayData = koneksi.ambilData("""
                    SELECT COUNT(*)
                    FROM tb_transaksi
                    WHERE DATE(tanggal_transaksi) = DATE('now','localtime')
                """);

        List<Object[]> yesterdayData = koneksi.ambilData("""
                    SELECT COUNT(*)
                    FROM tb_transaksi
                    WHERE DATE(tanggal_transaksi) = DATE('now', '-1 day')
                """);

        double today = 0;
        if (!todayData.isEmpty() && todayData.get(0)[0] != null) {
            today = ((Number) todayData.get(0)[0]).doubleValue();
        }

        double yesterday = 0;
        if (!yesterdayData.isEmpty() && yesterdayData.get(0)[0] != null) {
            yesterday = ((Number) yesterdayData.get(0)[0]).doubleValue();
        }

        kpiTransaksi.setText(String.valueOf((int) today));
        setPersenKPI(kpiTransaksiPersen, today, yesterday);
    }

    private void loadProdukTerjual() {

        List<Object[]> todayData = koneksi.ambilData("""
                    SELECT COALESCE(SUM(jumlah), 0)
                    FROM tb_detail_transaksi dt
                    JOIN tb_transaksi t ON dt.id_transaksi = t.id_transaksi
                    WHERE DATE(t.tanggal_transaksi) = DATE('now','localtime')
                """);

        List<Object[]> yesterdayData = koneksi.ambilData("""
                    SELECT COALESCE(SUM(jumlah), 0)
                    FROM tb_detail_transaksi dt
                    JOIN tb_transaksi t ON dt.id_transaksi = t.id_transaksi
                    WHERE DATE(t.tanggal_transaksi) = DATE('now', '-1 day')
                """);

        double today = 0;
        if (!todayData.isEmpty() && todayData.get(0)[0] != null) {
            today = ((Number) todayData.get(0)[0]).doubleValue();
        }

        double yesterday = 0;
        if (!yesterdayData.isEmpty() && yesterdayData.get(0)[0] != null) {
            yesterday = ((Number) yesterdayData.get(0)[0]).doubleValue();

        }
        kpiProduk.setText(String.valueOf((int) today) + " unit");
        setPersenKPI(kpiProdukPersen, today, yesterday);

    }

    private void loadStokMenipis() {
        List<Object[]> stokData = koneksi.ambilData("""
                    SELECT COUNT(*)
                    FROM tb_barang
                    WHERE stok <= 5
                """);

        int stokMenipis = 0;
        if (!stokData.isEmpty() && stokData.get(0)[0] != null) {
            stokMenipis = ((Number) stokData.get(0)[0]).intValue();
        }
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

    // ═════════════════════════════════════════════════════
    // CHARTS —Table data dari database
    // ═════════════════════════════════════════════════════
    private void setupCharts() {
        loadPiePiutang();
        loadLineRataRata();
        setupSalesChart();
        setupTrxChart();
        setupMonthChart();
        loadBarChart();
        loadBarChartPs();
        loadKomposisiPendapatan();
        loadOmzetPerKategori();
        loadTransaksiPerJam();
        loadTerlarisMingguIni();
    }

    // ── Area chart: Penjualan 7 hari terakhir ────────────

    // ── Area chart: Penjualan 7 hari terakhir ────────────
    private void setupSalesChart() {

        String sql = """
                SELECT
                    date(tanggal_transaksi) AS tanggal,
                    SUM(total_pembayaran) AS total
                FROM tb_transaksi
                WHERE tanggal_transaksi >= date('now','localtime', '-7 day')
                AND status_pembayaran = 'Lunas'
                GROUP BY date(tanggal_transaksi)
                ORDER BY date(tanggal_transaksi)
                """;

        List<Object[]> data = koneksi.ambilData(sql);

        String[] namaHari = {
                "Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab"
        };

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Penjualan");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (data.isEmpty()) {

            for (int i = 6; i >= 0; i--) {

                LocalDate tanggal = LocalDate.now().minusDays(i);

                String label = namaHari[tanggal.getDayOfWeek().getValue() % 7]
                        + "\n"
                        + tanggal.format(
                                DateTimeFormatter.ofPattern("dd/MM"));

                series.getData().add(new XYChart.Data<>(label, 0));
            }

        } else {

            for (Object[] row : data) {

                LocalDate tanggal = LocalDate.parse(String.valueOf(row[0]), formatter);

                String label = namaHari[tanggal.getDayOfWeek().getValue() % 7]
                        + "\n"
                        + tanggal.format(
                                DateTimeFormatter.ofPattern("dd/MM"));

                long total = ((Number) row[1]).longValue();

                series.getData().add(new XYChart.Data<>(label, total));
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
            if (val > max) {
                max = val;
            }
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

    private void loadBarChartPs() {
        String sql = """
                SELECT pp.durasi, COUNT(pp.id_paket_ps) AS total
                FROM tb_paket_ps pp
                GROUP BY pp.durasi
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

        vboxChartPs.getChildren().clear();

        // Tambah judul
        Label judul = new Label("Top 5 Durasi PS Terbanyak");
        judul.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 13px;");
        vboxChartPs.getChildren().add(judul);

        String[] colors = { "#6C63FF", "#00D4FF", "#00E5A0", "#FFD166", "#FF5C7C" };

        if (data.isEmpty()) {
            Label kosong = new Label("Belum ada data PS");
            kosong.setStyle("-fx-text-fill: #888;");
            vboxChartPs.getChildren().add(kosong);
            return;
        }

        for (int i = 0; i < data.size(); i++) {
            Object[] row = data.get(i);
            int durasi = ((Number) row[0]).intValue(); // dalam menit
            long total = ((Number) row[1]).longValue();
            double pct = (double) total / max;

            // Format durasi: jam & menit
            String labelDurasi;
            if (durasi >= 60) {
                int jam = durasi / 60;
                int menit = durasi % 60;
                labelDurasi = menit > 0
                        ? jam + " jam " + menit + " mnt"
                        : jam + " jam";
            } else {
                labelDurasi = durasi + " mnt";
            }

            // ── Nama durasi ──
            Label lblNama = new Label(labelDurasi);
            lblNama.getStyleClass().add("bar-nama");
            lblNama.setPrefWidth(140);
            lblNama.setMinWidth(140);
            lblNama.setMaxWidth(140);

            // ── Total sesi ──
            Label lblTotal = new Label(total + "x");
            lblTotal.getStyleClass().add("bar-total");

            // ── Progress bar ──
            ProgressBar pb = new ProgressBar(pct);
            pb.getStyleClass().add("bar-progress");
            pb.setPrefHeight(12);
            pb.setMaxWidth(Double.MAX_VALUE);
            pb.setStyle("-fx-accent: " + colors[i % colors.length] + ";");
            HBox.setHgrow(pb, Priority.ALWAYS);

            // ── Row ──
            HBox row2 = new HBox(10, lblNama, pb, lblTotal);
            row2.setAlignment(Pos.CENTER_LEFT);
            row2.getStyleClass().add("bar-row");

            vboxChartPs.getChildren().add(row2);
        }
    }

    // ── Bar chart: Jumlah transaksi per hari ─────────────
    private void setupTrxChart() {
        String sql = """
                SELECT
                    strftime('%w', tanggal_transaksi) AS hari,
                    COUNT(*) AS jumlah
                FROM tb_transaksi
                WHERE tanggal_transaksi >= date('now','localtime', '-7 day')
                GROUP BY date(tanggal_transaksi)
                ORDER BY date(tanggal_transaksi)
                """;

        List<Object[]> data = koneksi.ambilData(sql);

        String[] namaHari = { "Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab" };

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Transaksi");

        if (data.isEmpty()) {
            for (String d : namaHari) {
                series.getData().add(new XYChart.Data<>(d, 0));
            }
        } else {
            for (Object[] row : data) {
                int hariIdx = Integer.parseInt(String.valueOf(row[0]));
                int jumlah = ((Number) row[1]).intValue();
                series.getData().add(new XYChart.Data<>(namaHari[hariIdx], jumlah));
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
                    strftime('%m', tanggal_transaksi) AS bulan,
                    SUM(total_pembayaran) AS total
                FROM tb_transaksi
                WHERE tanggal_transaksi >= date('now', '-6 month')
                AND status_pembayaran = 'Lunas'
                GROUP BY strftime('%m', tanggal_transaksi)
                ORDER BY bulan
                """;

        List<Object[]> data = koneksi.ambilData(sql);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Omzet");
        String[] namaBulan = { "", "Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
                "Jul", "Ags", "Sep", "Okt", "Nov", "Des" };
        if (data.isEmpty()) {
            // Tambah map bulan

            // Ganti bagian parsing:
            for (String m : namaBulan) {
                series.getData().add(new XYChart.Data<>(m, 0));
            }
        } else {
            // Ganti bagian parsing:
            for (Object[] row : data) {
                int bulanIdx = Integer.parseInt(String.valueOf(row[0]));
                long total = ((Number) row[1]).longValue();
                series.getData().add(new XYChart.Data<>(namaBulan[bulanIdx], total));
            }
        }

        monthChart.getData().clear();
        monthChart.getData().add(series);
        monthChart.setLegendVisible(false);
        monthChart.setAnimated(true);
        monthChart.setCreateSymbols(false);
    }

    // =================================================
    // Charts dan KPI baru
    // ===================================================
    // ─────────────────────────────────────────────────────
    // Komposisi Pendapatan: Produk vs PS
    // ─────────────────────────────────────────────────────
    // ─────────────────────────────────────────────────────
    // Pie Piutang vs Lunas hari ini
    // ─────────────────────────────────────────────────────
    private void loadPiePiutang() {
        List<Object[]> lunas = koneksi.ambilData("""
                SELECT COUNT(*) FROM tb_transaksi
                WHERE status_pembayaran = 'Lunas'
                AND DATE(tanggal_transaksi) = DATE('now','localtime')
                """);

        List<Object[]> belumLunas = koneksi.ambilData("""
                SELECT COUNT(*) FROM tb_transaksi
                WHERE status_pembayaran != 'Lunas'
                AND DATE(tanggal_transaksi) = DATE('now','localtime')
                """);

        int jumlahLunas = 0;
        int jumlahBelum = 0;

        if (!lunas.isEmpty() && lunas.get(0)[0] != null)
            jumlahLunas = ((Number) lunas.get(0)[0]).intValue();
        if (!belumLunas.isEmpty() && belumLunas.get(0)[0] != null)
            jumlahBelum = ((Number) belumLunas.get(0)[0]).intValue();

        piePiutang.getData().clear();

        if (jumlahLunas == 0 && jumlahBelum == 0) {
            piePiutang.getData().add(new PieChart.Data("Belum ada data", 1));
            return;
        }

        piePiutang.getData().add(new PieChart.Data("Lunas (" + jumlahLunas + ")", jumlahLunas));
        piePiutang.getData().add(new PieChart.Data("Belum Lunas (" + jumlahBelum + ")", jumlahBelum));
        piePiutang.setAnimated(true);
        piePiutang.setLabelsVisible(true);

        Platform.runLater(() -> {
            if (piePiutang.getData().size() >= 2) {
                piePiutang.getData().get(0).getNode().setStyle("-fx-pie-color: #00E5A0;");
                piePiutang.getData().get(1).getNode().setStyle("-fx-pie-color: #FF5C7C;");
            }
        });
    }

    // ─────────────────────────────────────────────────────
    // Line Chart: Tren rata-rata transaksi 7 hari terakhir
    // ─────────────────────────────────────────────────────
    private void loadLineRataRata() {
        String sql = """
                SELECT
                    date(tanggal_transaksi) AS tgl,
                    AVG(total_pembayaran) AS rata
                FROM tb_transaksi
                WHERE status_pembayaran = 'Lunas'
                AND tanggal_transaksi >= date('now','localtime', '-7 day')
                GROUP BY date(tanggal_transaksi)
                ORDER BY tgl
                """;

        List<Object[]> data = koneksi.ambilData(sql);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Rata-rata");

        String[] namaHari = { "Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab" };
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (data.isEmpty()) {
            for (int i = 6; i >= 0; i--) {
                LocalDate tgl = LocalDate.now().minusDays(i);
                String label = namaHari[tgl.getDayOfWeek().getValue() % 7]
                        + "\n" + tgl.format(DateTimeFormatter.ofPattern("dd/MM"));
                series.getData().add(new XYChart.Data<>(label, 0));
            }
        } else {
            for (Object[] row : data) {
                LocalDate tgl = LocalDate.parse(String.valueOf(row[0]), formatter);
                String label = namaHari[tgl.getDayOfWeek().getValue() % 7]
                        + "\n" + tgl.format(DateTimeFormatter.ofPattern("dd/MM"));
                double rata = ((Number) row[1]).doubleValue();
                series.getData().add(new XYChart.Data<>(label, rata));
            }
        }

        lineRataRata.getData().clear();
        lineRataRata.getData().add(series);
        lineRataRata.setAnimated(true);
    }

    private void loadKomposisiPendapatan() {
        String sql = """
                SELECT
                    CASE
                        WHEN dt.id_paket_ps IS NOT NULL THEN 'Rental PS'
                        ELSE 'Produk'
                    END AS kategori,
                    SUM(dt.harga * dt.jumlah) AS total
                FROM tb_detail_transaksi dt
                JOIN tb_transaksi t ON dt.id_transaksi = t.id_transaksi
                WHERE t.tanggal_transaksi >= date('now', '-30 day')
                GROUP BY kategori
                """;

        List<Object[]> data = koneksi.ambilData(sql);

        pieKomposisi.getData().clear();

        if (data.isEmpty()) {
            pieKomposisi.getData().add(new PieChart.Data("Belum ada data", 1));
            return;
        }

        for (Object[] row : data) {
            String kategori = row[0].toString();
            double total = ((Number) row[1]).doubleValue();
            pieKomposisi.getData().add(new PieChart.Data(kategori, total));
        }

        pieKomposisi.setAnimated(true);
        pieKomposisi.setLabelsVisible(true);

        // Warna custom setelah data masuk
        String[] colors = { "#6C63FF", "#00D4FF", "#00E5A0", "#FFD166", "#FF5C7C" };
        for (int i = 0; i < pieKomposisi.getData().size(); i++) {
            pieKomposisi.getData().get(i).getNode()
                    .setStyle("-fx-pie-color: " + colors[i % colors.length] + ";");
        }
    }

    // ─────────────────────────────────────────────────────
    // Omzet per Kategori Barang
    // ─────────────────────────────────────────────────────
    private void loadOmzetPerKategori() {
        String sql = """
                SELECT b.kategori, SUM(dt.jumlah * dt.harga) AS total
                FROM tb_detail_transaksi dt
                JOIN tb_barang b ON dt.id_barang = b.id_barang
                JOIN tb_transaksi t ON dt.id_transaksi = t.id_transaksi
                WHERE DATE(t.tanggal_transaksi) >= DATE('now', '-30 day')
                GROUP BY b.kategori
                ORDER BY total DESC
                """;

        List<Object[]> data = koneksi.ambilData(sql);

        long max = 1;
        for (Object[] row : data) {
            long val = ((Number) row[1]).longValue();
            if (val > max)
                max = val;
        }

        vboxKategori.getChildren().clear();

        if (data.isEmpty()) {
            vboxKategori.getChildren().add(new Label("Belum ada data"));
            return;
        }

        String[] colors = { "#00E5A0", "#FFD166", "#FF5C7C", "#6C63FF", "#00D4FF" };

        for (int i = 0; i < data.size(); i++) {
            String kategori = data.get(i)[0].toString();
            long total = ((Number) data.get(i)[1]).longValue();
            double pct = (double) total / max;

            Label lblNama = new Label(kategori);
            lblNama.getStyleClass().add("bar-nama");
            lblNama.setPrefWidth(100);
            lblNama.setMinWidth(100);

            Label lblTotal = new Label("Rp " + String.format("%,.0f", (double) total));
            lblTotal.getStyleClass().add("bar-total");

            ProgressBar pb = new ProgressBar(pct);
            pb.getStyleClass().add("bar-progress");
            pb.setPrefHeight(12);
            pb.setMaxWidth(Double.MAX_VALUE);
            pb.setStyle("-fx-accent: " + colors[i % colors.length] + ";");
            HBox.setHgrow(pb, Priority.ALWAYS);

            HBox row = new HBox(10, lblNama, pb, lblTotal);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("bar-row");

            vboxKategori.getChildren().add(row);
        }
    }

    // ─────────────────────────────────────────────────────
    // Transaksi per Jam (hari ini)
    // ─────────────────────────────────────────────────────
    private void loadTransaksiPerJam() {
        String sql = """
                SELECT
                    strftime('%H', tanggal_transaksi) AS jam,
                    COUNT(*) AS jumlah
                FROM tb_transaksi
                WHERE DATE(tanggal_transaksi) = DATE('now','localtime')
                GROUP BY jam
                ORDER BY jam
                """;

        List<Object[]> data = koneksi.ambilData(sql);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Transaksi");

        // Siapkan map jam -> jumlah
        java.util.Map<String, Integer> mapJam = new java.util.LinkedHashMap<>();
        for (int h = 7; h <= 22; h++) {
            mapJam.put(String.format("%02d", h), 0);
        }
        for (Object[] row : data) {
            String jam = String.valueOf(row[0]);
            int jumlah = ((Number) row[1]).intValue();
            if (mapJam.containsKey(jam)) {
                mapJam.put(jam, jumlah);
            }
        }

        for (java.util.Map.Entry<String, Integer> entry : mapJam.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        jamChart.getData().clear();
        jamChart.getData().add(series);
        jamChart.setAnimated(true);
    }

    // ─────────────────────────────────────────────────────
    // Piutang hari ini
    // ─────────────────────────────────────────────────────
    private void loadPiutangHariIni() {
        List<Object[]> data = koneksi.ambilData("""
                SELECT COUNT(*), COALESCE(SUM(kekurangan), 0)
                FROM tb_transaksi
                WHERE status_pembayaran != 'Lunas'
                AND DATE(tanggal_transaksi) = DATE('now','localtime')
                """);

        int jumlah = 0;
        double nominal = 0;

        if (!data.isEmpty() && data.get(0)[0] != null) {
            jumlah = ((Number) data.get(0)[0]).intValue();
            nominal = ((Number) data.get(0)[1]).doubleValue();
        }

        kpiPiutangJumlah.setText(jumlah + " Transaksi");
        kpiPiutangNominal.setText("Rp " + String.format("%,.0f", nominal));

        if (jumlah == 0) {
            kpiPiutangJumlah.setStyle("-fx-text-fill: #00E5A0; -fx-font-weight: bold;");
            kpiPiutangNominal.setText("✔ Semua lunas");
            kpiPiutangNominal.setStyle("-fx-text-fill: #00E5A0;");
        } else {
            kpiPiutangJumlah.setStyle("-fx-text-fill: #FF5C7C; -fx-font-weight: bold;");
            kpiPiutangNominal.setStyle("-fx-text-fill: #FF5C7C;");
        }
    }

    // ─────────────────────────────────────────────────────
    // Produk Terlaris Minggu Ini
    // ─────────────────────────────────────────────────────
    private void loadTerlarisMingguIni() {
        String sql = """
                SELECT b.nama_barang, b.kategori, SUM(dt.jumlah) AS terjual
                FROM tb_detail_transaksi dt
                JOIN tb_barang b ON dt.id_barang = b.id_barang
                JOIN tb_transaksi t ON dt.id_transaksi = t.id_transaksi
                WHERE DATE(t.tanggal_transaksi) >= DATE('now', 'localtime', '-7 day')
                GROUP BY b.nama_barang
                ORDER BY terjual DESC
                LIMIT 5
                """;

        List<Object[]> data = koneksi.ambilData(sql);

        long max = 1;
        for (Object[] row : data) {
            long val = ((Number) row[2]).longValue();
            if (val > max)
                max = val;
        }

        vboxTerlarisMinggu.getChildren().clear();

        if (data.isEmpty()) {
            vboxTerlarisMinggu.getChildren().add(new Label("Belum ada data minggu ini"));
            return;
        }

        String[] colors = { "#6C63FF", "#00D4FF", "#00E5A0", "#FFD166", "#FF5C7C" };

        for (int i = 0; i < data.size(); i++) {
            String nama = data.get(i)[0].toString();
            String kategori = data.get(i)[1].toString();
            long terjual = ((Number) data.get(i)[2]).longValue();
            double pct = (double) terjual / max;

            Label lblNama = new Label(nama);
            lblNama.getStyleClass().add("bar-nama");
            lblNama.setPrefWidth(130);
            lblNama.setMinWidth(130);

            Label lblTotal = new Label(terjual + " unit");
            lblTotal.getStyleClass().add("bar-total");

            ProgressBar pb = new ProgressBar(pct);
            pb.getStyleClass().add("bar-progress");
            pb.setPrefHeight(12);
            pb.setMaxWidth(Double.MAX_VALUE);
            pb.setStyle("-fx-accent: " + colors[i % colors.length] + ";");
            HBox.setHgrow(pb, Priority.ALWAYS);

            Label lblKategori = new Label(kategori);
            lblKategori.setStyle("-fx-text-fill: #8B8FA8; -fx-font-size: 10px;");

            HBox row = new HBox(10, lblNama, pb, lblTotal);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("bar-row");

            vboxTerlarisMinggu.getChildren().addAll(row, lblKategori);
        }
    }

    // ─────────────────────────────────────────────────────
    // Rata-rata nilai transaksi hari ini
    // ─────────────────────────────────────────────────────
    private void loadRataRataTransaksi() {
        List<Object[]> data = koneksi.ambilData("""
                SELECT AVG(total_pembayaran), COUNT(*)
                FROM tb_transaksi
                WHERE DATE(tanggal_transaksi) = DATE('now','localtime')
                AND status_pembayaran = 'Lunas'
                """);

        double rata = 0;
        int jumlah = 0;

        if (!data.isEmpty() && data.get(0)[0] != null) {
            rata = ((Number) data.get(0)[0]).doubleValue();
            jumlah = ((Number) data.get(0)[1]).intValue();
        }

        kpiRataRata.setText("Rp " + String.format("%,.0f", rata));
        kpiRataRataInfo.setText("Dari " + jumlah + " transaksi lunas hari ini");

        if (rata == 0) {
            kpiRataRataInfo.setText("Belum ada transaksi lunas hari ini");
            kpiRataRataInfo.setStyle("-fx-text-fill: #8B8FA8;");
        } else {
            kpiRataRataInfo.setStyle("-fx-text-fill: #00E5A0;");
        }
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
