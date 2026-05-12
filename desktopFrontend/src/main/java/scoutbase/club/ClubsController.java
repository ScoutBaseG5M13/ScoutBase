package scoutbase.club;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import scoutbase.team.TeamsController;
import scoutbase.userClub.UserClubDTO;
import scoutbase.userClub.UserClubService;

import java.util.List;
import java.util.Optional;

/**
 * Controlador de la vista de gestión de clubes.
 */
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
    private final UserClubService userClubService = new UserClubService();

    private String selectedUserClubId;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        resolveDefaultUserClubAndLoadClubs();
    }

    public void setSelectedUserClubId(String userClubId) {
        this.selectedUserClubId = userClubId;
        loadClubs();
    }

    private void resolveDefaultUserClubAndLoadClubs() {
        try {
            UserClubDTO defaultUserClub = userClubService.getDefaultUserClub();

            if (defaultUserClub != null
                    && defaultUserClub.getId() != null
                    && !defaultUserClub.getId().isBlank()) {
                selectedUserClubId = defaultUserClub.getId();
            }

            loadClubs();

        } catch (Exception e) {
            e.printStackTrace();
            loadAllClubsFallback("Error al resolver UserClub. Clubes cargados en modo global.");
        }
    }

    private void loadClubs() {
        try {
            List<ClubDTO> clubs = null;

            if (selectedUserClubId != null && !selectedUserClubId.isBlank()) {
                try {
                    clubs = clubService.getClubsByUserClub(selectedUserClubId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (clubs == null || clubs.isEmpty()) {
                clubs = clubService.getAllClubs();

                clubsTable.setItems(FXCollections.observableArrayList(
                        clubs != null ? clubs : List.of()
                ));

                if (clubs == null || clubs.isEmpty()) {
                    statusLabel.setText("No hay clubes disponibles");
                } else {
                    statusLabel.setText("Clubes cargados correctamente en modo global");
                }

                return;
            }

            clubsTable.setItems(FXCollections.observableArrayList(clubs));
            statusLabel.setText("Clubes cargados correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al cargar clubes");
        }
    }

    private void loadAllClubsFallback(String message) {
        try {
            List<ClubDTO> clubs = clubService.getAllClubs();

            clubsTable.setItems(FXCollections.observableArrayList(
                    clubs != null ? clubs : List.of()
            ));

            statusLabel.setText(message);

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al cargar clubes globales");
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
        if (selectedUserClubId == null || selectedUserClubId.isBlank()) {
            try {
                UserClubDTO defaultUserClub = userClubService.getDefaultUserClub();

                if (defaultUserClub != null
                        && defaultUserClub.getId() != null
                        && !defaultUserClub.getId().isBlank()) {
                    selectedUserClubId = defaultUserClub.getId();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (selectedUserClubId == null || selectedUserClubId.isBlank()) {
            statusLabel.setText("No hay UserClub disponible para crear clubes");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nuevo club");
        dialog.setHeaderText("Crear club");
        dialog.setContentText("Nombre del club:");

        Optional<String> result = dialog.showAndWait();

        if (result.isEmpty()) {
            return;
        }

        String name = result.get().trim();

        if (name.isBlank()) {
            statusLabel.setText("El nombre del club no puede estar vacío");
            return;
        }

        try {
            clubService.createClub(selectedUserClubId, name);
            loadClubs();
            statusLabel.setText("Club creado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al crear club");
        }
    }
}