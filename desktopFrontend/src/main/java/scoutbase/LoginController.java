package scoutbase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador de la pantalla de inicio de sesión.
 *
 * <p>Se encarga de validar los datos introducidos por el usuario,
 * realizar la autenticación contra el backend y abrir el dashboard
 * cuando el login es correcto.</p>
 */
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final AuthService authService = new AuthService();

    @FXML
    public void initialize() {
        errorLabel.setText("");
    }

    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        errorLabel.setText("");

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isBlank() ||
                password == null || password.isBlank()) {
            errorLabel.setText("Introduce usuario y contraseña");
            return;
        }

        try {
            ApiResponse response = authService.login(username, password);

            if (!response.isSuccess()) {
                errorLabel.setText("Usuario o contraseña incorrectos");
                return;
            }

            String token = authService.extractToken(response);

            if (token == null || token.isBlank()) {
                errorLabel.setText("No se pudo obtener el token de autenticación");
                return;
            }

            SessionManager.saveSession(
                    token,
                    response.getSessionId(),
                    username,
                    null
            );

            System.out.println("LOGIN CORRECTO");
            System.out.println("TOKEN: " + token);
            System.out.println("SESSION ID: " + response.getSessionId());
            System.out.println("USERNAME: " + username);

            openDashboard(event);

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Usuario o contraseña incorrectos");
        }
    }

    private void openDashboard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-view.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(
                getClass().getResource("/scoutbase/dark_theme.css").toExternalForm()
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Scoutbase - Dashboard");
        stage.show();
    }
}