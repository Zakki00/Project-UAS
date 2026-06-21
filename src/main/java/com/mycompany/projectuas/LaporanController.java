package com.mycompany.projectuas;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;


public class LaporanController implements Initializable {
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
    // FXML — KPI ORIGINAL
    // ═══════════════════════════════════════════════════════
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

    // ═══════════════════════════════════════════════════════
    // FXML — KPI TAMBAHAN
    // ═══════════════════════════════════════════════════════
    @FXML
    private Label kpiPiutangJumlah;
    @FXML
    private Label kpiPiutangNominal;
    @FXML
    private Label kpiRataRata;
    @FXML
    private Label kpiRataRataInfo;

    // ═══════════════════════════════════════════════════════
    // FXML — TABEL LAPORAN
    // ═══════════════════════════════════════════════════════
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
    private TableColumn<LaporanTransaksiItem, String> colJumlahItem;
    @FXML
    private TableColumn<LaporanTransaksiItem, String> colDurasiPs;
    @FXML
    private TableColumn<LaporanModel.LaporanTransaksiItem, LocalDate> colTanggalTransaksi;

    // ═══════════════════════════════════════════════════════
    // FXML — SHIFT CARDS
    // ═══════════════════════════════════════════════════════
    @FXML
    private FlowPane flowKasirPagi;

    @FXML
    private FlowPane flowKasirMalam;
    @FXML
    private VBox cardPagi;
    @FXML
    private VBox cardMalam;
    @FXML
    private StackPane wrapperPagi;
    @FXML
    private StackPane wrapperMalam;
    @FXML
    private Label lblTrxPagi;
    @FXML
    private Label lblItemPagi;
    @FXML
    private Label lblPaketPSPagi;
    @FXML
    private Label lblPaketPSMalam;
    @FXML
    private Label lblPendapatanPagi;
    @FXML
    private Label lblStatusPagi;
    @FXML
    private Label lblJamPagi;
  
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

    // ═══════════════════════════════════════════════════════
    // FXML — CHARTS ORIGINAL
    // ═══════════════════════════════════════════════════════
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
    private VBox stockList;
    @FXML
    private Label lblInfoProduk;
    @FXML
    private Label lblInfoPs;

    // ═══════════════════════════════════════════════════════
    // FXML — CHARTS TAMBAHAN
    // ═══════════════════════════════════════════════════════
    @FXML
    private PieChart pieKomposisi;
    @FXML
    private VBox vboxKategori;
    @FXML
    private BarChart<String, Number> jamChart;
    @FXML
    private PieChart piePiutang;
    @FXML
    private LineChart<String, Number> lineRataRata;
    @FXML
    private VBox vboxTerlarisMinggu;

    // ═══════════════════════════════════════════════════════
    // STATE & KONSTANTA
    // ═══════════════════════════════════════════════════════
    private boolean sidebarCollapsed = false;
    private boolean isUpdating = false;
    private static final double SIDEBAR_FULL = 220;
    private static final double SIDEBAR_MINI = 60;
    private static final NumberFormat FMT = NumberFormat.getInstance(new Locale("id", "ID"));

    // ═══════════════════════════════════════════════════════
    // INITIALIZE
    // ═══════════════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupNavHover();
        setupLogoutHover();
        setActiveNav(navLaporan);
        setupForm();
        loadLaporanTransaksi();
        setupTableLaporan();
        loadKPI();
        setupCharts();
        setupStockList();
    }

    // ═══════════════════════════════════════════════════════
    // ANIMASI HELPER
    // ═══════════════════════════════════════════════════════
    private void animateFadeIn(Node node, double delayMs) {
        node.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(500), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setDelay(Duration.millis(delayMs));
        ft.play();
    }

    private void animateScaleFadeIn(Node node, double delayMs) {
        node.setOpacity(0);
        node.setScaleX(0.85);
        node.setScaleY(0.85);
        FadeTransition ft = new FadeTransition(Duration.millis(500), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ScaleTransition st = new ScaleTransition(Duration.millis(500), node);
        st.setFromX(0.85);
        st.setFromY(0.85);
        st.setToX(1.0);
        st.setToY(1.0);
        ParallelTransition pt = new ParallelTransition(ft, st);
        pt.setDelay(Duration.millis(delayMs));
        pt.play();
    }

    private void animateProgressBar(ProgressBar pb, double targetValue, double delayMs) {
        pb.setProgress(0);
        Timeline tl = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(pb.progressProperty(), 0)),
                new KeyFrame(Duration.millis(800), new KeyValue(pb.progressProperty(), targetValue)));
        tl.setDelay(Duration.millis(delayMs));
        tl.play();
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

    // ═══════════════════════════════════════════════════════
    // FORM & TABEL LAPORAN
    // ═══════════════════════════════════════════════════════
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
            navllblakun.setText(session.role);
            navlblnama.setText(session.nama);
        } else {
            navllblakun.setText(session.role);
            navlblnama.setText(session.nama);
        }


        cbStatusPembayaran.getItems().addAll("Semua", "Lunas", "Belum Lunas");
        cbStatusPembayaran.setOnAction(e -> loadLaporanTransaksi());
        dpTanggal.setOnAction(e -> loadLaporanTransaksi());
        tfNamaPelanggan.textProperty().addListener((obs, old, niu) -> loadLaporanTransaksi());
        tfNominal.textProperty().addListener((obs, old, niu) -> {
            loadLaporanTransaksi();
            onNominalChanged();
        });
        applyRoundedClip(wrapperPagi);
        applyRoundedClip(wrapperMalam);
    }

    private void loadLaporanTransaksi() {
        StringBuilder sql = new StringBuilder("""
                    SELECT
                        t.id_transaksi, t.tanggal_transaksi, t.pelanggan,
                        t.status_pembayaran, t.total_pembayaran, t.uang_pembayaran,
                        t.kembalian, t.kekurangan, u.username, u.nama_lengkap,
                        COUNT(DISTINCT CASE WHEN dt.id_barang IS NOT NULL THEN dt.id_detail END) AS jumlah_item,
                        COALESCE(SUM(DISTINCT pp.durasi), 0) AS total_durasi_ps
                    FROM tb_transaksi t
                    JOIN tb_karyawan u ON t.id_karyawan = u.id_karyawan
                    LEFT JOIN tb_detail_transaksi dt ON t.id_transaksi = dt.id_transaksi
                    LEFT JOIN tb_paket_ps pp ON t.id_transaksi = pp.id_transaksi
                    WHERE 1=1
                """);
        java.util.List<Object> params = new java.util.ArrayList<>();
        if (cbStatusPembayaran.getValue() != null
                && !cbStatusPembayaran.getValue().isEmpty()
                && !cbStatusPembayaran.getValue().equals("Semua")) {
            sql.append(" AND t.status_pembayaran = ?");
            params.add(cbStatusPembayaran.getValue());
        }
        if (dpTanggal.getValue() != null) {
            sql.append(" AND DATE(t.tanggal_transaksi) = ?");
            params.add(dpTanggal.getValue().toString());
        }
        if (!tfNamaPelanggan.getText().trim().isEmpty()) {
            sql.append(" AND t.pelanggan LIKE ?");
            params.add("%" + tfNamaPelanggan.getText().trim() + "%");
        }
        String raw = tfNominal.getText().replaceAll("[^0-9]", "");
        if (!raw.isEmpty()) {
            sql.append(" AND t.total_pembayaran >= ?");
            params.add(Long.parseLong(raw));
        }
        sql.append(
                " GROUP BY t.id_transaksi, t.tanggal_transaksi, t.pelanggan, t.status_pembayaran, t.total_pembayaran, t.uang_pembayaran, t.kembalian, t.kekurangan, u.username, u.nama_lengkap");
        sql.append(" ORDER BY t.tanggal_transaksi DESC");
        List<Object[]> results = params.isEmpty()
                ? koneksi.ambilData(sql.toString())
                : koneksi.ambilData(sql.toString(), params.toArray());
        LaporanModel.dataLaporanTransaksi.clear();
        int no = 1;
        for (Object[] row : results) {
            int idTransaksi = ((Number) row[0]).intValue();
            String tanggalStr = (String) row[1];
            LocalDate tanggal = java.time.LocalDateTime.parse(tanggalStr.replace(" ", "T")).toLocalDate();
            String pelanggan = (String) row[2];
            String statusPembayaran = (String) row[3];
            long totalPembayaran = ((Number) row[4]).longValue();
            long uangPembayaran = ((Number) row[5]).longValue();
            long kembalian = ((Number) row[6]).longValue();
            long kekurangan = ((Number) row[7]).longValue();
            String username = (String) row[8];
            String namaLengkap = (String) row[9];
            int jumlahItem = ((Number) row[10]).intValue();
            int totalDurasiPs = ((Number) row[11]).intValue();
            LaporanModel.dataLaporanTransaksi.add(new LaporanTransaksiItem(
                    no++, idTransaksi, username, namaLengkap, pelanggan,
                    totalPembayaran, uangPembayaran, kembalian, kekurangan,
                    statusPembayaran, tanggal, jumlahItem, totalDurasiPs));
        }
        TableLaporan.setItems(LaporanModel.dataLaporanTransaksi);
        // update tombol export setelah data dimuat
        btnExport.setDisable(LaporanModel.dataLaporanTransaksi.isEmpty());
    }

    private void setupTableLaporan() {
        colNo.setCellValueFactory(
                data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().no).asObject());
        colIdTransaksi.setCellValueFactory(
                data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().idTransaksi).asObject());
        colNama.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().pelanggan));
        colTotalPembayaran.setCellValueFactory(
                data -> new SimpleStringProperty("Rp " + FMT.format(data.getValue().totalPembayaran)));
        colUangPembayaran.setCellValueFactory(
                data -> new SimpleStringProperty("Rp " + FMT.format(data.getValue().uangPembayaran)));
        colKembalian.setCellValueFactory(
                data -> new SimpleStringProperty("Rp " + FMT.format(data.getValue().kembalian)));
        colKekurangan.setCellValueFactory(
                data -> new SimpleStringProperty("Rp " + FMT.format(data.getValue().kekurangan)));
        colStatus.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().statusPembayaran));
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
                label.setAlignment(Pos.CENTER);
                label.setPadding(new Insets(4, 10, 4, 10));
                if (status.equalsIgnoreCase("Lunas")) {
                    label.setStyle(
                            "-fx-text-fill: #00C853; -fx-font-weight: bold; -fx-background-color: rgba(0,200,83,0.15); -fx-background-radius: 6;");
                } else if (status.equalsIgnoreCase("Belum Lunas")) {
                    label.setStyle(
                            "-fx-text-fill: #D50000; -fx-font-weight: bold; -fx-background-color: rgba(213,0,0,0.12); -fx-background-radius: 6;");
                } else {
                    label.setStyle("");
                }
                setGraphic(label);
                setStyle("");
            }
        });
        colTanggalTransaksi.setCellValueFactory(
                data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().tanggalTransaksi));
        colUser.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().username));
        colNamalengkap.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().namaLengkap));
        colJumlahItem.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().jumlahItem > 0 ? data.getValue().jumlahItem + " item" : "-"));
        colDurasiPs.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().totalDurasiPs > 0 ? data.getValue().totalDurasiPs + " menit" : "-"));
        TableLaporan.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private boolean isUpdating() {
        return isUpdating;
    }

    private void onNominalChanged() {
        if (isUpdating)
            return;
        isUpdating = true;

        String raw = tfNominal.getText().replaceAll("[^0-9]", "");

        if (raw.isEmpty()) {
            Platform.runLater(() -> {
                tfNominal.setText("");
                isUpdating = false;
            });
            return;
        }

        try {
            long value = Long.parseLong(raw);
            String formatted = "Rp " + FMT.format(value);
            Platform.runLater(() -> {
                tfNominal.setText(formatted);
                tfNominal.positionCaret(formatted.length());
                isUpdating = false;
            });
        } catch (NumberFormatException e) {
            isUpdating = false;
        }
    }

    // ═══════════════════════════════════════════════════════
    // KPI ORIGINAL
    // ═══════════════════════════════════════════════════════
    private void loadKPI() {
        loadTotalPenjualan();
        loadTotalTransaksi();
        loadProdukTerjual();
        loadStokMenipis();
        loadShiftData();
        loadPiutangHariIni();
        loadRataRataTransaksi();
    }

    private void setPersenKPI(Label label, double today, double yesterday) {
        double persen = yesterday == 0 ? (today > 0 ? 100 : 0) : ((today - yesterday) / yesterday) * 100;
        String icon, status, color;
        if (persen > 0) {
            icon = "▲ +";
            status = "lebih besar dari kemarin";
            color = "#22c55e";
        } else if (persen < 0) {
            icon = "▼ -";
            status = "lebih kecil dari kemarin";
            color = "#ef4444";
        } else {
            icon = "■ ";
            status = "tidak berubah dari kemarin";
            color = "#9ca3af";
        }
        label.setText(icon + String.format("%.1f%% %s", Math.abs(persen), status));
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: " + color + ";");
    }

    private void loadTotalPenjualan() {
        List<Object[]> todayData = koneksi.ambilData(
                "SELECT COALESCE(SUM(total_pembayaran - kekurangan), 0) FROM tb_transaksi WHERE DATE(tanggal_transaksi) = DATE('now','localtime')");
        List<Object[]> yesterdayData = koneksi.ambilData(
                "SELECT COALESCE(SUM(uang_pembayaran), 0) FROM tb_transaksi WHERE DATE(tanggal_transaksi) = DATE('now', '-1 day')");
        double today = (!todayData.isEmpty() && todayData.get(0)[0] != null)
                ? ((Number) todayData.get(0)[0]).doubleValue()
                : 0;
        double yesterday = (!yesterdayData.isEmpty() && yesterdayData.get(0)[0] != null)
                ? ((Number) yesterdayData.get(0)[0]).doubleValue()
                : 0;
        kpiPenjualan.setText("Rp " + String.format("%,.0f", today));
        setPersenKPI(kpiPenjulanPersen, today, yesterday);
    }

    private void loadTotalTransaksi() {
        List<Object[]> todayData = koneksi
                .ambilData("SELECT COUNT(*) FROM tb_transaksi WHERE DATE(tanggal_transaksi) = DATE('now','localtime')");
        List<Object[]> yesterdayData = koneksi
                .ambilData("SELECT COUNT(*) FROM tb_transaksi WHERE DATE(tanggal_transaksi) = DATE('now', '-1 day')");
        double today = (!todayData.isEmpty() && todayData.get(0)[0] != null)
                ? ((Number) todayData.get(0)[0]).doubleValue()
                : 0;
        double yesterday = (!yesterdayData.isEmpty() && yesterdayData.get(0)[0] != null)
                ? ((Number) yesterdayData.get(0)[0]).doubleValue()
                : 0;
        kpiTransaksi.setText(String.valueOf((int) today));
        setPersenKPI(kpiTransaksiPersen, today, yesterday);
    }

    private void loadProdukTerjual() {
        List<Object[]> todayData = koneksi.ambilData(
                "SELECT COALESCE(SUM(jumlah), 0) FROM tb_detail_transaksi dt JOIN tb_transaksi t ON dt.id_transaksi = t.id_transaksi WHERE DATE(t.tanggal_transaksi) = DATE('now','localtime')");
        List<Object[]> yesterdayData = koneksi.ambilData(
                "SELECT COALESCE(SUM(jumlah), 0) FROM tb_detail_transaksi dt JOIN tb_transaksi t ON dt.id_transaksi = t.id_transaksi WHERE DATE(t.tanggal_transaksi) = DATE('now', '-1 day')");
        double today = (!todayData.isEmpty() && todayData.get(0)[0] != null)
                ? ((Number) todayData.get(0)[0]).doubleValue()
                : 0;
        double yesterday = (!yesterdayData.isEmpty() && yesterdayData.get(0)[0] != null)
                ? ((Number) yesterdayData.get(0)[0]).doubleValue()
                : 0;
        kpiProduk.setText((int) today + " unit");
        setPersenKPI(kpiProdukPersen, today, yesterday);
    }

    private void loadStokMenipis() {
        List<Object[]> stokData = koneksi.ambilData("SELECT COUNT(*) FROM tb_barang WHERE stok <= 5");
        int stokMenipis = (!stokData.isEmpty() && stokData.get(0)[0] != null) ? ((Number) stokData.get(0)[0]).intValue()
                : 0;
        kpiStok.setText(stokMenipis + " Item");
        if (stokMenipis == 0) {
            kpiStokInfo.setText("Semua stok aman");
            kpiStokInfo.setStyle("-fx-text-fill: #00C853; -fx-font-weight: bold;");
        } else if (stokMenipis <= 5) {
            kpiStokInfo.setText("⚠ Perlu restock segera");
            kpiStokInfo.setStyle("-fx-text-fill: #D50000; -fx-font-weight: bold;");
        } else {
            kpiStokInfo.setText("Stok kritis");
        }
    }

    // ═══════════════════════════════════════════════════════
    // KPI TAMBAHAN
    // ═══════════════════════════════════════════════════════
    private void loadPiutangHariIni() {
        List<Object[]> data = koneksi.ambilData("""
                    SELECT COUNT(*), COALESCE(SUM(kekurangan), 0)
                    FROM tb_transaksi
                    WHERE status_pembayaran != 'Lunas'
                    AND DATE(tanggal_transaksi) = DATE('now','localtime')
                """);
        int jumlah = (!data.isEmpty() && data.get(0)[0] != null) ? ((Number) data.get(0)[0]).intValue() : 0;
        double nominal = (!data.isEmpty() && data.get(0)[1] != null) ? ((Number) data.get(0)[1]).doubleValue() : 0;
        kpiPiutangJumlah.setText(jumlah + " Transaksi");
        if (jumlah == 0) {
            kpiPiutangJumlah.setStyle("-fx-text-fill: #00E5A0; -fx-font-weight: bold;");
            kpiPiutangNominal.setText("✔ Semua lunas");
            kpiPiutangNominal.setStyle("-fx-text-fill: #00E5A0;");
        } else {
            kpiPiutangJumlah.setStyle("-fx-text-fill: #FF5C7C; -fx-font-weight: bold;");
            kpiPiutangNominal.setText("Rp " + String.format("%,.0f", nominal));
            kpiPiutangNominal.setStyle("-fx-text-fill: #FF5C7C;");
        }
    }

    private void loadRataRataTransaksi() {
        List<Object[]> data = koneksi.ambilData("""
                    SELECT AVG(total_pembayaran), COUNT(*)
                    FROM tb_transaksi
                    WHERE DATE(tanggal_transaksi) = DATE('now','localtime')
                    AND status_pembayaran = 'Lunas'
                """);
        double rata = (!data.isEmpty() && data.get(0)[0] != null) ? ((Number) data.get(0)[0]).doubleValue() : 0;
        int jumlah = (!data.isEmpty() && data.get(0)[1] != null) ? ((Number) data.get(0)[1]).intValue() : 0;
        kpiRataRata.setText("Rp " + String.format("%,.0f", rata));
        if (rata == 0) {
            kpiRataRataInfo.setText("Belum ada transaksi lunas hari ini");
            kpiRataRataInfo.setStyle("-fx-text-fill: #8B8FA8;");
        } else {
            kpiRataRataInfo.setText("Dari " + jumlah + " transaksi lunas hari ini");
            kpiRataRataInfo.setStyle("-fx-text-fill: #00E5A0;");
        }
    }

    // ═══════════════════════════════════════════════════════
    // SHIFT DATA
    // ═══════════════════════════════════════════════════════
    private void loadShiftData() {

        loadShift(
                "06:00:00",
                "12:00:00",
                lblTrxPagi,
                lblItemPagi,
                lblPaketPSPagi,
                lblPendapatanPagi,
                lblJamPagi,
                "06:00 — 12:00");

        loadShift(
                "12:00:00",
                "21:00:00",
                lblTrxMalam,
                lblItemMalam,
                lblPaketPSMalam,
                lblPendapatanMalam,
                lblJamMalam,
                "12:00 — 21:00");

        loadKasirShift(
                "06:00:00",
                "12:00:00",
                flowKasirPagi,true);

        loadKasirShift(
                "12:00:00",
                "23:00:00",
                flowKasirMalam,false);

        updateStatusDot();
    }

    private void loadShift(String jamMulai, String jamSelesai,
            Label lblTrx,
            Label lblItem,
            Label lblPaketPS,
            Label lblPendapatan,
            Label lblJam,
            String jamText) {

        String sql = """
                    SELECT
                        COUNT(DISTINCT t.id_transaksi) AS total_trx,
                        COALESCE(
                            SUM(
                                CASE
                                    WHEN dt.id_barang IS NOT NULL
                                    THEN dt.jumlah
                                    ELSE 0
                                END
                            ),
                        0) AS total_item,
                        COALESCE(COUNT(DISTINCT ps.id_paket_ps),0) AS total_paket_ps,
                        COALESCE(SUM(t.uang_pembayaran - t.kembalian),0) AS pendapatan
                    FROM tb_transaksi t
                    LEFT JOIN tb_detail_transaksi dt
                        ON t.id_transaksi = dt.id_transaksi
                    LEFT JOIN tb_paket_ps ps
                        ON t.id_transaksi = ps.id_transaksi
                    WHERE DATE(t.tanggal_transaksi)=DATE('now','localtime')
                      AND TIME(t.tanggal_transaksi) BETWEEN ? AND ?
                """;

        List<Object[]> data = koneksi.ambilData(
                sql,
                jamMulai,
                jamSelesai);

        lblJam.setText(jamText);

        if (data.isEmpty()) {
            lblTrx.setText("0");
            lblItem.setText("0");
            lblPaketPS.setText("0");
            lblPendapatan.setText("Rp 0");
            return;
        }
        

        Object[] row = data.get(0);

        lblTrx.setText(String.valueOf(((Number) row[0]).intValue()));
        lblItem.setText(String.valueOf(((Number) row[1]).intValue()));
        lblPaketPS.setText(String.valueOf(((Number) row[2]).intValue()));
        lblPendapatan.setText("Rp " + FMT.format(((Number) row[3]).longValue()));
    }

    private void loadKasirShift(
            String jamMulai,
            String jamSelesai,
            FlowPane flowPane,
            boolean shiftPagi) {

        String sql = """
                SELECT DISTINCT u.nama_lengkap
                FROM tb_transaksi t
                JOIN tb_karyawan u ON u.id_karyawan = t.id_karyawan
                WHERE DATE(t.tanggal_transaksi)=DATE('now','localtime')
                  AND TIME(t.tanggal_transaksi) BETWEEN ? AND ?
                ORDER BY u.nama_lengkap
                """;

        List<Object[]> data = koneksi.ambilData(sql, jamMulai, jamSelesai);

        flowPane.getChildren().clear();

        if (data.isEmpty()) {
            Label chip = new Label("Tidak ada kasir");
            chip.getStyleClass().add("kasir-chip");
            chip.getStyleClass().add(shiftPagi ? "chip-pagi" : "chip-malam");
            flowPane.getChildren().add(chip);
            return;
        }

        for (Object[] row : data) {
            Label chip = new Label(String.valueOf(row[0]));
            chip.getStyleClass().add("kasir-chip");
            chip.getStyleClass().add(shiftPagi ? "chip-pagi" : "chip-malam");
            flowPane.getChildren().add(chip);
        }
    }

    private void updateStatusDot() {
        int jam = java.time.LocalTime.now().getHour();
        boolean pagiAktif = jam >= 6 && jam < 12;
        boolean malamAktif = jam >= 12 && jam < 21;
        lblStatusPagi.getStyleClass().removeAll("shift-status-aktif", "shift-status-nonaktif");
        lblStatusPagi.getStyleClass().add(pagiAktif ? "shift-status-aktif" : "shift-status-nonaktif");
        lblStatusMalam.getStyleClass().removeAll("shift-status-aktif", "shift-status-nonaktif");
        lblStatusMalam.getStyleClass().add(malamAktif ? "shift-status-aktif" : "shift-status-nonaktif");
    }

    // ═══════════════════════════════════════════════════════
    // SETUP CHARTS — urutan sesuai tampilan
    // ═══════════════════════════════════════════════════════
    private void setupCharts() {
        // KPI row: pie piutang | terlaris minggu | rata-rata line
        loadPiePiutang();
        loadTerlarisMingguIni();
        loadLineRataRata();
        // Grafik penjualan & transaksi/hari
        setupSalesChart();
        setupTrxChart();
        // Top produk, top PS, tren 6 bulan
        loadBarChart();
        loadBarChartPs();
        setupMonthChart();
        // Komposisi, kategori, transaksi per jam
        loadKomposisiPendapatan();
        loadOmzetPerKategori();
        loadTransaksiPerJam();
    }

    // ═══════════════════════════════════════════════════════
    // CHARTS ORIGINAL
    // ═══════════════════════════════════════════════════════
    private void setupSalesChart() {
        String sql = """
                    SELECT date(tanggal_transaksi) AS tanggal, SUM(total_pembayaran) AS total
                    FROM tb_transaksi
                    WHERE tanggal_transaksi >= date('now','localtime', '-7 day')
                    AND status_pembayaran = 'Lunas'
                    GROUP BY date(tanggal_transaksi)
                    ORDER BY date(tanggal_transaksi)
                """;
        List<Object[]> data = koneksi.ambilData(sql);
        String[] namaHari = { "Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab" };
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter fmtLabel = DateTimeFormatter.ofPattern("dd/MM");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Penjualan");
        if (data.isEmpty()) {
            for (int i = 6; i >= 0; i--) {
                LocalDate tgl = LocalDate.now().minusDays(i);
                series.getData().add(new XYChart.Data<>(
                        namaHari[tgl.getDayOfWeek().getValue() % 7] + "\n" + tgl.format(fmtLabel), 0));
            }
        } else {
            for (Object[] row : data) {
                LocalDate tgl = LocalDate.parse(String.valueOf(row[0]), fmt);
                series.getData()
                        .add(new XYChart.Data<>(
                                namaHari[tgl.getDayOfWeek().getValue() % 7] + "\n" + tgl.format(fmtLabel),
                                ((Number) row[1]).longValue()));
            }
        }
        salesChart.getData().clear();
        salesChart.getData().add(series);
        salesChart.setLegendVisible(false);
        salesChart.setAnimated(true);
        animateFadeIn(salesChart, 200);
    }

    private void setupTrxChart() {
        String sql = """
                    SELECT strftime('%w', tanggal_transaksi) AS hari, COUNT(*) AS jumlah
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
            for (String d : namaHari)
                series.getData().add(new XYChart.Data<>(d, 0));
        } else {
            for (Object[] row : data) {
                int hariIdx = Integer.parseInt(String.valueOf(row[0]));
                series.getData().add(new XYChart.Data<>(namaHari[hariIdx], ((Number) row[1]).intValue()));
            }
        }
        trxChart.getData().clear();
        trxChart.getData().add(series);
        trxChart.setLegendVisible(false);
        trxChart.setAnimated(true);
        animateFadeIn(trxChart, 300);
    }

    private void setupMonthChart() {
        String sql = """
                    SELECT strftime('%m', tanggal_transaksi) AS bulan, SUM(total_pembayaran) AS total
                    FROM tb_transaksi
                    WHERE tanggal_transaksi >= date('now', '-6 month')
                    AND status_pembayaran = 'Lunas'
                    GROUP BY strftime('%m', tanggal_transaksi)
                    ORDER BY bulan
                """;
        List<Object[]> data = koneksi.ambilData(sql);
        String[] namaBulan = { "", "Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Ags", "Sep", "Okt", "Nov", "Des" };
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Omzet");
        if (data.isEmpty()) {
            for (String m : namaBulan)
                series.getData().add(new XYChart.Data<>(m, 0));
        } else {
            for (Object[] row : data) {
                int bulanIdx = Integer.parseInt(String.valueOf(row[0]));
                series.getData().add(new XYChart.Data<>(namaBulan[bulanIdx], ((Number) row[1]).longValue()));
            }
        }
        monthChart.getData().clear();
        monthChart.getData().add(series);
        monthChart.setLegendVisible(false);
        monthChart.setAnimated(true);
        monthChart.setCreateSymbols(false);
        animateFadeIn(monthChart, 400);
    }

    private void loadBarChart() {
        String sql = """
                    SELECT b.nama_barang, SUM(dt.jumlah) AS total
                    FROM tb_detail_transaksi dt
                    JOIN tb_barang b ON dt.id_barang = b.id_barang
                    GROUP BY b.nama_barang
                    ORDER BY total DESC LIMIT 5
                """;

        List<Object[]> data = koneksi.ambilData(sql);

        // Hapus data chart lama, sisakan judul dan subjudul
        if (vboxChart.getChildren().size() > 2) {
            vboxChart.getChildren().remove(2, vboxChart.getChildren().size());
        }

        if (data.isEmpty()) {
            lblInfoProduk.setText("Tidak ada data tersedia");
            return;
        }

        lblInfoProduk.setText("Berdasarkan jumlah terjual");

        long max = 1;
        for (Object[] row : data) {
            long v = ((Number) row[1]).longValue();
            if (v > max) {
                max = v;
            }
        }

        String[] colors = {
                "#6C63FF",
                "#00D4FF",
                "#00E5A0",
                "#FFD166",
                "#FF5C7C"
        };

        for (int i = 0; i < data.size(); i++) {

            String nama = data.get(i)[0].toString();
            long total = ((Number) data.get(i)[1]).longValue();
            double pct = (double) total / max;

            Label lblNama = new Label(nama);
            lblNama.getStyleClass().add("bar-nama");
            lblNama.setPrefWidth(140);
            lblNama.setMinWidth(140);
            lblNama.setMaxWidth(140);

            Label lblTotal = new Label(total + " unit");
            lblTotal.getStyleClass().add("bar-total");

            ProgressBar pb = new ProgressBar(0);
            pb.getStyleClass().add("bar-progress");
            pb.setPrefHeight(12);
            pb.setMaxWidth(Double.MAX_VALUE);
            pb.setStyle("-fx-accent: " + colors[i % colors.length] + ";");

            HBox.setHgrow(pb, Priority.ALWAYS);

            HBox row = new HBox(10, lblNama, pb, lblTotal);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("bar-row");

            vboxChart.getChildren().add(row);

            animateProgressBar(pb, pct, 100 + i * 80);
            animateFadeIn(row, 100 + i * 80);
        }
    }

    private void loadBarChartPs() {
        String sql = """
                    SELECT pp.durasi, COUNT(pp.id_paket_ps) AS total
                    FROM tb_paket_ps pp
                    GROUP BY pp.durasi
                    ORDER BY total DESC LIMIT 5
                """;

        List<Object[]> data = koneksi.ambilData(sql);

        // Hapus data chart lama, sisakan judul dan subjudul
        if (vboxChartPs.getChildren().size() > 2) {
            vboxChartPs.getChildren().remove(2, vboxChartPs.getChildren().size());
        }

        if (data.isEmpty()) {
            lblInfoPs.setText("Tidak ada data tersedia");
            return;
        }

        lblInfoPs.setText("Berdasarkan jumlah pemain");

        long max = 1;
        for (Object[] row : data) {
            long v = ((Number) row[1]).longValue();
            if (v > max) {
                max = v;
            }
        }

        String[] colors = {
                "#6C63FF",
                "#00D4FF",
                "#00E5A0",
                "#FFD166",
                "#FF5C7C"
        };

        for (int i = 0; i < data.size(); i++) {

            int durasi = ((Number) data.get(i)[0]).intValue();
            long total = ((Number) data.get(i)[1]).longValue();
            double pct = (double) total / max;

            int jam = durasi / 60;
            int menit = durasi % 60;

            String labelDurasi = durasi >= 60
                    ? (menit > 0 ? jam + " jam " + menit + " mnt" : jam + " jam")
                    : durasi + " mnt";

            Label lblNama = new Label(labelDurasi);
            lblNama.getStyleClass().add("bar-nama");
            lblNama.setPrefWidth(140);
            lblNama.setMinWidth(140);
            lblNama.setMaxWidth(140);

            Label lblTotal = new Label(total + "x");
            lblTotal.getStyleClass().add("bar-total");

            ProgressBar pb = new ProgressBar(0);
            pb.getStyleClass().add("bar-progress");
            pb.setPrefHeight(12);
            pb.setMaxWidth(Double.MAX_VALUE);
            pb.setStyle("-fx-accent: " + colors[i % colors.length] + ";");

            HBox.setHgrow(pb, Priority.ALWAYS);

            HBox row = new HBox(10, lblNama, pb, lblTotal);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("bar-row");

            vboxChartPs.getChildren().add(row);

            animateProgressBar(pb, pct, 150 + i * 80);
            animateFadeIn(row, 150 + i * 80);
        }
    }

    // ═══════════════════════════════════════════════════════
    // STOCK LIST
    // ═══════════════════════════════════════════════════════
    private void setupStockList() {
        String sql = """
                    SELECT nama_barang, stok,
                        CASE
                            WHEN stok = 0   THEN 'Habis'
                            WHEN stok <= 5  THEN 'Kritis'
                            WHEN stok <= 20 THEN 'Menipis'
                            ELSE 'Aman'
                        END AS status
                    FROM tb_barang
                    WHERE stok <= 20
                    ORDER BY stok ASC LIMIT 4
                """;
        List<Object[]> data = koneksi.ambilData(sql);
        if (data.isEmpty()) {
            chartslbstok.setText("Tidak Ada Stok Barang Yang Habis");
            chartslbSegeraStok.setVisible(false);
        }
        stockList.getChildren().clear();
        for (int idx = 0; idx < data.size(); idx++) {
            Object[] row = data.get(idx);
            String name = String.valueOf(row[0]);
            int stock = ((Number) row[1]).intValue();
            String status = String.valueOf(row[2]);
            double pct = Math.min(1.0, (double) stock / 50);
            boolean kritis = status.equals("Kritis") || status.equals("Habis");
            Label nameLabel = new Label(name);
            nameLabel.getStyleClass().add("stock-item-name");
            Label countLabel = new Label(stock + " unit");
            countLabel.getStyleClass().add("stock-item-count");
            Label badge = new Label(status);
            badge.getStyleClass().add(kritis ? "badge-kritis" : "badge-menipis");
            HBox rightBox = new HBox(6, countLabel, badge);
            rightBox.setAlignment(Pos.CENTER_RIGHT);
            rightBox.setMinWidth(Region.USE_PREF_SIZE);
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            HBox topRow = new HBox(8, nameLabel, spacer, rightBox);
            topRow.setAlignment(Pos.CENTER_LEFT);
            topRow.setMaxWidth(Double.MAX_VALUE);
            ProgressBar pb = new ProgressBar(0);
            pb.setMaxWidth(Double.MAX_VALUE);
            pb.setPrefHeight(5);
            pb.getStyleClass().add(kritis ? "progress-kritis" : "progress-menipis");
            VBox itemBox = new VBox(4, topRow, pb);
            stockList.getChildren().add(itemBox);
            animateProgressBar(pb, pct, 100 + idx * 100);
            animateFadeIn(itemBox, 100 + idx * 100);
        }
    }

    // ═══════════════════════════════════════════════════════
    // CHARTS TAMBAHAN
    // ═══════════════════════════════════════════════════════

    /** Pie: Lunas vs Belum Lunas hari ini */
    private void loadPiePiutang() {
        List<Object[]> lunas = koneksi.ambilData("""
                    SELECT COUNT(*) FROM tb_transaksi
                    WHERE status_pembayaran = 'Lunas'
                    AND DATE(tanggal_transaksi) = DATE('now','localtime')
                """);
        List<Object[]> belum = koneksi.ambilData("""
                    SELECT COUNT(*) FROM tb_transaksi
                    WHERE status_pembayaran != 'Lunas'
                    AND DATE(tanggal_transaksi) = DATE('now','localtime')
                """);
        int jLunas = (!lunas.isEmpty() && lunas.get(0)[0] != null) ? ((Number) lunas.get(0)[0]).intValue() : 0;
        int jBelum = (!belum.isEmpty() && belum.get(0)[0] != null) ? ((Number) belum.get(0)[0]).intValue() : 0;
        piePiutang.getData().clear();
        if (jLunas == 0 && jBelum == 0) {
            piePiutang.getData().add(new PieChart.Data("Belum ada data", 1));
            return;
        }
        piePiutang.getData().add(new PieChart.Data("Lunas (" + jLunas + ")", jLunas));
        piePiutang.getData().add(new PieChart.Data("Belum Lunas (" + jBelum + ")", jBelum));
        piePiutang.setAnimated(true);
        piePiutang.setLabelsVisible(true);
        piePiutang.setMinWidth(280);
        piePiutang.setMinWidth(280);
        piePiutang.setPrefWidth(280);
        piePiutang.setPrefHeight(280);
        piePiutang.setMinHeight(280);
        animateScaleFadeIn(piePiutang, 150);
        Platform.runLater(() -> {
            if (piePiutang.getData().size() >= 2) {
                piePiutang.getData().get(0).getNode().setStyle("-fx-pie-color: #00E5A0;");
                piePiutang.getData().get(1).getNode().setStyle("-fx-pie-color: #FF5C7C;");

                // Fix warna legend
                javafx.scene.chart.PieChart.Data d0 = piePiutang.getData().get(0);
                javafx.scene.chart.PieChart.Data d1 = piePiutang.getData().get(1);

                piePiutang.lookupAll(".chart-legend-item-symbol").forEach(node -> {
                    String text = ((javafx.scene.control.Label) node.getParent()).getText();
                    if (text != null && text.startsWith("Lunas (")) {
                        node.setStyle("-fx-background-color: #00E5A0;");
                    } else if (text != null && text.startsWith("Belum Lunas (")) {
                        node.setStyle("-fx-background-color: #FF5C7C;");
                    }
                });
            }
        });
    }

    /** Progress bar: Produk terlaris minggu ini */
    private void loadTerlarisMingguIni() {
        String sql = """
                    SELECT b.nama_barang, b.kategori, SUM(dt.jumlah) AS terjual
                    FROM tb_detail_transaksi dt
                    JOIN tb_barang b ON dt.id_barang = b.id_barang
                    JOIN tb_transaksi t ON dt.id_transaksi = t.id_transaksi
                    WHERE DATE(t.tanggal_transaksi) >= DATE('now', 'localtime', '-7 day')
                    GROUP BY b.nama_barang
                    ORDER BY terjual DESC LIMIT 5
                """;
        List<Object[]> data = koneksi.ambilData(sql);
        long max = 1;
        for (Object[] row : data) {
            long v = ((Number) row[2]).longValue();
            if (v > max)
                max = v;
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
            ProgressBar pb = new ProgressBar(0);
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
            animateProgressBar(pb, pct, 100 + i * 80);
            animateFadeIn(row, 100 + i * 80);
        }
    }

    /** Line chart: tren rata-rata transaksi 7 hari */
    private void loadLineRataRata() {
        String sql = """
                    SELECT date(tanggal_transaksi) AS tgl, AVG(total_pembayaran) AS rata
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
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter fmtLabel = DateTimeFormatter.ofPattern("dd/MM");
        if (data.isEmpty()) {
            for (int i = 6; i >= 0; i--) {
                LocalDate tgl = LocalDate.now().minusDays(i);
                series.getData().add(new XYChart.Data<>(
                        namaHari[tgl.getDayOfWeek().getValue() % 7] + "\n" + tgl.format(fmtLabel), 0));
            }
        } else {
            for (Object[] row : data) {
                LocalDate tgl = LocalDate.parse(String.valueOf(row[0]), fmt);
                series.getData()
                        .add(new XYChart.Data<>(
                                namaHari[tgl.getDayOfWeek().getValue() % 7] + "\n" + tgl.format(fmtLabel),
                                ((Number) row[1]).doubleValue()));
            }
        }
        lineRataRata.getData().clear();
        lineRataRata.getData().add(series);
        lineRataRata.setAnimated(true);
        animateFadeIn(lineRataRata, 250);
    }

    /** Pie: Komposisi pendapatan Produk vs PS + summary samping */
    private void loadKomposisiPendapatan() {
        String sql = """
                    SELECT
                        CASE WHEN dt.id_paket_ps IS NOT NULL THEN 'Rental PS' ELSE 'Produk' END AS kategori,
                        SUM(dt.harga * dt.jumlah) AS total
                    FROM tb_detail_transaksi dt
                    JOIN tb_transaksi t ON dt.id_transaksi = t.id_transaksi
                    WHERE t.tanggal_transaksi >= date('now', '-30 day')
                    GROUP BY kategori
                """;
        List<Object[]> data = koneksi.ambilData(sql);
        pieKomposisi.getData().clear();
        VBox parentCard = (VBox) pieKomposisi.getParent();
        parentCard.getChildren().removeIf(n -> "komposisi-summary".equals(n.getUserData()));
        parentCard.getChildren().removeIf(n -> "komposisi-wrapper".equals(n.getUserData()));
        if (data.isEmpty()) {
            pieKomposisi.getData().add(new PieChart.Data("Belum ada data", 1));
            return;
        }
        double grandTotal = 0;
        for (Object[] row : data)
            grandTotal += ((Number) row[1]).doubleValue();
        for (Object[] row : data) {
            pieKomposisi.getData().add(new PieChart.Data(row[0].toString(), ((Number) row[1]).doubleValue()));
        }
        pieKomposisi.setAnimated(true);
        pieKomposisi.setLabelsVisible(true);
        String[] colors = { "#6C63FF", "#00D4FF" };
        final double gt = grandTotal;
        Platform.runLater(() -> {
            if (pieKomposisi.getData().size() >= 2) {
                pieKomposisi.getData().get(0).getNode().setStyle("-fx-pie-color: #6C63FF;");
                pieKomposisi.getData().get(1).getNode().setStyle("-fx-pie-color: #00D4FF;");

                // Fix warna legend
                javafx.scene.chart.PieChart.Data d0 = pieKomposisi.getData().get(0);
                javafx.scene.chart.PieChart.Data d1 = pieKomposisi.getData().get(1);

                pieKomposisi.lookupAll(".chart-legend-item-symbol").forEach(node -> {
                    String text = ((javafx.scene.control.Label) node.getParent()).getText();

                    if ("Produk".equals(text)) {
                        node.setStyle("-fx-background-color: #6C63FF;");
                    } else if ("Rental PS".equals(text)) {
                        node.setStyle("-fx-background-color: #00D4FF;");
                    }
                });
            }
        });
        // Summary box kanan
        VBox summaryBox = new VBox(10);
        summaryBox.setAlignment(Pos.CENTER);
        summaryBox.setUserData("komposisi-summary");
        HBox.setHgrow(summaryBox, Priority.ALWAYS);
        for (int i = 0; i < data.size(); i++) {
            String kategori = data.get(i)[0].toString();
            double total = ((Number) data.get(i)[1]).doubleValue();
            double pct = gt > 0 ? (total / gt) * 100 : 0;
            Label dot = new Label("●");
            dot.setStyle("-fx-text-fill: " + colors[i % colors.length] + "; -fx-font-size: 14px;");
            Label lblNama = new Label(kategori);
            lblNama.setStyle("-fx-text-fill: #8B8FA8; -fx-font-size: 12px;");
            Label lblNominal = new Label("Rp " + String.format("%,.0f", total));
            lblNominal.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 12px;");
            Label lblPct = new Label(String.format("%.1f%%", pct));
            lblPct.setStyle("-fx-text-fill: " + colors[i % colors.length] + "; -fx-font-size: 11px;");
            VBox item = new VBox(3, new HBox(5, dot, lblNama), lblNominal, lblPct);
            item.setAlignment(Pos.CENTER_LEFT);
            item.setStyle("-fx-background-color: #2E3250; -fx-background-radius: 8; -fx-padding: 8 14 8 14;");
            item.setMaxWidth(Double.MAX_VALUE);
            summaryBox.getChildren().add(item);
            animateFadeIn(item, 200 + i * 100);
        }
        Label lblTotalTitle = new Label("Total 30 hari");
        lblTotalTitle.setStyle("-fx-text-fill: #8B8FA8; -fx-font-size: 11px;");
        Label lblTotalNominal = new Label("Rp " + String.format("%,.0f", grandTotal));
        lblTotalNominal.setStyle("-fx-text-fill: #FFD166; -fx-font-weight: bold; -fx-font-size: 13px;");
        VBox totalItem = new VBox(3, lblTotalTitle, lblTotalNominal);
        totalItem.setAlignment(Pos.CENTER_LEFT);
        totalItem.setStyle("-fx-background-color: #2E3250; -fx-background-radius: 8; -fx-padding: 8 14 8 14;");
        totalItem.setMaxWidth(Double.MAX_VALUE);
        summaryBox.getChildren().add(totalItem);
        pieKomposisi.setMinWidth(280);
        pieKomposisi.setPrefWidth(280);
        pieKomposisi.setPrefHeight(280);
        pieKomposisi.setMinHeight(280);
        HBox.setHgrow(pieKomposisi, Priority.NEVER);
        HBox wrapper = new HBox(16, pieKomposisi, summaryBox);
        wrapper.setAlignment(Pos.CENTER_LEFT);
        wrapper.setUserData("komposisi-wrapper");
        wrapper.setMaxWidth(Double.MAX_VALUE);
        parentCard.getChildren().add(wrapper);
        animateScaleFadeIn(pieKomposisi, 100);
    }

    /** Progress bar: omzet per kategori barang */
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
            long v = ((Number) row[1]).longValue();
            if (v > max)
                max = v;
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
            ProgressBar pb = new ProgressBar(0);
            pb.getStyleClass().add("bar-progress");
            pb.setPrefHeight(12);
            pb.setMaxWidth(Double.MAX_VALUE);
            pb.setStyle("-fx-accent: " + colors[i % colors.length] + ";");
            HBox.setHgrow(pb, Priority.ALWAYS);
            HBox row = new HBox(10, lblNama, pb, lblTotal);
            row.setAlignment(Pos.CENTER_LEFT);
            row.getStyleClass().add("bar-row");
            vboxKategori.getChildren().add(row);
            animateProgressBar(pb, pct, 100 + i * 80);
            animateFadeIn(row, 100 + i * 80);
        }
    }

    /** Bar chart: transaksi per jam hari ini */
    private void loadTransaksiPerJam() {
        String sql = """
                    SELECT strftime('%H', tanggal_transaksi) AS jam, COUNT(*) AS jumlah
                    FROM tb_transaksi
                    WHERE DATE(tanggal_transaksi) = DATE('now','localtime')
                    GROUP BY jam ORDER BY jam
                """;
        List<Object[]> data = koneksi.ambilData(sql);
        java.util.Map<String, Integer> mapJam = new java.util.LinkedHashMap<>();
        for (int h = 7; h <= 22; h++)
            mapJam.put(String.format("%02d", h), 0);
        for (Object[] row : data) {
            String jam = String.valueOf(row[0]);
            if (mapJam.containsKey(jam))
                mapJam.put(jam, ((Number) row[1]).intValue());
        }
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Transaksi");
        for (java.util.Map.Entry<String, Integer> entry : mapJam.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        jamChart.getData().clear();
        jamChart.getData().add(series);
        jamChart.setAnimated(true);
        animateFadeIn(jamChart, 300);
    }

    // ═══════════════════════════════════════════════════════
    // EXPORT EXCEL
    // ═══════════════════════════════════════════════════════
    @FXML
    private void onExport() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Simpan Laporan Excel");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            fileChooser.setInitialFileName("laporan-transaksi.xlsx");
            File file = fileChooser.showSaveDialog(TableLaporan.getScene().getWindow());
            if (file == null)
                return;
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Laporan Transaksi");
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            Row headerRow = sheet.createRow(0);
            String[] headers = { "No", "ID Transaksi", "Nama Lengkap", "Username",
                    "Total Pembayaran", "Uang Pembayaran", "Kembalian", "Kekurangan", "Status", "Tanggal" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
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
                row.createCell(9).setCellValue(item.tanggalTransaksi.toString());
            }
            for (int i = 0; i < headers.length; i++)
                sheet.autoSizeColumn(i);
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
            workbook.close();
            System.out.println("Export Excel berhasil!");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    // OTHER HANDLERS
    // ═══════════════════════════════════════════════════════
 

    private void applyRoundedClip(StackPane pane) {
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(pane.widthProperty());
        clip.heightProperty().bind(pane.heightProperty());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        pane.setClip(clip);
    }
}