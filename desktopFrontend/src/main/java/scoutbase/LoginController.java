package scoutbase;

import scoutbase.MainApp;
import scoutbase.AuthService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMessage;

    private final AuthService authService = new AuthService();

    @FXML
    public void onLoginClick(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("Debes rellenar usuario y contraseña.");
            return;
        }

        boolean loginCorrecto = authService.login(username, password);

        if (loginCorrecto) {
            lblMessage.setStyle("-fx-text-fill: green;");
            lblMessage.setText("Login correcto.");

            abrirDashboard();
            cerrarVentanaActual();
        } else {
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("Usuario o contraseña incorrectos.");
        }
    }

    private void abrirDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    MainApp.class.getResource("/com/scoutbase/dashboard-view.fxml")
            );

            Scene scene = new Scene(loader.load(), 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Scoutbase - Panel principal");
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setStyle("-fx-text-fill: red;");
            lblMessage.setText("Error al abrir la siguiente ventana.");
        }
    }

    private void cerrarVentanaActual() {
        Stage stage = (Stage) txtUsername.getScene().getWindow();
        stage.close();
    }
}