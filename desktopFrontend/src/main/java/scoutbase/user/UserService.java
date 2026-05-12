package scoutbase.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import scoutbase.common.ApiClient;
import scoutbase.common.ApiResponse;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con usuarios.
 *
 * <p>Esta clase actúa como capa intermedia entre los controladores JavaFX
 * y la API REST del backend, centralizando las peticiones relacionadas
 * con la entidad usuario.</p>
 *
 * <p>Las respuestas del backend se reciben encapsuladas en {@link ApiResponse}
 * y posteriormente se convierten a objetos {@link UserDto}, listas tipadas
 * o valores simples como el rol del usuario dentro de un UserClub.</p>
 */
public class UserService {

    /**
     * Endpoint relativo base para las operaciones de usuarios.
     */
    private static final String USERS_ENDPOINT = "/users";

    /**
     * Cliente HTTP común utilizado para comunicarse con la API.
     */
    private final ApiClient apiClient = new ApiClient();

    /**
     * Objeto encargado de serializar y deserializar datos JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Obtiene todos los usuarios disponibles desde el backend.
     *
     * <p>Este endpoint requiere permisos de SUPERADMIN según la configuración
     * actual del backend.</p>
     *
     * @return lista de usuarios registrados en el sistema
     * @throws RuntimeException si ocurre un error al obtener o mapear los datos
     */
    public List<UserDto> getAllUsers() {
        try {
            String responseJson = apiClient.get(USERS_ENDPOINT);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo usuarios");

            return response.dataAs(new TypeReference<List<UserDto>>() {});

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo usuarios", e);
        }
    }

    /**
     * Obtiene un usuario concreto a partir de su identificador único.
     *
     * @param id identificador UUID del usuario
     * @return usuario encontrado
     * @throws RuntimeException si el usuario no existe o la API devuelve error
     */
    public UserDto getUserById(String id) {
        try {
            String responseJson = apiClient.get(USERS_ENDPOINT + "/" + id);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo usuario por ID");

            return response.dataAs(UserDto.class);

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo usuario por ID", e);
        }
    }

    /**
     * Obtiene un usuario a partir de su nombre de usuario.
     *
     * @param username nombre de usuario a buscar
     * @return usuario encontrado
     * @throws RuntimeException si el usuario no existe o la API devuelve error
     */
    public UserDto getUserByUsername(String username) {
        try {
            String responseJson = apiClient.get(USERS_ENDPOINT + "/username/" + username);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo usuario por username");

            return response.dataAs(UserDto.class);

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo usuario por username", e);
        }
    }

    /**
     * Obtiene los datos del usuario autenticado actualmente.
     *
     * @return usuario asociado al token JWT almacenado en sesión
     * @throws RuntimeException si no hay sesión válida o el backend devuelve error
     */
    public UserDto getCurrentUser() {
        try {
            String responseJson = apiClient.get(USERS_ENDPOINT + "/me");
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo usuario autenticado");

            return response.dataAs(UserDto.class);

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo usuario autenticado", e);
        }
    }

    /**
     * Crea un nuevo usuario en el backend.
     *
     * <p>El backend espera un {@code UserCreateRequest} con username, password,
     * name, surname y email. No se envía el rol porque los roles funcionales
     * dependen normalmente de UserClub o UserTeam.</p>
     *
     * @param username nombre de usuario
     * @param password contraseña
     * @param name nombre real
     * @param surname apellidos
     * @param email correo electrónico
     * @return usuario creado si el backend lo devuelve; {@code null} si la respuesta no contiene objeto usuario
     * @throws RuntimeException si ocurre un error durante la creación
     */
    public UserDto createUser(String username,
                              String password,
                              String name,
                              String surname,
                              String email) {
        try {
            Map<String, String> requestBody = new LinkedHashMap<>();
            requestBody.put("username", username);
            requestBody.put("password", password);
            requestBody.put("name", name);
            requestBody.put("surname", surname);
            requestBody.put("email", email);

            String jsonBody = objectMapper.writeValueAsString(requestBody);
            String responseJson = apiClient.post(USERS_ENDPOINT, jsonBody);

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponseWithoutRequiredData(response, "Error creando usuario");

            if (response.getData() == null
                    || response.getData().isNull()
                    || response.getData().isBoolean()) {
                return null;
            }

            return response.dataAs(UserDto.class);

        } catch (Exception e) {
            throw new RuntimeException("Error creando usuario", e);
        }
    }

    /**
     * Actualiza los datos básicos de un usuario existente.
     *
     * <p>Si la contraseña llega vacía o nula, no se incluye en el cuerpo
     * de la petición para evitar sobrescribirla accidentalmente.</p>
     *
     * @param userId identificador UUID del usuario
     * @param username nombre de usuario actualizado
     * @param password nueva contraseña opcional
     * @param name nombre actualizado
     * @param surname apellidos actualizados
     * @param email correo electrónico actualizado
     * @return usuario actualizado si el backend lo devuelve; {@code null} si no devuelve objeto usuario
     * @throws RuntimeException si ocurre un error durante la actualización
     */
    public UserDto updateUser(String userId,
                              String username,
                              String password,
                              String name,
                              String surname,
                              String email) {
        try {
            Map<String, String> requestBody = new LinkedHashMap<>();
            requestBody.put("id", userId);
            requestBody.put("username", username);
            requestBody.put("name", name);
            requestBody.put("surname", surname);
            requestBody.put("email", email);

            if (password != null && !password.isBlank()) {
                requestBody.put("password", password);
            }

            String jsonBody = objectMapper.writeValueAsString(requestBody);
            String responseJson = apiClient.put(USERS_ENDPOINT + "/" + userId, jsonBody);

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponseWithoutRequiredData(response, "Error actualizando usuario");

            if (response.getData() == null
                    || response.getData().isNull()
                    || response.getData().isBoolean()) {
                return null;
            }

            return response.dataAs(UserDto.class);

        } catch (Exception e) {
            throw new RuntimeException("Error actualizando usuario", e);
        }
    }

    /**
     * Elimina un usuario existente a partir de su identificador.
     *
     * @param userId identificador UUID del usuario a eliminar
     * @throws RuntimeException si ocurre un error durante la eliminación
     */
    public void deleteUser(String userId) {
        try {
            String responseJson = apiClient.delete(USERS_ENDPOINT + "/" + userId);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponseWithoutRequiredData(response, "Error eliminando usuario");

        } catch (Exception e) {
            throw new RuntimeException("Error eliminando usuario", e);
        }
    }

    /**
     * Obtiene el rol del usuario autenticado dentro de un UserClub concreto.
     *
     * <p>Utiliza el endpoint {@code /users/user-clubs/{id}/role}, donde
     * {@code id} corresponde al UserClub activo. Este rol representa permisos
     * dentro del contexto del club, no necesariamente un rol global del usuario.</p>
     *
     * @param userClubId identificador UUID del UserClub
     * @return rol del usuario autenticado dentro del UserClub
     * @throws RuntimeException si ocurre un error durante la petición
     */
    public String getCurrentUserRoleInsideClub(String userClubId) {
        try {
            String responseJson = apiClient.get(USERS_ENDPOINT + "/user-clubs/" + userClubId + "/role");
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo rol del usuario en UserClub");

            if (response.getData().isTextual()) {
                return response.getData().asText();
            }

            return response.getData().toString();

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo rol del usuario en UserClub", e);
        }
    }

    /**
     * Valida una respuesta estándar recibida desde la API exigiendo datos.
     *
     * @param response respuesta recibida desde el backend
     * @param defaultMessage mensaje por defecto si la respuesta no es correcta
     * @throws RuntimeException si la respuesta es nula, incorrecta o no contiene datos
     */
    private void validateResponse(ApiResponse response, String defaultMessage) {
        validateResponseWithoutRequiredData(response, defaultMessage);

        if (response.getData() == null || response.getData().isNull()) {
            throw new RuntimeException(defaultMessage + ": respuesta sin datos");
        }
    }

    /**
     * Valida una respuesta estándar recibida desde la API sin exigir datos.
     *
     * <p>Se utiliza en endpoints que pueden devolver {@code data: true}
     * o no devolver un objeto completo.</p>
     *
     * @param response respuesta recibida desde el backend
     * @param defaultMessage mensaje por defecto si la respuesta no es correcta
     * @throws RuntimeException si la respuesta es nula o indica fallo
     */
    private void validateResponseWithoutRequiredData(ApiResponse response, String defaultMessage) {
        if (response == null) {
            throw new RuntimeException(defaultMessage);
        }

        if (!response.isSuccess()) {
            throw new RuntimeException(response.getMessage() != null
                    ? response.getMessage()
                    : defaultMessage);
        }
    }
}