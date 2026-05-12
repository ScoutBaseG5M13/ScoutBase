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
import scoutbase.app.SessionManager;
import scoutbase.common.ApiResponse;
import scoutbase.user.UserDto;

import java.io.IOException;

/**
 * Controlador de la pantalla de inicio de sesión de ScoutBase Desktop.
 *
 * <p>Gestiona la interacción del usuario con la vista de login,
 * validando los datos introducidos, realizando la autenticación contra
 * el backend y redirigiendo al dashboard principal en caso de éxito.</p>
 *
 * <p>El proceso de autenticación incluye:</p>
 * <ul>
 *     <li>Validación básica de usuario y contraseña.</li>
 *     <li>Envío de credenciales al endpoint de login.</li>
 *     <li>Extracción del token JWT devuelto por la API.</li>
 *     <li>Almacenamiento temporal de la sesión mediante {@link SessionManager}.</li>
 *     <li>Obtención de los datos del usuario autenticado.</li>
 *     <li>Resolución del rol global visible del usuario.</li>
 *     <li>Carga de la pantalla principal de la aplicación.</li>
 * </ul>
 */
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    /**
     * Servicio encargado de realizar las operaciones de autenticación
     * contra la API REST del backend.
     */
    private final AuthService authService = new AuthService();

    /**
     * Inicializa el controlador tras la carga del archivo FXML.
     *
     * <p>Limpia cualquier mensaje de error previo para garantizar
     * que la pantalla se muestre en un estado inicial correcto.</p>
     */
    @FXML
    public void initialize() {
        errorLabel.setText("");
    }

    /**
     * Gestiona el evento de clic sobre el botón de inicio de sesión.
     *
     * <p>Valida los campos del formulario, realiza la autenticación
     * contra el backend y, si la respuesta es correcta, guarda los datos
     * de sesión necesarios para las peticiones posteriores.</p>
     *
     * @param event evento de acción generado por el botón de login
     */
    @FXML
    private void onLoginButtonClick(ActionEvent event) {
        errorLabel.setText("");

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {
            errorLabel.setText("Introduce usuario y contraseña");
            return;
        }

        try {
            ApiResponse response = authService.login(username.trim(), password);

            if (!response.isSuccess()) {
                errorLabel.setText("Usuario o contraseña incorrectos");
                return;
            }

            String token = authService.extractToken(response);

            if (token == null || token.isBlank()) {
                errorLabel.setText("No se pudo obtener el token de autenticación");
                return;
            }

            /*
             * Se guarda primero la sesión con el token para que ApiClient
             * pueda adjuntarlo al consultar /users/me.
             */
            SessionManager.saveSession(
                    token,
                    response.getSessionId(),
                    username.trim(),
                    null
            );

            UserDto currentUser = authService.getCurrentUser();

            String resolvedRole = resolveSessionRole(currentUser);

            SessionManager.saveSession(
                    token,
                    response.getSessionId(),
                    currentUser.getUsername(),
                    resolvedRole
            );

            openDashboard(event);

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Usuario o contraseña incorrectos");
        }
    }

    /**
     * Resuelve el rol global que debe guardarse en sesión.
     *
     * <p>En la estructura actual del backend, el usuario puede tener permisos
     * globales de superadministrador mediante el campo {@code superAdmin}.
     * El resto de roles pueden depender del UserClub/UserTeam seleccionado,
     * por lo que no siempre vienen directamente en {@link UserDto#getRole()}.</p>
     *
     * @param user usuario autenticado devuelto por el backend
     * @return rol global para guardar en sesión
     */
    private String resolveSessionRole(UserDto user) {
        if (user == null) {
            return null;
        }

        if (user.isSuperAdmin()) {
            return "SUPERADMIN";
        }

        if (user.getRole() != null && !user.getRole().isBlank()) {
            return user.getRole().replace("ROLE_", "");
        }

        return null;
    }

    /**
     * Carga y muestra la pantalla principal de la aplicación.
     *
     * <p>Este método sustituye la escena actual por la vista del dashboard
     * y aplica la hoja de estilos principal de la aplicación.</p>
     *
     * @param event evento de acción utilizado para obtener la ventana actual
     * @throws IOException si ocurre un error al cargar el archivo FXML
     */
    private void openDashboard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/scoutbase/dashboard-view.fxml")
        );

        Parent root = loader.load();

        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(
                getClass().getResource("/scoutbase/dark_theme.css").toExternalForm()
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("ScoutBase - Dashboard");
        stage.show();
    }
}