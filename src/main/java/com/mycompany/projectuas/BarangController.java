package com.mycompany.projectuas;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mycompany.Model.BarangModel;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class BarangController implements Initializable {

    // ═══════════════════════════════════════════════════════
    // FXML — FORM
    // ═══════════════════════════════════════════════════════
    @FXML
    private TextField txtNama;
    @FXML
    private ComboBox<String> cmbKategori;
    @FXML
    private TextField txtHarga;
    @FXML
    private TextField txtStok;
    @FXML
    private TextArea txtDeskripsi;
    @FXML
    private Label lblFilePath;
    @FXML
    private TextField txtCari;

    // ═══════════════════════════════════════════════════════
    // FXML — TABEL
    // ═══════════════════════════════════════════════════════
    @FXML
    private TableView<BarangModel> tabelBarang;
    @FXML
    private TableColumn<BarangModel, Integer> colId;
    @FXML
    private TableColumn<BarangModel, String> colGambar;
    @FXML
    private TableColumn<BarangModel, String> colNama;
    @FXML
    private TableColumn<BarangModel, String> colKategori;
    @FXML
    private TableColumn<BarangModel, Integer> colHarga;
    @FXML
    private TableColumn<BarangModel, Integer> colStok;
    @FXML
    private TableColumn<BarangModel, String> colDeskripsi;
    @FXML
    private TableColumn<BarangModel, String> colStatus;

    // ═══════════════════════════════════════════════════════
    // FXML — SIDEBAR
    // ═══════════════════════════════════════════════════════
    @FXML
    private HBox navLogout;
    @FXML
    private Label navLblLogout;
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

    // ═══════════════════════════════════════════════════════
    // STATE
    // ═══════════════════════════════════════════════════════
    private boolean sidebarCollapsed = false;
    private boolean isUpdatingHarga = false;

    private static final double SIDEBAR_FULL = 220;
    private static final double SIDEBAR_MINI = 60;

    private ObservableList<BarangModel> masterData = FXCollections.observableArrayList();
    private FilteredList<BarangModel> filteredData;

    // ═══════════════════════════════════════════════════════
    // INITIALIZE
    // ═══════════════════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupNavHover();
        setupLogoutHover();
        setActiveNav(navProduk);
        setUpTable();
        selectedData();
        setupFrom();
        loadDataFromDB();
    }





    //==============================================
    //SETUP FROM
    //==============================================
    private void setupFrom(){
        cmbKategori.setItems(FXCollections.observableArrayList("Makanan", "Minuman"));

        // ── Validasi txtStok hanya angka ──
        txtStok.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*"))
                txtStok.setText(newVal.replaceAll("[^\\d]", ""));
        });

        // ── txtHarga format Rp otomatis ──
        txtHarga.textProperty().addListener((obs, oldVal, newVal) -> {
            if (isUpdatingHarga)
                return;
            isUpdatingHarga = true;
            String angkaSaja = newVal.replaceAll("[^\\d]", "");
            if (angkaSaja.isEmpty()) {
                txtHarga.setText("");
            } else {
                txtHarga.setText("Rp " + String.format("%,d", Long.parseLong(angkaSaja)).replace(',', '.'));
                Platform.runLater(() -> txtHarga.positionCaret(txtHarga.getText().length()));
            }
            isUpdatingHarga = false;
        });
    }



    //==============================================
    //SETUP TABLE
    //==============================================
    
    private void setUpTable(){
        // ── Kolom tabel ──
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        colDeskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        tabelBarang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ── Kolom harga format Rp ──
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colHarga.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null
                        : "Rp " + String.format("%,d", item).replace(',', '.'));
            }
        });

        // ── Kolom gambar ──
        colGambar.setCellValueFactory(new PropertyValueFactory<>("gambar"));
        colGambar.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.isBlank()) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                try {
                    Image img = null;
                    var stream = getClass().getResourceAsStream("/image-barang/" + item);
                    if (stream != null) {
                        img = new Image(stream);
                    } else {
                        String appData = System.getenv("APPDATA");
                        File imgFile = (appData != null && !appData.isEmpty())
                                ? new File(appData + "\\ProjectUAS\\image-barang\\" + item)
                                : new File(System.getProperty("user.home") + "/ProjectUAS/image-barang/" + item);
                        if (imgFile.exists())
                            img = new Image(imgFile.toURI().toString());
                    }
                    if (img != null) {
                        imageView.setImage(img);
                        setGraphic(imageView);
                        setText(null);
                    } else {
                        setGraphic(null);
                        setText(item);
                    }
                } catch (Exception e) {
                    setGraphic(null);
                    setText(null);
                }
            }
        });
        
        // ── Kolom status ──
        colStatus.setCellValueFactory(cellData -> {
            int stok = cellData.getValue().getStok();
            return new SimpleStringProperty(stok > 0 ? "Tersedia" : "Habis");
        });
    }


    // ========================================
    // SELECTED DATA
    //=========================================
    private void selectedData(){
        filteredData = new FilteredList<>(masterData, p -> true);
        tabelBarang.setItems(filteredData);

        // ── Listener pilih baris tabel ──
        tabelBarang.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null)
                return;
            txtNama.setText(newVal.getNama());
            cmbKategori.setValue(newVal.getKategori());
            txtStok.setText(String.valueOf(newVal.getStok()));
            txtDeskripsi.setText(newVal.getDeskripsi());
            lblFilePath.setText(newVal.getGambar() != null ? newVal.getGambar() : "Tidak ada file dipilih");

            isUpdatingHarga = true;
            txtHarga.setText("Rp " + String.format("%,d", newVal.getHarga()).replace(',', '.'));
            Platform.runLater(() -> txtHarga.positionCaret(txtHarga.getText().length()));
            isUpdatingHarga = false;
        });
    }




    // ═══════════════════════════════════════════════════════
    // HELPER — AMBIL NILAI HARGA
    // ═══════════════════════════════════════════════════════
    private int getHargaValue() {
        String angkaSaja = txtHarga.getText().replaceAll("[^\\d]", "");
        if (angkaSaja.isEmpty())
            return 0;
        try {
            return Integer.parseInt(angkaSaja);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // ── Ambil Stage aktif ──
    private Stage getStage() {
        return (Stage) txtNama.getScene().getWindow();
    }

    // ═══════════════════════════════════════════════════════
    // VALIDASI INPUT
    // ═══════════════════════════════════════════════════════
    private boolean isInputValid(boolean isUpdate, BarangModel dipilih) {
        StringBuilder pesan = new StringBuilder();

        if (txtNama.getText().trim().isEmpty())
            pesan.append("- Nama barang wajib diisi.\n");
        if (cmbKategori.getValue() == null)
            pesan.append("- Kategori wajib dipilih.\n");
        if (txtHarga.getText().trim().isEmpty() || getHargaValue() <= 0)
            pesan.append("- Harga wajib diisi dan harus lebih dari 0.\n");
        if (txtStok.getText().trim().isEmpty())
            pesan.append("- Stok wajib diisi.\n");

        boolean gambarBaru = !lblFilePath.getText().equals("Tidak ada file dipilih");
        boolean gambarLama = isUpdate && dipilih != null
                && dipilih.getGambar() != null
                && !dipilih.getGambar().isBlank();

        if (!gambarBaru && !gambarLama)
            pesan.append("- Gambar barang wajib dipilih.\n");

        if (pesan.length() > 0) {
            new Popup().showModernPopup("VALIDASI GAGAL", pesan.toString().trim(),
                    Popup.PopupType.WARNING, getStage());
            return false;
        }
        return true;
    }

    // ═══════════════════════════════════════════════════════
    // LOAD DATA DARI DATABASE
    // ═══════════════════════════════════════════════════════
    private void loadDataFromDB() {
        masterData.clear();
        String query = "SELECT id_barang, nama_barang, kategori, harga, stok, deskripsi, image_path FROM tb_barang";
        try (Connection conn = koneksi.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                masterData.add(new BarangModel(
                        rs.getInt("id_barang"),
                        rs.getString("nama_barang"),
                        rs.getString("kategori"),
                        rs.getInt("harga"),
                        rs.getInt("stok"),
                        rs.getString("deskripsi"),
                        rs.getString("image_path")));
            }
        } catch (SQLException e) {
            new Popup().showModernPopup("ERROR DATABASE", "Gagal memuat data: " + e.getMessage(),
                    Popup.PopupType.ERROR, getStage());
        }
    }

    // ═══════════════════════════════════════════════════════
    // CRUD
    // ═══════════════════════════════════════════════════════
    @FXML
    void tambahBarang(ActionEvent event) {
        if (!isInputValid(false, null))
            return;
        try {
            String nama = txtNama.getText().trim();
            String kategori = cmbKategori.getValue();
            int harga = getHargaValue();
            int stok = Integer.parseInt(txtStok.getText().trim());
            String deskripsi = txtDeskripsi.getText();
            String gambar = lblFilePath.getText();

            String query = "INSERT INTO tb_barang (nama_barang, kategori, harga, stok, deskripsi, image_path) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = koneksi.getConnection();
                    PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, nama);
                ps.setString(2, kategori);
                ps.setInt(3, harga);
                ps.setInt(4, stok);
                ps.setString(5, deskripsi);
                ps.setString(6, gambar);
                ps.executeUpdate();
            }

            loadDataFromDB();
            clearForm(null);
            new Popup().showModernPopup("BERHASIL", "Barang \"" + nama + "\" berhasil ditambahkan.",
                    Popup.PopupType.SUCCESS, getStage());

        } catch (NumberFormatException e) {
            new Popup().showModernPopup("KESALAHAN INPUT", "Stok harus berupa angka bulat!",
                    Popup.PopupType.ERROR, getStage());
        } catch (SQLException e) {
            new Popup().showModernPopup("ERROR DATABASE", "Gagal menambah data: " + e.getMessage(),
                    Popup.PopupType.ERROR, getStage());
        }
    }

    @FXML
    void ubahBarang(ActionEvent event) {
        BarangModel dipilih = tabelBarang.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            new Popup().showModernPopup("PERINGATAN", "Pilih salah satu data barang di tabel terlebih dahulu!",
                    Popup.PopupType.WARNING, getStage());
            return;
        }
        if (!isInputValid(true, dipilih))
            return;

        new Popup().showConfirmPopup("UBAH BARANG", "Apakah Anda yakin ingin mengubah data barang ini?", () -> {
            try {
                String nama = txtNama.getText().trim();
                String kategori = cmbKategori.getValue();
                int harga = getHargaValue();
                int stok = Integer.parseInt(txtStok.getText().trim());
                String deskripsi = txtDeskripsi.getText();
                String gambar = lblFilePath.getText().equals("Tidak ada file dipilih")
                        ? dipilih.getGambar()
                        : lblFilePath.getText();

                String query = "UPDATE tb_barang SET nama_barang=?, kategori=?, harga=?, stok=?, deskripsi=?, image_path=? WHERE id_barang=?";
                try (Connection conn = koneksi.getConnection();
                        PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setString(1, nama);
                    ps.setString(2, kategori);
                    ps.setInt(3, harga);
                    ps.setInt(4, stok);
                    ps.setString(5, deskripsi);
                    ps.setString(6, gambar);
                    ps.setInt(7, dipilih.getId());
                    ps.executeUpdate();
                }

                loadDataFromDB();
                clearForm(null);
                new Popup().showModernPopup("BERHASIL", "Barang \"" + nama + "\" berhasil diubah.",
                        Popup.PopupType.SUCCESS, getStage());

            } catch (NumberFormatException e) {
                new Popup().showModernPopup("KESALAHAN INPUT", "Stok harus berupa angka bulat!",
                        Popup.PopupType.ERROR, getStage());
            } catch (SQLException e) {
                new Popup().showModernPopup("ERROR DATABASE", "Gagal mengubah data: " + e.getMessage(),
                        Popup.PopupType.ERROR, getStage());
            }
        });
    }

    @FXML
    void hapusBarang(ActionEvent event) {
        BarangModel dipilih = tabelBarang.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            new Popup().showModernPopup("PERINGATAN", "Pilih data di tabel terlebih dahulu!",
                    Popup.PopupType.WARNING, getStage());
            return;
        }

        new Popup().showConfirmPopup("HAPUS BARANG", "Apakah Anda yakin ingin menghapus data barang ini?", () -> {
            try {
                String namaBarang = dipilih.getNama();
                String query = "DELETE FROM tb_barang WHERE id_barang=?";
                try (Connection conn = koneksi.getConnection();
                        PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setInt(1, dipilih.getId());
                    ps.executeUpdate();
                }

                loadDataFromDB();
                clearForm(null);
                new Popup().showModernPopup("BERHASIL", "Barang \"" + namaBarang + "\" telah dihapus.",
                        Popup.PopupType.SUCCESS, getStage());

            } catch (SQLException e) {
                new Popup().showModernPopup("ERROR DATABASE", "Gagal menghapus data: " + e.getMessage(),
                        Popup.PopupType.ERROR, getStage());
            }
        });
    }

    @FXML
    void clearForm(ActionEvent event) {
        txtNama.clear();
        cmbKategori.setValue(null);
        isUpdatingHarga = true;
        txtHarga.clear();
        isUpdatingHarga = false;
        txtStok.clear();
        txtDeskripsi.clear();
        lblFilePath.setText("Tidak ada file dipilih");
        tabelBarang.getSelectionModel().clearSelection();
    }

    @FXML
    void cariBarang(KeyEvent event) {
        String keyword = txtCari.getText().toLowerCase();
        filteredData.setPredicate(barang -> {
            if (keyword == null || keyword.isEmpty())
                return true;
            return barang.getNama().toLowerCase().contains(keyword)
                    || barang.getKategori().toLowerCase().contains(keyword);
        });
    }

    @FXML
    void onPilihFoto(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Gambar (*.png, *.jpg, *.jpeg, *.webp, *.jfif)",
                        "*.png", "*.jpg", "*.jpeg", "*.webp", "*.jfif"));

        File file = fc.showOpenDialog(getStage());
        if (file == null)
            return;

        try {
            String appData = System.getenv("APPDATA");
            File imgFolder = (appData != null && !appData.isEmpty())
                    ? new File(appData + "\\ProjectUAS\\image-barang")
                    : new File(System.getProperty("user.home") + "/ProjectUAS/image-barang");

            if (!imgFolder.exists())
                imgFolder.mkdirs();

            Path tujuan = Path.of(imgFolder.getAbsolutePath(), file.getName());
            Files.copy(file.toPath(), tujuan, StandardCopyOption.REPLACE_EXISTING);
            lblFilePath.setText(file.getName());

        } catch (IOException e) {
            new Popup().showModernPopup("ERROR", "Gagal menyalin file gambar: " + e.getMessage(),
                    Popup.PopupType.ERROR, getStage());
        }
    }

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
                navKasir, navPiutang, navLaporan, navPengaturan); // navLogout DIKELUARKAN
        for (HBox item : items) {
            item.setAlignment(collapsed ? Pos.CENTER : Pos.CENTER_LEFT);
            item.setPadding(pad);
        }

        // navLogout dihandle terpisah — hanya alignment & padding, tanpa setStyle()
        navLogout.setAlignment(collapsed ? Pos.CENTER : Pos.CENTER_LEFT);
        navLogout.setPadding(pad);
    }

    private void setActiveNav(HBox selected) {
        List<HBox> all = List.of(navDashboard, navProduk, navKasir,
                navLaporan, navPengaturan); // navLogout DIKELUARKAN
        for (HBox item : all) {
            item.getStyleClass().removeAll("nav-active");
            if (!item.getStyleClass().contains("nav-item"))
                item.getStyleClass().add("nav-item");
        }
        selected.getStyleClass().add("nav-active");
    }

    private void setupNavHover() {
        List<HBox> all = List.of(navDashboard, navProduk, navKasir,
                navLaporan, navPengaturan); // navLogout DIKELUARKAN
        for (HBox item : all) {
            item.setOnMouseEntered(e -> item.setStyle(
                    "-fx-background-color: #252840; -fx-background-radius: 10;"));
            item.setOnMouseExited(e -> item.setStyle(""));
        }
    }

    // Tambahkan method baru ini
    private void setupLogoutHover() {
        String styleNormal = "-fx-background-color: transparent;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;";

        String styleHover = "-fx-background-color: #FF5C7C26;" +
                "-fx-background-radius: 10;" +
                "-fx-cursor: hand;";

        navLogout.setStyle(styleNormal);
        navLogout.setOnMouseEntered(e -> navLogout.setStyle(styleHover));
        navLogout.setOnMouseExited(e -> navLogout.setStyle(styleNormal));
    }

    // =====================================
    // LOG OUT
    // =====================================

    @FXML
    private void onNavLogout() {
        new Popup().showConfirmPopup(
                "Konfirmasi Logout",
                "Yakin ingin keluar dari aplikasi?",
                () -> {
                    new navigation().navigateToLogin();
                    ((Stage) navLogout.getScene().getWindow()).close();
                });
    }

    @FXML
    private void onNavLogoutHover() {
        navLogout.setStyle(
                "-fx-background-color: #FF5C7C26;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 10 14 10 0;");
    }

    @FXML
    private void onNavLogoutExit() {
        navLogout.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 10 14 10 0;");
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
        new navigation().navigateToTransaksi();
        ((Stage) navKasir.getScene().getWindow()).close();
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

}