package com.mycompany.projectuas;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.mycompany.Model.DetailTransaksiModel;
import com.mycompany.Model.DetailTransaksiModel.ItemTransaksi;

import com.mycompany.Model.TransaksiModel.CartItem;
import com.mycompany.Model.TransaksiModel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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

    // --------------passing reference object
    private TransaksiController transaksiController;

    public void setTransaksiController(TransaksiController transaksiController) {
        this.transaksiController = transaksiController;
    }

    // ── Data ──────────────────────────────────────────
    private static final NumberFormat FMT = NumberFormat.getInstance(new Locale("id", "ID"));
    int id_transaksi = -1;

    void idtransaksi() {
        String sql_idtransaksi = "SELECT * FROM tb_transaksi ORDER BY id_transaksi DESC LIMIT 1";
        List<Object[]> data = koneksi.ambilData(sql_idtransaksi);

        if (!data.isEmpty()) {
            Object[] row = data.get(0);
            id_transaksi = ((Number) row[0]).intValue();
        }
    }


    private VBox detailList;

    // ═════════════════════════════════════════════════
    // INITIALIZE
    // ═════════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idtransaksi();
        setupLayout();
        renderList();

        if (!TransaksiModel.keranjang.values().isEmpty()) {
            System.out.println("data ada");
        }
    }

    // ── Dummy data ────────────────────────────────────

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

        if (TransaksiModel.keranjang.isEmpty()) {
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
        int no = 1;
        for (CartItem ci : TransaksiModel.keranjang.values()) {
            detailList.getChildren().add(setdatatransaksi(ci, no));
            no++;
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

        HBox header = new HBox(12, thNo, thNama, thHarga, thQty, thSubtotal);
        header.getStyleClass().add("table-header");
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    private HBox setdatatransaksi(CartItem ci, int no) {
        // No urut
        Label lblNo = new Label(String.valueOf(no));
        lblNo.getStyleClass().add("item-no");
        lblNo.setPrefWidth(40);
        lblNo.setMinWidth(40);
        lblNo.setAlignment(Pos.CENTER);

        // Nama + kategori
        Label lblNama = new Label(ci.produk.nama);
        lblNama.getStyleClass().add("item-nama");

        Label lblKat = new Label(ci.produk.kategori);
        lblKat.getStyleClass().add("item-kategori");

        VBox namaBox = new VBox(2, lblNama, lblKat);
        namaBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(namaBox, Priority.ALWAYS);
        namaBox.setMaxWidth(Double.MAX_VALUE);

        // Harga satuan
        Label lblHarga = new Label("Rp " + FMT.format(ci.produk.harga));
        lblHarga.getStyleClass().add("item-harga");
        lblHarga.setPrefWidth(130);
        lblHarga.setMinWidth(130);
        lblHarga.setAlignment(Pos.CENTER_RIGHT);

        // Qty
        Label lblQty = new Label("x" + ci.qty);
        lblQty.getStyleClass().add("item-qty");
        lblQty.setPrefWidth(60);
        lblQty.setMinWidth(60);
        lblQty.setAlignment(Pos.CENTER);

        // Subtotal
        Label lblSub = new Label("Rp " + FMT.format(ci.produk.harga * ci.qty));
        lblSub.getStyleClass().add("item-subtotal");
        lblSub.setPrefWidth(120);
        lblSub.setMinWidth(120);
        lblSub.setAlignment(Pos.CENTER_RIGHT);

        HBox row = new HBox(12, lblNo, namaBox, lblHarga, lblQty, lblSub);
        row.getStyleClass().add("item-row");
        row.setAlignment(Pos.CENTER_LEFT);

        return new HBox(12, lblNo, namaBox, lblHarga, lblQty, lblSub);
    }

    // ── Item row ──────────────────────────────────────

    // ── Summary ───────────────────────────────────────
    private void updateSummary() {
        // TransaksiModel.subtotal = listItem.stream()
        // .mapToLong(ItemTransaksi::subtotal).sum();
        long pajak = (long) (TransaksiModel.subtotal * 0.11);
        // long total = TransaksiModel.subtotal + pajak;

        lblSubtotal.setText("Rp " + FMT.format(TransaksiModel.subtotal));
        lblPajak.setText("Rp " + FMT.format(pajak));
        lblTotal.setText("Rp " + FMT.format(TransaksiModel.total));
        Label lbel_total = new Label("Total");

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

        for (CartItem ci : TransaksiModel.keranjang.values()) {
            String sql_dtransaksi = "INSERT INTO tb_detail_transaksi (id_transaksi,id_barang,jumlah,harga) VALUES ('"
                    + id_transaksi + "','" + ci.produk.id + "','" + ci.qty + "','" + ci.produk.harga + "')";
            koneksi.eksekusiQuery(sql_dtransaksi);
        }
        String sql_update_pelanggan = "UPDATE tb_transaksi SET pelanggan = '" + pelanggan + "' WHERE id_transaksi = '"
                + id_transaksi + "'";
        koneksi.eksekusiQuery(sql_update_pelanggan);

        // TODO: simpan ke database
        System.out.println("=== SIMPAN TRANSAKSI ===");
        System.out.println("Pelanggan: " + pelanggan);
        DetailTransaksiModel.ItemTransaksi.listItem.forEach(
                item -> System.out.printf("  %s x%d = Rp %s%n", item.nama, item.qty, FMT.format(item.subtotal())));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Berhasil");
        alert.setHeaderText(null);
        alert.setContentText("✅ Transaksi berhasil disimpan!");
        alert.showAndWait();

        TransaksiModel.keranjang.clear();
        transaksiController.renderKeranjang();
        transaksiController.updateSummary();
        closeForm(btnSimpan);
    }

    @FXML
    private void onBatalTransaksi() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi");
        confirm.setHeaderText(null);
        confirm.setContentText("Yakin ingin membatalkan transaksi?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                tfPelanggan.clear();
                renderList();
                closeForm(btnBatal);
                String sql_hapus_transaksi = "DELETE FROM tb_transaksi WHERE id_transaksi = '" + id_transaksi + "'";
                koneksi.eksekusiQuery(sql_hapus_transaksi);
            }
        });
    }
}
