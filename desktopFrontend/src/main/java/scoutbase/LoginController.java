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
                errorLabel.setText(
                        response.getMessage() != null
                                ? response.getMessage()
                                : "No se pudo iniciar sesión"
                );
                return;
            }

            String token = authService.extractToken(response);

            UserDto user = authService.getUserByUsername(username, token);
            String role = user.getRole();

            SessionManager.saveSession(
                    token,
                    response.getSessionId(),
                    username,
                    role
            );

            System.out.println("LOGIN CORRECTO");
            System.out.println("TOKEN: " + token);
            System.out.println("SESSION ID: " + response.getSessionId());
            System.out.println("USERNAME: " + username);
            System.out.println("ROLE: " + role);

            openDashboard(event);

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error al iniciar sesión: " + e.getMessage());
        }
    }

    private void openDashboard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 900, 600));
        stage.setTitle("Scoutbase - Dashboard");
        stage.show();
    }
}