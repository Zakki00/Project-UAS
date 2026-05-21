/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.projectuas;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


/**
 * FXML Controller class
 *
 * @author zakki mubarroq
 */
public class TransaksiController implements Initializable {

   // ── FXML refs ────────────────────────────────────────
    @FXML private FlowPane  flowProduk;
    @FXML private TextField tfCari;
    @FXML private ComboBox<String> cbKategori;
    @FXML private Label     lblJumlahProduk;
    @FXML private Button    btnClearSearch;

    @FXML private VBox      sidebar;
    @FXML private Button    toggleBtn;
    @FXML private HBox      logoRow;
    @FXML private VBox      logoBrand;
    @FXML private VBox      userInfo;
    @FXML private HBox      userRow;
    @FXML private VBox      navMenu;
    @FXML private HBox      navDashboard;
    @FXML private HBox      navProduk;
    @FXML private HBox      navKasir;
    @FXML private HBox      navPelanggan;
    @FXML private HBox      navLaporan;
    @FXML private HBox      navPengaturan;

    @FXML private Label     navLblDashboard;
    @FXML private Label     navLblProduk;
    @FXML private Label     navLblKasir;
    @FXML private Label     navLblPelanggan;
    @FXML private Label     navLblLaporan;
    @FXML private Label     navLblPengaturan;

    private boolean sidebarCollapsed = false;
    private static final double SIDEBAR_FULL = 220;
    private static final double SIDEBAR_MINI = 60;

    @FXML private VBox      vboxKeranjang;
    @FXML private VBox      emptyCart;
    @FXML private Label     lblJumlahItem;
    @FXML private Label     lblNamaKasir;
    @FXML private Label     lblShift;

    @FXML private Label     lblSubtotal;
    @FXML private Label     lblDiskon;
    @FXML private Label     lblPajak;
    @FXML private Label     lblTotal;
    @FXML private TextField tfDiskon;
    @FXML private TextField tfTunai;
    @FXML private Label     lblKembalian;
    @FXML private VBox      tunaiBox;
    @FXML private Label     lblNoTrx;

    @FXML private Button    btnBayar;
    @FXML private Button    btnTunai;
    @FXML private Button    btnQris;
    @FXML private Button    btnDebit;

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

    @FXML private void onNotif() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notifikasi");
        alert.setHeaderText(null);
        alert.setContentText("Tidak ada pemberitahuan baru.");
        alert.showAndWait();
    }

    @FXML private void onNavDashboard() { setActiveNav(navDashboard); }
    @FXML private void onNavProduk()     { setActiveNav(navProduk); }
    @FXML private void onNavKasir()      { setActiveNav(navKasir); }
    @FXML private void onNavPelanggan()  { setActiveNav(navPelanggan); }
    @FXML private void onNavLaporan()    { setActiveNav(navLaporan); }
    @FXML private void onNavPengaturan() { setActiveNav(navPengaturan); }

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

    private void setActiveNav(HBox selected) {
        List<HBox> all = List.of(
                navDashboard, navProduk, navKasir,
                navPelanggan, navLaporan, navPengaturan);
        for (HBox item : all) {
            item.getStyleClass().removeAll("nav-active");
        }
        if (selected != null) {
            selected.getStyleClass().add("nav-active");
        }
    }

    // ── Data ─────────────────────────────────────────────
    private final List<Produk> semuaProduk = new ArrayList<>();
    private final Map<String, CartItem> keranjang = new LinkedHashMap<>();
    private String metodeBayar = "TUNAI";
    private int noTrx = 1;

    private static final NumberFormat FMT = NumberFormat.getInstance(
            new Locale("id", "ID"));

    // ── Model ─────────────────────────────────────────────
    static class Produk {
        String id, nama, kategori, emoji;
        long harga;
        int stok;

        Produk(String id, String nama, String kategori,
               String emoji, long harga, int stok) {
            this.id = id; this.nama = nama;
            this.kategori = kategori; this.emoji = emoji;
            this.harga = harga; this.stok = stok;
        }
    }

    static class CartItem {
        Produk produk;
        int qty;
        CartItem(Produk p) { this.produk = p; this.qty = 1; }
        long subtotal() { return (long) produk.harga * qty; }
    }

    // ═════════════════════════════════════════════════════
    //  INITIALIZE
    // ═════════════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDummyProduk();
        setupKategori();
        setupSearch();
        renderProduk(semuaProduk);
        setActiveNav(navKasir);
        updateSummary();
        lblNamaKasir.setText("Budi S.");
        lblShift.setText("Shift Siang");
        lblNoTrx.setText(String.format("#TRX-%04d", noTrx));
        btnBayar.setDisable(true);
    }

    // ── Dummy data ────────────────────────────────────────
    private void loadDummyProduk() {
        semuaProduk.addAll(List.of(
            new Produk("P01","Indomie Goreng","Makanan","🍜",3_500,248),
            new Produk("P02","Aqua 600ml",    "Minuman","💧",4_000,180),
            new Produk("P03","Teh Sosro",     "Minuman","🍵",5_000,12),
            new Produk("P04","Beng-Beng",     "Snack",  "🍫",4_000,67),
            new Produk("P05","Minyak Goreng", "Dapur",  "🧴",18_000,3),
            new Produk("P06","Sabun Lifebuoy","Kebersihan","🧼",12_500,95),
            new Produk("P07","Kopi Kapal Api","Minuman","☕",8_000,40),
            new Produk("P08","Gula Pasir 1kg","Dapur",  "🍚",14_000,8),
            new Produk("P09","Oreo Vanilla",  "Snack",  "🍪",8_500,55),
            new Produk("P10","Susu Ultra 250","Minuman","🥛",6_000,0)  // stok habis
        ));
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

    // ═════════════════════════════════════════════════════
    //  RENDER PRODUK CARDS
    // ═════════════════════════════════════════════════════
    private void renderProduk(List<Produk> list) {
        flowProduk.getChildren().clear();
        for (Produk p : list) {
            flowProduk.getChildren().add(buildProdukCard(p));
        }
        lblJumlahProduk.setText(list.size() + " produk");
    }

    private VBox buildProdukCard(Produk p) {
        boolean habis = p.stok == 0;

        // Emoji
        Label emoji = new Label(p.emoji);
        emoji.getStyleClass().add("produk-card-emoji");

        // Nama
        Label nama = new Label(p.nama);
        nama.getStyleClass().add("produk-card-nama");
        nama.setMaxWidth(130);
        nama.setWrapText(true);

        // Harga
        Label harga = new Label("Rp " + FMT.format(p.harga));
        harga.getStyleClass().add("produk-card-harga");

        // Stok
        Label stok = new Label(habis ? "Stok habis" : "Stok: " + p.stok);
        stok.getStyleClass().add("produk-card-stok");

        // Tombol tambah
        Button btnTambah = new Button("+ Tambah");
        btnTambah.getStyleClass().add("btn-tambah-card");
        btnTambah.setDisable(habis);
        btnTambah.setOnAction(e -> tambahKeKeranjang(p));

        VBox card = new VBox(6, emoji, nama, harga, stok, btnTambah);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(12));
        card.setPrefWidth(150);
        card.setPrefHeight(165);
        card.getStyleClass().add(habis ? "produk-card produk-card-habis" : "produk-card");

        // Klik card = tambah ke keranjang
        if (!habis) {
            card.setOnMouseClicked(e -> tambahKeKeranjang(p));
        }

        return card;
    }

    // ═════════════════════════════════════════════════════
    //  FILTER
    // ═════════════════════════════════════════════════════
    private void filterProduk() {
        String query = tfCari.getText().toLowerCase().trim();
        String kat   = cbKategori.getValue();

        List<Produk> hasil = semuaProduk.stream()
            .filter(p -> (kat == null || kat.equals("Semua Kategori")
                          || p.kategori.equals(kat)))
            .filter(p -> query.isEmpty()
                         || p.nama.toLowerCase().contains(query))
            .toList();

        renderProduk(hasil);
    }

    @FXML
    private void onClearSearch() {
        tfCari.clear();
        cbKategori.setValue("Semua Kategori");
        renderProduk(semuaProduk);
    }

    // ═════════════════════════════════════════════════════
    //  KERANJANG
    // ═════════════════════════════════════════════════════
    private void tambahKeKeranjang(Produk p) {
        if (keranjang.containsKey(p.id)) {
            CartItem ci = keranjang.get(p.id);
            if (ci.qty < p.stok) ci.qty++;
        } else {
            keranjang.put(p.id, new CartItem(p));
        }
        renderKeranjang();
        updateSummary();
    }

    private void renderKeranjang() {
        vboxKeranjang.getChildren().clear();
        boolean kosong = keranjang.isEmpty();

        emptyCart.setVisible(kosong);
        emptyCart.setManaged(kosong);

        int totalItem = 0;
        for (CartItem ci : keranjang.values()) {
            totalItem += ci.qty;
            vboxKeranjang.getChildren().add(buildCartItem(ci));
        }

        lblJumlahItem.setText(totalItem + " item");
        btnBayar.setDisable(kosong);
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
            if (ci.qty > 1) { ci.qty--; } else { keranjang.remove(ci.produk.id); }
            renderKeranjang();
            updateSummary();
        });

        Label qtyLabel = new Label(String.valueOf(ci.qty));
        qtyLabel.getStyleClass().add("lbl-qty");

        Button btnPlus = new Button("+");
        btnPlus.getStyleClass().add("btn-qty");
        btnPlus.setDisable(ci.qty >= ci.produk.stok);
        btnPlus.setOnAction(e -> {
            if (ci.qty < ci.produk.stok) ci.qty++;
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
            keranjang.remove(ci.produk.id);
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
    //  SUMMARY
    // ═════════════════════════════════════════════════════
    private void updateSummary() {
        long subtotal = keranjang.values().stream()
            .mapToLong(CartItem::subtotal).sum();

        double diskonPct = parseDouble(tfDiskon.getText());
        long diskon = (long) (subtotal * diskonPct / 100.0);
        long afterDiskon = subtotal - diskon;
        long pajak = (long) (afterDiskon * 0.11);
        long total = afterDiskon + pajak;

        lblSubtotal.setText("Rp " + FMT.format(subtotal));
        lblDiskon.setText("- Rp " + FMT.format(diskon));
        lblPajak.setText("Rp " + FMT.format(pajak));
        lblTotal.setText("Rp " + FMT.format(total));

        // Kembalian
        long tunai = parseLong(tfTunai.getText().replaceAll("[^0-9]", ""));
        long kembalian = tunai - total;
        lblKembalian.setText(kembalian >= 0
            ? "Rp " + FMT.format(kembalian)
            : "Kurang Rp " + FMT.format(Math.abs(kembalian)));
        lblKembalian.setStyle(kembalian >= 0
            ? "-fx-text-fill: #00E5A0;"
            : "-fx-text-fill: #FF5C7C;");
    }

    // ═════════════════════════════════════════════════════
    //  HANDLERS
    // ═════════════════════════════════════════════════════
    @FXML private void onDiskonChanged() { updateSummary(); }
    @FXML private void onTunaiChanged()  { updateSummary(); }

    @FXML private void onKosongkanKeranjang() {
        keranjang.clear();
        renderKeranjang();
        updateSummary();
    }

    // Metode bayar
    @FXML private void onPayTunai() {
        metodeBayar = "TUNAI";
        btnTunai.getStyleClass().setAll("pay-method-active");
        btnQris.getStyleClass().setAll("pay-method");
        btnDebit.getStyleClass().setAll("pay-method");
        tunaiBox.setVisible(true);
        tunaiBox.setManaged(true);
    }

    @FXML private void onPayQris() {
        metodeBayar = "QRIS";
        btnQris.getStyleClass().setAll("pay-method-active");
        btnTunai.getStyleClass().setAll("pay-method");
        btnDebit.getStyleClass().setAll("pay-method");
        tunaiBox.setVisible(false);
        tunaiBox.setManaged(false);
    }

    @FXML private void onPayDebit() {
        metodeBayar = "DEBIT";
        btnDebit.getStyleClass().setAll("pay-method-active");
        btnTunai.getStyleClass().setAll("pay-method");
        btnQris.getStyleClass().setAll("pay-method");
        tunaiBox.setVisible(false);
        tunaiBox.setManaged(false);
    }

    // Quick nominal tunai
    @FXML private void onQuick5()  { tfTunai.setText("5000");   updateSummary(); }
    @FXML private void onQuick10() { tfTunai.setText("10000");  updateSummary(); }
    @FXML private void onQuick20() { tfTunai.setText("20000");  updateSummary(); }
    @FXML private void onQuick50() { tfTunai.setText("50000");  updateSummary(); }

    // Proses bayar
    @FXML
    private void onProsesBayar() {
        if (keranjang.isEmpty()) return;

        // TODO: simpan ke database, cetak struk, dll.
        System.out.println("=== TRANSAKSI BERHASIL ===");
        System.out.println("No: #TRX-" + String.format("%04d", noTrx));
        System.out.println("Metode: " + metodeBayar);
        keranjang.forEach((id, ci) ->
            System.out.printf("  %s x%d = Rp %s%n",
                ci.produk.nama, ci.qty, FMT.format(ci.subtotal())));

        // Reset
        noTrx++;
        keranjang.clear();
        tfTunai.clear();
        tfDiskon.clear();
        renderKeranjang();
        updateSummary();
        lblNoTrx.setText(String.format("#TRX-%04d", noTrx));

        // Tampilkan alert sukses
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Transaksi Berhasil");
        alert.setHeaderText(null);
        alert.setContentText("✅ Pembayaran berhasil diproses!");
        alert.showAndWait();
    }

    // ── Helpers ───────────────────────────────────────────
    private double parseDouble(String s) {
        try { return s == null || s.isBlank() ? 0 : Double.parseDouble(s.trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    private long parseLong(String s) {
        try { return s == null || s.isBlank() ? 0 : Long.parseLong(s.trim()); }
        catch (NumberFormatException e) { return 0; }
    }
    
}
