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
import com.mycompany.Model.TransaksiModel.Produk;

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

    koneksi koneksi = new koneksi();

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

    @FXML
    private VBox sidebar;
    @FXML
    private Button toggleBtn;
    @FXML
    private HBox logoRow;
    @FXML
    private VBox logoBrand;
    @FXML
    private VBox userInfo;
    @FXML
    private HBox userRow;
    @FXML
    private VBox navMenu;
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
    private Label lblNoTrx;

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

    private boolean sidebarCollapsed = false;
    private static final double SIDEBAR_FULL = 220;
    private static final double SIDEBAR_MINI = 60;

    private final java.util.List<Produk> semuaProduk = TransaksiModel.semuaProduk;
    private static final NumberFormat FMT = NumberFormat.getInstance(new Locale("id", "ID"));

    @FXML
    private void onToggleSidebar() {
        sidebarCollapsed = !sidebarCollapsed;

        double targetWidth = sidebarCollapsed ? SIDEBAR_MINI : SIDEBAR_FULL;

        // Animasi lebar sidebar
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(sidebar.prefWidthProperty(), sidebar.getPrefWidth()),
                        new KeyValue(sidebar.minWidthProperty(), sidebar.getMinWidth())),
                new KeyFrame(Duration.millis(350), new KeyValue(sidebar.prefWidthProperty(), targetWidth),
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

    @FXML
    private void onNotif() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notifikasi");
        alert.setHeaderText(null);
        alert.setContentText("Tidak ada pemberitahuan baru.");
        alert.showAndWait();
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
        TransaksiModel.keranjang.clear();

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
                navLblPiutang, navLblPengaturan);
        for (Label lbl : labels) {
            lbl.setVisible(visible);
            lbl.setManaged(visible);
        }
    }

    private void updateNavPadding(boolean collapsed) {
        Insets collapsedPad = new Insets(10, 0, 10, 0);
        Insets normalPad = new Insets(10, 14, 10, 0);
        Insets pad = collapsed ? collapsedPad : normalPad;

        List<HBox> items = List.of(navDashboard, navProduk, navKasir, navPelanggan, navLaporan, navPiutang,
                navPengaturan);
        for (HBox item : items) {
            item.setAlignment(collapsed ? Pos.CENTER : Pos.CENTER_LEFT);
            item.setPadding(pad);
        }
    }

    private void setActiveNav(HBox selected) {
        List<HBox> all = List.of(navDashboard, navProduk, navKasir, navPelanggan, navLaporan, navPiutang,
                navPengaturan);
        for (HBox item : all) {
            item.getStyleClass().removeAll("nav-active");
        }
        if (selected != null) {
            selected.getStyleClass().add("nav-active");
        }
    }

    // ── Variables ──────────────────────────────────────────────

    // ═════════════════════════════════════════════════════
    // INITIALIZE
    // ═════════════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadproduk();
        setupKategori();
        setupSearch();
        renderProduk(semuaProduk);
        setActiveNav(navKasir);
        updateSummary();
        lblNamaKasir.setText("Budi S.");
        lblShift.setText("Shift Siang");
        lblNoTrx.setText(String.format("#TRX-%04d", TransaksiModel.noTrx));
        setupForm();
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
        btnBayar.setDisable(true);
        tfTunai.setDisable(true);
        lblKembalian.setText("Rp 0");
        btnQuick5.setDisable(true);
        btnQuick10.setDisable(true);
        btnQuick20.setDisable(true);
        btnQuick50.setDisable(true);
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
            var url = getClass().getResource("/image/not_found.png");
            if (url != null) {
                img.setImage(new Image(url.toExternalForm()));
            }
        } catch (Exception e) {
            System.out.println("Gambar tidak ditemukan");
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

        // tambah filter kategori hanya jika bukan "Semua Kategori"
        if (kat != null && !kat.equals("Semua Kategori")) {
            sql.append(" AND kategori = '").append(kat).append("'");
        }

        // tambah filter nama hanya jika ada ketikan
        if (!query.isEmpty()) {
            sql.append(" AND nama_barang LIKE '%").append(query).append("%'");
        }

        // ── Ambil data ────────────────────────────────
        List<Object[]> hasil = koneksi.ambilData(sql.toString());
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
        boolean kosong = TransaksiModel.keranjang.isEmpty();

        emptyCart.setVisible(kosong);
        emptyCart.setManaged(kosong);

        int totalItem = 0;
        for (CartItem ci : TransaksiModel.keranjang.values()) {
            totalItem += ci.qty;
            vboxKeranjang.getChildren().add(buildCartItem(ci));
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
        TransaksiModel.subtotal = TransaksiModel.keranjang.values().stream().mapToLong(CartItem::subtotal).sum();

        double diskonPct = parseDouble(tfDiskon.getText().replace("[^0-9]", ""));
        long diskon = (long) (TransaksiModel.subtotal * diskonPct / 100.0);
        TransaksiModel.total = TransaksiModel.subtotal - diskon;

        lblSubtotal.setText("Rp " + FMT.format(TransaksiModel.subtotal));
        lblDiskon.setText("- Rp " + FMT.format(diskon));

        lblTotal.setText("Rp " + FMT.format(TransaksiModel.total));

        // Kembalian
        tunai = parseLong(tfTunai.getText().replaceAll("[^0-9]", ""));
        kembalian = tunai - TransaksiModel.total;
        lblKembalian.setText(kembalian >= 0 ? "Kembalian Rp " + FMT.format(kembalian)
                : "Kurang Rp " + FMT.format(Math.abs(kembalian)));
        lblKembalian.setStyle(kembalian >= 0 ? "-fx-text-fill: #00E5A0;" : "-fx-text-fill: #FF5C7C;");
    }

    // ═════════════════════════════════════════════════════
    // HANDLERS
    // ═════════════════════════════════════════════════════
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
        if (TransaksiModel.keranjang.isEmpty()) {
            return;
        }
        if (tfTunai.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Silahkan isi Nominal Uang Pembayaran");
            alert.showAndWait();
            return;
        }

        CartItem ci = TransaksiModel.keranjang.values().iterator().next(); // ambil salah satu item untuk contoh
        if (kembalian >= 0) {

            String sqlTransaksi = String.format("INSERT INTO tb_transaksi "
                    + "(id_user, total_pembayaran, uang_pembayaran, kembalian, kekurangan, status_pembayaran, tanggal_transaksi, pelanggan) "
                    + "VALUES (%d, %d, %d, %d, %d, '%s', NOW(), '%s')",

                    session.id_user, TransaksiModel.total, tunai, kembalian, 0, "Lunas", "");

            koneksi.eksekusiQuery(sqlTransaksi);

        } else {

            String sqlTransaksi = String.format("INSERT INTO tb_transaksi "
                    + "(id_user, total_pembayaran, uang_pembayaran, kembalian, kekurangan, status_pembayaran, tanggal_transaksi, pelanggan) "
                    + "VALUES (%d, %d, %d, %d, %d, '%s', NOW(), '%s')",

                    session.id_user, TransaksiModel.total, tunai, 0, Math.abs(kembalian), "Belum Lunas", "");

            koneksi.eksekusiQuery(sqlTransaksi);
        }

        for (CartItem item : TransaksiModel.keranjang.values()) {

            String sqlUpdateStok = "UPDATE tb_barang SET stok = stok - '" + item.qty + "' WHERE id_barang = '"
                    + item.produk.id + "'";

            koneksi.eksekusiQuery(sqlUpdateStok);
            System.out.println("Berhasil update stok barang ID: " + item.produk.id +
                    " qty: " + item.qty);
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

}
