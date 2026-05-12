package scoutbase.scout;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import scoutbase.userTeam.UserTeamDTO;
import scoutbase.userTeam.UserTeamService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private final ScoutService scoutService = new ScoutService();
    private final UserClubService userClubService = new UserClubService();
    private final ClubService clubService = new ClubService();
    private final TeamService teamService = new TeamService();
    private final UserTeamService userTeamService = new UserTeamService();

    private final ObservableList<ScoutDTO> scoutsList = FXCollections.observableArrayList();

    private List<ClubDTO> availableClubs = new ArrayList<>();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        scoutsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        scoutsTable.setItems(scoutsList);

        loadClubs();
        loadScouts();
    }

    private void loadClubs() {
        try {
            UserClubDTO defaultUserClub = userClubService.getDefaultUserClub();

            if (defaultUserClub == null
                    || defaultUserClub.getId() == null
                    || defaultUserClub.getId().isBlank()) {
                availableClubs = new ArrayList<>();
                statusLabel.setText("No hay UserClub disponible para cargar clubes");
                return;
            }

            availableClubs = clubService.getClubsByUserClub(defaultUserClub.getId());

            if (availableClubs == null) {
                availableClubs = new ArrayList<>();
            }

        } catch (Exception e) {
            e.printStackTrace();
            availableClubs = new ArrayList<>();
            statusLabel.setText("Error al cargar clubes");
        }
    }

    private void loadScouts() {
        try {
            List<ScoutDTO> backendScouts = scoutService.getAllScouts();

            scoutsList.clear();

            if (backendScouts != null) {
                scoutsList.addAll(backendScouts);
            }

            statusLabel.setText("Scouts cargados correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al cargar scouts. Se mantienen los scouts temporales en memoria.");
        }
    }

    @FXML
    private void onReloadClick() {
        loadClubs();
        loadScouts();
    }

    @FXML
    private void onAddScoutClick() {
        if (availableClubs == null || availableClubs.isEmpty()) {
            loadClubs();
        }

        if (availableClubs == null || availableClubs.isEmpty()) {
            statusLabel.setText("No hay clubes disponibles para asignar el scout");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Nuevo scout");
        dialog.setHeaderText("Crear usuario scout y asignarlo a un equipo");

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

        ComboBox<ClubDTO> clubComboBox = new ComboBox<>();
        ComboBox<TeamDTO> teamComboBox = new ComboBox<>();

        clubComboBox.setItems(FXCollections.observableArrayList(availableClubs));
        teamComboBox.setDisable(true);

        clubComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(ClubDTO club) {
                return club == null ? "" : club.getName();
            }

            @Override
            public ClubDTO fromString(String string) {
                return null;
            }
        });

        teamComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(TeamDTO team) {
                return team == null ? "" : team.getName();
            }

            @Override
            public TeamDTO fromString(String string) {
                return null;
            }
        });

        clubComboBox.valueProperty().addListener((observable, oldClub, selectedClub) -> {
            teamComboBox.getSelectionModel().clearSelection();

            if (selectedClub == null
                    || selectedClub.getId() == null
                    || selectedClub.getId().isBlank()) {
                teamComboBox.setItems(FXCollections.observableArrayList());
                teamComboBox.setDisable(true);
                return;
            }

            try {
                List<TeamDTO> teams = teamService.getTeamsByClubId(selectedClub.getId());

                if (teams == null || teams.isEmpty()) {
                    teamComboBox.setItems(FXCollections.observableArrayList());
                    teamComboBox.setDisable(true);
                    statusLabel.setText("El club seleccionado no tiene equipos");
                    return;
                }

                teamComboBox.setItems(FXCollections.observableArrayList(teams));
                teamComboBox.setDisable(false);

            } catch (Exception e) {
                e.printStackTrace();
                teamComboBox.setItems(FXCollections.observableArrayList());
                teamComboBox.setDisable(true);
                statusLabel.setText("Error al cargar equipos del club seleccionado");
            }
        });

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

        grid.add(new Label("Club:"), 0, 5);
        grid.add(clubComboBox, 1, 5);

        grid.add(new Label("Equipo:"), 0, 6);
        grid.add(teamComboBox, 1, 6);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isEmpty() || result.get() != createButtonType) {
            return;
        }

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String name = nameField.getText().trim();
        String surname = surnameField.getText().trim();
        String email = emailField.getText().trim();

        ClubDTO selectedClub = clubComboBox.getValue();
        TeamDTO selectedTeam = teamComboBox.getValue();

        if (username.isBlank() || password.isBlank() || name.isBlank() || email.isBlank()) {
            statusLabel.setText("Username, password, nombre y email son obligatorios");
            return;
        }

        if (selectedClub == null) {
            statusLabel.setText("Selecciona un club");
            return;
        }

        if (selectedTeam == null
                || selectedTeam.getId() == null
                || selectedTeam.getId().isBlank()) {
            statusLabel.setText("Selecciona un equipo");
            return;
        }

        try {
            ScoutDTO createdScout = scoutService.createScout(
                    username,
                    password,
                    name,
                    surname,
                    email
            );

            addScoutToTableIfMissing(createdScout);

            tryAssignScoutToTeam(createdScout, selectedTeam);

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al crear scout");
        }
    }

    private void tryAssignScoutToTeam(ScoutDTO createdScout, TeamDTO selectedTeam) {
        try {
            UserClubDTO defaultUserClub = userClubService.getDefaultUserClub();

            if (defaultUserClub == null
                    || defaultUserClub.getId() == null
                    || defaultUserClub.getId().isBlank()) {
                statusLabel.setText("Scout creado y mostrado localmente. No se encontró UserClub activo.");
                return;
            }

            UserTeamDTO userTeam = userTeamService.getOrCreateUserTeamForTeam(
                    defaultUserClub.getId(),
                    selectedTeam
            );

            if (userTeam == null
                    || userTeam.getId() == null
                    || userTeam.getId().isBlank()) {
                statusLabel.setText("Scout creado y mostrado localmente. Asignación pendiente por UserTeam no resuelto.");
                return;
            }

            scoutService.addScoutToUserTeam(
                    userTeam.getId(),
                    createdScout.getId()
            );

            statusLabel.setText("Scout creado y asignado correctamente al equipo: " + selectedTeam.getName());

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Scout creado y mostrado localmente. Asignación pendiente por error backend.");
        }
    }

    private void addScoutToTableIfMissing(ScoutDTO scout) {
        if (scout == null) {
            return;
        }

        boolean alreadyExists = scoutsList.stream()
                .anyMatch(existingScout ->
                        existingScout.getId() != null
                                && scout.getId() != null
                                && existingScout.getId().equals(scout.getId())
                );

        if (!alreadyExists) {
            scoutsList.add(scout);
        }
    }
}