package com.mycompany.projectuas;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

import com.mycompany.Model.TransaksiModel;
import com.mycompany.Model.TransaksiModel.CartItem;
import com.mycompany.Model.TransaksiModel.ItemPs;
import com.mycompany.Model.TransaksiModel.Produk;
import java.io.File;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransaksiController implements Initializable {

    // ═══════════════════════════════════════════════════════
    // FXML — TOPBAR
    // ═══════════════════════════════════════════════════════
    @FXML
    private Label tanggal;
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
    // FXML — AREA PRODUK
    // ═══════════════════════════════════════════════════════
    @FXML
    private FlowPane flowProduk;
    @FXML
    private TextField tfCari;
    @FXML
    private ComboBox<String> cbKategori;
    @FXML
    private Label lblJumlahProduk;
    @FXML
    private Button btnClearSearch;
    @FXML
    private ScrollPane scrolpane;

    // ═══════════════════════════════════════════════════════
    // FXML — SIDEBAR
    // ═══════════════════════════════════════════════════════
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
    private HBox navLogout;
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
    private Label navLblLogout;
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
    // FXML — KERANJANG & SUMMARY
    // ═══════════════════════════════════════════════════════
    @FXML
    private Button btnProduk;
    @FXML
    private Button btnPS;
    @FXML
    private VBox vboxKeranjang;
    @FXML
    private VBox emptyCart;
    @FXML
    private Label lblJumlahItem;
    @FXML
    private Label lblNamaKasir;
    @FXML
    private Label lblShift;
    @FXML
    private VBox totalBox;
    @FXML
    private Label lblSubtotal;
    @FXML
    private Label lblDiskon;
    @FXML
    private Label lblTotal;
    @FXML
    private TextField tfDiskon;
    @FXML
    private TextField tfTunai;
    @FXML
    private Label lblKembalian;
    @FXML
    private VBox tunaiBox;
    @FXML
    private Button btnQris;
    @FXML
    private Button btnTunai;
    @FXML
    private HBox QuickBox;
    @FXML
    private Button btnBayar;
    @FXML
    private Button btnQuick5;
    @FXML
    private Button btnQuick10;
    @FXML
    private Button btnQuick20;
    @FXML
    private Button btnQuick50;

    // ═══════════════════════════════════════════════════════
    // FXML — RENTAL PS
    // ═══════════════════════════════════════════════════════
    @FXML
    private VBox boxRentalPs;
    @FXML
    private Button btnPreset1, btnPreset2, btnPreset3, btnPreset4;
    @FXML
    private Label lblJam, lblMenit;
    @FXML
    private Label lblDurasiText, lblHargaTotal;
    @FXML
    private Button btnKonfirmasi;
    @FXML
    private Button btnJamMin, btnJamPlus;
    @FXML
    private Button btnMenMin, btnMenPlus;

    // ═══════════════════════════════════════════════════════
    // KONSTANTA & VARIABEL
    // ═══════════════════════════════════════════════════════
    private static final NumberFormat FMT = NumberFormat.getInstance(new Locale("id", "ID"));
    private static final double SIDEBAR_FULL = 220;
    private static final double SIDEBAR_MINI = 60;
    private static final long HARGA_30_MENIT = 3_000;
    private static final long HARGA_1_JAM = 5_000;

    private final List<Produk> semuaProduk = TransaksiModel.semuaProduk;

    private boolean sidebarCollapsed = false;
    private boolean pembayaraanQris = false;
    private boolean isUpdating = false;

    // State rental PS
    private int jamPs = 0;
    private int menitPs = 0;
    private int activePreset = -1;

    // State pembayaran
    long kembalian = 0;
    long tunai = 0;

    // ═══════════════════════════════════════════════════════
    // INITIALIZE
    // ═══════════════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inisialisasiForm();
        muatDataProduk();
        inisialisasiKategori();
        inisialisasiPencarianProduk();
        tampilkanSemuaProduk();
        setActiveNav(navKasir);
        perbaruiRingkasanBayar();
        inisialisasiMetodeBayar();
        inisialisasiMenuUtama();
        setupNavHover();
        setupLogoutHover();
    }

    // ═══════════════════════════════════════════════════════
    // INISIALISASI FORM
    // ═══════════════════════════════════════════════════════
    private void inisialisasiForm() {
        Notifikasi.updateBadge(notifBadge);

        Locale localeID = new Locale("id", "ID");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", localeID);
        tanggal.setText(LocalDate.now().format(formatter));

        session.applyFotoProfile(lblAvatartopbar, lblAvatarnavbar,
                imgAvatarGoogletopbar, imgAvatarGooglenavbar);

        navllblakun.setText(session.role);
        navlblnama.setText(session.nama);

        int jamSekarang = java.time.LocalTime.now().getHour();
        lblShift.setText((jamSekarang >= 6 && jamSekarang < 12) ? "Shift Siang" : "Shift Malam");

        TransaksiModel.keranjang.clear();
        TransaksiModel.pesananPs = null;
        TransaksiModel.semuaProduk.clear();

        btnBayar.setDisable(true);
        tfTunai.setDisable(true);
        lblKembalian.setText("Rp 0");
        btnQuick5.setDisable(true);
        btnQuick10.setDisable(true);
        btnQuick20.setDisable(true);
        btnQuick50.setDisable(true);
        boxRentalPs.setVisible(false);
        boxRentalPs.setManaged(false);
        lblNamaKasir.setText(session.nama);

        tfDiskon.setTextFormatter(new TextFormatter<>(change -> {
            String text = change.getControlNewText();
            return text.matches("\\d{0,3}") ? change : null;
        }));
    }

    private void inisialisasiKategori() {
        Set<String> cats = new LinkedHashSet<>();
        cats.add("Semua Kategori");
        semuaProduk.forEach(p -> cats.add(p.kategori));
        cbKategori.setItems(FXCollections.observableArrayList(cats));
        cbKategori.setValue("Semua Kategori");
        cbKategori.setOnAction(e -> filterProduk());
    }

    private void inisialisasiPencarianProduk() {
        tfCari.textProperty().addListener((obs, o, n) -> filterProduk());
    }

    private void inisialisasiMetodeBayar() {
        btnTunai.getStyleClass().add("pay-method-active");

        btnQris.setOnAction(e -> {
            TransaksiModel.metodeBayar = "QRIS";
            pembayaraanQris = true;
            btnQris.getStyleClass().add("pay-method-active");
            btnTunai.getStyleClass().remove("pay-method-active");
            tunaiBox.setVisible(false);
            tunaiBox.setManaged(false);
            QuickBox.setVisible(false);
            QuickBox.setManaged(false);
        });

        btnTunai.setOnAction(e -> {
            TransaksiModel.metodeBayar = "Tunai";
            pembayaraanQris = false;
            btnQris.getStyleClass().remove("pay-method-active");
            btnTunai.getStyleClass().add("pay-method-active");
            tunaiBox.setVisible(true);
            tunaiBox.setManaged(true);
            QuickBox.setVisible(true);
            QuickBox.setManaged(true);
        });
    }

    private void inisialisasiMenuUtama() {
        btnProduk.getStyleClass().add("btn-menu-active");

        btnProduk.setOnAction(e -> tampilkanMenuProduk());
        btnPS.setOnAction(e -> tampilkanMenuRentalPs());
    }

    // ═══════════════════════════════════════════════════════
    // SIDEBAR
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
            sembunyikanTeksSidebar();
            toggleBtn.setText("▶");
            logoRow.setAlignment(Pos.CENTER);
            logoRow.setPadding(new Insets(18, 0, 18, 0));
            userRow.setAlignment(Pos.CENTER);
            userRow.setPadding(new Insets(12, 0, 12, 0));
        } else {
            timeline.setOnFinished(e -> {
                tampilkanTeksSidebar();
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

    private void sembunyikanTeksSidebar() {
        logoBrand.setVisible(false);
        logoBrand.setManaged(false);
        userInfo.setVisible(false);
        userInfo.setManaged(false);
        aturVisibilitasLabelNav(false);
    }

    private void tampilkanTeksSidebar() {
        logoBrand.setVisible(true);
        logoBrand.setManaged(true);
        userInfo.setVisible(true);
        userInfo.setManaged(true);
        aturVisibilitasLabelNav(true);
    }

    private void aturVisibilitasLabelNav(boolean visible) {
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
                navKasir, navPiutang, navLaporan, navPengaturan);
        for (HBox item : items) {
            item.setAlignment(collapsed ? Pos.CENTER : Pos.CENTER_LEFT);
            item.setPadding(pad);
        }
        navLogout.setAlignment(collapsed ? Pos.CENTER : Pos.CENTER_LEFT);
        navLogout.setPadding(pad);
    }

    private void setActiveNav(HBox selected) {
        List<HBox> all = List.of(navDashboard, navProduk, navKasir, navLaporan, navPengaturan);
        for (HBox item : all) {
            item.getStyleClass().removeAll("nav-active");
            if (!item.getStyleClass().contains("nav-item"))
                item.getStyleClass().add("nav-item");
        }
        selected.getStyleClass().add("nav-active");
    }

    private void setupNavHover() {
        List<HBox> all = List.of(navDashboard, navProduk, navKasir, navLaporan, navPengaturan);
        for (HBox item : all) {
            item.setOnMouseEntered(e -> item.setStyle("-fx-background-color: #252840; -fx-background-radius: 10;"));
            item.setOnMouseExited(e -> item.setStyle(""));
        }
    }

    private void setupLogoutHover() {
        String styleNormal = "-fx-background-color: transparent; -fx-background-radius: 10; -fx-cursor: hand;";
        String styleHover = "-fx-background-color: #FF5C7C26; -fx-background-radius: 10; -fx-cursor: hand;";
        navLogout.setStyle(styleNormal);
        navLogout.setOnMouseEntered(e -> navLogout.setStyle(styleHover));
        navLogout.setOnMouseExited(e -> navLogout.setStyle(styleNormal));
    }

    // ═══════════════════════════════════════════════════════
    // NAV HANDLERS
    // ═══════════════════════════════════════════════════════
    @FXML
    private void onNavLogout() {
        new Popup().showConfirmPopup("Konfirmasi Logout", "Yakin ingin keluar dari aplikasi?", () -> {
            new navigation().navigateToLogin();
            ((Stage) navLogout.getScene().getWindow()).close();
        });
    }

    @FXML
    private void onNavLogoutHover() {
        navLogout.setStyle(
                "-fx-background-color: #FF5C7C26; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 10 14 10 0;");
    }

    @FXML
    private void onNavLogoutExit() {
        navLogout.setStyle(
                "-fx-background-color: transparent; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 10 14 10 0;");
    }

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

    // ═══════════════════════════════════════════════════════
    // MENU UTAMA (PRODUK / PS)
    // ═══════════════════════════════════════════════════════
    private void tampilkanMenuProduk() {
        btnProduk.getStyleClass().add("btn-menu-active");
        btnPS.getStyleClass().remove("btn-menu-active");
        boxRentalPs.setVisible(false);
        boxRentalPs.setManaged(false);
        scrolpane.setVisible(true);
        scrolpane.setManaged(true);
        cbKategori.setDisable(false);
    }

    private void tampilkanMenuRentalPs() {
        boxRentalPs.setVisible(true);
        boxRentalPs.setManaged(true);
        scrolpane.setVisible(false);
        scrolpane.setManaged(false);
        btnProduk.getStyleClass().remove("btn-menu-active");
        btnPS.getStyleClass().add("btn-menu-active");
        cbKategori.setDisable(true);
    }

    // ═══════════════════════════════════════════════════════
    // DATA PRODUK
    // ═══════════════════════════════════════════════════════
    private void muatDataProduk() {
        String sql = "SELECT * FROM tb_barang";
        List<Object[]> data = koneksi.ambilData(sql);
        for (Object[] row : data) {
            semuaProduk.add(new Produk(
                    (int) row[0],
                    (String) row[1],
                    (int) row[2],
                    (String) row[3],
                    (int) row[4],
                    (String) row[5],
                    (String) row[6]));
        }
    }

    private void tampilkanSemuaProduk() {
        renderDaftarProduk(semuaProduk);
    }

    private void filterProduk() {
        String query = tfCari.getText().toLowerCase().trim();
        String kat = cbKategori.getValue();

        StringBuilder sql = new StringBuilder("SELECT * FROM tb_barang WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (kat != null && !kat.equals("Semua Kategori")) {
            sql.append(" AND kategori = ?");
            params.add(kat);
        }
        if (!query.isEmpty()) {
            sql.append(" AND nama_barang LIKE ?");
            params.add("%" + query + "%");
        }

        List<Object[]> hasil = params.isEmpty()
                ? koneksi.ambilData(sql.toString())
                : koneksi.ambilData(sql.toString(), params.toArray());

        List<Produk> list = new ArrayList<>();
        for (Object[] row : hasil) {
            list.add(new Produk(
                    (int) row[0], (String) row[1], (int) row[2],
                    (String) row[3], (int) row[4], (String) row[5], (String) row[6]));
        }

        renderDaftarProduk(list);
    }

    @FXML
    private void onClearSearch() {
        tfCari.clear();
        cbKategori.setValue("Semua Kategori");
        renderDaftarProduk(semuaProduk);
    }

    // ═══════════════════════════════════════════════════════
    // RENDER DAFTAR PRODUK
    // ═══════════════════════════════════════════════════════
    public void renderDaftarProduk(List<Produk> list) {
        flowProduk.getChildren().clear();
        for (Produk p : list) {
            flowProduk.getChildren().add(buildKartuProduk(p));
        }
        lblJumlahProduk.setText(list.size() + " produk");
    }

    /** Membangun card UI untuk satu produk di area pilih produk */
    private VBox buildKartuProduk(Produk p) {
        boolean habis = p.stok == 0;
        boolean stokSedikit = p.stok > 0 && p.stok <= 5;

        // Gambar produk
        ImageView img = new ImageView();
        img.setFitWidth(170);
        img.setFitHeight(120);
        img.setPreserveRatio(false);
        img.setSmooth(true);
        img.imageProperty().addListener((obs, oldImg, newImg) -> {
            if (newImg == null)
                return;
            if (newImg.isBackgroundLoading()) {
                newImg.progressProperty().addListener((o, ov, nv) -> {
                    if (nv.doubleValue() >= 1.0)
                        terapkanCoverViewport(img, newImg, 170, 120);
                });
            } else {
                terapkanCoverViewport(img, newImg, 170, 120);
            }
        });
        muatGambarProduk(img, p.imageUrl);

        StackPane imgWrapper = new StackPane(img);
        imgWrapper.getStyleClass().add("produk-card-img-wrapper");
        imgWrapper.setPrefSize(170, 120);
        imgWrapper.setMinSize(170, 120);
        imgWrapper.setMaxSize(170, 120);
        terapkanKlipBulat(imgWrapper);

        // Label nama
        Label lblNama = new Label(p.nama);
        lblNama.getStyleClass().add("produk-card-nama");
        lblNama.setAlignment(Pos.CENTER);
        lblNama.setMaxWidth(Double.MAX_VALUE);
        lblNama.setWrapText(false);

        // Label deskripsi + tooltip
        Label lblDesc = new Label(p.description);
        lblDesc.getStyleClass().add("produk-card-desc");
        lblDesc.setAlignment(Pos.CENTER);
        lblDesc.setMaxWidth(Double.MAX_VALUE);
        lblDesc.setWrapText(true);
        lblDesc.setMinHeight(32);
        lblDesc.setMaxHeight(32);
        lblDesc.setPrefHeight(32);
        if (p.description != null && !p.description.isBlank()) {
            Tooltip tooltip = new Tooltip(p.description);
            tooltip.setWrapText(true);
            tooltip.setMaxWidth(200);
            tooltip.getStyleClass().add("produk-card-tooltip");
            tooltip.setShowDelay(Duration.millis(300));
            Tooltip.install(lblDesc, tooltip);
        }

        // Label harga
        Label lblHarga = new Label("Rp " + FMT.format(p.harga));
        lblHarga.getStyleClass().add("produk-card-harga");
        lblHarga.setAlignment(Pos.CENTER);
        lblHarga.setMaxWidth(Double.MAX_VALUE);

        // Label / badge stok
        Node stokNode = buildBadgeStok(p, habis, stokSedikit);

        // Tombol tambah
        Button btnTambah = new Button("+ Tambah");
        btnTambah.getStyleClass().add("btn-tambah-card");
        btnTambah.setDisable(habis);
        btnTambah.setMaxWidth(Double.MAX_VALUE);
        btnTambah.setOnAction(e -> tambahKeKeranjang(p));

        // Susun body card
        VBox body = new VBox(4, lblNama, lblDesc, lblHarga, stokNode, btnTambah);
        body.setAlignment(Pos.CENTER);
        body.setPadding(new Insets(10, 12, 14, 12));

        VBox card = new VBox(0, imgWrapper, body);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(170);
        card.getStyleClass().add(habis ? "produk-card produk-card-habis" : "produk-card");
        if (!habis)
            card.setOnMouseClicked(e -> tambahKeKeranjang(p));

        return card;
    }

    /** Membangun node badge / label stok produk */
    private Node buildBadgeStok(Produk p, boolean habis, boolean stokSedikit) {
        if (habis) {
            Label badge = new Label("Stok habis");
            badge.getStyleClass().add("produk-card-badge-habis");
            HBox wrapper = new HBox(badge);
            wrapper.setAlignment(Pos.CENTER);
            wrapper.setMinHeight(24);
            return wrapper;
        }
        Label lblStok = new Label("Stok: " + p.stok);
        lblStok.setAlignment(Pos.CENTER);
        lblStok.setMaxWidth(Double.MAX_VALUE);
        lblStok.setMinHeight(24);
        lblStok.getStyleClass().add(stokSedikit ? "produk-card-stok-warn" : "produk-card-stok");
        return lblStok;
    }

    /**
     * Memuat gambar produk ke ImageView, fallback ke not_found.png jika tidak ada
     */
    private void muatGambarProduk(ImageView img, String imageUrl) {
        try {
            if (imageUrl == null || imageUrl.isBlank()) {
                muatGambarDefault(img);
                return;
            }
            String appData = System.getenv("APPDATA");
            File imgFile = (appData != null && !appData.isEmpty())
                    ? new File(appData + "\\ProjectUAS\\image-barang\\" + imageUrl)
                    : new File(System.getProperty("user.home") + "/ProjectUAS/image-barang/" + imageUrl);

            if (imgFile.exists()) {
                img.setImage(new Image(imgFile.toURI().toString()));
            } else {
                var url = getClass().getResource("/image-barang/" + imageUrl);
                if (url != null) {
                    img.setImage(new Image(url.toExternalForm()));
                } else {
                    muatGambarDefault(img);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void muatGambarDefault(ImageView img) {
        var url = getClass().getResource("/image/not_found.png");
        if (url != null)
            img.setImage(new Image(url.toExternalForm()));
    }

    private void terapkanCoverViewport(ImageView imgView, Image image, double targetW, double targetH) {
        double imgW = image.getWidth();
        double imgH = image.getHeight();
        if (imgW <= 0 || imgH <= 0)
            return;
        double scale = Math.max(targetW / imgW, targetH / imgH);
        double cropW = targetW / scale;
        double cropH = targetH / scale;
        imgView.setViewport(new Rectangle2D((imgW - cropW) / 2, (imgH - cropH) / 2, cropW, cropH));
    }

    private void terapkanKlipBulat(StackPane pane) {
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(pane.widthProperty());
        clip.heightProperty().bind(pane.heightProperty());
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        pane.setClip(clip);
    }

    // ═══════════════════════════════════════════════════════
    // KERANJANG BELANJA
    // ═══════════════════════════════════════════════════════
    private void tambahKeKeranjang(Produk p) {
        if (TransaksiModel.keranjang.containsKey(p.id)) {
            CartItem ci = TransaksiModel.keranjang.get(p.id);
            if (ci.qty < p.stok)
                ci.qty++;
        } else {
            TransaksiModel.keranjang.put(p.id, new CartItem(p));
        }
        renderKeranjang();
        perbaruiRingkasanBayar();
    }

    public void renderKeranjang() {
        vboxKeranjang.getChildren().clear();

        boolean kosong = TransaksiModel.keranjang.isEmpty()
                && TransaksiModel.pesananPs == null;

        emptyCart.setVisible(kosong);
        emptyCart.setManaged(kosong);

        int totalItem = 0;

        for (CartItem ci : TransaksiModel.keranjang.values()) {
            totalItem += ci.qty;
            vboxKeranjang.getChildren().add(buildItemKeranjangProduk(ci));
        }

        if (TransaksiModel.pesananPs != null) {
            totalItem++;
            vboxKeranjang.getChildren().add(buildItemKeranjangPs(TransaksiModel.pesananPs));
        }

        lblJumlahItem.setText(totalItem + " item");

        btnBayar.setDisable(kosong);
        tfTunai.setDisable(kosong);
        btnQuick5.setDisable(kosong);
        btnQuick10.setDisable(kosong);
        btnQuick20.setDisable(kosong);
        btnQuick50.setDisable(kosong);

        tfTunai.setText("0");
        tfDiskon.setText("0");
    }

    /** Membangun tampilan satu item produk di keranjang belanja */
    private HBox buildItemKeranjangProduk(CartItem ci) {

        // ── Kolom Kiri: Info & Kontrol ─────────────────
        Label lblNama = new Label(ci.produk.nama);
        lblNama.getStyleClass().add("cart-item-nama");

        Label lblHargaSatuan = new Label("@ Rp " + FMT.format(ci.produk.harga));
        lblHargaSatuan.getStyleClass().add("cart-item-harga");

        Button btnKurang = new Button("−");
        btnKurang.getStyleClass().add("btn-qty");
        btnKurang.setOnAction(e -> {
            if (ci.qty > 1) {
                ci.qty--;
            } else {
                TransaksiModel.keranjang.remove(ci.produk.id);
            }
            renderKeranjang();
            perbaruiRingkasanBayar();
        });

        Label lblQty = new Label(String.valueOf(ci.qty));
        lblQty.getStyleClass().add("lbl-qty");

        Button btnTambah = new Button("+");
        btnTambah.getStyleClass().add("btn-qty");
        btnTambah.setDisable(ci.qty >= ci.produk.stok);
        btnTambah.setOnAction(e -> {
            if (ci.qty < ci.produk.stok)
                ci.qty++;
            renderKeranjang();
            perbaruiRingkasanBayar();
        });

        HBox qtyBox = new HBox(4, btnKurang, lblQty, btnTambah);
        qtyBox.setAlignment(Pos.CENTER_LEFT);

        Label lblSubtotalItem = new Label("Rp " + FMT.format(ci.subtotal()));
        lblSubtotalItem.getStyleClass().add("cart-item-subtotal");

        VBox kiriBox = new VBox(4, lblNama, lblHargaSatuan, qtyBox, lblSubtotalItem);
        kiriBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(kiriBox, Priority.ALWAYS);

        // ── Kolom Tengah: Gambar Produk ────────────────
        ImageView img = new ImageView();
        img.setFitWidth(70);
        img.setFitHeight(70);
        img.setPreserveRatio(false);
        img.setSmooth(true);
        img.imageProperty().addListener((obs, oldImg, newImg) -> {
            if (newImg == null)
                return;
            if (newImg.isBackgroundLoading()) {
                newImg.progressProperty().addListener((o, ov, nv) -> {
                    if (nv.doubleValue() >= 1.0)
                        terapkanCoverViewport(img, newImg, 70, 70);
                });
            } else {
                terapkanCoverViewport(img, newImg, 70, 70);
            }
        });
        muatGambarProduk(img, ci.produk.imageUrl);

        StackPane imgWrapper = new StackPane(img);
        imgWrapper.setPrefSize(70, 70);
        imgWrapper.setMinSize(70, 70);
        imgWrapper.setMaxSize(70, 70);
        terapkanKlipBulat(imgWrapper);

        // ── Kolom Kanan: Tombol Hapus ──────────────────
        Button btnHapus = new Button("✕");
        btnHapus.getStyleClass().add("btn-hapus-item");
        btnHapus.setOnAction(e -> {
            TransaksiModel.keranjang.remove(ci.produk.id);
            renderKeranjang();
            perbaruiRingkasanBayar();
        });

        VBox kananBox = new VBox(btnHapus);
        kananBox.setAlignment(Pos.TOP_CENTER);

        // ── Gabungkan 3 Kolom ─────────────────────────
        HBox item = new HBox(8, kiriBox, imgWrapper, kananBox);
        item.setAlignment(Pos.CENTER_LEFT);
        item.getStyleClass().add("cart-item");
        return item;
    }

    /** Membangun tampilan item rental PS di keranjang belanja */
    private HBox buildItemKeranjangPs(ItemPs itemPs) {
        Label lblNama = new Label("Play Station");
        lblNama.getStyleClass().add("cart-item-nama");

        Label lblHargaSatuan = new Label("1 Jam Rp 5.000 / 30 Menit Rp 3.000");
        lblHargaSatuan.getStyleClass().add("cart-item-harga");

        VBox infoBox = new VBox(2, lblNama, lblHargaSatuan);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        // Tombol hapus — pojok kanan atas
        Button btnHapus = new Button("✕");
        btnHapus.getStyleClass().add("btn-hapus-item");
        btnHapus.setOnAction(e -> {
            TransaksiModel.pesananPs = null;
            renderKeranjang();
            perbaruiRingkasanBayar();
        });

        HBox topRow = new HBox(infoBox, btnHapus);
        topRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // Kontrol durasi
        Button btnKurangDurasi = new Button("−");
        btnKurangDurasi.getStyleClass().add("btn-qty");
        btnKurangDurasi.setOnAction(e -> {
            int menitSekarang = (itemPs.durasiJam * 60) + itemPs.durasiMenit;
            if (menitSekarang > 30) {
                menitSekarang -= 30;
                itemPs.durasiJam = menitSekarang / 60;
                itemPs.durasiMenit = menitSekarang % 60;
                itemPs.harga = hitungHargaPs(menitSekarang);
                renderKeranjang();
                perbaruiRingkasanBayar();
            }
        });

        Label lblDurasi = new Label(formatTeksDurasi(itemPs.durasiJam, itemPs.durasiMenit));
        lblDurasi.getStyleClass().add("lbl-qty");

        Button btnTambahDurasi = new Button("+");
        btnTambahDurasi.getStyleClass().add("btn-qty");
        btnTambahDurasi.setOnAction(e -> {
            int menitSekarang = (itemPs.durasiJam * 60) + itemPs.durasiMenit + 30;
            itemPs.durasiJam = menitSekarang / 60;
            itemPs.durasiMenit = menitSekarang % 60;
            itemPs.harga = hitungHargaPs(menitSekarang);
            renderKeranjang();
            perbaruiRingkasanBayar();
        });

        HBox durasiBox = new HBox(4, btnKurangDurasi, lblDurasi, btnTambahDurasi);
        durasiBox.setAlignment(Pos.CENTER_LEFT);

        // Subtotal — paling bawah
        Label lblSubtotalItem = new Label("Rp " + FMT.format(itemPs.harga));
        lblSubtotalItem.getStyleClass().add("cart-item-subtotal");

        VBox content = new VBox(4, topRow, durasiBox, lblSubtotalItem);
        content.setAlignment(Pos.CENTER_LEFT);

        HBox item = new HBox(content);
        HBox.setHgrow(content, Priority.ALWAYS);
        item.getStyleClass().add("cart-item");
        return item;
    }

    // ═══════════════════════════════════════════════════════
    // RINGKASAN PEMBAYARAN
    // ═══════════════════════════════════════════════════════
    public void perbaruiRingkasanBayar() {
        long subtotalBarang = TransaksiModel.keranjang.values()
                .stream().mapToLong(CartItem::subtotal).sum();

        long subtotalPs = (TransaksiModel.pesananPs != null)
                ? TransaksiModel.pesananPs.harga
                : 0;

        TransaksiModel.subtotal = subtotalBarang + subtotalPs;

        double diskonPct = parseDouble(tfDiskon.getText().replaceAll("[^0-9]", ""));
        long diskon = (long) (TransaksiModel.subtotal * diskonPct / 100.0);
        TransaksiModel.total = TransaksiModel.subtotal - diskon;

        lblSubtotal.setText("Rp " + FMT.format(TransaksiModel.subtotal));
        lblDiskon.setText("- Rp " + FMT.format(diskon));
        lblTotal.setText("Rp " + FMT.format(TransaksiModel.total));

        tunai = parseLong(tfTunai.getText().replaceAll("[^0-9]", ""));
        kembalian = tunai - TransaksiModel.total;

        lblKembalian.setText(kembalian >= 0
                ? "Kembalian Rp " + FMT.format(kembalian)
                : "Kurang Rp " + FMT.format(Math.abs(kembalian)));
        lblKembalian.setStyle(kembalian >= 0
                ? "-fx-text-fill: #00E5A0;"
                : "-fx-text-fill: #FF5C7C;");
    }

    // ═══════════════════════════════════════════════════════
    // HANDLERS PEMBAYARAN
    // ═══════════════════════════════════════════════════════
    @FXML
    private void onDiskonChanged() {
        perbaruiRingkasanBayar();
    }

    @FXML
    private void onTunaiChanged() {
        if (isUpdating)
            return;
        isUpdating = true;
        String raw = tfTunai.getText().replaceAll("[^0-9]", "");
        if (raw.isEmpty()) {
            tfTunai.setText("");
            isUpdating = false;
            perbaruiRingkasanBayar();
            return;
        }
        long value = Long.parseLong(raw);
        tfTunai.setText("Rp " + FMT.format(value));
        tfTunai.positionCaret(tfTunai.getText().length());
        isUpdating = false;
        perbaruiRingkasanBayar();
    }

    @FXML
    private void onKosongkanKeranjang() {
        TransaksiModel.keranjang.clear();
        TransaksiModel.pesananPs = null;
        renderKeranjang();
        perbaruiRingkasanBayar();
    }

    @FXML
    private void onQuick5() {
        tfTunai.setText("5.000");
        perbaruiRingkasanBayar();
    }

    @FXML
    private void onQuick10() {
        tfTunai.setText("10.000");
        perbaruiRingkasanBayar();
    }

    @FXML
    private void onQuick20() {
        tfTunai.setText("20.000");
        perbaruiRingkasanBayar();
    }

    @FXML
    private void onQuick50() {
        tfTunai.setText("50.000");
        perbaruiRingkasanBayar();
    }

    @FXML
    private void onProsesBayar() {
        Stage ownerStage = (Stage) btnBayar.getScene().getWindow();

        if (TransaksiModel.keranjang.isEmpty() && TransaksiModel.pesananPs == null)
            return;

        if ("Admin".equals(session.role)) {
            new Popup().showConfirmPopup("OTORITAS",
                    "Proses Pembayaran Tidak Dapat Dilakukan Karena Role Tidak Sesuai, Silahkan Login Dengan Akun Karyawan",
                    () -> {
                    });
            return;
        }

        if (pembayaraanQris) {
            simpanTransaksi(TransaksiModel.total, TransaksiModel.total, 0, 0, "Lunas");
        } else {
            if (tfTunai.getText() == null || tfTunai.getText().isBlank() || tunai < 1000) {
                new Popup().showModernPopup("WARNING", "Uang Pembayaran Minimal Rp.1000",
                        Popup.PopupType.WARNING, ownerStage);
                return;
            }
            if (kembalian >= 0) {
                simpanTransaksi(tunai, kembalian, 0, 0, "Lunas");
            } else {
                simpanTransaksi(tunai, 0, Math.abs(kembalian), 0, "Belum Lunas");
            }
        }

        perbaruiStokProduk();
        simpanPaketPsJikaAda();

        semuaProduk.clear();
        muatDataProduk();
        onClearSearch();

        tfTunai.clear();
        new navigation().detailTransaksi((Stage) btnBayar.getScene().getWindow(), this);
    }

    private void simpanTransaksi(long uangBayar, long kembalianVal, long kekurangan, long dummy, String status) {
        String sql = "INSERT INTO tb_transaksi "
                + "(id_karyawan, total_pembayaran, uang_pembayaran, kembalian, kekurangan, status_pembayaran, tanggal_transaksi, pelanggan) "
                + "VALUES (?, ?, ?, ?, ?, ?, DATETIME('now','localtime'), ?)";
        koneksi.eksekusiQuery(sql, session.id, TransaksiModel.total, uangBayar, kembalianVal, kekurangan, status, "");
    }

    private void perbaruiStokProduk() {
        for (CartItem item : TransaksiModel.keranjang.values()) {
            koneksi.eksekusiQuery("UPDATE tb_barang SET stok = stok - ? WHERE id_barang = ?",
                    item.qty, item.produk.id);
        }
    }

    private void simpanPaketPsJikaAda() {
        List<Object[]> data = koneksi.ambilData(
                "SELECT id_transaksi FROM tb_transaksi ORDER BY id_transaksi DESC LIMIT 1");
        if (!data.isEmpty() && TransaksiModel.pesananPs != null) {
            int idTransaksi = ((Number) data.get(0)[0]).intValue();
            int totalMenit = (TransaksiModel.pesananPs.durasiJam * 60) + TransaksiModel.pesananPs.durasiMenit;
            koneksi.eksekusiQuery(
                    "INSERT INTO tb_paket_ps (id_transaksi, durasi, harga) VALUES (?, ?, ?)",
                    idTransaksi, totalMenit, TransaksiModel.pesananPs.harga);
        }
    }

    // ═══════════════════════════════════════════════════════
    // RENTAL PS — PRESET & SPINNER
    // ═══════════════════════════════════════════════════════
    @FXML
    private void onPreset1() {
        terapkanPresetPs(0, 30, 1);
    }

    @FXML
    private void onPreset2() {
        terapkanPresetPs(1, 0, 2);
    }

    @FXML
    private void onPreset3() {
        terapkanPresetPs(1, 30, 3);
    }

    @FXML
    private void onPreset4() {
        terapkanPresetPs(2, 0, 4);
    }

    private void terapkanPresetPs(int j, int m, int presetNo) {
        jamPs = j;
        menitPs = m;
        activePreset = presetNo;
        lblJam.setText(String.valueOf(jamPs));
        lblMenit.setText(String.valueOf(menitPs));
        resetGayaPreset();
        new Button[] { btnPreset1, btnPreset2, btnPreset3, btnPreset4 }[presetNo - 1]
                .getStyleClass().add("preset-btn-active");
        perbaruiTampilanRentalPs();
    }

    private void resetGayaPreset() {
        for (Button b : new Button[] { btnPreset1, btnPreset2, btnPreset3, btnPreset4 }) {
            b.getStyleClass().setAll("preset-btn");
        }
    }

    @FXML
    private void onJamPlus() {
        jamPs++;
        activePreset = -1;
        resetGayaPreset();
        lblJam.setText(String.valueOf(jamPs));
        perbaruiTampilanRentalPs();
    }

    @FXML
    private void onJamMin() {
        if (jamPs > 0) {
            jamPs--;
            activePreset = -1;
            resetGayaPreset();
            lblJam.setText(String.valueOf(jamPs));
            perbaruiTampilanRentalPs();
        }
    }

    @FXML
    private void onMenitPlus() {
        menitPs = (menitPs == 0) ? 30 : 0;
        if (menitPs == 0)
            jamPs++;
        activePreset = -1;
        resetGayaPreset();
        lblJam.setText(String.valueOf(jamPs));
        lblMenit.setText(String.valueOf(menitPs));
        perbaruiTampilanRentalPs();
    }

    @FXML
    private void onMenitMin() {
        if (menitPs == 30) {
            menitPs = 0;
        } else if (jamPs > 0) {
            jamPs--;
            menitPs = 30;
        }
        activePreset = -1;
        resetGayaPreset();
        lblJam.setText(String.valueOf(jamPs));
        lblMenit.setText(String.valueOf(menitPs));
        perbaruiTampilanRentalPs();
    }

    private void perbaruiTampilanRentalPs() {
        int totalMenit = (jamPs * 60) + menitPs;
        long harga = hitungHargaPs(totalMenit);
        lblDurasiText.setText(totalMenit == 0 ? "-" : formatTeksDurasi(jamPs, menitPs));
        lblHargaTotal.setText("Rp " + FMT.format(harga));
        btnKonfirmasi.setDisable(totalMenit == 0);
    }

    private long hitungHargaPs(int totalMenit) {
        if (totalMenit == 0)
            return 0;
        if (totalMenit == 30)
            return HARGA_30_MENIT;
        long jamPenuh = totalMenit / 60;
        int sisaMenit = totalMenit % 60;
        return (jamPenuh * HARGA_1_JAM) + (sisaMenit > 0 ? HARGA_30_MENIT : 0);
    }

    private String formatTeksDurasi(int j, int m) {
        if (j == 0 && m == 0)
            return "-";
        if (j == 0)
            return m + " Menit";
        if (m == 0)
            return j + " Jam";
        return j + " Jam " + m + " Menit";
    }

    @FXML
    private void onKonfirmasi() {
        int totalMenit = (jamPs * 60) + menitPs;
        TransaksiModel.pesananPs = new ItemPs(-1, jamPs, menitPs, hitungHargaPs(totalMenit));
        renderKeranjang();
        perbaruiRingkasanBayar();
    }

    @FXML
    private void onBatal() {
        tampilkanMenuProduk();
    }

    // ═══════════════════════════════════════════════════════
    // NOTIFIKASI
    // ═══════════════════════════════════════════════════════
    @FXML
    private void onNotif() {
        Notifikasi.show((Stage) notifBadge.getScene().getWindow());
    }

    // ═══════════════════════════════════════════════════════
    // HELPER
    // ═══════════════════════════════════════════════════════
    private double parseDouble(String s) {
        try {
            return (s == null || s.isBlank()) ? 0 : Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private long parseLong(String s) {
        try {
            return (s == null || s.isBlank()) ? 0 : Long.parseLong(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void setTransaksiController(TransaksiController tc) {
        /* reserved */ }
}