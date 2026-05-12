package scoutbase.user;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

/**
 * Controlador de la vista de gestión de usuarios.
 *
 * <p>Permite cargar usuarios desde el backend, crear nuevos usuarios,
 * buscar usuarios por nombre de usuario, consultar el usuario autenticado,
 * modificar usuarios existentes y eliminar el usuario seleccionado.</p>
 *
 * <p>La vista trabaja con objetos {@link UserDto} y delega las operaciones
 * principales en {@link UserService}, manteniendo el controlador centrado
 * en la gestión de la interfaz JavaFX.</p>
 *
 * <p>En la estructura actual del backend, el rol global del usuario puede
 * representarse mediante el campo {@code superAdmin}. Los roles funcionales
 * como ADMIN, TRAINER o SCOUTER pueden depender del contexto de un UserClub
 * o UserTeam concreto.</p>
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
    private ComboBox<String> roleComboBox;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField emailField;

    @FXML
    private Label statusLabel;

    /**
     * Rol de superadministrador global del sistema.
     */
    private static final String ROLE_SUPERADMIN = "SUPERADMIN";

    /**
     * Rol de administrador contextual de club.
     */
    private static final String ROLE_ADMIN = "ADMIN";

    /**
     * Rol de entrenador principal contextual de equipo.
     */
    private static final String ROLE_TRAINER = "TRAINER";

    /**
     * Rol de segundo entrenador contextual de equipo.
     */
    private static final String ROLE_SECOND_TRAINER = "SECOND_TRAINER";

    /**
     * Rol de scouter contextual.
     */
    private static final String ROLE_SCOUTER = "SCOUTER";

    /**
     * Lista observable utilizada como modelo de datos para la tabla.
     */
    private final ObservableList<UserDto> usersList = FXCollections.observableArrayList();

    /**
     * Servicio encargado de gestionar las operaciones de usuarios.
     */
    private final UserService userService = new UserService();

    /**
     * Inicializa la tabla de usuarios, configura el selector de roles,
     * prepara la selección de filas y carga el listado completo desde el backend.
     */
    @FXML
    public void initialize() {
        configureRoleComboBox();
        configureTableColumns();
        configureTableSelection();

        usersTable.setItems(usersList);
        loadUsers();
    }

    /**
     * Configura el desplegable de roles disponibles en la interfaz.
     *
     * <p>Estos roles proceden de los valores definidos por el backend.
     * Actualmente se muestran para preparar futuras asignaciones de permisos,
     * aunque la creación básica de usuario no envía todavía el rol porque
     * el {@code UserCreateRequest} documentado no lo incluye.</p>
     */
    private void configureRoleComboBox() {
        roleComboBox.setItems(FXCollections.observableArrayList(
                ROLE_SUPERADMIN,
                ROLE_ADMIN,
                ROLE_TRAINER,
                ROLE_SECOND_TRAINER,
                ROLE_SCOUTER
        ));
    }

    /**
     * Configura las columnas de la tabla de usuarios.
     */
    private void configureTableColumns() {
        idColumn.setCellValueFactory(data ->
                new SimpleStringProperty(valueOrEmpty(data.getValue().getId()))
        );

        usernameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(valueOrEmpty(data.getValue().getUsername()))
        );

        roleColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDisplayRole())
        );
    }

    /**
     * Configura el comportamiento al seleccionar un usuario en la tabla.
     *
     * <p>Al seleccionar una fila, sus datos principales se cargan en el formulario
     * para facilitar la modificación o eliminación.</p>
     */
    private void configureTableSelection() {
        usersTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, selectedUser) -> {
                    if (selectedUser != null) {
                        fillForm(selectedUser);
                    }
                });
    }

    /**
     * Carga todos los usuarios desde el backend y actualiza la tabla.
     *
     * <p>Este endpoint puede requerir permisos de SUPERADMIN. Si el usuario
     * autenticado no tiene permisos suficientes, se muestra un mensaje
     * informativo en la interfaz.</p>
     */
    private void loadUsers() {
        try {
            usersList.setAll(userService.getAllUsers());
            statusLabel.setText("Usuarios cargados correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("No tienes permisos para cargar todos los usuarios");
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
     * <p>El rol seleccionado se valida para la interfaz, pero no se envía
     * al endpoint de creación porque los roles funcionales se gestionan
     * mediante relaciones de UserClub/UserTeam.</p>
     */
    @FXML
    private void onAddUserClick() {
        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String selectedRole = roleComboBox.getValue();
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String email = emailField.getText().trim();

            if (username.isBlank() || password.isBlank()
                    || name.isBlank() || email.isBlank()) {
                statusLabel.setText("Username, password, nombre y email son obligatorios");
                return;
            }

            if (selectedRole == null || selectedRole.isBlank()) {
                statusLabel.setText("Selecciona un rol");
                return;
            }

            userService.createUser(username, password, name, surname, email);

            loadUsers();
            clearFields();
            statusLabel.setText("Usuario creado correctamente. Rol seleccionado pendiente de asignación contextual: " + selectedRole);

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al crear usuario");
        }
    }

    /**
     * Modifica los datos del usuario seleccionado en la tabla.
     *
     * <p>El rol seleccionado no se envía porque los roles contextuales
     * se gestionan mediante UserClub/UserTeam.</p>
     */
    @FXML
    private void onUpdateUserClick() {
        UserDto selectedUser = usersTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            statusLabel.setText("Selecciona un usuario para modificar");
            return;
        }

        try {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String selectedRole = roleComboBox.getValue();
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            String email = emailField.getText().trim();

            if (username.isBlank() || name.isBlank() || email.isBlank()) {
                statusLabel.setText("Username, nombre y email son obligatorios");
                return;
            }

            userService.updateUser(
                    selectedUser.getId(),
                    username,
                    password,
                    name,
                    surname,
                    email
            );

            loadUsers();
            clearFields();

            if (selectedRole != null && !selectedRole.isBlank()) {
                statusLabel.setText("Usuario modificado correctamente. Rol seleccionado pendiente de asignación contextual: " + selectedRole);
            } else {
                statusLabel.setText("Usuario modificado correctamente");
            }

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al modificar usuario");
        }
    }

    /**
     * Elimina el usuario seleccionado en la tabla.
     */
    @FXML
    private void onDeleteUserClick() {
        UserDto selectedUser = usersTable.getSelectionModel().getSelectedItem();

        if (selectedUser == null) {
            statusLabel.setText("Selecciona un usuario para eliminar");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Eliminar usuario");
        confirmation.setHeaderText("¿Seguro que quieres eliminar este usuario?");
        confirmation.setContentText("Usuario: " + selectedUser.getUsername());

        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isEmpty() || result.get() != ButtonType.OK) {
            return;
        }

        try {
            userService.deleteUser(selectedUser.getId());

            usersList.remove(selectedUser);
            clearFields();
            statusLabel.setText("Usuario eliminado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error al eliminar usuario");
        }
    }

    /**
     * Busca un usuario en el backend a partir del nombre de usuario indicado.
     */
    @FXML
    private void onSearchUserClick() {
        try {
            String username = usernameField.getText().trim();

            if (username.isBlank()) {
                statusLabel.setText("Introduce un username para buscar");
                return;
            }

            UserDto user = userService.getUserByUsername(username);

            usersList.setAll(user);
            usersTable.getSelectionModel().select(user);
            statusLabel.setText("Usuario encontrado");

        } catch (Exception e) {
            e.printStackTrace();
            usersList.clear();
            statusLabel.setText("Error al buscar usuario");
        }
    }

    /**
     * Carga los datos del usuario autenticado actualmente desde el backend.
     */
    @FXML
    private void onLoadMeClick() {
        try {
            UserDto user = userService.getCurrentUser();

            usersList.setAll(user);
            usersTable.getSelectionModel().select(user);
            statusLabel.setText("Usuario actual cargado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            usersList.clear();
            statusLabel.setText("Error al cargar el usuario actual");
        }
    }

    /**
     * Limpia manualmente todos los campos del formulario.
     */
    @FXML
    private void onClearFieldsClick() {
        clearFields();
        usersTable.getSelectionModel().clearSelection();
        statusLabel.setText("Formulario limpiado");
    }

    /**
     * Carga los datos de un usuario en el formulario.
     *
     * @param user usuario seleccionado en la tabla
     */
    private void fillForm(UserDto user) {
        usernameField.setText(valueOrEmpty(user.getUsername()));
        passwordField.clear();
        nameField.setText(valueOrEmpty(user.getName()));
        surnameField.setText(valueOrEmpty(user.getSurname()));
        emailField.setText(valueOrEmpty(user.getEmail()));

        String displayRole = user.getDisplayRole();

        if (displayRole != null
                && !displayRole.isBlank()
                && !displayRole.equals("SIN ROL GLOBAL")) {
            roleComboBox.setValue(displayRole);
        } else {
            roleComboBox.getSelectionModel().clearSelection();
        }
    }

    /**
     * Limpia todos los campos del formulario.
     */
    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().clearSelection();
        nameField.clear();
        surnameField.clear();
        emailField.clear();
    }

    /**
     * Devuelve una cadena vacía cuando el valor recibido es nulo.
     *
     * @param value valor a comprobar
     * @return valor original o cadena vacía
     */
    private String valueOrEmpty(String value) {
        return value != null ? value : "";
    }
}