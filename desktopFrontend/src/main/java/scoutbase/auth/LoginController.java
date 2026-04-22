package scoutbase.auth;

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
import scoutbase.common.ApiResponse;
import scoutbase.app.SessionManager;

import java.io.IOException;

/**
 * Controlador de la pantalla de inicio de sesión.
 *
 * <p>Gestiona la interacción del usuario con la vista de login,
 * validando los datos introducidos, realizando la autenticación
 * contra el backend y redirigiendo al dashboard en caso de éxito.</p>
 *
 * <p>También se encarga de mostrar mensajes de error cuando las
 * credenciales no son válidas o ocurre algún problema durante el proceso.</p>
 */
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    /**
     * Servicio encargado de realizar las operaciones de autenticación.
     */
    private final AuthService authService = new AuthService();

    /**
     * Método de inicialización del controlador.
     *
     * <p>Se ejecuta automáticamente al cargar el FXML y limpia
     * cualquier mensaje de error previo.</p>
     */
    @FXML
    public void initialize() {
        errorLabel.setText("");
    }

    /**
     * Maneja el evento de clic sobre el botón de login.
     *
     * <p>Valida los campos introducidos, realiza la petición de login
     * al backend y, si la autenticación es correcta, guarda la sesión
     * y abre la pantalla principal de la aplicación.</p>
     *
     * @param event evento de acción generado por el botón de login
     */
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

    /**
     * Carga y muestra la pantalla principal (dashboard) tras un login exitoso.
     *
     * <p>Reemplaza la escena actual por la vista del dashboard,
     * aplicando la hoja de estilos correspondiente.</p>
     *
     * @param event evento de acción que permite obtener el escenario actual
     * @throws IOException si ocurre un error al cargar el archivo FXML
     */
    private void openDashboard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scoutbase/dashboard-view.fxml"));
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