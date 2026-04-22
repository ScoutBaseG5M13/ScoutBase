package scoutbase.player;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import scoutbase.team.TeamDTO;
import scoutbase.team.TeamService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador de la vista de gestión de jugadores.
 *
 * <p>Se encarga de cargar los jugadores visibles en la aplicación,
 * aplicar filtros por equipo y por texto, recargar la información
 * mostrada y permitir la creación de nuevos jugadores.</p>
 *
 * <p>También integra los datos obtenidos del backend con la caché local
 * de jugadores creados temporalmente para mantener la información visible
 * aunque el backend no devuelva aún todos los jugadores correctamente.</p>
 */
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
    private ComboBox<TeamDTO> teamFilterComboBox;

    @FXML
    private TextField searchField;

    @FXML
    private Label statusLabel;

    /**
     * Servicio encargado de gestionar las operaciones relacionadas con jugadores.
     */
    private final PlayerService playerService = new PlayerService();

    /**
     * Servicio encargado de gestionar las operaciones relacionadas con equipos.
     */
    private final TeamService teamService = new TeamService();

    /**
     * Lista de equipos cargados y disponibles para filtrar jugadores.
     */
    private List<TeamDTO> loadedTeams = new ArrayList<>();

    /**
     * Lista de jugadores actualmente cargados en memoria.
     */
    private List<PlayerDTO> loadedPlayers = new ArrayList<>();

    /**
     * Equipo seleccionado desde una navegación previa.
     */
    private TeamDTO selectedTeamFromNavigation;

    /**
     * Inicializa el controlador configurando la tabla, el selector de equipos
     * y cargando los datos iniciales.
     */
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

        configureTeamComboBox();
        loadTeams();
        loadAllPlayersFromVisibleTeams();
    }

    /**
     * Configura el comportamiento visual del selector de equipos.
     *
     * <p>Define cómo se representa cada equipo dentro del {@link ComboBox}
     * mostrando únicamente su nombre.</p>
     */
    private void configureTeamComboBox() {
        if (teamFilterComboBox == null) return;

        teamFilterComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(TeamDTO team) {
                return team == null ? "" : team.getName();
            }

            @Override
            public TeamDTO fromString(String string) {
                return null;
            }
        });
    }

    /**
     * Establece un equipo seleccionado desde otra vista o navegación previa.
     *
     * <p>Si el equipo existe entre los cargados en el selector, lo marca
     * automáticamente y aplica los filtros correspondientes.</p>
     *
     * @param selectedTeam equipo que debe quedar seleccionado
     */
    public void setSelectedTeam(TeamDTO selectedTeam) {
        this.selectedTeamFromNavigation = selectedTeam;

        if (teamFilterComboBox != null && selectedTeam != null) {
            for (TeamDTO team : teamFilterComboBox.getItems()) {
                if (team.getId() != null && team.getId().equals(selectedTeam.getId())) {
                    teamFilterComboBox.setValue(team);
                    break;
                }
            }
        }

        applyFilters();
    }

    /**
     * Carga la lista de equipos desde el backend y la asigna al selector.
     */
    private void loadTeams() {
        try {
            loadedTeams = teamService.getAllTeams();

            if (teamFilterComboBox != null) {
                teamFilterComboBox.setItems(FXCollections.observableArrayList(loadedTeams));
            }

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al cargar equipos");
        }
    }

    /**
     * Carga todos los jugadores visibles a partir de los equipos disponibles.
     *
     * <p>Para cada equipo, intenta obtener los jugadores desde el backend
     * y combinarlos con los almacenados en la caché local, evitando duplicados.
     * Finalmente actualiza la lista cargada y aplica los filtros activos.</p>
     */
    private void loadAllPlayersFromVisibleTeams() {
        List<PlayerDTO> allPlayers = new ArrayList<>();

        for (TeamDTO team : loadedTeams) {
            try {
                List<PlayerDTO> backendPlayers = playerService.getPlayersByTeamId(team.getId());
                List<PlayerDTO> localPlayers = PlayerCache.getPlayersByTeamId(team.getId());

                for (PlayerDTO backendPlayer : backendPlayers) {
                    backendPlayer.setTeamId(team.getId());
                }

                allPlayers.addAll(mergePlayers(backendPlayers, localPlayers));

            } catch (Exception e) {
                List<PlayerDTO> localPlayers = PlayerCache.getPlayersByTeamId(team.getId());
                allPlayers.addAll(localPlayers);
            }
        }

        loadedPlayers = removeDuplicatePlayers(allPlayers);
        applyFilters();
    }

    /**
     * Recarga la información de equipos y jugadores mostrada en la vista.
     */
    @FXML
    private void onReloadClick() {
        loadTeams();
        loadAllPlayersFromVisibleTeams();
    }

    /**
     * Aplica manualmente los filtros configurados en la interfaz.
     */
    @FXML
    private void onApplyFiltersClick() {
        applyFilters();
    }

    /**
     * Aplica los filtros de equipo y texto sobre la lista de jugadores cargados.
     *
     * <p>Actualiza la tabla con los jugadores que coinciden con el equipo
     * seleccionado y con el texto introducido en el campo de búsqueda.</p>
     */
    private void applyFilters() {
        TeamDTO selectedTeam = null;

        if (teamFilterComboBox != null) {
            selectedTeam = teamFilterComboBox.getValue();
        }

        if (selectedTeam == null) {
            selectedTeam = selectedTeamFromNavigation;
        }

        String searchText = "";
        if (searchField != null && searchField.getText() != null) {
            searchText = searchField.getText().trim().toLowerCase();
        }

        final TeamDTO finalSelectedTeam = selectedTeam;
        final String finalSearchText = searchText;

        List<PlayerDTO> filtered = loadedPlayers.stream()
                .filter(player -> {
                    if (finalSelectedTeam == null) return true;
                    return finalSelectedTeam.getId().equals(player.getTeamId());
                })
                .filter(player -> {
                    if (finalSearchText.isBlank()) return true;

                    String fullName = ((player.getName() == null ? "" : player.getName()) + " "
                            + (player.getSurname() == null ? "" : player.getSurname())).toLowerCase();

                    return fullName.contains(finalSearchText);
                })
                .collect(Collectors.toList());

        playersTable.setItems(FXCollections.observableArrayList(filtered));

        if (finalSelectedTeam != null) {
            statusLabel.setText("Jugadores visibles del equipo: " + finalSelectedTeam.getName() + " (" + filtered.size() + ")");
        } else {
            statusLabel.setText("Jugadores visibles: " + filtered.size());
        }
    }

    /**
     * Muestra un formulario para crear un nuevo jugador en el equipo seleccionado.
     *
     * <p>Recoge los datos desde un cuadro de diálogo, intenta crear el jugador
     * mediante el servicio correspondiente y, si el backend no devuelve la
     * información esperada, lo almacena localmente en caché como alternativa.</p>
     */
    @FXML
    private void onAddPlayerClick() {
        TeamDTO selectedTeam = null;

        if (teamFilterComboBox != null) {
            selectedTeam = teamFilterComboBox.getValue();
        }

        if (selectedTeam == null) {
            selectedTeam = selectedTeamFromNavigation;
        }

        if (selectedTeam == null) {
            statusLabel.setText("Selecciona primero un equipo");
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
        TextField emailField = new TextField();
        TextField numberField = new TextField();
        TextField positionField = new TextField();
        TextField priorityField = new TextField();

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Apellido:"), 0, 1);
        grid.add(surnameField, 1, 1);

        grid.add(new Label("Edad:"), 0, 2);
        grid.add(ageField, 1, 2);

        grid.add(new Label("Email:"), 0, 3);
        grid.add(emailField, 1, 3);

        grid.add(new Label("Dorsal:"), 0, 4);
        grid.add(numberField, 1, 4);

        grid.add(new Label("Posición:"), 0, 5);
        grid.add(positionField, 1, 5);

        grid.add(new Label("Prioridad:"), 0, 6);
        grid.add(priorityField, 1, 6);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == createButtonType) {
            try {
                int age = Integer.parseInt(ageField.getText().trim());
                int number = Integer.parseInt(numberField.getText().trim());
                int priority = Integer.parseInt(priorityField.getText().trim());

                try {
                    PlayerDTO newPlayer = playerService.createPlayer(
                            nameField.getText().trim(),
                            surnameField.getText().trim(),
                            age,
                            emailField.getText().trim(),
                            number,
                            positionField.getText().trim(),
                            priority,
                            selectedTeam.getId()
                    );

                    PlayerCache.addPlayer(newPlayer, selectedTeam.getId());

                } catch (Exception ex) {
                    if (ex.getMessage() != null && ex.getMessage().contains("No value present")) {
                        PlayerDTO localPlayer = playerService.createLocalPlayer(
                                nameField.getText().trim(),
                                surnameField.getText().trim(),
                                age,
                                emailField.getText().trim(),
                                number,
                                positionField.getText().trim(),
                                priority,
                                selectedTeam.getId()
                        );

                        PlayerCache.addPlayer(localPlayer, selectedTeam.getId());
                    } else {
                        throw ex;
                    }
                }

                loadAllPlayersFromVisibleTeams();
                statusLabel.setText("Jugador creado correctamente");

            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setText("Error al crear jugador");
            }
        }
    }

    /**
     * Combina los jugadores obtenidos del backend con los almacenados localmente,
     * evitando duplicados por identificador.
     *
     * @param backendPlayers jugadores recuperados desde el backend
     * @param localPlayers jugadores almacenados en caché local
     * @return lista combinada de jugadores sin duplicados
     */
    private List<PlayerDTO> mergePlayers(List<PlayerDTO> backendPlayers, List<PlayerDTO> localPlayers) {
        List<PlayerDTO> merged = new ArrayList<>(backendPlayers);

        for (PlayerDTO localPlayer : localPlayers) {
            boolean exists = merged.stream()
                    .anyMatch(player -> player.getId() != null && player.getId().equals(localPlayer.getId()));

            if (!exists) {
                merged.add(localPlayer);
            }
        }

        return merged;
    }

    /**
     * Elimina jugadores duplicados de una lista en función de su identificador.
     *
     * @param players lista de jugadores a depurar
     * @return lista de jugadores única sin elementos repetidos
     */
    private List<PlayerDTO> removeDuplicatePlayers(List<PlayerDTO> players) {
        List<PlayerDTO> uniquePlayers = new ArrayList<>();

        for (PlayerDTO player : players) {
            boolean exists = uniquePlayers.stream()
                    .anyMatch(existing -> existing.getId() != null && existing.getId().equals(player.getId()));

            if (!exists) {
                uniquePlayers.add(player);
            }
        }

        return uniquePlayers;
    }
}