package com.mycompany.projectuas;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class BarangController implements Initializable {

    // --- Elemen FXML Form ---
    @FXML private TextField txtNama;
    @FXML private ComboBox<String> cmbKategori;
    @FXML private TextField txtHarga;
    @FXML private TextField txtStok;
    @FXML private TextArea txtDeskripsi;
    @FXML private Label lblFilePath;
    @FXML private TextField txtCari;

    // --- Elemen FXML Tabel ---
    @FXML private TableView<BarangModel> tabelBarang;
    @FXML private TableColumn<BarangModel, Integer> colId;
    @FXML private TableColumn<BarangModel, String> colGambar;
    @FXML private TableColumn<BarangModel, String> colNama;
    @FXML private TableColumn<BarangModel, String> colKategori;
    @FXML private TableColumn<BarangModel, Integer> colHarga;
    @FXML private TableColumn<BarangModel, Integer> colStok;
    @FXML private TableColumn<BarangModel, String> colDeskripsi;
    @FXML private TableColumn<BarangModel, String> colStatus;

    // --- Elemen FXML Sidebar ---
    @FXML private VBox sidebar;
    @FXML private VBox logoBrand;
    @FXML private Button toggleBtn;
    @FXML private Label navLblDashboard;
    @FXML private Label navLblProduk;
    @FXML private Label navLblKasir;
    @FXML private Label navLblPelanggan;
    @FXML private Label navLblLaporan;
    @FXML private Label navLblPengaturan;
    @FXML private Label navLblPengaturan1;
    @FXML private VBox userInfo;

    // --- Nav HBox (untuk setActiveNav) ---
    @FXML private HBox navDashboard;
    @FXML private HBox navProduk;
    @FXML private HBox navKasir;
    @FXML private HBox navPelanggan;
    @FXML private HBox navLaporan;
    @FXML private HBox navPiutang;
    @FXML private HBox navPengaturan;

    // --- Data List ---
    private ObservableList<BarangModel> masterData = FXCollections.observableArrayList();
    private FilteredList<BarangModel> filteredData;
    private int idCounter = 4;
    private boolean isSidebarExpanded = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbKategori.setItems(FXCollections.observableArrayList(
            "Makanan", "Minuman", "Sembako", "Elektronik", "Pakaian"
        ));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colGambar.setCellValueFactory(new PropertyValueFactory<>("gambar"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        colDeskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));

        colStatus.setCellValueFactory(cellData -> {
            int stokVal = cellData.getValue().getStok();
            String statusTeks = (stokVal > 0) ? "Tersedia" : "Habis";
            return new SimpleStringProperty(statusTeks);
        });

        masterData.add(new BarangModel(1, "Indomie Goreng", "Makanan", 3500, 50, "Indomie Rasa Mi Goreng Spesial", "📦"));
        masterData.add(new BarangModel(2, "Coca Cola 390ml", "Minuman", 5000, 12, "Minuman Bersoda Segar", "📦"));
        masterData.add(new BarangModel(3, "Beras Ramos 5kg", "Sembako", 68000, 0, "Beras Putih Premium", "📦"));

        filteredData = new FilteredList<>(masterData, p -> true);
        tabelBarang.setItems(filteredData);

        tabelBarang.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNama.setText(newSelection.getNama());
                cmbKategori.setValue(newSelection.getKategori());
                txtHarga.setText(String.valueOf(newSelection.getHarga()));
                txtStok.setText(String.valueOf(newSelection.getStok()));
                txtDeskripsi.setText(newSelection.getDeskripsi());
                lblFilePath.setText(newSelection.getGambar());
            }
        });
    }

    // ═══════════════════════════════════════════
    // CRUD
    // ═══════════════════════════════════════════

    @FXML
    void tambahBarang(ActionEvent event) {
        if (txtNama.getText().isEmpty() || cmbKategori.getValue() == null
                || txtHarga.getText().isEmpty() || txtStok.getText().isEmpty()) {
            showAlert("Peringatan", "Semua kolom utama wajib diisi!");
            return;
        }
        try {
            String nama      = txtNama.getText();
            String kategori  = cmbKategori.getValue();
            int harga        = Integer.parseInt(txtHarga.getText());
            int stok         = Integer.parseInt(txtStok.getText());
            String deskripsi = txtDeskripsi.getText();
            String gambar    = lblFilePath.getText().equals("Tidak ada file dipilih") ? "📦" : lblFilePath.getText();

            masterData.add(new BarangModel(idCounter++, nama, kategori, harga, stok, deskripsi, gambar));
            clearForm(null);
        } catch (NumberFormatException e) {
            showAlert("Kesalahan Input", "Harga dan Stok harus diisi menggunakan angka bulat!");
        }
    }

    @FXML
    void ubahBarang(ActionEvent event) {
        BarangModel dipilih = tabelBarang.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            showAlert("Peringatan", "Silakan pilih salah satu data barang di tabel yang ingin diubah!");
            return;
        }
        try {
            dipilih.setNama(txtNama.getText());
            dipilih.setKategori(cmbKategori.getValue());
            dipilih.setHarga(Integer.parseInt(txtHarga.getText()));
            dipilih.setStok(Integer.parseInt(txtStok.getText()));
            dipilih.setDeskripsi(txtDeskripsi.getText());
            dipilih.setGambar(lblFilePath.getText());

            tabelBarang.refresh();
            clearForm(null);
        } catch (NumberFormatException e) {
            showAlert("Kesalahan Input", "Harga dan Stok harus diisi menggunakan angka bulat!");
        }
    }

    @FXML
    void hapusBarang(ActionEvent event) {
        BarangModel dipilih = tabelBarang.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            showAlert("Peringatan", "Silakan pilih data di tabel terlebih dahulu yang ingin dihapus!");
            return;
        }
        masterData.remove(dipilih);
        clearForm(null);
    }

    @FXML
    void clearForm(ActionEvent event) {
        txtNama.clear();
        cmbKategori.setValue(null);
        txtHarga.clear();
        txtStok.clear();
        txtDeskripsi.clear();
        lblFilePath.setText("Tidak ada file dipilih");
        tabelBarang.getSelectionModel().clearSelection();
    }

    @FXML
    void cariBarang(KeyEvent event) {
        String keyword = txtCari.getText().toLowerCase();
        filteredData.setPredicate(barang -> {
            if (keyword == null || keyword.isEmpty()) return true;
            return barang.getNama().toLowerCase().contains(keyword)
                    || barang.getKategori().toLowerCase().contains(keyword);
        });
    }

    @FXML
    void onPilihFoto(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Gambar (*.png, *.jpg)", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fc.showOpenDialog(txtNama.getScene().getWindow());
        if (file != null) {
            lblFilePath.setText(file.getName());
        }
    }

    // ═══════════════════════════════════════════
    // SIDEBAR TOGGLE
    // ═══════════════════════════════════════════

    @FXML
    void onToggleSidebar(ActionEvent event) {
        if (isSidebarExpanded) {
            sidebar.setPrefWidth(60);
            logoBrand.setVisible(false);
            userInfo.setVisible(false);
            toggleBtn.setText("▶");
            setNavLabelsVisible(false);
            isSidebarExpanded = false;
        } else {
            sidebar.setPrefWidth(220);
            logoBrand.setVisible(true);
            userInfo.setVisible(true);
            toggleBtn.setText("◀");
            setNavLabelsVisible(true);
            isSidebarExpanded = true;
        }
    }

    private void setNavLabelsVisible(boolean visible) {
        navLblDashboard.setVisible(visible);
        navLblProduk.setVisible(visible);
        navLblKasir.setVisible(visible);
        navLblPelanggan.setVisible(visible);
        navLblLaporan.setVisible(visible);
        navLblPengaturan.setVisible(visible);
        navLblPengaturan1.setVisible(visible);
    }

    // ═══════════════════════════════════════════
    // NAVIGASI SIDEBAR — ikut pola PiutangController
    // ═══════════════════════════════════════════

    @FXML
    void onNavDashboard() {
        setActiveNav(navDashboard);
        navigation nav = new navigation();
        nav.navigateToDashboard();
        Stage stage = (Stage) navDashboard.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onNavProduk() {
        setActiveNav(navProduk);
        System.out.println("ℹ️ Sudah di halaman Produk");
    }

    @FXML
    void onNavKasir() {
        setActiveNav(navKasir);
        navigation nav = new navigation();
        nav.navigateToTransaksi();
        Stage stage = (Stage) navKasir.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onNavPelanggan() {
        setActiveNav(navPelanggan);
        // tambahkan nav.navigateToPelanggan() jika sudah ada di class navigation
    }

    @FXML
    void onNavLaporan() {
        setActiveNav(navLaporan);
        navigation nav = new navigation();
        nav.navigateToLaporan();
        Stage stage = (Stage) navLaporan.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onNavPiutang() {
        setActiveNav(navPiutang);
        navigation nav = new navigation();
        nav.navigateToPiutang();
        Stage stage = (Stage) navPiutang.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onNavPengaturan() {
        setActiveNav(navPengaturan);
        // tambahkan nav.navigateToPengaturan() jika sudah ada di class navigation
    }

    private void setActiveNav(HBox selected) {
        java.util.List<HBox> all = java.util.List.of(
            navDashboard, navProduk, navKasir,
            navPelanggan, navLaporan, navPiutang, navPengaturan
        );
        for (HBox item : all) {
            item.getStyleClass().removeAll("nav-active");
            if (!item.getStyleClass().contains("nav-item")) {
                item.getStyleClass().add("nav-item");
            }
        }
        selected.getStyleClass().add("nav-active");
    }

    // ═══════════════════════════════════════════
    // HELPER
    // ═══════════════════════════════════════════

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}