package com.mycompany.projectuas;

import java.awt.Toolkit;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class navigation {

    public void navigateTo(String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/" + fxmlPath));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setMaximized(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void navigateToDashboard() {
        navigateTo("dashboard.fxml", "Dashboard");
    }

    public void navigateToTransaksi() {
        navigateTo("transaksi.fxml", "Transaksi");
    }

    public void navigateToProduk() {
        navigateTo("produk.fxml", "Produk");
    }

    public void navigateToLaporan() {
        navigateTo("laporan.fxml", "Laporan");
    }

    public void navigateToPiutang() {
        navigateTo("piutang.fxml", "Piutang");
    }

    public void navigateToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root, 1080, 900));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void detailTransaksi(Stage ownerStage, TransaksiController transaksiController) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/detail_transaksi.fxml"));
            Parent root = loader.load();
            DetailTransaksiController controller = loader.getController();
            controller.setTransaksiController(transaksiController);
            Stage stage = new Stage();
            stage.setTitle("Detail Transaksi");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(ownerStage);
            stage.setOnCloseRequest(event -> {
                event.consume();
                shakeStage(stage);
            });

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shakeStage(Stage stage) {
        // ── Suara Windows ──
        Toolkit.getDefaultToolkit().beep();

        // ── Animasi getar pakai setX langsung ──
        double originalX = stage.getX();

        Timeline shake = new Timeline(new KeyFrame(Duration.millis(0), e -> stage.setX(originalX)),
                new KeyFrame(Duration.millis(50), e -> stage.setX(originalX + 10)),
                new KeyFrame(Duration.millis(100), e -> stage.setX(originalX - 10)),
                new KeyFrame(Duration.millis(150), e -> stage.setX(originalX + 8)),
                new KeyFrame(Duration.millis(200), e -> stage.setX(originalX - 8)),
                new KeyFrame(Duration.millis(250), e -> stage.setX(originalX + 5)),
                new KeyFrame(Duration.millis(300), e -> stage.setX(originalX - 5)),
                new KeyFrame(Duration.millis(350), e -> stage.setX(originalX)));

        shake.play();
    }

}
