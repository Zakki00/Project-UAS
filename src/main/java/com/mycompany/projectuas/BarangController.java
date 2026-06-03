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

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class BarangController implements Initializable {

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

    @FXML
    private VBox sidebar;
    @FXML
    private VBox logoBrand;
    @FXML
    private Button toggleBtn;
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
    private Label navLblPengaturan;
    @FXML
    private Label navLblPengaturan1;
    @FXML
    private VBox userInfo;

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

    private ObservableList<BarangModel> masterData = FXCollections.observableArrayList();
    private FilteredList<BarangModel> filteredData;
    private boolean isSidebarExpanded = true;
    private boolean isUpdatingHarga = false;

    // ═══════════════════════════════════════════
    // MODERN POPUP TOAST
    // ═══════════════════════════════════════════
    public enum PopupType {
        SUCCESS, ERROR, WARNING
    }

    private void showModernPopup(String title, String message, PopupType type) {
        Stage popupStage = new Stage(StageStyle.TRANSPARENT);
        popupStage.setAlwaysOnTop(true);

        // Warna berdasarkan tipe
        String bgColor, iconText, borderColor;
        switch (type) {
            case SUCCESS:
                bgColor = "#1a1a2e";
                iconText = "✓";
                borderColor = "#00d084";
                break;
            case ERROR:
                bgColor = "#1a1a2e";
                iconText = "✕";
                borderColor = "#ff4d6d";
                break;
            case WARNING:
                bgColor = "#1a1a2e";
                iconText = "⚠";
                borderColor = "#ffd60a";
                break;
            default:
                bgColor = "#1a1a2e";
                iconText = "ℹ";
                borderColor = "#4cc9f0";
        }

        // ── Icon circle ──
        Label icon = new Label(iconText);
        icon.setFont(Font.font("System", FontWeight.BOLD, 18));
        icon.setTextFill(Color.web(borderColor));
        icon.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: " + borderColor + ";" +
                        "-fx-border-radius: 50;" +
                        "-fx-background-radius: 50;" +
                        "-fx-border-width: 2;" +
                        "-fx-min-width: 36px;" +
                        "-fx-min-height: 36px;" +
                        "-fx-max-width: 36px;" +
                        "-fx-max-height: 36px;" +
                        "-fx-alignment: center;");

        // ── Teks ──
        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblTitle.setTextFill(Color.WHITE);

        Label lblMessage = new Label(message);
        lblMessage.setFont(Font.font("System", 12));
        lblMessage.setTextFill(Color.web("#a0a0b0"));
        lblMessage.setWrapText(true);
        lblMessage.setMaxWidth(220);

        VBox textBox = new VBox(3, lblTitle, lblMessage);
        textBox.setAlignment(Pos.CENTER_LEFT);

        // ── Progress bar auto-dismiss ──
        ProgressBar progressBar = new ProgressBar(1.0);
        progressBar.setPrefWidth(310);
        progressBar.setPrefHeight(3);
        progressBar.setStyle(
                "-fx-accent: " + borderColor + ";" +
                        "-fx-background-color: #2a2a3e;" +
                        "-fx-background-radius: 0;" +
                        "-fx-border-radius: 0;");

        // ── Layout utama ──
        HBox content = new HBox(14, icon, textBox);
        content.setAlignment(Pos.CENTER_LEFT);
        content.setPadding(new Insets(16, 18, 12, 18));

        VBox card = new VBox(0, content, progressBar);
        card.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: " + borderColor + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 12;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 20, 0, 0, 6);");
        card.setMinWidth(310);
        card.setMaxWidth(310);

        StackPane root = new StackPane(card);
        root.setStyle("-fx-background-color: transparent;");
        root.setPadding(new Insets(6));

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        popupStage.setScene(scene);
        popupStage.sizeToScene();

        // ── Posisi kanan bawah layar ──
        javafx.geometry.Rectangle2D screen = javafx.stage.Screen.getPrimary().getVisualBounds();
        popupStage.setX(screen.getMaxX() - 340);
        popupStage.setY(screen.getMaxY() - 110);

        popupStage.show();

        // ── Animasi fade in ──
        FadeTransition fadeIn = new FadeTransition(Duration.millis(250), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        TranslateTransition slideIn = new TranslateTransition(Duration.millis(250), root);
        slideIn.setFromX(40);
        slideIn.setToX(0);

        ParallelTransition enterAnim = new ParallelTransition(fadeIn, slideIn);
        enterAnim.play();

        // ── Progress bar countdown (3 detik) ──
        Timeline countdown = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 1.0)),
                new KeyFrame(Duration.seconds(3), new KeyValue(progressBar.progressProperty(), 0.0)));
        countdown.play();

        // ── Fade out lalu tutup ──
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), root);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);

            TranslateTransition slideOut = new TranslateTransition(Duration.millis(300), root);
            slideOut.setFromX(0);
            slideOut.setToX(40);

            ParallelTransition exitAnim = new ParallelTransition(fadeOut, slideOut);
            exitAnim.setOnFinished(ev -> popupStage.close());
            exitAnim.play();
        });
        pause.play();

        // ── Klik untuk tutup manual ──
        root.setOnMouseClicked(e -> {
            pause.stop();
            countdown.stop();
            FadeTransition fadeOut = new FadeTransition(Duration.millis(200), root);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> popupStage.close());
            fadeOut.play();
        });
    }

    // ═══════════════════════════════════════════
    // INITIALIZE
    // ═══════════════════════════════════════════
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbKategori.setItems(FXCollections.observableArrayList("Makanan", "Minuman"));

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colKategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        colDeskripsi.setCellValueFactory(new PropertyValueFactory<>("deskripsi"));
        tabelBarang.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // ── Kolom harga format Rp ──
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colHarga.setCellFactory(col -> new TableCell<BarangModel, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null)
                    setText(null);
                else
                    setText("Rp " + String.format("%,d", item).replace(',', '.'));
            }
        });

        // ── Kolom gambar ──
        colGambar.setCellValueFactory(new PropertyValueFactory<>("gambar"));
        colGambar.setCellFactory(column -> new TableCell<BarangModel, String>() {
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
                } else {
                    try {
                        var stream = getClass().getResourceAsStream("/image-barang/" + item);
                        if (stream != null) {
                            imageView.setImage(new Image(stream));
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
            }
        });

        // ── Kolom status ──
        colStatus.setCellValueFactory(cellData -> {
            int stokVal = cellData.getValue().getStok();
            return new SimpleStringProperty(stokVal > 0 ? "Tersedia" : "Habis");
        });

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
                String formatted = "Rp " + String.format("%,d", Long.parseLong(angkaSaja)).replace(',', '.');
                txtHarga.setText(formatted);
                Platform.runLater(() -> txtHarga.positionCaret(txtHarga.getText().length()));
            }
            isUpdatingHarga = false;
        });

        loadDataFromDB();

        filteredData = new FilteredList<>(masterData, p -> true);
        tabelBarang.setItems(filteredData);

        // ── Listener pilih baris tabel ──
        tabelBarang.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNama.setText(newSelection.getNama());
                cmbKategori.setValue(newSelection.getKategori());
                txtStok.setText(String.valueOf(newSelection.getStok()));
                txtDeskripsi.setText(newSelection.getDeskripsi());
                lblFilePath.setText(
                        newSelection.getGambar() != null ? newSelection.getGambar() : "Tidak ada file dipilih");

                isUpdatingHarga = true;
                txtHarga.setText("Rp " + String.format("%,d", newSelection.getHarga()).replace(',', '.'));
                Platform.runLater(() -> txtHarga.positionCaret(txtHarga.getText().length()));
                isUpdatingHarga = false;
            }
        });
    }

    // ── Ambil angka murni dari txtHarga ──
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

    // ═══════════════════════════════════════════
    // VALIDASI INPUT
    // ═══════════════════════════════════════════
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
            showModernPopup("Validasi Gagal", pesan.toString().trim(), PopupType.WARNING);
            return false;
        }
        return true;
    }

    // ═══════════════════════════════════════════
    // LOAD DATA DARI DATABASE
    // ═══════════════════════════════════════════
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
            showModernPopup("Error Database", "Gagal memuat data: " + e.getMessage(), PopupType.ERROR);
        }
    }

    // ═══════════════════════════════════════════
    // CRUD
    // ═══════════════════════════════════════════
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
            showModernPopup("Berhasil Disimpan", "Barang \"" + nama + "\" berhasil ditambahkan.", PopupType.SUCCESS);

        } catch (NumberFormatException e) {
            showModernPopup("Kesalahan Input", "Stok harus berupa angka bulat!", PopupType.ERROR);
        } catch (SQLException e) {
            showModernPopup("Error Database", "Gagal menambah data: " + e.getMessage(), PopupType.ERROR);
        }
    }

    @FXML
    void ubahBarang(ActionEvent event) {
        BarangModel dipilih = tabelBarang.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            showModernPopup("Peringatan", "Pilih salah satu data barang di tabel terlebih dahulu!", PopupType.WARNING);
            return;
        }
        if (!isInputValid(true, dipilih))
            return;

        try {
            String nama = txtNama.getText().trim();
            String kategori = cmbKategori.getValue();
            int harga = getHargaValue();
            int stok = Integer.parseInt(txtStok.getText().trim());
            String deskripsi = txtDeskripsi.getText();
            String gambar = lblFilePath.getText().equals("Tidak ada file dipilih")
                    ? dipilih.getGambar()
                    : lblFilePath.getText();

            String query = "UPDATE tb_barang SET nama_barang=?, kategori=?, harga=?, stok=?, deskripsi=?, image_path =? WHERE id_barang=?";
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
            showModernPopup("Berhasil Diperbarui", "Barang \"" + nama + "\" berhasil diubah.", PopupType.SUCCESS);

        } catch (NumberFormatException e) {
            showModernPopup("Kesalahan Input", "Stok harus berupa angka bulat!", PopupType.ERROR);
        } catch (SQLException e) {
            showModernPopup("Error Database", "Gagal mengubah data: " + e.getMessage(), PopupType.ERROR);
        }
    }

    @FXML
    void hapusBarang(ActionEvent event) {
        BarangModel dipilih = tabelBarang.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            showModernPopup("Peringatan", "Pilih data di tabel terlebih dahulu!", PopupType.WARNING);
            return;
        }
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
            showModernPopup("Berhasil Dihapus", "Barang \"" + namaBarang + "\" telah dihapus.", PopupType.SUCCESS);

        } catch (SQLException e) {
            showModernPopup("Error Database", "Gagal menghapus data: " + e.getMessage(), PopupType.ERROR);
        }
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

        File file = fc.showOpenDialog(txtNama.getScene().getWindow());
        if (file != null) {
            try {
                Path tujuan = Path.of("src/main/resources/image-barang/" + file.getName());
                Files.copy(file.toPath(), tujuan, StandardCopyOption.REPLACE_EXISTING);
                lblFilePath.setText(file.getName());
            } catch (IOException e) {
                showModernPopup("Error", "Gagal menyalin file gambar: " + e.getMessage(), PopupType.ERROR);
            }
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
    // NAVIGASI SIDEBAR
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
    }

    private void setActiveNav(HBox selected) {
        java.util.List<HBox> all = java.util.List.of(
                navDashboard, navProduk, navKasir,
                navPelanggan, navLaporan, navPiutang, navPengaturan);
        for (HBox item : all) {
            item.getStyleClass().removeAll("nav-active");
            if (!item.getStyleClass().contains("nav-item"))
                item.getStyleClass().add("nav-item");
        }
        selected.getStyleClass().add("nav-active");
    }
}