package scoutbase.scout;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.util.Optional;

/**
 * Controlador de la vista de gestión de scouts.
 *
 * <p>Permite visualizar scouts existentes en el backend,
 * recargar su listado y crear nuevos scouts utilizando
 * el endpoint de usuarios.</p>
 */
public class ScoutsController {

    @FXML
    private TableView<ScoutDTO> scoutsTable;

    @FXML
    private TableColumn<ScoutDTO, String> idColumn;

    @FXML
    private TableColumn<ScoutDTO, String> usernameColumn;

    @FXML
    private TableColumn<ScoutDTO, String> nameColumn;

    @FXML
    private TableColumn<ScoutDTO, String> surnameColumn;

    @FXML
    private TableColumn<ScoutDTO, String> emailColumn;

    @FXML
    private TableColumn<ScoutDTO, String> roleColumn;

    @FXML
    private Label statusLabel;

    /**
     * Servicio encargado de gestionar las operaciones relacionadas con scouts.
     */
    private final ScoutService scoutService = new ScoutService();

    /**
     * Inicializa la tabla y carga los scouts desde backend.
     */
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        scoutsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadScouts();
    }

    /**
     * Carga los scouts desde backend y actualiza la tabla.
     */
    private void loadScouts() {
        try {
            scoutsTable.setItems(FXCollections.observableArrayList(scoutService.getAllScouts()));
            statusLabel.setText("Scouts cargados correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al cargar scouts");
        }
    }

    /**
     * Recarga manualmente el listado de scouts.
     */
    @FXML
    private void onReloadClick() {
        loadScouts();
    }

    /**
     * Abre un cuadro de diálogo para crear un nuevo scout.
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

        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField nameField = new TextField();
        TextField surnameField = new TextField();
        TextField emailField = new TextField();

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);

        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);

        grid.add(new Label("Nombre:"), 0, 2);
        grid.add(nameField, 1, 2);

        grid.add(new Label("Apellidos:"), 0, 3);
        grid.add(surnameField, 1, 3);

        grid.add(new Label("Email:"), 0, 4);
        grid.add(emailField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == createButtonType) {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String email = emailField.getText().trim();

            if (username.isBlank() || password.isBlank() || name.isBlank()
                    || surname.isBlank() || email.isBlank()) {
                statusLabel.setText("Todos los campos son obligatorios");
                return;
            }

            try {
                scoutService.createScout(username, password, name, surname, email);
                loadScouts();
                statusLabel.setText("Scout creado correctamente");
            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setText("Error al crear scout");
            }
        }
    }
}