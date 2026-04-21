package scoutbase.scout;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.util.Optional;
import java.util.UUID;

/**
 * Controlador de la vista de gestión de scouts.
 *
 * <p>Permite visualizar scouts existentes y añadir nuevos scouts
 * de forma local para la interfaz mientras se completa la integración
 * real con backend.</p>
 */
public class ScoutsController {

    @FXML
    private TableView<ScoutDTO> scoutsTable;

    @FXML
    private TableColumn<ScoutDTO, String> idColumn;

    @FXML
    private TableColumn<ScoutDTO, String> nameColumn;

    @FXML
    private TableColumn<ScoutDTO, String> surnameColumn;

    @FXML
    private TableColumn<ScoutDTO, String> emailColumn;

    @FXML
    private TableColumn<ScoutDTO, String> clubColumn;

    @FXML
    private TableColumn<ScoutDTO, String> teamColumn;

    @FXML
    private Label statusLabel;

    /**
     * Inicializa la tabla y carga scouts de ejemplo o desde caché.
     */
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        clubColumn.setCellValueFactory(new PropertyValueFactory<>("clubName"));
        teamColumn.setCellValueFactory(new PropertyValueFactory<>("teamName"));

        scoutsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadScouts();
    }

    /**
     * Carga los scouts desde caché. Si no hay ninguno, añade ejemplos iniciales.
     */
    private void loadScouts() {
        if (ScoutCache.isEmpty()) {
            ScoutCache.addScout(new ScoutDTO(
                    UUID.randomUUID().toString(),
                    "Alejandro",
                    "Ruiz",
                    "alejandro.ruiz@scoutbase.com",
                    "My Favourite Club",
                    "Jane's Team"
            ));

            ScoutCache.addScout(new ScoutDTO(
                    UUID.randomUUID().toString(),
                    "Marta",
                    "López",
                    "marta.lopez@scoutbase.com",
                    "Barça",
                    "Barça B"
            ));
        }

        scoutsTable.setItems(FXCollections.observableArrayList(ScoutCache.getScouts()));
        statusLabel.setText("Scouts cargados correctamente");
    }

    /**
     * Recarga la tabla de scouts.
     */
    @FXML
    private void onReloadClick() {
        scoutsTable.setItems(FXCollections.observableArrayList(ScoutCache.getScouts()));
        statusLabel.setText("Listado de scouts actualizado");
    }

    /**
     * Abre un diálogo para crear un scout localmente.
     */
    @FXML
    private void onAddScoutClick() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuevo scout");
        dialog.setHeaderText("Crear scout");

        ButtonType createButtonType = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField surnameField = new TextField();
        TextField emailField = new TextField();
        TextField clubField = new TextField();
        TextField teamField = new TextField();

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Apellidos:"), 0, 1);
        grid.add(surnameField, 1, 1);

        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);

        grid.add(new Label("Club:"), 0, 3);
        grid.add(clubField, 1, 3);

        grid.add(new Label("Equipo:"), 0, 4);
        grid.add(teamField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == createButtonType) {
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String email = emailField.getText().trim();
            String club = clubField.getText().trim();
            String team = teamField.getText().trim();

            if (name.isBlank() || surname.isBlank() || email.isBlank()) {
                statusLabel.setText("Nombre, apellidos y email son obligatorios");
                return;
            }

            ScoutDTO scout = new ScoutDTO(
                    UUID.randomUUID().toString(),
                    name,
                    surname,
                    email,
                    club,
                    team
            );

            ScoutCache.addScout(scout);
            scoutsTable.setItems(FXCollections.observableArrayList(ScoutCache.getScouts()));
            statusLabel.setText("Scout creado correctamente");
        }
    }
}