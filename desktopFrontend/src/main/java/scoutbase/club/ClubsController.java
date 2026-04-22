package scoutbase.club;

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
import scoutbase.team.TeamsController;
import scoutbase.app.SessionManager;
import scoutbase.auth.AuthService;
import scoutbase.user.UserDto;

import java.util.Optional;
import java.util.List;

/**
 * Controlador de la vista de gestión de clubes.
 *
 * <p>Se encarga de inicializar la tabla de clubes, cargar los datos
 * desde el backend, recargar la información mostrada, abrir la vista
 * de equipos del club seleccionado y permitir la creación de nuevos clubes.</p>
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

    /**
     * Servicio encargado de gestionar las operaciones relacionadas con clubes.
     */
    private final ClubService clubService = new ClubService();

    /**
     * Método de inicialización del controlador.
     *
     * <p>Configura las columnas de la tabla y carga la lista inicial
     * de clubes al abrir la vista.</p>
     */
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        loadClubs();
    }

    /**
     * Carga la lista de clubes desde el backend y la muestra en la tabla.
     *
     * <p>Si la operación se completa correctamente, actualiza la tabla
     * y muestra un mensaje informativo en la interfaz. En caso de error,
     * informa del problema mediante la etiqueta de estado.</p>
     */
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

    /**
     * Recarga manualmente la lista de clubes mostrada en la tabla.
     */
    @FXML
    private void onReloadClick() {
        loadClubs();
    }

    /**
     * Abre la vista de equipos del club seleccionado.
     *
     * <p>Obtiene el club seleccionado en la tabla, carga la vista de equipos
     * y la inserta en el contenedor principal de contenido. Si no hay ningún
     * club seleccionado o se produce un error, se muestra un mensaje en la interfaz.</p>
     */
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

    /**
     * Muestra un cuadro de diálogo para crear un nuevo club.
     *
     * <p>Solicita el nombre del club al usuario, valida que no esté vacío,
     * obtiene el usuario autenticado actual y envía la petición de creación
     * al backend. Tras ello, recarga la tabla de clubes y actualiza el mensaje
     * de estado según el resultado.</p>
     */
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