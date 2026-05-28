package com.mycompany.projectuas;

import java.io.File;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

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
    @FXML private Label navLblDashboard, navLblProduk, navLblKasir, navLblPelanggan, navLblLaporan, navLblPengaturan, navLblPengaturan1;
    @FXML private VBox userInfo;

    // --- Data List ---
    private ObservableList<BarangModel> masterData = FXCollections.observableArrayList();
    private FilteredList<BarangModel> filteredData;
    private int idCounter = 4; 
    private boolean isSidebarExpanded = true;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Pilihan Kategori di ComboBox
        cmbKategori.setItems(FXCollections.observableArrayList("Makanan", "Minuman", "Sembako", "Elektronik", "Pakaian"));

        // 2. Hubungkan kolom tabel ke Property BarangModel kamu
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colGambar.setCellValueFactory(new PropertyValueFactory<>("gambar"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        colDeskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        
        // Trik cerdik: Membuat kolom status membaca jumlah stok secara realtime
        colStatus.setCellValueFactory(cellData -> {
            int stokVal = cellData.getValue().getStok();
            String statusTeks = (stokVal > 0) ? "Tersedia" : "Habis";
            return new SimpleStringProperty(statusTeks);
        });

        // 3. Masukkan Data Dummy Sesuai Urutan Constructor BarangModel Kamu
        // Format: (id, nama, kategori, harga, stok, deskripsi, gambar)
        masterData.add(new BarangModel(1, "Indomie Goreng", "Makanan", 3500, 50, "Indomie Rasa Mi Goreng Spesial", "📦"));
        masterData.add(new BarangModel(2, "Coca Cola 390ml", "Minuman", 5000, 12, "Minuman Bersoda Segar", "📦"));
        masterData.add(new BarangModel(3, "Beras Ramos 5kg", "Sembako", 68000, 0, "Beras Putih Premium", "📦"));

        // 4. Set FilteredList untuk Fitur Pencarian
        filteredData = new FilteredList<>(masterData, p -> true);
        tabelBarang.setItems(filteredData);

        // 5. Saat baris tabel diklik, lempar datanya kembali ke Form Input
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

    // --- AKSI CRUD (Tambah, Ubah, Hapus) ---

    @FXML
    void tambahBarang(ActionEvent event) {
        if (txtNama.getText().isEmpty() || cmbKategori.getValue() == null || txtHarga.getText().isEmpty() || txtStok.getText().isEmpty()) {
            showAlert("Peringatan", "Semua kolom utama wajib diisi!");
            return;
        }
        try {
            String nama = txtNama.getText();
            String kategori = cmbKategori.getValue();
            int harga = Integer.parseInt(txtHarga.getText());
            int stok = Integer.parseInt(txtStok.getText());
            String deskripsi = txtDeskripsi.getText();
            String gambar = lblFilePath.getText().equals("Tidak ada file dipilih") ? "📦" : lblFilePath.getText();

            // Menggunakan urutan constructor asli miliki kamu
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
            return barang.getNama().toLowerCase().contains(keyword) || 
                   barang.getKategori().toLowerCase().contains(keyword);
        });
    }

    @FXML
    void onPilihFoto(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Gambar (*.png, *.jpg)", "*.png", "*.jpg", "*.jpeg"));
        File file = fc.showOpenDialog(txtNama.getScene().getWindow());
        if (file != null) {
            lblFilePath.setText(file.getName());
        }
    }

    // --- ANIMASI SIDEBAR ---
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // --- Metode Navigasi Kosong ---
    @FXML void onNavDashboard(MouseEvent event) {}
    @FXML void onNavKasir(MouseEvent event) {}
    @FXML void onNavLaporan(MouseEvent event) {}
    @FXML void onNavPelanggan(MouseEvent event) {}
    @FXML void onNavPengaturan(MouseEvent event) {}
    @FXML void onNavPiutang(MouseEvent event) {}
    @FXML void onNavProduk(MouseEvent event) {}
}