/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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

/**
 * FXML Controller class
 *
 * @author zakki mubarroq
 */
public class TransaksiController implements Initializable {

    // ── FXML refs ────────────────────────────────────────
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

    //==========================
    @FXML
    private Button btnProduk;

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
    private Button btnPS;
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
    @FXML
    private ScrollPane scrolpane;

    // rental ps

    // pesana paket ps
    // ── FXML refs ──────────────────────────────────
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

    // ── Variables ──────────────────────────────────────────────

    // ═════════════════════════════════════════════════════
    // INITIALIZE
    // ═════════════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupForm();
        loadproduk();
        setupKategori();
        setupSearch();
        renderProduk(semuaProduk);
        setActiveNav(navKasir);
        updateSummary();
        setupMetodeBayar();
        setupMenu();

    }

    private boolean sidebarCollapsed = false;
    private static final double SIDEBAR_FULL = 220;
    private static final double SIDEBAR_MINI = 60;

    private final java.util.List<Produk> semuaProduk = TransaksiModel.semuaProduk;
    private static final NumberFormat FMT = NumberFormat.getInstance(new Locale("id", "ID"));

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
                navLblDashboard, navLblProduk, navLblKaryawan, navLblKasir, navLblPiutang,
                navLblLaporan, navLblPengaturan);
        for (Label lbl : labels) {
            lbl.setVisible(visible);
            lbl.setManaged(visible);
        }
    }

    private void updateNavPadding(boolean collapsed) {
        Insets pad = collapsed ? new Insets(10, 0, 10, 0) : new Insets(10, 14, 10, 0);
        List<HBox> items = List.of(navDashboard, navProduk, navKaryawan, navKasir, navPiutang, navLaporan,
                navPengaturan);
        for (HBox item : items) {
            item.setAlignment(collapsed ? Pos.CENTER : Pos.CENTER_LEFT);
            item.setPadding(pad);
        }
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

    // ──data ────────────────────────────────────────
    private void loadproduk() {
        String sql = "SELECT * FROM tb_barang";
        List<Object[]> data = koneksi.ambilData(sql);
        for (Object[] row : data) {
            int id = (int) row[0];
            String nama = (String) row[1];
            int harga = (int) row[2];
            String kategori = (String) row[3];
            int stok = (int) row[4];
            String description = (String) row[5];
            String imageUrl = (String) row[6];

            semuaProduk.add(new Produk(id, nama, harga, kategori, stok, description, imageUrl));
        }
    }

    private void setupKategori() {
        Set<String> cats = new LinkedHashSet<>();
        cats.add("Semua Kategori");
        semuaProduk.forEach(p -> cats.add(p.kategori));
        cbKategori.setItems(FXCollections.observableArrayList(cats));
        cbKategori.setValue("Semua Kategori");
        cbKategori.setOnAction(e -> filterProduk());
    }

    private void setupSearch() {
        tfCari.textProperty().addListener((obs, o, n) -> filterProduk());
    }

    // setupa form
    private void setupForm() {
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

    }

    // ═════════════════════════════════════════════════════
    // RENDER PRODUK CARDS
    // ═════════════════════════════════════════════════════
    public void renderProduk(List<Produk> list) {
        flowProduk.getChildren().clear();
        for (Produk p : list) {
            flowProduk.getChildren().add(buildProdukCard(p));
        }
        lblJumlahProduk.setText(list.size() + " produk");
    }

    private VBox buildProdukCard(Produk p) {
        boolean habis = p.stok == 0;

        // ── ImageView ──────────────────────────────────
        ImageView img = new ImageView();
        img.setFitWidth(120);
        img.setFitHeight(90);
        img.setPreserveRatio(true); // ← tetap true, jaga rasio
        img.setSmooth(true);

        // Crop tengah — potong bagian yang keluar
        Rectangle clip = new Rectangle(120, 90);
        clip.setArcWidth(10); // rounded clip juga
        clip.setArcHeight(10);
        img.setClip(clip);

        try {
            String imageName = p.imageUrl;

            if (imageName == null || imageName.isBlank()) {
                var url = getClass().getResource("/image/not_found.png");
                if (url != null)
                    img.setImage(new Image(url.toExternalForm()));
            } else {
                // Coba baca dari AppData dulu
                String appData = System.getenv("APPDATA");
                File imgFile = (appData != null && !appData.isEmpty())
                        ? new File(appData + "\\ProjectUAS\\image-barang\\" + imageName)
                        : new File(System.getProperty("user.home") + "/ProjectUAS/image-barang/" + imageName);

                if (imgFile.exists()) {
                    img.setImage(new Image(imgFile.toURI().toString()));
                } else {
                    // Fallback: baca dari dalam JAR
                    var url = getClass().getResource("/image-barang/" + imageName);
                    if (url != null) {
                        img.setImage(new Image(url.toExternalForm()));
                    } else {
                        var notFound = getClass().getResource("/image/not_found.png");
                        if (notFound != null)
                            img.setImage(new Image(notFound.toExternalForm()));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        StackPane imgWrapper = new StackPane(img);
        imgWrapper.getStyleClass().add("produk-card-img-wrapper");
        imgWrapper.setPrefSize(120, 90);
        imgWrapper.setMinSize(120, 90);
        imgWrapper.setMaxSize(120, 90);

        // ── Nama ───────────────────────────────────────
        Label nama = new Label(p.nama);
        nama.getStyleClass().add("produk-card-nama");
        nama.setMaxWidth(130);
        nama.setWrapText(true);

        // ── Deskripsi ──────────────────────────────────
        Label desc = new Label(p.description);
        desc.getStyleClass().add("produk-card-desc");
        desc.setMaxWidth(130);
        desc.setWrapText(true);

        // ── Harga ──────────────────────────────────────
        Label harga = new Label("Rp " + FMT.format(p.harga));
        harga.getStyleClass().add("produk-card-harga");

        // ── Stok ───────────────────────────────────────
        Label stok = new Label(habis ? "Stok habis" : "Stok: " + p.stok);
        stok.getStyleClass().add("produk-card-stok");

        // ── Tombol tambah ──────────────────────────────
        Button btnTambah = new Button("+ Tambah");
        btnTambah.getStyleClass().add("btn-tambah-card");
        btnTambah.setDisable(habis);
        btnTambah.setMaxWidth(Double.MAX_VALUE);
        btnTambah.setOnAction(e -> tambahKeKeranjang(p));

        // ── Rakit card — imgWrapper & desc sudah masuk ─
        VBox card = new VBox(6, imgWrapper, nama, desc, harga, stok, btnTambah);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(12));
        card.setPrefWidth(155);
        card.setPrefHeight(240); // lebih tinggi karena ada gambar + deskripsi
        card.getStyleClass().add(habis ? "produk-card produk-card-habis" : "produk-card");

        // ── Klik card = tambah ke keranjang ────────────
        if (!habis) {
            card.setOnMouseClicked(e -> tambahKeKeranjang(p));
        }

        return card;
    }

    // ═════════════════════════════════════════════════════
    // FILTER
    // ═════════════════════════════════════════════════════
    private void filterProduk() {
        String query = tfCari.getText().toLowerCase().trim();
        String kat = cbKategori.getValue();

        // ── Bangun SQL dinamis ────────────────────────
        StringBuilder sql = new StringBuilder("SELECT * FROM tb_barang WHERE 1=1");
        java.util.List<Object> params = new java.util.ArrayList<>();

        // tambah filter kategori hanya jika bukan "Semua Kategori"
        if (kat != null && !kat.equals("Semua Kategori")) {
            sql.append(" AND kategori = ?");
            params.add(kat);
        }

        // tambah filter nama hanya jika ada ketikan
        if (!query.isEmpty()) {
            sql.append(" AND nama_barang LIKE ?");
            params.add("%" + query + "%");
        }

        // ── Ambil data ────────────────────────────────
        List<Object[]> hasil = params.isEmpty() ? koneksi.ambilData(sql.toString())
                : koneksi.ambilData(sql.toString(), params.toArray());
        List<Produk> list = new ArrayList<>();

        for (Object[] row : hasil) {
            int id = (int) row[0];
            String nama = (String) row[1];
            int harga = (int) row[2];
            String kategori = (String) row[3];
            int stok = (int) row[4];
            String description = (String) row[5];
            String imageUrl = (String) row[6];

            list.add(new Produk(id, nama, harga, kategori, stok, description, imageUrl));
        }

        renderProduk(list);
    }

    @FXML
    private void onClearSearch() {
        tfCari.clear();
        cbKategori.setValue("Semua Kategori");
        renderProduk(semuaProduk);
    }

    // ═════════════════════════════════════════════════════
    // KERANJANG
    // ═════════════════════════════════════════════════════
    private void tambahKeKeranjang(Produk p) {
        if (TransaksiModel.keranjang.containsKey(p.id)) {
            CartItem ci = TransaksiModel.keranjang.get(p.id);
            if (ci.qty < p.stok) {
                ci.qty++;
            }
        } else {
            TransaksiModel.keranjang.put(p.id, new CartItem(p));
        }
        renderKeranjang();
        updateSummary();

    }

    public void renderKeranjang() {
        vboxKeranjang.getChildren().clear();

        boolean kosong = TransaksiModel.keranjang.isEmpty()
                && TransaksiModel.pesananPs == null;

        emptyCart.setVisible(kosong);
        emptyCart.setManaged(kosong);

        int totalItem = 0;

        // Barang
        for (CartItem ci : TransaksiModel.keranjang.values()) {
            totalItem += ci.qty;
            vboxKeranjang.getChildren().add(buildCartItem(ci));
        }

        // Rental PS
        if (TransaksiModel.pesananPs != null) {
            totalItem++;
            vboxKeranjang.getChildren()
                    .add(buildItemPs(TransaksiModel.pesananPs));
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

    private HBox buildCartItem(CartItem ci) {
        // Nama & harga satuan
        Label nama = new Label(ci.produk.nama);
        nama.getStyleClass().add("cart-item-nama");

        Label hargaSatuan = new Label("@ Rp " + FMT.format(ci.produk.harga));
        hargaSatuan.getStyleClass().add("cart-item-harga");

        VBox infoBox = new VBox(2, nama, hargaSatuan);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // Qty control
        Button btnMin = new Button("−");
        btnMin.getStyleClass().add("btn-qty");
        btnMin.setOnAction(e -> {
            if (ci.qty > 1) {
                ci.qty--;
            } else {
                TransaksiModel.keranjang.remove(ci.produk.id);
            }
            renderKeranjang();
            updateSummary();
        });

        Label qtyLabel = new Label(String.valueOf(ci.qty));
        qtyLabel.getStyleClass().add("lbl-qty");

        Button btnPlus = new Button("+");
        btnPlus.getStyleClass().add("btn-qty");
        btnPlus.setDisable(ci.qty >= ci.produk.stok);
        btnPlus.setOnAction(e -> {
            if (ci.qty < ci.produk.stok) {
                ci.qty++;
            }
            renderKeranjang();
            updateSummary();
        });

        HBox qtyBox = new HBox(4, btnMin, qtyLabel, btnPlus);
        qtyBox.setAlignment(Pos.CENTER);

        // Subtotal
        Label subtotal = new Label("Rp " + FMT.format(ci.subtotal()));
        subtotal.getStyleClass().add("cart-item-subtotal");

        // Hapus
        Button btnHapus = new Button("✕");
        btnHapus.getStyleClass().add("btn-hapus-item");
        btnHapus.setOnAction(e -> {
            TransaksiModel.keranjang.remove(ci.produk.id);
            renderKeranjang();
            updateSummary();
        });

        // Row bawah: qty + subtotal + hapus
        HBox bottomRow = new HBox(8, qtyBox, subtotal, btnHapus);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        VBox content = new VBox(6, infoBox, bottomRow);
        HBox item = new HBox(content);
        item.getStyleClass().add("cart-item");
        return item;
    }

    // ═════════════════════════════════════════════════════
    // SUMMARY
    // ═════════════════════════════════════════════════════
    long kembalian;
    long tunai;

    public void updateSummary() {

        // Subtotal barang
        long subtotalBarang = TransaksiModel.keranjang.values()
                .stream()
                .mapToLong(CartItem::subtotal)
                .sum();

        // Subtotal rental PS
        long subtotalPs = 0;
        if (TransaksiModel.pesananPs != null) {
            subtotalPs = TransaksiModel.pesananPs.harga;
        }

        // Total subtotal transaksi
        TransaksiModel.subtotal = subtotalBarang + subtotalPs;

        double diskonPct = parseDouble(
                tfDiskon.getText().replaceAll("[^0-9]", ""));

        long diskon = (long) (TransaksiModel.subtotal * diskonPct / 100.0);

        TransaksiModel.total = TransaksiModel.subtotal - diskon;

        lblSubtotal.setText("Rp " + FMT.format(TransaksiModel.subtotal));
        lblDiskon.setText("- Rp " + FMT.format(diskon));
        lblTotal.setText("Rp " + FMT.format(TransaksiModel.total));

        // Kembalian
        tunai = parseLong(
                tfTunai.getText().replaceAll("[^0-9]", ""));

        kembalian = tunai - TransaksiModel.total;

        lblKembalian.setText(
                kembalian >= 0
                        ? "Kembalian Rp " + FMT.format(kembalian)
                        : "Kurang Rp " + FMT.format(Math.abs(kembalian)));

        lblKembalian.setStyle(
                kembalian >= 0
                        ? "-fx-text-fill: #00E5A0;"
                        : "-fx-text-fill: #FF5C7C;");
    }

    // ═════════════════════════════════════════════════════
    // METODE PEMBAYARAN LAIN
    // ═════════════════════════════════════════════════════
    boolean pembayaraanQris = false;

    void setupMetodeBayar() {
        System.out.println(btnQris.getStyleClass());
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

    void setupMenu() {
        btnProduk.getStyleClass().add("btn-menu-active");
        btnProduk.setOnAction(e -> {
            btnProduk.getStyleClass().add("btn-menu-active");
            btnPS.getStyleClass().remove("btn-menu-active");
            boxRentalPs.setVisible(false);
            boxRentalPs.setManaged(false);
            scrolpane.setVisible(true);
            scrolpane.setManaged(true);

        });
        btnPS.setOnAction(e -> {
            boxRentalPs.setVisible(true);
            boxRentalPs.setManaged(true);
            scrolpane.setVisible(false);
            scrolpane.setManaged(false);
            btnProduk.getStyleClass().remove("btn-menu-active");
            btnPS.getStyleClass().add("btn-menu-active");

        });
    }

    // ═════════════════════════════════════════════════════
    // HANDLERS
    // ══════════════════════════════════════════════════
    @FXML
    private void onDiskonChanged() {
        updateSummary();
    }

    private boolean isUpdating = false;

    @FXML
    private void onTunaiChanged() {
        if (isUpdating)
            return;
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

    @FXML
    private void onKosongkanKeranjang() {
        TransaksiModel.keranjang.clear();
        TransaksiModel.pesananPs = null;

        renderKeranjang();
        updateSummary();
    }

    // Quick nominal tunai
    @FXML
    private void onQuick5() {
        tfTunai.setText("5.000");
        updateSummary();
    }

    @FXML
    private void onQuick10() {
        tfTunai.setText("10.000");
        updateSummary();
    }

    @FXML
    private void onQuick20() {
        tfTunai.setText("20.000");
        updateSummary();
    }

    @FXML
    private void onQuick50() {
        tfTunai.setText("50.000");
        updateSummary();
    }

    // Proses bayar
    @FXML
    private void onProsesBayar() {
        Stage ownerStage = (Stage) btnBayar.getScene().getWindow();
        if (TransaksiModel.keranjang.isEmpty()
                && TransaksiModel.pesananPs == null) {
            return;
        }

        if (pembayaraanQris) {
            String sqlTransaksi = "INSERT INTO tb_transaksi "
                    + "(id_karyawan, total_pembayaran, uang_pembayaran, kembalian, kekurangan, status_pembayaran, tanggal_transaksi, pelanggan) "
                    + "VALUES (?, ?, ?, ?, ?, ?, DATETIME('now','localtime'), ?)";

            koneksi.eksekusiQuery(sqlTransaksi, session.id, TransaksiModel.total, TransaksiModel.total, 0, 0,
                    "Lunas",
                    "");

        } else {
            if (tfTunai.getText() == null || tfTunai.getText().isBlank() || tunai == 0) {
                new Popup().showModernPopup(
                        "WARNING",
                        "Silahkan Masukkan Nominal Tunai",
                        Popup.PopupType.WARNING, ownerStage);
                return;
            } else {
                if (kembalian >= 0) {

                    String sqlTransaksi = "INSERT INTO tb_transaksi "
                            + "(id_karyawan, total_pembayaran, uang_pembayaran, kembalian, kekurangan, status_pembayaran, tanggal_transaksi, pelanggan) "
                            + "VALUES (?, ?, ?, ?, ?, ?, DATETIME('now','localtime'), ?)";

                    koneksi.eksekusiQuery(sqlTransaksi, session.id, TransaksiModel.total, tunai, kembalian, 0,
                            "Lunas",
                            "");

                } else {

                    String sqlTransaksi = "INSERT INTO tb_transaksi "
                            + "(id_karyawan, total_pembayaran, uang_pembayaran, kembalian, kekurangan, status_pembayaran, tanggal_transaksi, pelanggan) "
                            + "VALUES (?, ?, ?, ?, ?, ?, DATETIME('now','localtime'), ?)";

                    koneksi.eksekusiQuery(sqlTransaksi, session.id, TransaksiModel.total, tunai, 0,
                            Math.abs(kembalian),
                            "Belum Lunas", "");
                }
            }

        }

        for (CartItem item : TransaksiModel.keranjang.values()) {

            String sqlUpdateStok = "UPDATE tb_barang SET stok = stok - ? WHERE id_barang = ?";

            koneksi.eksekusiQuery(sqlUpdateStok, item.qty, item.produk.id);
            System.out.println("Berhasil update stok barang ID: " + item.produk.id +
                    " qty: " + item.qty);
        }

        String sql_idtransaksi = "SELECT id_transaksi FROM tb_transaksi ORDER BY id_transaksi DESC LIMIT 1";

        List<Object[]> data = koneksi.ambilData(sql_idtransaksi);

        if (!data.isEmpty() && TransaksiModel.pesananPs != null) {

            int idTransaksi = ((Number) data.get(0)[0]).intValue();

            int totalMenit = (TransaksiModel.pesananPs.durasiJam * 60)
                    + TransaksiModel.pesananPs.durasiMenit;

            String sql = "INSERT INTO tb_paket_ps "
                    + "(id_transaksi, durasi, harga) "
                    + "VALUES (?, ?, ?)";

            koneksi.eksekusiQuery(
                    sql,
                    idTransaksi,
                    totalMenit,
                    TransaksiModel.pesananPs.harga);
        }
        // Update stok di database

        semuaProduk.clear();
        loadproduk();
        onClearSearch();

        // TODO: simpan ke database, cetak struk, dll.
        System.out.println("=== TRANSAKSI BERHASIL ===");
        System.out.println("No: #TRX-" + String.format("%04d", TransaksiModel.noTrx));
        System.out.println("Metode: " + TransaksiModel.metodeBayar);
        tfTunai.clear();
        navigation nav = new navigation();
        Stage stage = (Stage) btnBayar.getScene().getWindow();
        nav.detailTransaksi(stage, this);
        // TransaksiModel.keranjang.clear();
        // TransaksiModel.pesananPs = null;

    }

    // ── Helpers ───────────────────────────────────────────
    private double parseDouble(String s) {
        try {
            return s == null || s.isBlank() ? 0 : Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private long parseLong(String s) {
        try {
            return s == null || s.isBlank() ? 0 : Long.parseLong(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @FXML
    private void onRentalPs() {

    }

    // ── State ──────────────────────────────────────
    private int jam = 0;
    private int menit = 0; // hanya 0 atau 30
    private int activePreset = -1; // -1 = custom

    private TransaksiController transaksiController;

    // private static final NumberFormat FMT = NumberFormat.getInstance(
    // new Locale("id", "ID"));

    // harga per 30 menit = Rp 3.000
    // harga per 60 menit = Rp 5.000
    // custom: 30 menit pertama = 3.000, tiap jam = 5.000
    private static final long HARGA_30_MENIT = 3_000;
    private static final long HARGA_1_JAM = 5_000;

    // ── Setter dipanggil dari navigation ──────────
    public void setTransaksiController(TransaksiController tc) {
        this.transaksiController = tc;
    }

    // ═══════════════════════════════════════════════
    // PRESET HANDLERS
    // ═══════════════════════════════════════════════
    @FXML
    private void onPreset1() {
        setPreset(0, 30, 1);
    } // 30 menit

    @FXML
    private void onPreset2() {
        setPreset(1, 0, 2);
    } // 1 jam

    @FXML
    private void onPreset3() {
        setPreset(1, 30, 3);
    } // 1,5 jam

    @FXML
    private void onPreset4() {
        setPreset(2, 0, 4);
    } // 2 jam

    private void resetPresetStyle() {
        for (Button b : new Button[] { btnPreset1, btnPreset2, btnPreset3, btnPreset4 }) {
            b.getStyleClass().setAll("preset-btn");
        }
    }

    private void setPreset(int j, int m, int presetNo) {

        jam = j;
        menit = m;
        activePreset = presetNo;

        lblJam.setText(String.valueOf(jam));
        lblMenit.setText(String.valueOf(menit));

        resetPresetStyle();

        Button[] btns = { btnPreset1, btnPreset2, btnPreset3, btnPreset4 };

        btns[presetNo - 1]
                .getStyleClass()
                .add("preset-btn-active");

        updateDisplay();
    }

    // ═══════════════════════════════════════════════
    // CUSTOM SPINNER HANDLERS
    // ═══════════════════════════════════════════════
    @FXML
    private void onJamPlus() {
        jam++;
        activePreset = -1; // switch ke custom
        resetPresetStyle();
        lblJam.setText(String.valueOf(jam));
        updateDisplay();
    }

    @FXML
    private void onJamMin() {
        if (jam > 0) {
            jam--;
            activePreset = -1;
            resetPresetStyle();
            lblJam.setText(String.valueOf(jam));
            updateDisplay();
        }
    }

    @FXML
    private void onMenitPlus() {
        menit = (menit == 0) ? 30 : 0;
        if (menit == 0)
            jam++; // overflow 30+30 = 1 jam
        activePreset = -1;
        resetPresetStyle();
        lblJam.setText(String.valueOf(jam));
        lblMenit.setText(String.valueOf(menit));
        updateDisplay();
    }

    @FXML
    private void onMenitMin() {
        if (menit == 30) {
            menit = 0;
        } else if (jam > 0) {
            jam--;
            menit = 30;
        }
        activePreset = -1;
        resetPresetStyle();
        lblJam.setText(String.valueOf(jam));
        lblMenit.setText(String.valueOf(menit));
        updateDisplay();
    }

    // ═══════════════════════════════════════════════
    // HITUNG HARGA & UPDATE DISPLAY
    // ═══════════════════════════════════════════════
    private void updateDisplay() {
        int totalMenit = (jam * 60) + menit;
        long harga = hitungHarga(totalMenit);

        // teks durasi
        String durasiText = buildDurasiText();
        lblDurasiText.setText(totalMenit == 0 ? "-" : durasiText);
        lblHargaTotal.setText("Rp " + FMT.format(harga));

        // disable konfirmasi kalau durasi 0
        btnKonfirmasi.setDisable(totalMenit == 0);
    }

    private long hitungHarga(int totalMenit) {
        if (totalMenit == 0)
            return 0;
        if (totalMenit == 30)
            return HARGA_30_MENIT; // 30 menit = 3.000

        // tiap jam = 5.000, sisa 30 menit = 3.000
        long jamPenuh = totalMenit / 60;
        int sisaMenit = totalMenit % 60;
        return (jamPenuh * HARGA_1_JAM) + (sisaMenit > 0 ? HARGA_30_MENIT : 0);
    }

    private String buildDurasiText() {
        if (jam == 0 && menit == 0)
            return "-";
        if (jam == 0)
            return menit + " Menit";
        if (menit == 0)
            return jam + " Jam";
        return jam + " Jam " + menit + " Menit";
    }

    // ═══════════════════════════════════════════════
    // KONFIRMASI — masuk ke keranjang
    // ═══════════════════════════════════════════════
    @FXML
    private void onKonfirmasi() {

        int totalMenit = (jam * 60) + menit;
        long harga = hitungHarga(totalMenit);

        TransaksiModel.pesananPs = new ItemPs(
                -1,
                jam,
                menit,
                harga);

        renderKeranjang();
        updateSummary();
    }

    @FXML
    private void onBatal() {
        Stage stage = (Stage) btnKonfirmasi.getScene().getWindow();
        stage.close();
    }

    private HBox buildItemPs(ItemPs itemPs) {

        Label nama = new Label("Play Station");
        nama.getStyleClass().add("cart-item-nama");

        Label hargaSatuan = new Label(
                "1 Jam Rp 5.000 / 30 Menit Rp 3.000");
        hargaSatuan.getStyleClass().add("cart-item-harga");

        VBox infoBox = new VBox(2, nama, hargaSatuan);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(infoBox, Priority.ALWAYS);

        // ==================================================
        // HITUNG TOTAL MENIT SAAT INI
        // ==================================================
        int totalMenit = (itemPs.durasiJam * 60)
                + itemPs.durasiMenit;

        // ==================================================
        // BUTTON MINUS
        // ==================================================
        Button btnMin = new Button("−");
        btnMin.getStyleClass().add("btn-qty");

        btnMin.setOnAction(e -> {

            int menitSekarang = (itemPs.durasiJam * 60)
                    + itemPs.durasiMenit;

            if (menitSekarang > 30) {

                menitSekarang -= 30;

                itemPs.durasiJam = menitSekarang / 60;
                itemPs.durasiMenit = menitSekarang % 60;

                itemPs.harga = hitungHarga(menitSekarang);

                renderKeranjang();
                updateSummary();
            }
        });

        // ==================================================
        // LABEL DURASI
        // ==================================================
        Label lblDurasi = new Label(
                itemPs.durasiJam + " Jam "
                        + itemPs.durasiMenit + " Menit");

        lblDurasi.getStyleClass().add("lbl-qty");

        // ==================================================
        // BUTTON PLUS
        // ==================================================
        Button btnPlus = new Button("+");
        btnPlus.getStyleClass().add("btn-qty");

        btnPlus.setOnAction(e -> {

            int menitSekarang = (itemPs.durasiJam * 60)
                    + itemPs.durasiMenit;

            menitSekarang += 30;

            itemPs.durasiJam = menitSekarang / 60;
            itemPs.durasiMenit = menitSekarang % 60;

            itemPs.harga = hitungHarga(menitSekarang);

            renderKeranjang();
            updateSummary();
        });

        HBox durasiBox = new HBox(
                5,
                btnMin,
                lblDurasi,
                btnPlus);

        durasiBox.setAlignment(Pos.CENTER_LEFT);

        // ==================================================
        // SUBTOTAL
        // ==================================================
        Label subtotal = new Label(
                "Rp " + FMT.format(itemPs.harga));

        subtotal.getStyleClass().add("cart-item-subtotal");

        // ==================================================
        // HAPUS
        // ==================================================
        Button btnHapus = new Button("✕");
        btnHapus.getStyleClass().add("btn-hapus-item");

        btnHapus.setOnAction(e -> {
            TransaksiModel.pesananPs = null;

            renderKeranjang();
            updateSummary();
        });

        // ==================================================
        // BOTTOM ROW
        // ==================================================
        HBox bottomRow = new HBox(
                8,
                durasiBox,
                subtotal,
                btnHapus);

        bottomRow.setAlignment(Pos.CENTER_LEFT);

        VBox content = new VBox(
                6,
                infoBox,
                bottomRow);

        HBox item = new HBox(content);
        item.getStyleClass().add("cart-item");

        return item;
    }

    // ═══════════════════════════════════════════════════════
    // OTHER HANDLERS
    // ═══════════════════════════════════════════════════════
    @FXML
    private void onNotif() {
        System.out.println("Notifikasi dibuka");
    }

    @FXML
    private void onLihatSemua() {
        System.out.println("Lihat semua transaksi");
    }

}
