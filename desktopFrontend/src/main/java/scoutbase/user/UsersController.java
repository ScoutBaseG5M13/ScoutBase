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
 * <p>Permite cargar el listado completo de usuarios desde el backend,
 * crear nuevos usuarios, buscar usuarios por nombre de usuario y
 * mostrar en tabla tanto el listado completo como resultados concretos.</p>
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

    @FXML
    private Label statusLabel;

    /**
     * Lista observable utilizada como modelo de datos para la tabla.
     */
    private final ObservableList<UserDto> usersList = FXCollections.observableArrayList();

    /**
     * Cliente HTTP utilizado para operaciones puntuales contra el backend.
     */
    private final ApiClient apiClient = new ApiClient();

    /**
     * Servicio encargado de obtener el listado completo de usuarios.
     */
    private final UserService userService = new UserService();

    /**
     * Objeto encargado de la serialización y deserialización de JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Inicializa la tabla de usuarios y carga el listado completo desde backend.
     */
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

    /**
     * Carga todos los usuarios desde el backend y actualiza la tabla.
     */
    private void loadUsers() {
        try {
            usersList.setAll(userService.getAllUsers());
            usersTable.setItems(usersList);
            statusLabel.setText("Usuarios cargados correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al cargar usuarios");
        }
    }

    /**
     * Recarga manualmente el listado completo de usuarios.
     */
    @FXML
    private void onReloadClick() {
        loadUsers();
    }

    /**
     * Crea un nuevo usuario a partir de los datos introducidos en el formulario.
     *
     * <p>Si la creación se completa correctamente, recarga el listado
     * completo desde el backend y limpia el formulario.</p>
     */
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
                statusLabel.setText("Todos los campos son obligatorios");
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

            loadUsers();
            clearFields();
            statusLabel.setText("Usuario creado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al crear usuario");
        }
    }

    /**
     * Busca un usuario en el backend a partir del nombre de usuario indicado.
     *
     * <p>Si encuentra un resultado válido, muestra únicamente ese usuario
     * en la tabla.</p>
     */
    @FXML
    private void onSearchUserClick() {
        try {
            String username = usernameField.getText().trim();

            if (username.isBlank()) {
                statusLabel.setText("Introduce un username para buscar");
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

                usersList.setAll(user);
                usersTable.setItems(usersList);
                statusLabel.setText("Usuario encontrado");
            } else {
                usersList.clear();
                usersTable.setItems(usersList);
                statusLabel.setText("No se encontró ningún usuario");
            }

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al buscar usuario");
        }
    }

    /**
     * Carga los datos del usuario autenticado actualmente desde el backend.
     *
     * <p>Si la respuesta es válida, muestra únicamente ese usuario en la tabla.</p>
     */
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

                usersList.setAll(user);
                usersTable.setItems(usersList);
                statusLabel.setText("Usuario actual cargado correctamente");
            } else {
                usersList.clear();
                usersTable.setItems(usersList);
                statusLabel.setText("No se pudo cargar el usuario actual");
            }

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al cargar el usuario actual");
        }
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        roleField.clear();
        nameField.clear();
        surnameField.clear();
        emailField.clear();
    }
}