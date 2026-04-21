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

    private final TeamService teamService = new TeamService();

    private ClubDTO selectedClub;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        subcategoryColumn.setCellValueFactory(new PropertyValueFactory<>("subcategory"));
    }

    public void setSelectedClub(ClubDTO selectedClub) {
        this.selectedClub = selectedClub;
        loadTeams();
    }

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

    @FXML
    private void onReloadClick() {
        loadTeams();
    }

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