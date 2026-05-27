package com.mycompany.projectuas;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PiutangController implements Initializable {

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
    private Label navLblPiutang;
    @FXML
    private Label navLblPengaturan;

    // ── FXML refs ──────────────────────────────────
    @FXML
    private TextField tfPelanggan;
    @FXML
    private TableView<DataHutang> tableHutang;
    @FXML
    private TableColumn<DataHutang, String> colNo;
    @FXML
    private TableColumn<DataHutang, String> colNama;
    @FXML
    private TableColumn<DataHutang, String> colKekurangan;
    @FXML
    private TableColumn<DataHutang, String> colPembayaran;
    @FXML
    private TableColumn<DataHutang, String> colTanggal_Transaksi;

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

    // ── Data ───────────────────────────────────────
    private static final NumberFormat FMT = NumberFormat.getInstance(
            new Locale("id", "ID"));

    // Model
    // static class BarangItem {
    // String no, nama;
    // long harga;
    // int qty;
    // BarangItem(String no, String nama, long harga, int qty) {
    // this.no = no;
    // this.nama = nama;
    // this.harga = harga;
    // this.qty = qty;
    // }
    // long subtotal() {
    // return harga * qty;
    // }
    // }
    // ---------prepare data hutang dari database----------------
    private final List<DataHutang> dataHutang = new ArrayList<>();

    static class DataHutang {
        int no;
        String idTransaksi;
        String namaPelanggan;
        long kekurangan;
        Long total_pembayaran;
        String status_pembayaran;
        String tanggal_transaksi;
        String nama_barang;
        long harga_barang;
        int qty;
        DataHutang(int no,
                String idTransaksi,
                String namaPelanggan,
                long kekurangan,
                Long total_pembayaran,
                String status_pembayaran,
                String tanggal_transaksi,
                String nama_barang,
                long harga_barang,
                int qty) {
            this.no = no;
            this.idTransaksi = idTransaksi;
            this.namaPelanggan = namaPelanggan;
            this.kekurangan = kekurangan;
            this.total_pembayaran = total_pembayaran;
            this.status_pembayaran = status_pembayaran;
            this.tanggal_transaksi = tanggal_transaksi;
            this.nama_barang = nama_barang;
            this.harga_barang = harga_barang;
            this.qty = qty;
        
            this.harga_barang = harga_barang;
            this.qty = qty;
        }

    }

    private void load_data_hutang(String namapelanggan) {
        String sql = "SELECT "
                + "t.id_transaksi, "
                + "t.pelanggan AS nama_pelanggan, "
                + "t.kekurangan, "
                + "0 AS total_pembayaran, "
                + "t.status_pembayaran, "
                + "t.tanggal_transaksi, "
                + "b.nama_barang, "
                + "b.harga AS harga_barang, "
                + "td.jumlah AS qty "
                + "FROM tb_transaksi t "
                + "JOIN tb_detail_transaksi td "
                + "ON t.id_transaksi = td.id_transaksi "
                + "JOIN tb_barang b "
                + "ON td.id_barang = b.id_barang "
                + "WHERE t.status_pembayaran = 'Belum Lunas' "
                + "AND t.pelanggan LIKE '%" + namapelanggan + "%'";

        int rowNo = 1;
        List<Object[]> results = koneksi.ambilData(sql);
        for (Object[] row : results) {

            String idTransaksi = (String) row[0];
            String namaPelanggan = (String) row[1];
            long kekurangan = (long) row[2];
            Long total_pembayaran = (Long) row[3];
            String status_pembayaran = (String) row[4];
            String tanggal_transaksi = (String) row[5];
            String nama_barang = (String) row[6];
            long harga_barang = (long) row[7];
            int qty = (int) row[8];

            dataHutang.add(new DataHutang(
                    rowNo++,
                    idTransaksi,
                    namaPelanggan,
                    kekurangan,
                    total_pembayaran,
                    status_pembayaran,
                    tanggal_transaksi,
                    nama_barang,
                    harga_barang,
                    qty));
        }
        tableHutang.getItems().setAll(dataHutang);
    }

    private Stage myStage;

    public void setStage(Stage stage) {
        this.myStage = stage;
    }

    // ── State ─────────────────────────────────────────────
    private boolean sidebarCollapsed = false;
    private static final double SIDEBAR_FULL = 220;
    private static final double SIDEBAR_MINI = 60;

    // ═════════════════════════════════════════════════════
    // INITIALIZE
    // ═════════════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupNavHover();
        setupTable();
        load_data_hutang(null);
    }

    // ═════════════════════════════════════════════════════
    // SIDEBAR TOGGLE (animasi smooth)
    // ═════════════════════════════════════════════════════
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
                navLblDashboard, navLblProduk, navLblKasir,
                navLblPelanggan, navLblLaporan, navLblPiutang, navLblPengaturan);
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
                navPelanggan, navLaporan, navPiutang, navPengaturan);
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
        navigation nav = new navigation();
        nav.navigateToLaporan();
    }

    @FXML
    private void onNavPiutang() {
        setActiveNav(navPiutang);
    }

    @FXML
    private void onNavPengaturan() {
        setActiveNav(navPengaturan);

    }

    private void setActiveNav(HBox selected) {
        List<HBox> all = List.of(
                navDashboard, navProduk, navKasir,
                navPelanggan, navLaporan, navPiutang, navPengaturan);
        for (HBox item : all) {
            item.getStyleClass().removeAll("nav-active");
            if (!item.getStyleClass().contains("nav-item")) {
                item.getStyleClass().add("nav-item");
            }
        }
        selected.getStyleClass().add("nav-active");
    }

    private void setupNavHover() {
        List<HBox> all = List.of(
                navDashboard, navProduk, navKasir,
                navPelanggan, navLaporan, navPiutang, navPengaturan);
        for (HBox item : all) {
            item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: #252840; -fx-background-radius: 10;"));
            item.setOnMouseExited(e -> item.setStyle(""));
        }
    }

    // ── Setup tabel ────────────────────────────────
    private void setupTable() {
        colNo.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(tableHutang.getItems().indexOf(cellData.getValue()) + 1)));
        colNama.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().namaPelanggan));
        colKekurangan.setCellValueFactory(data -> new SimpleStringProperty("Rp " + FMT.format(data.getValue().kekurangan)));
        colPembayaran.setCellValueFactory(data -> new SimpleStringProperty("Rp " + FMT.format(data.getValue().total_pembayaran)));
        colTanggal_Transaksi.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().tanggal_transaksi));
    }

    // ═══════════════════════════════════════════════
    // HANDLERS
    // ═══════════════════════════════════════════════
    @FXML
    private void onLunas() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi Lunas");
        confirm.setHeaderText(null);
        confirm.setContentText("Tandai transaksi ini sebagai LUNAS?");
        confirm.showAndWait().ifPresent(response -> {
            if (response.getButtonData().isDefaultButton()) {
                // TODO: update status di database
                System.out.println("Transaksi "
                        + lblIdTransaksi.getText() + " ditandai LUNAS");
                closeForm();
            }
        });
    }

    @FXML
    private void onBatal() {
        closeForm();
    }

    private void closeForm() {
        if (myStage != null) {
            myStage.setOnCloseRequest(null);
            myStage.close();
        } else {
            // fallback kalau myStage tidak di-set
            Stage stage = (Stage) btnBatal.getScene().getWindow();
            stage.setOnCloseRequest(null);
            stage.close();
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

    long kembalian;
    long tunai;

    public void updateSummary() {

        // Kembalian
        tunai = parseLong(tfTunai.getText().replaceAll("[^0-9]", ""));
        kembalian = tunai - data_transaksi.total;
        lblKembalian.setText(kembalian >= 0
                ? "Kembalian Rp " + FMT.format(kembalian)
                : "Kurang Rp " + FMT.format(Math.abs(kembalian)));
        lblKembalian.setStyle(kembalian >= 0
                ? "-fx-text-fill: #00E5A0;"
                : "-fx-text-fill: #FF5C7C;");
    }

    @FXML
    private void onQuick5() {
        tfTunai.setText("5000");
        updateSummary();
    }

    @FXML
    private void onQuick10() {
        tfTunai.setText("10000");
        updateSummary();
    }

    @FXML
    private void onQuick20() {
        tfTunai.setText("20000");
        updateSummary();
    }

    @FXML
    private void onQuick50() {
        tfTunai.setText("50000");
        updateSummary();
    }

    private boolean isUpdating = false;

    @FXML
    private void onTunaiChanged() {
        if (isUpdating) {
            return;
        }
        isUpdating = true;
        String raw = tfTunai.getText().replaceAll("[^0-9]", "");
        if (raw.isEmpty()) {
            tfTunai.setText("");
            isUpdating = false;
            updateSummary();
        }
        long value = Long.parseLong(raw);
        tfTunai.setText("Rp " + FMT.format(value));
        tfTunai.positionCaret(tfTunai.getText().length());
        isUpdating = false;
        updateSummary();
    }

    private long parseLong(String s) {
        try {
            return s == null || s.isBlank() ? 0 : Long.parseLong(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
