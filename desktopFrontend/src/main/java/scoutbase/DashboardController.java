package scoutbase;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de la vista principal del dashboard.
 *
 * <p>Se encarga de mostrar la información del usuario autenticado
 * y de gestionar las acciones generales disponibles desde el menú.</p>
 */
public class DashboardController {

    private static final String DARK_THEME = "/scoutbase/dark_theme.css";
    private static final String LIGHT_THEME = "/scoutbase/light_theme.css";

    private boolean darkThemeActive = true;

    @FXML
    private Label appTitleLabel;

    @FXML
    private Label userInfoLabel;

    @FXML
    private Button themeToggleButton;

    @FXML
    private Button jugadoresButton;

    @FXML
    private Button clubesButton;

    @FXML
    private Button informesButton;

    @FXML
    private Button estadisticasButton;

    @FXML
    private Button scoutsButton;

    @FXML
    private Button usuariosButton;

    @FXML
    private VBox contentContainer;

    /**
     * Inicializa la vista con los datos del usuario autenticado.
     */
    @FXML
    public void initialize() {
        loadUserInfo();
        configureDashboardByRole();
        themeToggleButton.setText("Modo claro");
    }

    /**
     * Muestra en pantalla el nombre y el rol del usuario autenticado.
     */
    private void loadUserInfo() {
        String username = SessionManager.getUsername();
        String role = SessionManager.getRole();

        if (username == null || username.isBlank()) {
            userInfoLabel.setText("Usuario no identificado");
            return;
        }

        if (role != null && !role.isBlank()) {
            String cleanRole = role.replace("ROLE_", "");
            userInfoLabel.setText(username + " (" + cleanRole + ")");
        } else {
            userInfoLabel.setText(username);
        }
    }

    /**
     * Configura el dashboard según el rol del usuario autenticado.
     */
    private void configureDashboardByRole() {
        String role = SessionManager.getRole();

        if (isAdmin(role)) {
            configureAdminMenu();
            loadAdminHome();
        } else {
            configureUserMenu();
            loadUserHome();
        }
    }

    /**
     * Comprueba si el rol corresponde a un administrador.
     *
     * @param role rol del usuario autenticado
     * @return true si es administrador; false en caso contrario
     */
    private boolean isAdmin(String role) {
        if (role == null || role.isBlank()) {
            return false;
        }

        String cleanRole = role.replace("ROLE_", "").trim().toUpperCase();
        return cleanRole.equals("ADMIN");
    }

    /**
     * Configura la visibilidad del menú para administradores.
     */
    private void configureAdminMenu() {
        setButtonVisible(jugadoresButton, true);
        setButtonVisible(clubesButton, true);
        setButtonVisible(informesButton, true);
        setButtonVisible(estadisticasButton, true);
        setButtonVisible(scoutsButton, true);
        setButtonVisible(usuariosButton, true);
    }

    /**
     * Configura la visibilidad del menú para usuarios normales.
     */
    private void configureUserMenu() {
        setButtonVisible(jugadoresButton, true);
        setButtonVisible(clubesButton, true);
        setButtonVisible(informesButton, true);
        setButtonVisible(estadisticasButton, true);
        setButtonVisible(scoutsButton, true);
        setButtonVisible(usuariosButton, true);
    }

    /**
     * Muestra u oculta un botón y evita que deje hueco visual cuando está oculto.
     *
     * @param button botón a modificar
     * @param visible indica si debe mostrarse o no
     */
    private void setButtonVisible(Button button, boolean visible) {
        button.setVisible(visible);
        button.setManaged(visible);
    }

    /**
     * Marca visualmente el botón activo del menú.
     *
     * @param activeButton botón que debe quedar marcado
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
     * Devuelve la lista de botones de navegación actualmente disponibles.
     *
     * @return lista de botones del menú
     */
    private List<Button> getMenuButtons() {
        List<Button> buttons = new ArrayList<>();
        buttons.add(jugadoresButton);
        buttons.add(clubesButton);
        buttons.add(informesButton);

        if (estadisticasButton.isManaged()) {
            buttons.add(estadisticasButton);
        }
        if (scoutsButton.isManaged()) {
            buttons.add(scoutsButton);
        }
        if (usuariosButton.isManaged()) {
            buttons.add(usuariosButton);
        }

        return buttons;
    }

    /**
     * Vuelve a la pantalla principal correspondiente al rol del usuario autenticado.
     */
    @FXML
    private void onInicioClick() {
        setActiveButton(null);

        if (isAdmin(SessionManager.getRole())) {
            loadAdminHome();
        } else {
            loadUserHome();
        }
    }

    /**
     * Alterna entre el tema oscuro y el tema claro.
     *
     * @param event evento generado al pulsar el botón de cambio de tema
     */
    @FXML
    private void onToggleThemeClick(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        scene.getStylesheets().clear();

        if (darkThemeActive) {
            scene.getStylesheets().add(
                    getClass().getResource(LIGHT_THEME).toExternalForm()
            );
            darkThemeActive = false;
            themeToggleButton.setText("Modo oscuro");
        } else {
            scene.getStylesheets().add(
                    getClass().getResource(DARK_THEME).toExternalForm()
            );
            darkThemeActive = true;
            themeToggleButton.setText("Modo claro");
        }
    }

    /**
     * Carga la pantalla principal para administradores.
     */
    private void loadAdminHome() {
        contentContainer.getChildren().clear();

        Label title = new Label("Panel de administración");
        title.getStyleClass().add("dashboard-title");

        Label subtitle = new Label("Resumen general del sistema");
        subtitle.getStyleClass().add("section-subtitle");

        HBox cardsRow = new HBox(15);
        cardsRow.getChildren().addAll(
                createStatCard("Usuarios registrados", "18"),
                createStatCard("Jugadores activos", "245"),
                createStatCard("Clubes registrados", "12"),
                createStatCard("Informes generados", "39")
        );

        VBox activityBox = createInfoBox(
                "Actividad reciente",
                "• john_doe inició sesión\n" +
                        "• Se creó un nuevo informe\n" +
                        "• Se registró un nuevo club\n" +
                        "• Se modificó un jugador"
        );

        VBox quickActionsBox = new VBox(10);
        quickActionsBox.getStyleClass().add("info-box");
        quickActionsBox.setPrefWidth(350);

        Label quickTitle = new Label("Acciones rápidas");
        quickTitle.getStyleClass().add("panel-title");

        Button createUserBtn = new Button("Crear usuario");
        Button addPlayerBtn = new Button("Añadir jugador");
        Button registerClubBtn = new Button("Registrar club");
        Button generateReportBtn = new Button("Generar informe");

        styleActionButton(createUserBtn);
        styleActionButton(addPlayerBtn);
        styleActionButton(registerClubBtn);
        styleActionButton(generateReportBtn);

        quickActionsBox.getChildren().addAll(
                quickTitle,
                createUserBtn,
                addPlayerBtn,
                registerClubBtn,
                generateReportBtn
        );

        HBox bottomRow = new HBox(20, activityBox, quickActionsBox);

        contentContainer.getChildren().addAll(title, subtitle, cardsRow, bottomRow);
    }

    /**
     * Carga la pantalla principal para usuarios normales.
     */
    private void loadUserHome() {
        contentContainer.getChildren().clear();

        String username = SessionManager.getUsername();
        if (username == null || username.isBlank()) {
            username = "usuario";
        }

        Label title = new Label("Panel principal");
        title.getStyleClass().add("dashboard-title");

        Label subtitle = new Label("Bienvenido, " + username);
        subtitle.getStyleClass().add("section-subtitle");

        HBox cardsRow = new HBox(15);
        cardsRow.getChildren().addAll(
                createStatCard("Mis informes", "6"),
                createStatCard("Jugadores vistos", "14"),
                createStatCard("Clubes seguidos", "4"),
                createStatCard("Último acceso", "Hoy")
        );

        VBox activityBox = createInfoBox(
                "Mi actividad reciente",
                "• Consultaste 3 jugadores\n" +
                        "• Abriste el informe Sub-19\n" +
                        "• Visitaste el club Scoutbase FC\n" +
                        "• Último acceso hoy"
        );

        VBox quickActionsBox = new VBox(10);
        quickActionsBox.getStyleClass().add("info-box");
        quickActionsBox.setPrefWidth(350);

        Label quickTitle = new Label("Accesos rápidos");
        quickTitle.getStyleClass().add("panel-title");

        Button openPlayersBtn = new Button("Ver jugadores");
        Button openReportsBtn = new Button("Abrir informes");
        Button openClubsBtn = new Button("Buscar clubes");
        Button editProfileBtn = new Button("Mi perfil");

        styleActionButton(openPlayersBtn);
        styleActionButton(openReportsBtn);
        styleActionButton(openClubsBtn);
        styleActionButton(editProfileBtn);

        quickActionsBox.getChildren().addAll(
                quickTitle,
                openPlayersBtn,
                openReportsBtn,
                openClubsBtn,
                editProfileBtn
        );

        HBox bottomRow = new HBox(20, activityBox, quickActionsBox);

        contentContainer.getChildren().addAll(title, subtitle, cardsRow, bottomRow);
    }

    /**
     * Crea una tarjeta estadística reutilizable.
     *
     * @param titleText texto descriptivo de la tarjeta
     * @param valueText valor principal de la tarjeta
     * @return VBox con el contenido de la tarjeta
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
     * @return VBox con título y contenido
     */
    private VBox createInfoBox(String titleText, String contentText) {
        Label title = new Label(titleText);
        title.getStyleClass().add("panel-title");

        Label content = new Label(contentText);
        content.setWrapText(true);
        content.getStyleClass().add("panel-text");

        VBox box = new VBox(10, title, content);
        box.setPrefWidth(350);
        box.setMinHeight(220);
        box.getStyleClass().add("info-box");

        HBox.setHgrow(box, Priority.ALWAYS);
        return box;
    }

    /**
     * Aplica un estilo base a los botones de acciones rápidas.
     *
     * @param button botón a estilizar
     */
    private void styleActionButton(Button button) {
        button.setPrefWidth(180);
        button.setPrefHeight(34);
        button.getStyleClass().add("quick-action-button");
    }

    /**
     * Carga una sección provisional en el contenedor central.
     *
     * @param titleText título principal de la sección
     * @param descriptionText descripción secundaria
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

    /**
     * Cierra la sesión actual, limpia los datos almacenados
     * y vuelve a la pantalla de login.
     *
     * @param event evento generado al pulsar el botón de cerrar sesión
     */
    @FXML
    private void onLogoutClick(ActionEvent event) {
        SessionManager.clear();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 900, 600);
            scene.getStylesheets().add(
                    getClass().getResource("/scoutbase/dark_theme.css").toExternalForm()
            );

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(scene);
            stage.setTitle("Scoutbase - Login");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al volver a la pantalla de login");
            e.printStackTrace();
        }
    }

    @FXML
    private void onJugadoresClick(ActionEvent event) {
        setActiveButton(jugadoresButton);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("players-view.fxml"));
            Parent playersView = loader.load();

            contentContainer.getChildren().clear();
            contentContainer.getChildren().add(playersView);

        } catch (IOException e) {
            e.printStackTrace();
            loadPlaceholderSection(
                    "Gestión de jugadores",
                    "No se pudo cargar la vista de jugadores."
            );
        }
    }

    @FXML
    private void onClubesClick(ActionEvent event) {
        setActiveButton(clubesButton);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("clubs-view.fxml"));
            Parent clubsView = loader.load();

            contentContainer.getChildren().clear();
            contentContainer.getChildren().add(clubsView);

        } catch (IOException e) {
            e.printStackTrace();
            loadPlaceholderSection(
                    "Gestión de clubes",
                    "No se pudo cargar la vista de clubes."
            );
        }
    }

    @FXML
    private void onInformesClick(ActionEvent event) {
        setActiveButton(informesButton);
        loadPlaceholderSection(
                "Gestión de informes",
                "Aquí se mostrará el listado de informes, su creación, consulta y administración."
        );
    }

    @FXML
    private void onEstadisticasClick(ActionEvent event) {
        setActiveButton(estadisticasButton);
        loadPlaceholderSection(
                "Estadísticas del sistema",
                "Aquí se mostrarán métricas globales del sistema, resúmenes de actividad y datos agregados."
        );
    }

    @FXML
    private void onScoutsClick(ActionEvent event) {
        setActiveButton(scoutsButton);
        loadPlaceholderSection(
                "Gestión de scouts",
                "Aquí se mostrará el listado de scouts, sus datos y las funciones de gestión asociadas."
        );
    }

    @FXML
    private void onUsuariosClick(ActionEvent event) {
        setActiveButton(usuariosButton);
        loadPlaceholderSection(
                "Gestión de usuarios",
                "Aquí se mostrará el listado de usuarios, sus roles, su estado y las opciones de administración."
        );
    }
}