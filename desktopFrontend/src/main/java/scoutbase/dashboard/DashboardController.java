package scoutbase.dashboard;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import scoutbase.app.SessionManager;
import scoutbase.club.ClubsController;
import scoutbase.userClub.UserClubDTO;
import scoutbase.userClub.UserClubService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de la vista principal del dashboard.
 *
 * <p>Gestiona la pantalla principal de ScoutBase Desktop una vez que el usuario
 * ha iniciado sesión, mostrando información contextual, accesos rápidos y
 * navegación hacia las distintas secciones funcionales.</p>
 *
 * <p>También permite alternar entre tema oscuro y claro, cargar vistas internas
 * dentro del contenedor principal y resolver el UserClub activo para las pantallas
 * que dependen de este contexto.</p>
 */
public class DashboardController {

    /**
     * Ruta de la hoja de estilos del tema oscuro.
     */
    private static final String DARK_THEME = "/scoutbase/dark_theme.css";

    /**
     * Ruta de la hoja de estilos del tema claro.
     */
    private static final String LIGHT_THEME = "/scoutbase/light_theme.css";

    /**
     * Indica si el tema oscuro está activo actualmente.
     */
    private boolean darkThemeActive = true;

    @FXML
    private Label userInfoLabel;

    @FXML
    private Label appTitleLabel;

    @FXML
    private Button themeToggleButton;

    @FXML
    private Button jugadoresButton;

    @FXML
    private Button clubesButton;

    @FXML
    private Button estadisticasButton;

    @FXML
    private Button scoutsButton;

    @FXML
    private Button usuariosButton;

    @FXML
    private VBox contentContainer;

    /**
     * Servicio encargado de obtener los UserClubs disponibles para el usuario autenticado.
     */
    private final UserClubService userClubService = new UserClubService();

    /**
     * Inicializa la vista del dashboard con los datos de sesión actuales.
     */
    @FXML
    public void initialize() {
        loadUserInfo();
        configureDashboardMenu();
        themeToggleButton.setText("Modo claro");
        loadHome();
    }

    /**
     * Muestra en pantalla el usuario autenticado y su rol global si existe.
     */
    private void loadUserInfo() {
        String username = SessionManager.getUsername();
        String role = SessionManager.getRole();

        if (username == null || username.isBlank()) {
            userInfoLabel.setText("Usuario no identificado");
            return;
        }

        if (role != null && !role.isBlank()) {
            userInfoLabel.setText(username + " (" + role.replace("ROLE_", "") + ")");
        } else {
            userInfoLabel.setText(username);
        }
    }

    /**
     * Configura la visibilidad común de los botones principales del dashboard.
     *
     * <p>No se ocultan secciones por rol global en frontend, ya que los permisos
     * reales dependen de la jerarquía del sistema: SUPERADMIN, administradores
     * de club y roles contextuales dentro de clubes o equipos.</p>
     */
    private void configureDashboardMenu() {
        setButtonVisible(jugadoresButton, true);
        setButtonVisible(clubesButton, true);
        setButtonVisible(estadisticasButton, true);
        setButtonVisible(scoutsButton, true);
        setButtonVisible(usuariosButton, true);
    }

    /**
     * Muestra u oculta un botón del menú.
     *
     * @param button botón a modificar
     * @param visible indica si debe mostrarse
     */
    private void setButtonVisible(Button button, boolean visible) {
        button.setVisible(visible);
        button.setManaged(visible);
    }

    /**
     * Marca visualmente el botón activo del menú.
     *
     * @param activeButton botón activo
     */
    private void setActiveButton(Button activeButton) {
        for (Button button : getMenuButtons()) {
            button.getStyleClass().remove("menu-button-active");
        }

        if (activeButton != null && activeButton.isVisible()) {
            activeButton.getStyleClass().add("menu-button-active");
        }
    }

    /**
     * Devuelve los botones principales del menú.
     *
     * @return lista de botones de navegación
     */
    private List<Button> getMenuButtons() {
        List<Button> buttons = new ArrayList<>();
        buttons.add(jugadoresButton);
        buttons.add(clubesButton);
        buttons.add(estadisticasButton);
        buttons.add(scoutsButton);
        buttons.add(usuariosButton);
        return buttons;
    }

    /**
     * Carga la pantalla inicial del dashboard.
     */
    private void loadHome() {
        contentContainer.getChildren().clear();

        String username = SessionManager.getUsername() != null
                ? SessionManager.getUsername()
                : "usuario";

        String role = SessionManager.getRole() != null && !SessionManager.getRole().isBlank()
                ? SessionManager.getRole().replace("ROLE_", "")
                : "SIN ROL GLOBAL";

        Label title = new Label("Panel principal");
        title.getStyleClass().add("dashboard-title");

        Label subtitle = new Label("Bienvenido, " + username);
        subtitle.getStyleClass().add("section-subtitle");

        HBox cardsRow = new HBox(15);
        cardsRow.getChildren().addAll(
                createStatCard("Usuario", username),
                createStatCard("Rol global", role),
                createStatCard("Sesión", SessionManager.isLoggedIn() ? "Activa" : "Inactiva")
        );

        VBox infoBox = createInfoBox(
                "Estado del sistema",
                "• Sesión iniciada correctamente\n" +
                        "• Token JWT cargado en sesión\n" +
                        "• API configurada con endpoints /api/v1\n" +
                        "• UserClub cargado automáticamente para la vista de clubes\n" +
                        "• Los permisos específicos dependen del club/equipo seleccionado"
        );

        contentContainer.getChildren().addAll(title, subtitle, cardsRow, infoBox);
    }

    /**
     * Vuelve a la pantalla principal del dashboard.
     */
    @FXML
    private void onInicioClick() {
        setActiveButton(null);
        loadHome();
    }

    /**
     * Alterna entre tema oscuro y tema claro.
     *
     * @param event evento generado al pulsar el botón de cambio de tema
     */
    @FXML
    private void onToggleThemeClick(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        scene.getStylesheets().clear();

        if (darkThemeActive) {
            scene.getStylesheets().add(getClass().getResource(LIGHT_THEME).toExternalForm());
            darkThemeActive = false;
            themeToggleButton.setText("Modo oscuro");
        } else {
            scene.getStylesheets().add(getClass().getResource(DARK_THEME).toExternalForm());
            darkThemeActive = true;
            themeToggleButton.setText("Modo claro");
        }
    }

    /**
     * Cierra la sesión actual y vuelve a la pantalla de login.
     *
     * @param event evento generado al pulsar el botón de cerrar sesión
     */
    @FXML
    private void onLogoutClick(ActionEvent event) {
        SessionManager.clear();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scoutbase/login-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(getClass().getResource(DARK_THEME).toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("ScoutBase - Login");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carga la vista de jugadores.
     *
     * @param event evento generado al pulsar la opción de jugadores
     */
    @FXML
    private void onJugadoresClick(ActionEvent event) {
        setActiveButton(jugadoresButton);
        loadFXML("/scoutbase/players-view.fxml", "Gestión de jugadores");
    }

    /**
     * Carga la vista de clubes.
     *
     * @param event evento generado al pulsar la opción de clubes
     */
    @FXML
    private void onClubesClick(ActionEvent event) {
        setActiveButton(clubesButton);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scoutbase/clubs-view.fxml"));
            Parent clubsView = loader.load();

            ClubsController controller = loader.getController();

            String userClubId = resolveDefaultUserClubId();
            if (userClubId != null) {
                controller.setSelectedUserClubId(userClubId);
            }

            contentContainer.getChildren().clear();
            contentContainer.getChildren().add(clubsView);

        } catch (IOException e) {
            e.printStackTrace();
            loadPlaceholderSection("Gestión de clubes", "No se pudo cargar la vista de clubes.");
        }
    }

    /**
     * Muestra la sección de estadísticas.
     *
     * @param event evento generado al pulsar la opción de estadísticas
     */
    @FXML
    private void onEstadisticasClick(ActionEvent event) {
        setActiveButton(estadisticasButton);
        loadFXML("/scoutbase/stats-view.fxml", "Estadísticas");
    }

    /**
     * Carga la vista de scouts.
     *
     * @param event evento generado al pulsar la opción de scouts
     */
    @FXML
    private void onScoutsClick(ActionEvent event) {
        setActiveButton(scoutsButton);
        loadFXML("/scoutbase/scouts-view.fxml", "Gestión de scouts");
    }

    /**
     * Carga la vista de usuarios.
     *
     * @param event evento generado al pulsar la opción de usuarios
     */
    @FXML
    private void onUsuariosClick(ActionEvent event) {
        setActiveButton(usuariosButton);
        loadFXML("/scoutbase/users-view.fxml", "Gestión de usuarios");
    }

    /**
     * Carga un archivo FXML dentro del contenedor central del dashboard.
     *
     * @param fxmlPath ruta del archivo FXML
     * @param fallbackTitle título alternativo si falla la carga
     */
    private void loadFXML(String fxmlPath, String fallbackTitle) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();

            contentContainer.getChildren().clear();
            contentContainer.getChildren().add(view);

        } catch (IOException e) {
            e.printStackTrace();
            loadPlaceholderSection(fallbackTitle, "No se pudo cargar la vista solicitada.");
        }
    }

    /**
     * Resuelve el UserClub por defecto del usuario autenticado.
     *
     * @return identificador UUID del UserClub, o {@code null} si no existe ninguno
     */
    private String resolveDefaultUserClubId() {
        try {
            UserClubDTO defaultUserClub = userClubService.getDefaultUserClub();

            if (defaultUserClub == null
                    || defaultUserClub.getId() == null
                    || defaultUserClub.getId().isBlank()) {
                return null;
            }

            return defaultUserClub.getId();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Crea una tarjeta estadística reutilizable.
     *
     * @param titleText título de la tarjeta
     * @param valueText valor mostrado
     * @return tarjeta visual
     */
    private VBox createStatCard(String titleText, String valueText) {
        Label title = new Label(titleText);
        title.setWrapText(true);
        title.getStyleClass().add("card-title");

        Label value = new Label(valueText);
        value.getStyleClass().add("card-value");

        VBox card = new VBox(10, title, value);
        card.setPrefWidth(180);
        card.setMinHeight(110);
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("dashboard-card");

        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    /**
     * Crea un bloque informativo reutilizable.
     *
     * @param titleText título del bloque
     * @param contentText contenido del bloque
     * @return bloque visual
     */
    private VBox createInfoBox(String titleText, String contentText) {
        Label title = new Label(titleText);
        title.getStyleClass().add("panel-title");

        Label content = new Label(contentText);
        content.setWrapText(true);
        content.getStyleClass().add("panel-text");

        VBox box = new VBox(10, title, content);
        box.setPrefWidth(500);
        box.setMinHeight(180);
        box.getStyleClass().add("info-box");

        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    /**
     * Carga una sección provisional en el contenedor central.
     *
     * @param titleText título de la sección
     * @param descriptionText descripción de la sección
     */
    private void loadPlaceholderSection(String titleText, String descriptionText) {
        contentContainer.getChildren().clear();

        Label title = new Label(titleText);
        title.getStyleClass().add("dashboard-title");

        Label description = new Label(descriptionText);
        description.setWrapText(true);
        description.getStyleClass().add("section-subtitle");

        contentContainer.getChildren().addAll(title, description);
    }
}