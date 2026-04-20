package scoutbase;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.Optional;
import java.util.List;

public class PlayersController {

    @FXML
    private TableView<PlayerDTO> playersTable;

    @FXML
    private TableColumn<PlayerDTO, String> idColumn;

    @FXML
    private TableColumn<PlayerDTO, String> nameColumn;

    @FXML
    private TableColumn<PlayerDTO, String> surnameColumn;

    @FXML
    private TableColumn<PlayerDTO, Integer> ageColumn;

    @FXML
    private TableColumn<PlayerDTO, String> positionColumn;

    @FXML
    private Label statusLabel;

    private final PlayerService playerService = new PlayerService();

    private TeamDTO selectedTeam;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
    }

    public void setSelectedTeam(TeamDTO selectedTeam) {
        this.selectedTeam = selectedTeam;
        loadPlayers();
    }

    private void loadPlayers() {
        try {
            if (selectedTeam == null) {
                statusLabel.setText("No se ha seleccionado ningún equipo");
                playersTable.setItems(FXCollections.observableArrayList());
                return;
            }

            List<PlayerDTO> players = playerService.getPlayersByTeamId(selectedTeam.getId());
            playersTable.setItems(FXCollections.observableArrayList(players));
            statusLabel.setText("Jugadores del equipo: " + selectedTeam.getName());

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al cargar jugadores");
        }
    }

    @FXML
    private void onReloadClick() {
        loadPlayers();
    }
    @FXML
    private void onAddPlayerClick() {
        if (selectedTeam == null) {
            statusLabel.setText("No hay un equipo seleccionado");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuevo jugador");
        dialog.setHeaderText("Crear jugador para el equipo: " + selectedTeam.getName());

        ButtonType createButtonType = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField surnameField = new TextField();
        TextField ageField = new TextField();
        TextField positionField = new TextField();

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Apellido:"), 0, 1);
        grid.add(surnameField, 1, 1);
        grid.add(new Label("Edad:"), 0, 2);
        grid.add(ageField, 1, 2);
        grid.add(new Label("Posición:"), 0, 3);
        grid.add(positionField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == createButtonType) {
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String ageText = ageField.getText().trim();
            String position = positionField.getText().trim();

            if (name.isBlank() || surname.isBlank() || ageText.isBlank() || position.isBlank()) {
                statusLabel.setText("Todos los campos son obligatorios");
                return;
            }

            try {
                int age = Integer.parseInt(ageText);

                playerService.createPlayer(name, surname, age, position, selectedTeam.getId());
                loadPlayers();
                statusLabel.setText("Jugador creado correctamente");

            } catch (NumberFormatException e) {
                statusLabel.setText("La edad debe ser un número válido");
            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setText("Error al crear jugador");
            }
        }
    }
}