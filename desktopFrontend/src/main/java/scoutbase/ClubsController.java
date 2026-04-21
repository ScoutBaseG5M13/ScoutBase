package scoutbase;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import java.util.List;

public class ClubsController {

    @FXML
    private TableView<ClubDTO> clubsTable;

    @FXML
    private TableColumn<ClubDTO, String> idColumn;

    @FXML
    private TableColumn<ClubDTO, String> nameColumn;

    @FXML
    private Label statusLabel;

    private final ClubService clubService = new ClubService();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        loadClubs();
    }

    private void loadClubs() {
        try {
            List<ClubDTO> clubs = clubService.getAllClubs();
            clubsTable.setItems(FXCollections.observableArrayList(clubs));
            statusLabel.setText("Clubs cargados correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al cargar clubs");
        }
    }

    @FXML
    private void onReloadClick() {
        loadClubs();
    }

    @FXML
    private void onViewTeamsClick() {
        ClubDTO selectedClub = clubsTable.getSelectionModel().getSelectedItem();

        if (selectedClub == null) {
            statusLabel.setText("Selecciona un club primero");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scoutbase/teams-view.fxml"));
            Parent teamsView = loader.load();

            TeamsController teamsController = loader.getController();
            teamsController.setSelectedClub(selectedClub);

            VBox contentContainer = (VBox) clubsTable.getScene().lookup("#contentContainer");

            if (contentContainer != null) {
                contentContainer.getChildren().clear();
                contentContainer.getChildren().add(teamsView);
            } else {
                statusLabel.setText("No se encontró el contenedor principal");
            }

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al abrir los equipos del club");
        }
    }
    @FXML
    private void onAddClubClick() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nuevo club");
        dialog.setHeaderText("Crear club");
        dialog.setContentText("Nombre del club:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String name = result.get().trim();

            if (name.isBlank()) {
                statusLabel.setText("El nombre del club no puede estar vacío");
                return;
            }

            try {
                AuthService authService = new AuthService();
                UserDto currentUser = authService.getUserByUsername(
                        SessionManager.getUsername(),
                        SessionManager.getAuthToken()
                );

                clubService.createClub(name, currentUser.getId());
                loadClubs();
                statusLabel.setText("Club creado correctamente");

            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setText("Error al crear club");
            }
        }
    }
}