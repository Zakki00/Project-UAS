package com.mycompany.projectuas;

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
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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

    // ═════════════════════════════════════════════════════
    // INITIALIZE
    // ═════════════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupCharts();
        setupTable();
        setupStockList();
        setupNavHover();
    }

    // ═════════════════════════════════════════════════════
    // SIDEBAR TOGGLE (animasi smooth)
    // ═════════════════════════════════════════════════════
    @FXML
    private void onToggleSidebar() {
        sidebarCollapsed = !sidebarCollapsed;

        double targetWidth = sidebarCollapsed ? SIDEBAR_MINI : SIDEBAR_FULL;

        // Animasi lebar sidebar
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(sidebar.prefWidthProperty(), sidebar.getPrefWidth()),
                        new KeyValue(sidebar.minWidthProperty(), sidebar.getMinWidth())),
                new KeyFrame(Duration.millis(350),
                        new KeyValue(sidebar.prefWidthProperty(), targetWidth),
                        new KeyValue(sidebar.minWidthProperty(), targetWidth)));

        // Sembunyikan / tampilkan teks dengan fade
        if (sidebarCollapsed) {
            // Langsung sembunyikan teks saat mulai collapse
            hideSidebarText();
            toggleBtn.setText("▶");
            // Ubah padding logo row ke center
            logoRow.setAlignment(javafx.geometry.Pos.CENTER);
            logoRow.setPadding(new Insets(18, 0, 18, 0));
            userRow.setAlignment(javafx.geometry.Pos.CENTER);
            userRow.setPadding(new Insets(12, 0, 12, 0));
        } else {
            // Tampilkan teks setelah animasi selesai
            timeline.setOnFinished(e -> {
                showSidebarText();
                logoRow.setAlignment(Pos.CENTER_LEFT);
                logoRow.setPadding(new Insets(18, 16, 18, 16));
                userRow.setAlignment(Pos.CENTER_LEFT);
                userRow.setPadding(new Insets(12, 16, 12, 16));
            });
            toggleBtn.setText("◀");
        }

        // Atur padding nav items saat collapse
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
                navLblDashboard, navLblProduk, navLblKasir,
                navLblPelanggan, navLblLaporan, navLblPengaturan);
        for (Label lbl : labels) {
            lbl.setVisible(visible);
            lbl.setManaged(visible);
        }
    }

    private void updateNavPadding(boolean collapsed) {
        Insets collapsedPad = new Insets(10, 0, 10, 0);
        Insets normalPad = new Insets(10, 14, 10, 0);
        Insets pad = collapsed ? collapsedPad : normalPad;

        List<HBox> items = List.of(
                navDashboard, navProduk, navKasir,
                navPelanggan, navLaporan, navPengaturan);
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
    private void onNavPengaturan() {
        setActiveNav(navPengaturan);
    }

    private void setActiveNav(HBox selected) {
        List<HBox> all = List.of(
                navDashboard, navProduk, navKasir,
                navPelanggan, navLaporan, navPengaturan);
        for (HBox item : all) {
            item.getStyleClass().removeAll("nav-active");
            if (!item.getStyleClass().contains("nav-item"))
                item.getStyleClass().add("nav-item");
        }
        selected.getStyleClass().add("nav-active");
    }

    private void setupNavHover() {
        List<HBox> all = List.of(
                navDashboard, navProduk, navKasir,
                navPelanggan, navLaporan, navPengaturan);
        for (HBox item : all) {
            item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: #252840; -fx-background-radius: 10;"));
            item.setOnMouseExited(e -> item.setStyle(""));
        }
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
        List<StockItem> items = List.of(
                new StockItem("Minyak Goreng", 3, 50, "Kritis"),
                new StockItem("Teh Botol Sosro", 12, 100, "Menipis"),
                new StockItem("Gula Pasir", 8, 40, "Kritis"),
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