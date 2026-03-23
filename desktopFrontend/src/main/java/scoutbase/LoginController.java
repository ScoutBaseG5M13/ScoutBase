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

    /**
     * Campo de texto para el nombre de usuario.
     */
    @FXML
    private TextField usernameField;

    /**
     * Campo de texto para la contraseña.
     */
    @FXML
    private PasswordField passwordField;

    /**
     * Etiqueta usada para mostrar mensajes de error en pantalla.
     */
    @FXML
    private Label errorLabel;

    /**
     * Servicio encargado de gestionar la autenticación.
     */
    private final AuthService authService = new AuthService();

    /**
     * Inicializa la vista dejando vacía la zona de error.
     */
    @FXML
    public void initialize() {
        errorLabel.setText("");
    }

    /**
     * Gestiona el intento de inicio de sesión del usuario.
     *
     * <p>Comprueba que los campos no estén vacíos, envía las credenciales
     * al backend, guarda la sesión y abre el dashboard si la autenticación
     * se realiza correctamente.</p>
     *
     * @param event evento generado al pulsar el botón de login
     */
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

    /**
     * Abre la pantalla principal del dashboard tras un login correcto.
     *
     * @param event evento que origina el cambio de vista
     * @throws IOException si ocurre un error al cargar el archivo FXML
     */
    private void openDashboard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard-view.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root, 900, 600));
        stage.setTitle("Scoutbase - Dashboard");
        stage.show();
    }
}