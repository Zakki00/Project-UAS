package com.mycompany.projectuas;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


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
    public void navigateToTransaksi() {
        navigateTo("transaksi.fxml", "Transaksi");  
    }
    public void navigateToLaporan() {
        navigateTo("laporan.fxml", "Laporan");
    }
    
}
