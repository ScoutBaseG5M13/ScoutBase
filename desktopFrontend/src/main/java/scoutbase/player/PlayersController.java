package scoutbase.player;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.StringConverter;
import scoutbase.club.ClubDTO;
import scoutbase.club.ClubService;
import scoutbase.team.TeamDTO;
import scoutbase.team.TeamService;
import scoutbase.userClub.UserClubDTO;
import scoutbase.userClub.UserClubService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador de la vista de gestión de jugadores.
 *
 * <p>Gestiona la carga, visualización, filtrado y creación de jugadores.</p>
 *
 * <p>La vista puede abrirse de dos formas:</p>
 * <ul>
 *     <li>Desde Clubs &gt; Teams &gt; Players, recibiendo un equipo concreto.</li>
 *     <li>Desde el botón superior Jugadores, cargando automáticamente todos los equipos visibles.</li>
 * </ul>
 *
 * <p>En la nueva arquitectura del backend, los jugadores se consultan mediante
 * el endpoint {@code /players/teams/{id}} y se crean mediante
 * {@code /teams/{id}/players}.</p>
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
     * Servicio encargado de obtener los UserClubs visibles para el usuario.
     */
    private final UserClubService userClubService = new UserClubService();

    /**
     * Servicio encargado de obtener clubes asociados a UserClub.
     */
    private final ClubService clubService = new ClubService();

    /**
     * Servicio encargado de obtener equipos asociados a clubes.
     */
    private final TeamService teamService = new TeamService();

    /**
     * Lista de equipos actualmente visibles para el usuario.
     */
    private List<TeamDTO> loadedTeams = new ArrayList<>();

    /**
     * Lista de jugadores actualmente cargados desde backend.
     */
    private List<PlayerDTO> loadedPlayers = new ArrayList<>();

    /**
     * Equipo seleccionado desde la navegación previa o desde el desplegable.
     */
    private TeamDTO selectedTeam;

    /**
     * Evita recargas duplicadas cuando se asigna el equipo desde navegación.
     */
    private boolean loadingFromNavigation = false;

    /**
     * Inicializa el controlador configurando la tabla, el selector de equipo
     * y cargando los equipos visibles si la vista se abre desde el menú superior.
     */
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));

        configureTeamComboBox();
        loadVisibleTeamsAndPlayers();
    }

    /**
     * Configura el comportamiento visual y funcional del selector de equipos.
     */
    private void configureTeamComboBox() {
        if (teamFilterComboBox == null) {
            return;
        }

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

        teamFilterComboBox.valueProperty().addListener((observable, oldTeam, newTeam) -> {
            if (loadingFromNavigation) {
                return;
            }

            selectedTeam = newTeam;

            if (selectedTeam != null) {
                loadPlayers();
            } else {
                loadAllPlayersFromLoadedTeams();
            }
        });
    }

    /**
     * Establece el equipo seleccionado desde la vista de equipos.
     *
     * <p>Una vez recibido el equipo, lo muestra en el selector y carga
     * automáticamente sus jugadores asociados.</p>
     *
     * @param selectedTeam equipo seleccionado desde la navegación previa
     */
    public void setSelectedTeam(TeamDTO selectedTeam) {
        this.selectedTeam = selectedTeam;

        if (teamFilterComboBox != null && selectedTeam != null) {
            loadingFromNavigation = true;
            loadedTeams = new ArrayList<>(List.of(selectedTeam));
            teamFilterComboBox.setItems(FXCollections.observableArrayList(loadedTeams));
            teamFilterComboBox.setValue(selectedTeam);
            loadingFromNavigation = false;
        }

        loadPlayers();
    }

    /**
     * Carga todos los equipos visibles para el usuario actual y sus jugadores.
     *
     * <p>Este método se utiliza cuando la pantalla se abre desde el botón superior
     * Jugadores y no se recibe un equipo previamente seleccionado.</p>
     */
    private void loadVisibleTeamsAndPlayers() {
        try {
            loadedTeams = resolveVisibleTeams();

            if (teamFilterComboBox != null) {
                teamFilterComboBox.setItems(FXCollections.observableArrayList(loadedTeams));
            }

            if (loadedTeams.isEmpty()) {
                loadedPlayers = new ArrayList<>();
                playersTable.setItems(FXCollections.observableArrayList());
                statusLabel.setText("No hay equipos visibles para cargar jugadores");
                return;
            }

            loadAllPlayersFromLoadedTeams();

        } catch (Exception e) {
            e.printStackTrace();
            loadedTeams = new ArrayList<>();
            loadedPlayers = new ArrayList<>();
            playersTable.setItems(FXCollections.observableArrayList());
            statusLabel.setText("Error al cargar equipos visibles");
        }
    }

    /**
     * Resuelve todos los equipos visibles recorriendo UserClubs, clubes y equipos.
     *
     * @return lista de equipos visibles para el usuario autenticado
     */
    private List<TeamDTO> resolveVisibleTeams() {
        List<TeamDTO> teams = new ArrayList<>();

        List<UserClubDTO> userClubs = userClubService.getMyUserClubs();

        if (userClubs == null || userClubs.isEmpty()) {
            return teams;
        }

        for (UserClubDTO userClub : userClubs) {
            if (userClub == null || userClub.getId() == null || userClub.getId().isBlank()) {
                continue;
            }

            List<ClubDTO> clubs = clubService.getClubsByUserClub(userClub.getId());

            if (clubs == null || clubs.isEmpty()) {
                continue;
            }

            for (ClubDTO club : clubs) {
                if (club == null || club.getId() == null || club.getId().isBlank()) {
                    continue;
                }

                List<TeamDTO> clubTeams = teamService.getTeamsByClubId(club.getId());

                if (clubTeams != null && !clubTeams.isEmpty()) {
                    teams.addAll(clubTeams);
                }
            }
        }

        return removeDuplicateTeams(teams);
    }

    /**
     * Carga los jugadores asociados al equipo seleccionado.
     */
    private void loadPlayers() {
        if (selectedTeam == null || selectedTeam.getId() == null || selectedTeam.getId().isBlank()) {
            statusLabel.setText("No hay ningún equipo seleccionado");
            return;
        }

        try {
            loadedPlayers = playerService.getPlayersByTeamId(selectedTeam.getId());

            for (PlayerDTO player : loadedPlayers) {
                player.setTeamId(selectedTeam.getId());
            }

            applyFilters();

        } catch (Exception e) {
            e.printStackTrace();
            loadedPlayers = new ArrayList<>();
            playersTable.setItems(FXCollections.observableArrayList());
            statusLabel.setText("Error al cargar jugadores");
        }
    }

    /**
     * Carga todos los jugadores de los equipos visibles ya cargados.
     */
    private void loadAllPlayersFromLoadedTeams() {
        List<PlayerDTO> allPlayers = new ArrayList<>();

        for (TeamDTO team : loadedTeams) {
            if (team == null || team.getId() == null || team.getId().isBlank()) {
                continue;
            }

            try {
                List<PlayerDTO> teamPlayers = playerService.getPlayersByTeamId(team.getId());

                for (PlayerDTO player : teamPlayers) {
                    player.setTeamId(team.getId());
                }

                allPlayers.addAll(teamPlayers);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        loadedPlayers = removeDuplicatePlayers(allPlayers);
        applyFilters();
    }

    /**
     * Recarga manualmente los jugadores o todos los datos visibles.
     */
    @FXML
    private void onReloadClick() {
        if (selectedTeam != null) {
            loadPlayers();
        } else {
            loadVisibleTeamsAndPlayers();
        }
    }

    /**
     * Aplica manualmente el filtro de texto configurado en la interfaz.
     */
    @FXML
    private void onApplyFiltersClick() {
        applyFilters();
    }

    /**
     * Aplica el filtro de texto sobre la lista de jugadores cargados.
     *
     * <p>El filtrado compara el texto introducido con el nombre completo
     * del jugador.</p>
     */
    private void applyFilters() {
        String searchText = "";

        if (searchField != null && searchField.getText() != null) {
            searchText = searchField.getText().trim().toLowerCase();
        }

        final String finalSearchText = searchText;
        final TeamDTO finalSelectedTeam = selectedTeam;

        List<PlayerDTO> filtered = loadedPlayers.stream()
                .filter(player -> {
                    if (finalSelectedTeam == null) {
                        return true;
                    }

                    return finalSelectedTeam.getId() != null
                            && finalSelectedTeam.getId().equals(player.getTeamId());
                })
                .filter(player -> {
                    if (finalSearchText.isBlank()) {
                        return true;
                    }

                    String fullName = ((player.getName() == null ? "" : player.getName()) + " "
                            + (player.getSurname() == null ? "" : player.getSurname())).toLowerCase();

                    return fullName.contains(finalSearchText);
                })
                .collect(Collectors.toList());

        playersTable.setItems(FXCollections.observableArrayList(filtered));

        if (finalSelectedTeam != null) {
            statusLabel.setText("Jugadores del equipo: " + finalSelectedTeam.getName() + " (" + filtered.size() + ")");
        } else {
            statusLabel.setText("Jugadores visibles: " + filtered.size());
        }
    }

    /**
     * Muestra un formulario para crear un nuevo jugador en el equipo seleccionado.
     *
     * <p>El backend espera un {@code PlayerCreateRequest}. En la nueva versión
     * de la API, este request utiliza {@code birthYear} en lugar de {@code age}.</p>
     */
    @FXML
    private void onAddPlayerClick() {
        if (selectedTeam == null || selectedTeam.getId() == null || selectedTeam.getId().isBlank()) {
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
        TextField birthYearField = new TextField();
        TextField emailField = new TextField();
        TextField numberField = new TextField();
        TextField positionField = new TextField();
        TextField priorityField = new TextField();

        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nameField, 1, 0);

        grid.add(new Label("Apellido:"), 0, 1);
        grid.add(surnameField, 1, 1);

        grid.add(new Label("Año nacimiento:"), 0, 2);
        grid.add(birthYearField, 1, 2);

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

        if (result.isEmpty() || result.get() != createButtonType) {
            return;
        }

        try {
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();

            if (name.isBlank() || surname.isBlank()) {
                statusLabel.setText("Nombre y apellido son obligatorios");
                return;
            }

            int birthYear = parseOptionalInt(birthYearField.getText(), 0);
            int number = parseOptionalInt(numberField.getText(), 0);
            int priority = parseOptionalInt(priorityField.getText(), 0);

            playerService.createPlayer(
                    selectedTeam.getId(),
                    name,
                    surname,
                    birthYear,
                    emailField.getText().trim(),
                    number,
                    positionField.getText().trim(),
                    priority
            );

            loadPlayers();
            statusLabel.setText("Jugador creado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al crear jugador");
        }
    }

    /**
     * Elimina equipos duplicados por identificador.
     *
     * @param teams lista de equipos a depurar
     * @return lista sin equipos duplicados
     */
    private List<TeamDTO> removeDuplicateTeams(List<TeamDTO> teams) {
        List<TeamDTO> uniqueTeams = new ArrayList<>();

        for (TeamDTO team : teams) {
            boolean exists = uniqueTeams.stream()
                    .anyMatch(existing -> existing.getId() != null
                            && existing.getId().equals(team.getId()));

            if (!exists) {
                uniqueTeams.add(team);
            }
        }

        return uniqueTeams;
    }

    /**
     * Elimina jugadores duplicados por identificador.
     *
     * @param players lista de jugadores a depurar
     * @return lista sin jugadores duplicados
     */
    private List<PlayerDTO> removeDuplicatePlayers(List<PlayerDTO> players) {
        List<PlayerDTO> uniquePlayers = new ArrayList<>();

        for (PlayerDTO player : players) {
            boolean exists = uniquePlayers.stream()
                    .anyMatch(existing -> existing.getId() != null
                            && existing.getId().equals(player.getId()));

            if (!exists) {
                uniquePlayers.add(player);
            }
        }

        return uniquePlayers;
    }

    /**
     * Convierte un texto numérico a entero.
     *
     * <p>Si el texto está vacío o es nulo, devuelve el valor por defecto.
     * Si el texto no representa un número válido, lanza una excepción.</p>
     *
     * @param value texto a convertir
     * @param defaultValue valor por defecto si el texto está vacío
     * @return número entero resultante
     */
    private int parseOptionalInt(String value, int defaultValue) {
        if (value == null || value.trim().isBlank()) {
            return defaultValue;
        }

        return Integer.parseInt(value.trim());
    }
}