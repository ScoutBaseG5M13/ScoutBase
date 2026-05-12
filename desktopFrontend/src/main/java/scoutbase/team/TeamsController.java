package scoutbase.team;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import scoutbase.club.ClubDTO;
import scoutbase.player.PlayersController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controlador de la vista de gestión de equipos.
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
     * Servicio de equipos.
     */
    private final TeamService teamService = new TeamService();

    /**
     * Club actualmente seleccionado.
     */
    private ClubDTO selectedClub;

    /**
     * Categorías obtenidas desde backend.
     */
    private List<TeamService.TeamCategoryDTO> availableCategories = new ArrayList<>();

    /**
     * Inicializa la tabla.
     */
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        subcategoryColumn.setCellValueFactory(new PropertyValueFactory<>("subcategory"));

        loadCategories();

        statusLabel.setText("Selecciona un club para cargar sus equipos");
    }

    /**
     * Carga categorías desde backend.
     */
    private void loadCategories() {
        try {
            availableCategories = teamService.getCategories();

            if (availableCategories == null) {
                availableCategories = new ArrayList<>();
            }

        } catch (Exception e) {
            e.printStackTrace();
            availableCategories = new ArrayList<>();
            statusLabel.setText("Error al cargar categorías");
        }
    }

    /**
     * Establece el club seleccionado.
     */
    public void setSelectedClub(ClubDTO selectedClub) {
        this.selectedClub = selectedClub;
        loadTeams();
    }

    /**
     * Carga los equipos del club.
     */
    private void loadTeams() {
        if (selectedClub == null
                || selectedClub.getId() == null
                || selectedClub.getId().isBlank()) {

            statusLabel.setText("No hay un club seleccionado");
            return;
        }

        try {
            List<TeamDTO> teams = teamService.getTeamsByClubId(selectedClub.getId());

            teamsTable.setItems(FXCollections.observableArrayList(teams));

            if (teams == null || teams.isEmpty()) {
                statusLabel.setText("Este club no tiene equipos todavía");
            } else {
                statusLabel.setText("Equipos cargados para el club: " + selectedClub.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al cargar equipos");
        }
    }

    /**
     * Recarga manual.
     */
    @FXML
    private void onReloadClick() {
        loadTeams();
    }

    /**
     * Abre jugadores del equipo seleccionado.
     */
    @FXML
    private void onViewPlayersClick() {

        TeamDTO selectedTeam = teamsTable.getSelectionModel().getSelectedItem();

        if (selectedTeam == null) {
            statusLabel.setText("Selecciona un equipo primero");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/scoutbase/players-view.fxml")
            );

            Parent playersView = loader.load();

            PlayersController playersController = loader.getController();
            playersController.setSelectedTeam(selectedTeam);

            VBox contentContainer =
                    (VBox) teamsTable.getScene().lookup("#contentContainer");

            if (contentContainer != null) {
                contentContainer.getChildren().clear();
                contentContainer.getChildren().add(playersView);
            } else {
                statusLabel.setText("No se encontró el contenedor principal");
            }

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al abrir jugadores");
        }
    }

    /**
     * Crear equipo.
     */
    @FXML
    private void onAddTeamClick() {

        if (selectedClub == null
                || selectedClub.getId() == null
                || selectedClub.getId().isBlank()) {

            statusLabel.setText("No hay un club seleccionado");
            return;
        }

        if (availableCategories == null || availableCategories.isEmpty()) {
            loadCategories();
        }

        if (availableCategories == null || availableCategories.isEmpty()) {
            statusLabel.setText("No se pudieron cargar categorías");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuevo equipo");
        dialog.setHeaderText(
                "Crear equipo para el club: " + selectedClub.getName()
        );

        ButtonType createButtonType =
                new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);

        dialog.getDialogPane().getButtonTypes().addAll(
                createButtonType,
                ButtonType.CANCEL
        );

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();

        ComboBox<TeamService.TeamCategoryDTO> categoryBox =
                new ComboBox<>();

        categoryBox.setItems(
                FXCollections.observableArrayList(availableCategories)
        );

        ComboBox<String> subcategoryBox = new ComboBox<>();
        subcategoryBox.setDisable(true);

        categoryBox.valueProperty().addListener(
                (observable, oldValue, selectedCategory) -> {

                    subcategoryBox.getSelectionModel().clearSelection();

                    if (selectedCategory == null
                            || selectedCategory.getSubcategories() == null
                            || selectedCategory.getSubcategories().isEmpty()) {

                        subcategoryBox.setItems(
                                FXCollections.observableArrayList()
                        );

                        subcategoryBox.setDisable(true);
                        return;
                    }

                    subcategoryBox.setItems(
                            FXCollections.observableArrayList(
                                    selectedCategory.getSubcategories()
                            )
                    );

                    subcategoryBox.setDisable(false);
                }
        );

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Categoría:"), 0, 1);
        grid.add(categoryBox, 1, 1);

        grid.add(new Label("Subcategoría:"), 0, 2);
        grid.add(subcategoryBox, 1, 2);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isEmpty() || result.get() != createButtonType) {
            return;
        }

        String name = nameField.getText().trim();

        TeamService.TeamCategoryDTO category =
                categoryBox.getValue();

        String subcategory = subcategoryBox.getValue();

        if (name.isBlank()
                || category == null
                || category.getName() == null
                || subcategory == null) {

            statusLabel.setText("Todos los campos son obligatorios");
            return;
        }

        try {

            teamService.createTeam(
                    selectedClub.getId(),
                    name,
                    category.getName(),
                    subcategory
            );

            loadTeams();

            statusLabel.setText("Equipo creado correctamente");

        } catch (Exception e) {

            if (isJuvenilCategory(category.getName())) {

                addMockJuvenilTeam(
                        name,
                        category.getName(),
                        subcategory
                );

                statusLabel.setText(
                        "Equipo juvenil simulado localmente para demo"
                );

            } else {

                e.printStackTrace();
                statusLabel.setText("Error al crear equipo");
            }
        }
    }

    /**
     * Detecta si la categoría es juvenil.
     */
    private boolean isJuvenilCategory(String category) {

        if (category == null) {
            return false;
        }

        return category.equalsIgnoreCase("JUVENIL");
    }

    /**
     * Inserta un equipo mock localmente para demo.
     */
    private void addMockJuvenilTeam(
            String name,
            String category,
            String subcategory
    ) {

        TeamDTO mockTeam = new TeamDTO();

        mockTeam.setId("MOCK-" + UUID.randomUUID());
        mockTeam.setName(name);
        mockTeam.setCategory(category);
        mockTeam.setSubcategory(subcategory);

        teamsTable.getItems().add(mockTeam);
    }
}