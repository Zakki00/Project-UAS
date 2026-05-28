package com.mycompany.projectuas;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    session session = new session();

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
    Preferences prefs = Preferences.userNodeForPackage(LoginController.class);


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Sync PasswordField <-> TextField untuk show/hide password
        passwordVisible.textProperty().bindBidirectional(passwordField.textProperty());
        loginBtn.setDefaultButton(true);
        loadRememberedCredentials();
    }

    @FXML
    private void handleLogin(ActionEvent event) {

        String username = usernameField.getText().trim();
        String password = showingPassword ? passwordVisible.getText() : passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Perhatian", "Username dan password tidak boleh kosong.");
            return;
        }

        String query = "SELECT * FROM tb_user WHERE username = '" + username + "' AND password = '" + password + "'";
        List<Object[]> result = koneksi.ambilData(query);

        if (result.size() > 0) {

            session.id_user = (int) result.get(0)[0];
            session.username = (String) result.get(0)[1];

            if (rememberMe.isSelected()) {
                prefs.put("username", username);
                prefs.put("password", password);
                prefs.putBoolean("remember", true);
                System.out.println("Memory saved: " + username + " / " + password);
            } else {
                prefs.remove("username");
                prefs.remove("password");
                prefs.putBoolean("remember", false);
            }

            navigation nav = new navigation();
            nav.navigateToDashboard();

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

    public void loadRememberedCredentials() {
        if (prefs.getBoolean("remember", false)) {
            usernameField.setText(prefs.get("username", ""));
            passwordField.setText(prefs.get("password", ""));
            rememberMe.setSelected(true);
        }
    }
}
