package com.mycompany.projectuas;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * LoginController Package : com.mycompany.projectuas FXML :
 * resources/fxml/login.fxml
 */
public class LoginController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField passwordVisible;
    @FXML
    private CheckBox rememberMe;
    @FXML
    private Button loginBtn;
    @FXML
    private Button guestBtn;
    @FXML
    private Button togglePasswordBtn;

    private boolean showingPassword = false;
    public String name;
    public int userId;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Sync PasswordField <-> TextField untuk show/hide password
        passwordVisible.textProperty().bindBidirectional(passwordField.textProperty());
        loginBtn.setDefaultButton(true);
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = showingPassword
                ? passwordVisible.getText()
                : passwordField.getText();
        String query = "SELECT * FROM tb_user WHERE username = '" + username + "' AND password = '" + password + "'";
        List<Object[]> result = koneksi.ambilData(query);
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Perhatian", "Username dan password tidak boleh kosong.");
            System.out.println(result.get(0)[0].toString());
        }

        // TODO: ganti dengan logika autentikasi nyata
        if (result.size() > 0) {
            // navigateToDashboard();
            navigation nav = new navigation();
            nav.navigateToDashboard();
            userId = (int) result.get(0)[0];
            name = (String) result.get(0)[1];
            System.out.println("Login berhasil untuk user ID: " + userId);
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            stage.close();
        } else {
            showAlert(Alert.AlertType.ERROR, "Gagal Masuk", "Username atau password salah.");
        }
    }

    @FXML
    private void handleGuestLogin(ActionEvent event) {
        navigation nav = new navigation();
        nav.navigateToDashboard();
        Stage stage = (Stage) guestBtn.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void togglePasswordVisibility(ActionEvent event) {
        showingPassword = !showingPassword;
        if (showingPassword) {
            passwordField.setManaged(false);
            passwordField.setVisible(false);
            passwordVisible.setManaged(true);
            passwordVisible.setVisible(true);
            togglePasswordBtn.setText("🙈");
        } else {
            passwordVisible.setManaged(false);
            passwordVisible.setVisible(false);
            passwordField.setManaged(true);
            passwordField.setVisible(true);
            togglePasswordBtn.setText("👁");
        }
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Lupa Password",
                "Silakan hubungi administrator untuk mereset password Anda.");
    }

   

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
