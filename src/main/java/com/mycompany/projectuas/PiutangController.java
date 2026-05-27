package com.mycompany.projectuas;

import java.net.URL;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

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
    @FXML private TextField  tfPelanggan;
    @FXML private TableView<BarangItem> tabelBarang;
    @FXML private TableColumn<BarangItem, String> colNo;
    @FXML private TableColumn<BarangItem, String> colNama;
    @FXML private TableColumn<BarangItem, String> colHarga;
    @FXML private TableColumn<BarangItem, String> colQty;
    @FXML private TableColumn<BarangItem, String> colSubtotal;

    @FXML private Label lblNamaPelanggan;
    @FXML private Label lblIdTransaksi;
    @FXML private Label lblJumlahHutang;
    @FXML private Button btnLunas;
    @FXML private Button btnBatal;

    
    // ── Data ───────────────────────────────────────
    private static final NumberFormat FMT = NumberFormat.getInstance(
            new Locale("id", "ID"));

    // Model
    static class BarangItem {
        String no, nama;
        long harga;
        int qty;

        BarangItem(String no, String nama, long harga, int qty) {
            this.no    = no;
            this.nama  = nama;
            this.harga = harga;
            this.qty   = qty;
        }

        long subtotal() { return harga * qty; }
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
        navigation nav = new  navigation();
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

     // ── Setup tabel ────────────────────────────────
    private void setupTable() {
        colNo.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().no));
        colNama.setCellValueFactory(d ->
            new SimpleStringProperty(d.getValue().nama));
        colHarga.setCellValueFactory(d ->
            new SimpleStringProperty("Rp " + FMT.format(d.getValue().harga)));
        colQty.setCellValueFactory(d ->
            new SimpleStringProperty(String.valueOf(d.getValue().qty)));
        colSubtotal.setCellValueFactory(d ->
            new SimpleStringProperty("Rp " + FMT.format(d.getValue().subtotal())));

        tabelBarang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    // ── Dummy data ─────────────────────────────────
    private void loadDummyData() {
        ObservableList<BarangItem> data = FXCollections.observableArrayList(
            new BarangItem("1", "Indomie Goreng",  3_500,  3),
            new BarangItem("2", "Aqua 600ml",      4_000,  2),
            new BarangItem("3", "Sabun Lifebuoy",  12_500, 1),
            new BarangItem("4", "Teh Sosro",       5_000,  4),
            new BarangItem("5", "Beng-Beng",       4_000,  2)
        );

        tabelBarang.setItems(data);

        // Hitung total hutang
        long total = data.stream().mapToLong(BarangItem::subtotal).sum();

        // Update info panel kanan
        lblNamaPelanggan.setText("Budi Santoso");
        lblIdTransaksi.setText("#TRX-0042");
        lblJumlahHutang.setText("Rp " + FMT.format(total));
    }

    // ═══════════════════════════════════════════════
    //  HANDLERS
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
}