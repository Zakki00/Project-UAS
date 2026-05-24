package com.mycompany.projectuas;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Stack;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.Separator;
import javafx.stage.Stage;

public class DetailTransaksiController implements Initializable {

    // ── FXML refs ─────────────────────────────────────
    @FXML
    private TextField tfPelanggan;
    @FXML
    private ScrollPane detailScroll;
    @FXML
    private Button btnSimpan;
    @FXML
    private Button btnBatal;
    @FXML
    private Label lblSubtotal;
    @FXML
    private Label lblPajak;
    @FXML
    private Label lblTotal;

    // ── Data ──────────────────────────────────────────
    private static final NumberFormat FMT = NumberFormat.getInstance(
            new Locale("id", "ID"));

    // Model item transaksi
    static class ItemTransaksi {
        String id, nama, kategori;
        long harga;
        int qty;

        ItemTransaksi(String id, String nama, String kategori,
                long harga, int qty) {
            this.id = id;
            this.nama = nama;
            this.kategori = kategori;
            this.harga = harga;
            this.qty = qty;
        }

        long subtotal() {
            return harga * qty;
        }
    }

    private final List<ItemTransaksi> listItem = new ArrayList<>();
    private VBox detailList;

    // ═════════════════════════════════════════════════
    // INITIALIZE
    // ═════════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDummyData();
        setupLayout();
        renderList();
    }

    // ── Dummy data ────────────────────────────────────
    private void loadDummyData() {
        listItem.add(new ItemTransaksi("P01", "Indomie Goreng", "Makanan", 3_500, 3));
        listItem.add(new ItemTransaksi("P02", "Aqua 600ml", "Minuman", 4_000, 2));
        listItem.add(new ItemTransaksi("P03", "Teh Sosro", "Minuman", 5_000, 1));
        listItem.add(new ItemTransaksi("P04", "Beng-Beng", "Snack", 4_000, 4));
        listItem.add(new ItemTransaksi("P06", "Sabun Lifebuoy", "Kebersihan", 12_500, 1));
        listItem.add(new ItemTransaksi("P07", "Sikat Gigi Pepsodent", "Kebersihan", 15_000, 1));
        listItem.add(new ItemTransaksi("P08", "Shampoo Sunsilk", "Kebersihan", 25_000, 1));
        listItem.add(new ItemTransaksi("P09", "Pasta Gigi Pepsodent", "Kebersihan", 10_000, 1));
        listItem.add(new ItemTransaksi("P10", "Susu Dancow", "Minuman", 20_000, 2));
        listItem.add(new ItemTransaksi("P11", "Susu Indomilk", "Minuman", 18_000, 1));
        listItem.add(new ItemTransaksi("P12", "Susu Bear Brand", "Minuman", 22_000, 1));
        listItem.add(new ItemTransaksi("P01", "Indomie Goreng", "Makanan", 3_500, 3));
        listItem.add(new ItemTransaksi("P02", "Aqua 600ml", "Minuman", 4_000, 2));
        listItem.add(new ItemTransaksi("P03", "Teh Sosro", "Minuman", 5_000, 1));
        listItem.add(new ItemTransaksi("P04", "Beng-Beng", "Snack", 4_000, 4));
        listItem.add(new ItemTransaksi("P06", "Sabun Lifebuoy", "Kebersihan", 12_500, 1));
        listItem.add(new ItemTransaksi("P07", "Sikat Gigi Pepsodent", "Kebersihan", 15_000, 1));
        listItem.add(new ItemTransaksi("P08", "Shampoo Sunsilk", "Kebersihan", 25_000, 1));
        listItem.add(new ItemTransaksi("P09", "Pasta Gigi Pepsodent", "Kebersihan", 10_000, 1));
        listItem.add(new ItemTransaksi("P10", "Susu Dancow", "Minuman", 20_000, 2));
        listItem.add(new ItemTransaksi("P11", "Susu Indomilk", "Minuman", 18_000, 1));
        listItem.add(new ItemTransaksi("P12", "Susu Bear Brand", "Minuman", 22_000, 1));

    }

    // ── Setup layout scroll content ───────────────────
    private void setupLayout() {
        detailList = new VBox(10);
        detailList.getStyleClass().add("detail-list");
        detailList.setPadding(new Insets(12, 0, 12, 0));

        detailScroll.setContent(detailList);
        detailScroll.getStyleClass().add("detail-scroll");
        detailScroll.setFitToWidth(true);
    }

    // ═════════════════════════════════════════════════
    // RENDER LIST
    // ═════════════════════════════════════════════════
    private void renderList() {
        detailList.getChildren().clear();

        if (listItem.isEmpty()) {
            // Empty state
            VBox empty = new VBox(8);
            empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(60, 0, 60, 0));

            Label icon = new Label("🛒");
            icon.getStyleClass().add("empty-icon");

            Label text = new Label("Belum ada item");
            text.getStyleClass().add("empty-text");

            Label sub = new Label("Tambahkan produk dari halaman kasir");
            sub.getStyleClass().add("empty-sub");

            empty.getChildren().addAll(icon, text, sub);
            detailList.getChildren().add(empty);
            btnSimpan.setDisable(true);
            return;
        }

        // Table header
        detailList.getChildren().add(buildTableHeader());

        // Item rows
        for (int i = 0; i < listItem.size(); i++) {
            detailList.getChildren().add(buildItemRow(listItem.get(i), i + 1));
        }

        // Summary
        updateSummary();

        btnSimpan.setDisable(false);
    }

    // ── Table header ──────────────────────────────────
    private HBox buildTableHeader() {
        Label thNo = new Label("No");
        Label thNama = new Label("Nama Produk");
        Label thHarga = new Label("Harga Satuan");
        Label thQty = new Label("Qty");
        Label thSubtotal = new Label("Subtotal");

        thNo.getStyleClass().add("th-label");
        thNama.getStyleClass().add("th-label");
        thHarga.getStyleClass().add("th-label");
        thQty.getStyleClass().add("th-label");
        thSubtotal.getStyleClass().add("th-label");

        // ← samakan ukuran dengan item row
        thNo.setPrefWidth(40);
        thNo.setMinWidth(40);
        thNo.setAlignment(Pos.CENTER);

        HBox.setHgrow(thNama, Priority.ALWAYS);
        thNama.setMaxWidth(Double.MAX_VALUE);

        thHarga.setPrefWidth(130);
        thHarga.setMinWidth(130);
        thHarga.setAlignment(Pos.CENTER_RIGHT);

        thQty.setPrefWidth(60);
        thQty.setMinWidth(60);
        thQty.setAlignment(Pos.CENTER);

        thSubtotal.setPrefWidth(120);
        thSubtotal.setMinWidth(120);
        thSubtotal.setAlignment(Pos.CENTER_RIGHT);

        // spacer untuk ganti tombol hapus
        Label spacer = new Label();
        spacer.setPrefWidth(30);
        spacer.setMinWidth(30);

        HBox header = new HBox(12, thNo, thNama, thHarga, thQty, thSubtotal, spacer);
        header.getStyleClass().add("table-header");
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    // ── Item row ──────────────────────────────────────
    private HBox buildItemRow(ItemTransaksi item, int no) {
        // No urut
        Label lblNo = new Label(String.valueOf(no));
        lblNo.getStyleClass().add("item-no");
        lblNo.setPrefWidth(40);
        lblNo.setMinWidth(40);
        lblNo.setAlignment(Pos.CENTER);

        // Nama + kategori
        Label lblNama = new Label(item.nama);
        lblNama.getStyleClass().add("item-nama");

        Label lblKat = new Label(item.kategori);
        lblKat.getStyleClass().add("item-kategori");

        VBox namaBox = new VBox(2, lblNama, lblKat);
        namaBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(namaBox, Priority.ALWAYS);
        namaBox.setMaxWidth(Double.MAX_VALUE);

        // Harga satuan
        Label lblHarga = new Label("Rp " + FMT.format(item.harga));
        lblHarga.getStyleClass().add("item-harga");
        lblHarga.setPrefWidth(130);
        lblHarga.setMinWidth(130);
        lblHarga.setAlignment(Pos.CENTER_RIGHT);

        // Qty
        Label lblQty = new Label("x" + item.qty);
        lblQty.getStyleClass().add("item-qty");
        lblQty.setPrefWidth(60);
        lblQty.setMinWidth(60);
        lblQty.setAlignment(Pos.CENTER);

        // Subtotal
        Label lblSub = new Label("Rp " + FMT.format(item.subtotal()));
        lblSub.getStyleClass().add("item-subtotal");
        lblSub.setPrefWidth(120);
        lblSub.setMinWidth(120);
        lblSub.setAlignment(Pos.CENTER_RIGHT);

        // Tombol hapus
        Button btnHapus = new Button("✕");
        btnHapus.getStyleClass().add("item-hapus");
        btnHapus.setPrefWidth(30);
        btnHapus.setMinWidth(30);
        btnHapus.setOnAction(e -> {
            listItem.remove(item);
            renderList();
        });

        HBox row = new HBox(12, lblNo, namaBox, lblHarga, lblQty, lblSub, btnHapus);
        row.getStyleClass().add("item-row");
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    // ── Summary ───────────────────────────────────────
    private void updateSummary() {
        long subtotal = listItem.stream()
                .mapToLong(ItemTransaksi::subtotal).sum();
        long pajak = (long) (subtotal * 0.11);
        long total = subtotal + pajak;

        lblSubtotal.setText("Rp " + FMT.format(subtotal));
        lblPajak.setText("Rp " + FMT.format(pajak));
        lblTotal.setText("Rp " + FMT.format(total));
    }

    private HBox buildSummaryRow(String key, String value, boolean isDiskon) {
        Label lblKey = new Label(key);
        lblKey.getStyleClass().add("summary-label");
        HBox.setHgrow(lblKey, Priority.ALWAYS);

        Label lblVal = new Label(value);
        lblVal.getStyleClass().add(isDiskon ? "summary-diskon" : "summary-value");

        HBox row = new HBox(lblKey, lblVal);
        row.getStyleClass().add("summary-row");
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    // ═════════════════════════════════════════════════
    // HANDLERS
    // ═════════════════════════════════════════════════
    void closeForm(Button source) {
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setOnCloseRequest(null);
        stage.close();
    }

    @FXML
    private void onSimpanTransaksi() {
        String pelanggan = tfPelanggan.getText().trim();
        if (pelanggan.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Nama pelanggan tidak boleh kosong!");
            alert.showAndWait();
            return;

        }
        // method helper untuk tutup form

        // TODO: simpan ke database
        System.out.println("=== SIMPAN TRANSAKSI ===");
        System.out.println("Pelanggan: " + pelanggan);
        listItem.forEach(item -> System.out.printf("  %s x%d = Rp %s%n",
                item.nama, item.qty, FMT.format(item.subtotal())));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Berhasil");
        alert.setHeaderText(null);
        alert.setContentText("✅ Transaksi berhasil disimpan!");
        alert.showAndWait();
        closeForm(btnSimpan);
    }

    @FXML
    private void onBatalTransaksi() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi");
        confirm.setHeaderText(null);
        confirm.setContentText("Yakin ingin membatalkan transaksi?");
        confirm.showAndWait().ifPresent(response -> {
            if (response.getButtonData().isDefaultButton()) {
                listItem.clear();
                tfPelanggan.clear();
                renderList();
            }
        });
        closeForm(btnBatal);

    }
}