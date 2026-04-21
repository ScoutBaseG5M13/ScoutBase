package scoutbase.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import scoutbase.common.ApiClient;
import scoutbase.common.ApiResponse;

/**
 * Controlador de la vista de gestión de usuarios.
 *
 * <p>Permite crear usuarios, buscarlos por username y mostrar en tabla
 * los usuarios almacenados localmente mientras backend no disponga
 * de un endpoint para listar todos los usuarios.</p>
 */
public class UsersController {

    @FXML
    private TableView<UserDto> usersTable;

    @FXML
    private TableColumn<UserDto, String> idColumn;

    @FXML
    private TableColumn<UserDto, String> usernameColumn;

    @FXML
    private TableColumn<UserDto, String> roleColumn;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField roleField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField emailField;

    private final ObservableList<UserDto> usersList = FXCollections.observableArrayList();
    private final ApiClient apiClient = new ApiClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getId() != null ? data.getValue().getId() : "")
        );

        usernameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getUsername() != null ? data.getValue().getUsername() : "")
        );

        roleColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getRole() != null ? data.getValue().getRole() : "")
        );

        loadUsers();
    }

    private void loadUsers() {
        usersList.setAll(UserCache.getUsers());
        usersTable.setItems(usersList);
    }

    @FXML
    private void onAddUserClick() {
        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String role = roleField.getText().trim();
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String email = emailField.getText().trim();

            if (username.isBlank() || password.isBlank() || role.isBlank()
                    || name.isBlank() || surname.isBlank() || email.isBlank()) {
                System.out.println("Todos los campos son obligatorios");
                return;
            }

            String body = """
                    {
                      "username": "%s",
                      "password": "%s",
                      "role": "%s",
                      "name": "%s",
                      "surname": "%s",
                      "email": "%s"
                    }
                    """.formatted(username, password, role, name, surname, email);

            String response = apiClient.post(
                    "https://scoutbase-dev-6r6d.onrender.com/api/v1/users",
                    body
            );

            System.out.println("CREATE USER RESPONSE: " + response);

            UserDto user = new UserDto();
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(role);
            user.setName(name);
            user.setSurname(surname);
            user.setEmail(email);

            UserCache.addUser(user);
            loadUsers();
            clearFields();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSearchUserClick() {
        try {
            String username = usernameField.getText().trim();

            if (username.isBlank()) {
                System.out.println("Introduce un username para buscar");
                return;
            }

            String response = apiClient.get(
                    "https://scoutbase-dev-6r6d.onrender.com/api/v1/users/username/" + username
            );

            System.out.println("SEARCH RESPONSE: " + response);

            ApiResponse apiResponse = objectMapper.readValue(response, ApiResponse.class);

            if (apiResponse.getData() != null && !apiResponse.getData().isNull()) {
                UserDto user = objectMapper.treeToValue(apiResponse.getData(), UserDto.class);

                if (user.getRole() == null || user.getRole().isBlank()) {
                    user.setRole("SIN ROL");
                }

                UserCache.addUser(user);
                loadUsers();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onLoadMeClick() {
        try {
            String response = apiClient.get(
                    "https://scoutbase-dev-6r6d.onrender.com/api/v1/users/me"
            );

            System.out.println("ME RESPONSE: " + response);

            ApiResponse apiResponse = objectMapper.readValue(response, ApiResponse.class);

            if (apiResponse.getData() != null && !apiResponse.getData().isNull()) {
                UserDto user = objectMapper.treeToValue(apiResponse.getData(), UserDto.class);

                if (user.getRole() == null || user.getRole().isBlank()) {
                    user.setRole("SIN ROL");
                }

                UserCache.addUser(user);
                loadUsers();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        roleField.clear();
        nameField.clear();
        surnameField.clear();
        emailField.clear();
    }
}