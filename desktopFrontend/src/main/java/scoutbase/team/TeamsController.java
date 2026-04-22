package scoutbase.team;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import scoutbase.app.SessionManager;
import scoutbase.auth.AuthService;
import scoutbase.club.ClubDTO;
import scoutbase.player.PlayersController;
import scoutbase.user.UserDto;

import java.util.List;
import java.util.Optional;

/**
 * Controlador de la vista de gestión de equipos.
 *
 * <p>Se encarga de mostrar los equipos disponibles, recargar su listado,
 * abrir la vista de jugadores del equipo seleccionado y permitir la creación
 * de nuevos equipos asociados a un club concreto.</p>
 */
public class TeamsController {

    @FXML
    private TableView<TeamDTO> teamsTable;

    @FXML
    private TableColumn<TeamDTO, String> idColumn;

    @FXML
    private TableColumn<TeamDTO, String> nameColumn;

    @FXML
    private TableColumn<TeamDTO, String> categoryColumn;

    @FXML
    private TableColumn<TeamDTO, String> subcategoryColumn;

    @FXML
    private Label statusLabel;

    /**
     * Servicio encargado de gestionar las operaciones relacionadas con equipos.
     */
    private final TeamService teamService = new TeamService();

    /**
     * Club actualmente seleccionado desde la navegación previa.
     */
    private ClubDTO selectedClub;

    /**
     * Inicializa la tabla de equipos configurando las columnas
     * con las propiedades correspondientes del DTO.
     */
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        subcategoryColumn.setCellValueFactory(new PropertyValueFactory<>("subcategory"));
    }

    /**
     * Establece el club seleccionado y carga los equipos asociados.
     *
     * @param selectedClub club seleccionado desde la vista anterior
     */
    public void setSelectedClub(ClubDTO selectedClub) {
        this.selectedClub = selectedClub;
        loadTeams();
    }

    /**
     * Carga el listado de equipos visibles y lo muestra en la tabla.
     *
     * <p>Si existe un club seleccionado, actualiza también el mensaje
     * de estado indicando el contexto actual.</p>
     */
    private void loadTeams() {
        try {
            List<TeamDTO> teams = teamService.getAllTeams();
            teamsTable.setItems(FXCollections.observableArrayList(teams));

            if (selectedClub != null) {
                statusLabel.setText("Equipos visibles del usuario para el club: " + selectedClub.getName());
            } else {
                statusLabel.setText("Todos los equipos cargados");
            }

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al cargar equipos");
        }
    }

    /**
     * Recarga manualmente el listado de equipos.
     */
    @FXML
    private void onReloadClick() {
        loadTeams();
    }

    /**
     * Abre la vista de jugadores del equipo seleccionado.
     *
     * <p>Carga la vista correspondiente, pasa el equipo seleccionado
     * al controlador de jugadores y sustituye el contenido actual
     * del contenedor principal.</p>
     */
    @FXML
    private void onViewPlayersClick() {
        TeamDTO selectedTeam = teamsTable.getSelectionModel().getSelectedItem();

        if (selectedTeam == null) {
            statusLabel.setText("Selecciona un equipo primero");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scoutbase/players-view.fxml"));
            Parent playersView = loader.load();

            PlayersController playersController = loader.getController();
            playersController.setSelectedTeam(selectedTeam);

            VBox contentContainer = (VBox) teamsTable.getScene().lookup("#contentContainer");

            if (contentContainer != null) {
                contentContainer.getChildren().clear();
                contentContainer.getChildren().add(playersView);
            } else {
                statusLabel.setText("No se encontró el contenedor principal");
            }

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al abrir los jugadores del equipo");
        }
    }

    /**
     * Muestra un cuadro de diálogo para crear un nuevo equipo en el club seleccionado.
     *
     * <p>Solicita al usuario el nombre, la categoría y la subcategoría del equipo,
     * valida que todos los campos estén informados, obtiene el usuario autenticado
     * actual y envía la petición de creación al backend.</p>
     */
    @FXML
    private void onAddTeamClick() {
        if (selectedClub == null) {
            statusLabel.setText("No hay un club seleccionado");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuevo equipo");
        dialog.setHeaderText("Crear equipo para el club: " + selectedClub.getName());

        ButtonType createButtonType = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();

        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll(
                "PREBENJAMIN",
                "BENJAMIN",
                "ALEVIN",
                "INFANTIL",
                "CADETE",
                "JUVENIL"
        );

        ComboBox<String> subcategoryBox = new ComboBox<>();
        subcategoryBox.getItems().addAll(
                "SUB8",
                "SUB10"
        );

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Categoría:"), 0, 1);
        grid.add(categoryBox, 1, 1);
        grid.add(new Label("Subcategoría:"), 0, 2);
        grid.add(subcategoryBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == createButtonType) {
            String name = nameField.getText().trim();
            String category = categoryBox.getValue();
            String subcategory = subcategoryBox.getValue();

            if (name.isBlank() || category == null || subcategory == null) {
                statusLabel.setText("Todos los campos son obligatorios");
                return;
            }

            try {
                AuthService authService = new AuthService();
                UserDto currentUser = authService.getCurrentUser(SessionManager.getAuthToken());

                teamService.createTeam(
                        name,
                        category,
                        subcategory,
                        selectedClub.getId(),
                        currentUser.getId()
                );

                loadTeams();
                statusLabel.setText("Equipo creado correctamente");
            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setText("Error al crear equipo");
            }
        }
    }
}