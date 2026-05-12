package scoutbase.stat;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import scoutbase.club.ClubDTO;
import scoutbase.club.ClubService;
import scoutbase.player.PlayerDTO;
import scoutbase.player.PlayerService;
import scoutbase.team.TeamDTO;
import scoutbase.team.TeamService;
import scoutbase.userClub.UserClubDTO;
import scoutbase.userClub.UserClubService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador de la pantalla de estadísticas.
 */
public class StatsController {

    @FXML
    private Label totalTeamsLabel;

    @FXML
    private Label totalPlayersLabel;

    @FXML
    private Label averageAgeLabel;

    @FXML
    private Label averagePriorityLabel;

    @FXML
    private PieChart positionPieChart;

    @FXML
    private PieChart categoryPieChart;

    @FXML
    private TableView<PlayerDTO> topPlayersTable;

    @FXML
    private TableColumn<PlayerDTO, String> playerNameColumn;

    @FXML
    private TableColumn<PlayerDTO, Integer> playerAgeColumn;

    @FXML
    private TableColumn<PlayerDTO, String> playerPositionColumn;

    @FXML
    private TableColumn<PlayerDTO, Integer> playerPriorityColumn;

    @FXML
    private ComboBox<PlayerDTO> playerStatsBox;

    @FXML
    private ComboBox<String> statCodeBox;

    @FXML
    private Spinner<Integer> statValueSpinner;

    @FXML
    private TableView<StatDTO> playerStatsTable;

    @FXML
    private TableColumn<StatDTO, String> statCodeColumn;

    @FXML
    private TableColumn<StatDTO, Integer> statValueColumn;

    @FXML
    private Label statusLabel;

    private final UserClubService userClubService = new UserClubService();
    private final ClubService clubService = new ClubService();
    private final TeamService teamService = new TeamService();
    private final PlayerService playerService = new PlayerService();
    private final StatsService statsService = new StatsService();

    @FXML
    public void initialize() {
        configurePlayersTable();
        configureStatsTable();
        configureStatForm();
        loadStatistics();
    }

    private void configurePlayersTable() {
        playerNameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFullName())
        );

        playerAgeColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getAge()).asObject()
        );

        playerPositionColumn.setCellValueFactory(data ->
                new SimpleStringProperty(valueOrDefault(data.getValue().getPosition(), "SIN POSICIÓN"))
        );

        playerPriorityColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getPriority()).asObject()
        );
    }

    private void configureStatsTable() {
        statCodeColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCode())
        );

        statValueColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().getValue()).asObject()
        );
    }

    private void configureStatForm() {
        statCodeBox.setItems(FXCollections.observableArrayList(
                "PAC",
                "SHO",
                "PAS",
                "DRI",
                "DEF",
                "PHY"
        ));

        statValueSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 5, 3)
        );
    }

    @FXML
    private void onReloadClick() {
        loadStatistics();
    }

    @FXML
    private void onLoadPlayerStatsClick() {
        PlayerDTO selectedPlayer = playerStatsBox.getValue();

        if (selectedPlayer == null) {
            statusLabel.setText("Selecciona un jugador");
            return;
        }

        try {
            List<StatDTO> stats = statsService.getStatsByPlayerId(selectedPlayer.getId());

            playerStatsTable.setItems(FXCollections.observableArrayList(stats));

            statusLabel.setText("Estadísticas cargadas correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error cargando estadísticas");
        }
    }

    @FXML
    private void onAddStatClick() {
        PlayerDTO selectedPlayer = playerStatsBox.getValue();
        String code = statCodeBox.getValue();
        Integer value = statValueSpinner.getValue();

        if (selectedPlayer == null || code == null || value == null) {
            statusLabel.setText("Completa todos los campos");
            return;
        }

        try {
            statsService.createStatForPlayer(
                    selectedPlayer.getId(),
                    code,
                    value
            );

            onLoadPlayerStatsClick();

            statusLabel.setText("Estadística creada correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error creando estadística");
        }
    }

    private void loadStatistics() {
        try {
            StatsData realData = loadRealDataFromBackend();

            if (realData.teams().isEmpty() || realData.players().isEmpty()) {
                System.out.println("StatsController: backend sin datos suficientes. Usando mock.");
                loadMockStatistics("Estadísticas cargadas con datos mock para demo");
                return;
            }

            updateCards(realData.teams(), realData.players());
            updatePositionChart(realData.players());
            updateCategoryChart(realData.teams());
            updateTopPlayersTable(realData.players());

            playerStatsBox.setItems(
                    FXCollections.observableArrayList(realData.players())
            );

            statusLabel.setText("Estadísticas cargadas correctamente desde backend");

        } catch (Exception e) {
            e.printStackTrace();
            loadMockStatistics("Error al cargar backend. Estadísticas simuladas localmente para demo");
        }
    }

    private StatsData loadRealDataFromBackend() {
        List<TeamDTO> allTeams = new ArrayList<>();
        List<PlayerDTO> allPlayers = new ArrayList<>();

        UserClubDTO defaultUserClub = userClubService.getDefaultUserClub();

        if (defaultUserClub == null
                || defaultUserClub.getId() == null
                || defaultUserClub.getId().isBlank()) {
            throw new RuntimeException("No hay UserClub disponible");
        }

        List<ClubDTO> clubs = clubService.getClubsByUserClub(defaultUserClub.getId());

        if (clubs == null || clubs.isEmpty()) {
            return new StatsData(allTeams, allPlayers);
        }

        for (ClubDTO club : clubs) {

            if (club == null || club.getId() == null || club.getId().isBlank()) {
                continue;
            }

            try {
                List<TeamDTO> clubTeams = teamService.getTeamsByClubId(club.getId());

                if (clubTeams == null || clubTeams.isEmpty()) {
                    continue;
                }

                allTeams.addAll(clubTeams);

                for (TeamDTO team : clubTeams) {

                    if (team == null
                            || team.getId() == null
                            || team.getId().isBlank()
                            || team.getId().startsWith("MOCK-")) {
                        continue;
                    }

                    try {
                        List<PlayerDTO> teamPlayers =
                                playerService.getPlayersByTeamId(team.getId());

                        if (teamPlayers != null) {

                            for (PlayerDTO player : teamPlayers) {
                                player.setTeamId(team.getId());
                                allPlayers.add(player);
                            }
                        }

                    } catch (Exception playerException) {
                        playerException.printStackTrace();
                    }
                }

            } catch (Exception teamException) {
                teamException.printStackTrace();
            }
        }

        return new StatsData(allTeams, allPlayers);
    }

    private void loadMockStatistics(String message) {

        List<TeamDTO> mockTeams = createMockTeams();
        List<PlayerDTO> mockPlayers = createMockPlayers();

        updateCards(mockTeams, mockPlayers);
        updatePositionChart(mockPlayers);
        updateCategoryChart(mockTeams);
        updateTopPlayersTable(mockPlayers);

        playerStatsBox.setItems(
                FXCollections.observableArrayList(mockPlayers)
        );

        statusLabel.setText(message);
    }

    private void updateCards(List<TeamDTO> teams, List<PlayerDTO> players) {

        totalTeamsLabel.setText(String.valueOf(teams.size()));
        totalPlayersLabel.setText(String.valueOf(players.size()));

        double averageAge = players.stream()
                .mapToInt(PlayerDTO::getAge)
                .average()
                .orElse(0);

        double averagePriority = players.stream()
                .mapToInt(PlayerDTO::getPriority)
                .average()
                .orElse(0);

        averageAgeLabel.setText(String.format("%.1f", averageAge));
        averagePriorityLabel.setText(String.format("%.1f", averagePriority));
    }

    private void updatePositionChart(List<PlayerDTO> players) {

        Map<String, Integer> counter = new HashMap<>();

        for (PlayerDTO player : players) {

            String position = valueOrDefault(
                    player.getPosition(),
                    "SIN POSICIÓN"
            );

            counter.put(
                    position,
                    counter.getOrDefault(position, 0) + 1
            );
        }

        positionPieChart.setData(FXCollections.observableArrayList(
                counter.entrySet().stream()
                        .map(entry ->
                                new PieChart.Data(entry.getKey(), entry.getValue()))
                        .toList()
        ));
    }

    private void updateCategoryChart(List<TeamDTO> teams) {

        Map<String, Integer> counter = new HashMap<>();

        for (TeamDTO team : teams) {

            String category = valueOrDefault(
                    team.getCategory(),
                    "SIN CATEGORÍA"
            );

            counter.put(
                    category,
                    counter.getOrDefault(category, 0) + 1
            );
        }

        categoryPieChart.setData(FXCollections.observableArrayList(
                counter.entrySet().stream()
                        .map(entry ->
                                new PieChart.Data(entry.getKey(), entry.getValue()))
                        .toList()
        ));
    }

    private void updateTopPlayersTable(List<PlayerDTO> players) {

        List<PlayerDTO> topPlayers = players.stream()
                .sorted(Comparator.comparingInt(PlayerDTO::getPriority).reversed())
                .limit(8)
                .toList();

        topPlayersTable.setItems(
                FXCollections.observableArrayList(topPlayers)
        );
    }

    private List<TeamDTO> createMockTeams() {

        List<TeamDTO> teams = new ArrayList<>();

        teams.add(createMockTeam("MOCK-T1", "Benjamín A", "BENJAMÍN", "SUB-10"));
        teams.add(createMockTeam("MOCK-T2", "Alevín A", "ALEVÍN", "SUB-12"));
        teams.add(createMockTeam("MOCK-T3", "Infantil A", "INFANTIL", "SUB-14"));
        teams.add(createMockTeam("MOCK-T4", "Cadete A", "CADETE", "SUB-16"));
        teams.add(createMockTeam("MOCK-T5", "Juvenil A", "JUVENIL", "SUB17-19"));

        return teams;
    }

    private TeamDTO createMockTeam(
            String id,
            String name,
            String category,
            String subcategory
    ) {

        TeamDTO team = new TeamDTO();

        team.setId(id);
        team.setName(name);
        team.setCategory(category);
        team.setSubcategory(subcategory);

        return team;
    }

    private List<PlayerDTO> createMockPlayers() {

        List<PlayerDTO> players = new ArrayList<>();

        players.add(createMockPlayer("Mario Roberto", 14, "MEDIOCENTRO", 9));
        players.add(createMockPlayer("Romano López", 15, "MEDIAPUNTA", 8));
        players.add(createMockPlayer("Alfredo Flores", 13, "MEDIAPUNTA", 7));
        players.add(createMockPlayer("Carlos Gómez", 16, "DEFENSA", 8));
        players.add(createMockPlayer("Iván Ruiz", 12, "DELANTERO", 9));
        players.add(createMockPlayer("Pablo Sánchez", 11, "PORTERO", 6));
        players.add(createMockPlayer("Álex Martín", 15, "EXTREMO", 7));
        players.add(createMockPlayer("Daniel Pérez", 14, "DEFENSA", 6));

        return players;
    }

    private PlayerDTO createMockPlayer(
            String fullName,
            int age,
            String position,
            int priority
    ) {

        String[] parts = fullName.split(" ", 2);

        PlayerDTO player = new PlayerDTO();

        player.setId("MOCK-" + fullName.replace(" ", "-"));
        player.setName(parts[0]);
        player.setSurname(parts.length > 1 ? parts[1] : "");
        player.setAge(age);
        player.setPosition(position);
        player.setPriority(priority);

        return player;
    }

    private String valueOrDefault(String value, String defaultValue) {
        return value != null && !value.isBlank()
                ? value
                : defaultValue;
    }

    private record StatsData(
            List<TeamDTO> teams,
            List<PlayerDTO> players
    ) {
    }
}