package scoutbase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador de la vista principal del dashboard.
 *
 * <p>Se encarga de mostrar la información del usuario autenticado
 * y de gestionar las acciones generales disponibles desde el menú.</p>
 */
public class DashboardController2 {

    /**
     * Etiqueta principal de bienvenida.
     */
    @FXML
    private Label welcomeLabel;

    /**
     * Etiqueta con el nombre y rol del usuario actual.
     */
    @FXML
    private Label userInfoLabel;

    /**
     * Inicializa la vista con los datos del usuario autenticado.
     */
    @FXML
    public void initialize() {
        loadUserInfo();
    }

    /**
     * Muestra en pantalla el nombre y el rol del usuario autenticado.
     */
    private void loadUserInfo() {
        String username = SessionManager.getUsername();
        String role = SessionManager.getRole();

        if (username == null || username.isBlank()) {
            welcomeLabel.setText("Bienvenido");
            userInfoLabel.setText("Usuario no identificado");
            return;
        }

        welcomeLabel.setText("Bienvenido, " + username);

        String cleanRole = (role != null && !role.isBlank())
                ? role.replace("ROLE_", "")
                : "SIN ROL";

        userInfoLabel.setText(username + " (" + cleanRole + ")");
    }

    /**
     * Cierra la sesión actual, limpia los datos almacenados
     * y vuelve a la pantalla de login.
     *
     * @param event evento generado al pulsar el botón de cerrar sesión
     */
    @FXML
    private void onLogoutClick(ActionEvent event) {
        System.out.println("Logout iniciado");
        System.out.println("Usuario actual: " + SessionManager.getUsername());

        SessionManager.clear();

        System.out.println("Sesión limpiada");
        System.out.println("Token después de logout: " + SessionManager.getAuthToken());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("Scoutbase - Login");
            stage.show();

            System.out.println("Redirección al login completada");
        } catch (IOException e) {
            System.err.println("Error al volver a la pantalla de login");
            e.printStackTrace();
        }
    }

    /**
     * Gestiona la acción del menú de jugadores.
     *
     * @param event evento lanzado desde la interfaz
     */
    @FXML
    private void onJugadoresClick(ActionEvent event) {
        System.out.println("Sección Jugadores pendiente de implementar");
    }

    /**
     * Gestiona la acción del menú de clubes.
     *
     * @param event evento lanzado desde la interfaz
     */
    @FXML
    private void onClubesClick(ActionEvent event) {
        System.out.println("Sección Clubes pendiente de implementar");
    }

    /**
     * Gestiona la acción del menú de informes.
     *
     * @param event evento lanzado desde la interfaz
     */
    @FXML
    private void onInformesClick(ActionEvent event) {
        System.out.println("Sección Informes pendiente de implementar");
    }

    /**
     * Gestiona la acción del menú de estadísticas.
     *
     * @param event evento lanzado desde la interfaz
     */
    @FXML
    private void onEstadisticasClick(ActionEvent event) {
        System.out.println("Sección Estadísticas pendiente de implementar");
    }

    /**
     * Gestiona la acción del menú de scouts.
     *
     * @param event evento lanzado desde la interfaz
     */
    @FXML
    private void onScoutsClick(ActionEvent event) {
        System.out.println("Sección Scouts pendiente de implementar");
    }

    /**
     * Gestiona la acción del menú de usuarios.
     *
     * @param event evento lanzado desde la interfaz
     */
    @FXML
    private void onUsuariosClick(ActionEvent event) {
        System.out.println("Sección Usuarios pendiente de implementar");
    }
}