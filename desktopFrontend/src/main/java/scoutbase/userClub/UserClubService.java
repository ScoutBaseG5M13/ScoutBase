package scoutbase.userClub;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import scoutbase.common.ApiClient;
import scoutbase.common.ApiResponse;

import java.util.List;
import java.util.Map;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con UserClub.
 *
 * <p>Esta clase actúa como capa de comunicación entre la aplicación desktop
 * y la API REST del backend para recuperar, crear, actualizar y eliminar
 * UserClubs.</p>
 *
 * <p>El endpoint principal {@code /user-clubs} devuelve los UserClubs
 * en los que participa el usuario autenticado.</p>
 */
public class UserClubService {

    /**
     * Endpoint relativo base para operaciones de UserClub.
     */
    private static final String USER_CLUBS_ENDPOINT = "/user-clubs";

    /**
     * Cliente HTTP utilizado para comunicarse con la API.
     */
    private final ApiClient apiClient = new ApiClient();

    /**
     * Objeto encargado de serializar y deserializar datos JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Obtiene todos los UserClubs en los que participa el usuario autenticado.
     *
     * @return lista de UserClubs disponibles para el usuario actual
     * @throws RuntimeException si ocurre un error durante la petición
     */
    public List<UserClubDTO> getMyUserClubs() {
        try {
            String responseJson = apiClient.get(USER_CLUBS_ENDPOINT);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo UserClubs");

            return response.dataAs(new TypeReference<List<UserClubDTO>>() {});

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo UserClubs", e);
        }
    }

    /**
     * Obtiene un UserClub concreto a partir de su identificador.
     *
     * @param userClubId identificador UUID del UserClub
     * @return UserClub encontrado
     * @throws RuntimeException si el UserClub no existe o la API devuelve error
     */
    public UserClubDTO getUserClubById(String userClubId) {
        try {
            String responseJson = apiClient.get(USER_CLUBS_ENDPOINT + "/" + userClubId);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo UserClub por ID");

            return response.dataAs(UserClubDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo UserClub por ID", e);
        }
    }

    /**
     * Crea un nuevo UserClub.
     *
     * <p>El backend espera un {@code UserClubCreateRequest} cuyo campo
     * obligatorio es {@code name}.</p>
     *
     * @param name nombre del nuevo UserClub
     * @return UserClub creado, si el backend lo devuelve en {@code data}
     * @throws RuntimeException si ocurre un error durante la creación
     */
    public UserClubDTO createUserClub(String name) {
        try {
            Map<String, String> body = Map.of("name", name);
            String jsonBody = objectMapper.writeValueAsString(body);

            String responseJson = apiClient.post(USER_CLUBS_ENDPOINT, jsonBody);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error creando UserClub");

            return response.dataAs(UserClubDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error creando UserClub", e);
        }
    }

    /**
     * Actualiza el nombre de un UserClub existente.
     *
     * <p>El backend espera un {@code UserClubUpdateRequest} con los campos
     * {@code id} y {@code name}.</p>
     *
     * @param userClubId identificador UUID del UserClub
     * @param name nuevo nombre del UserClub
     * @return UserClub actualizado, si el backend lo devuelve en {@code data}
     * @throws RuntimeException si ocurre un error durante la actualización
     */
    public UserClubDTO updateUserClub(String userClubId, String name) {
        try {
            Map<String, String> body = Map.of(
                    "id", userClubId,
                    "name", name
            );

            String jsonBody = objectMapper.writeValueAsString(body);

            String responseJson = apiClient.put(
                    USER_CLUBS_ENDPOINT + "/" + userClubId,
                    jsonBody
            );

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error actualizando UserClub");

            return response.dataAs(UserClubDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error actualizando UserClub", e);
        }
    }

    /**
     * Elimina un UserClub existente.
     *
     * @param userClubId identificador UUID del UserClub a eliminar
     * @throws RuntimeException si ocurre un error durante la eliminación
     */
    public void deleteUserClub(String userClubId) {
        try {
            String responseJson = apiClient.delete(USER_CLUBS_ENDPOINT + "/" + userClubId);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponseWithoutData(response, "Error eliminando UserClub");

        } catch (Exception e) {
            throw new RuntimeException("Error eliminando UserClub", e);
        }
    }

    /**
     * Añade un usuario como administrador de un UserClub.
     *
     * <p>Utiliza el endpoint {@code /user-clubs/{id}/users/{id2}/admin}.</p>
     *
     * @param userClubId identificador UUID del UserClub
     * @param userId identificador UUID del usuario administrador
     * @throws RuntimeException si ocurre un error durante la asignación
     */
    public void addAdmin(String userClubId, String userId) {
        try {
            String responseJson = apiClient.post(
                    USER_CLUBS_ENDPOINT + "/" + userClubId + "/users/" + userId + "/admin",
                    "{}"
            );

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);
            validateResponseWithoutData(response, "Error añadiendo administrador al UserClub");

        } catch (Exception e) {
            throw new RuntimeException("Error añadiendo administrador al UserClub", e);
        }
    }

    /**
     * Elimina un usuario administrador de un UserClub.
     *
     * <p>Utiliza el endpoint {@code /user-clubs/{id}/users/{id2}/admin}
     * mediante una petición DELETE.</p>
     *
     * @param userClubId identificador UUID del UserClub
     * @param userId identificador UUID del usuario administrador
     * @throws RuntimeException si ocurre un error durante la eliminación
     */
    public void removeAdmin(String userClubId, String userId) {
        try {
            String responseJson = apiClient.delete(
                    USER_CLUBS_ENDPOINT + "/" + userClubId + "/users/" + userId + "/admin"
            );

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);
            validateResponseWithoutData(response, "Error eliminando administrador del UserClub");

        } catch (Exception e) {
            throw new RuntimeException("Error eliminando administrador del UserClub", e);
        }
    }

    /**
     * Devuelve el primer UserClub disponible para el usuario autenticado.
     *
     * <p>Este método es útil como punto de entrada temporal para pantallas
     * como clubes, donde se necesita un UserClub activo para cargar
     * los clubes gestionados.</p>
     *
     * @return primer UserClub disponible, o {@code null} si no existe ninguno
     */
    public UserClubDTO getDefaultUserClub() {
        List<UserClubDTO> userClubs = getMyUserClubs();

        if (userClubs == null || userClubs.isEmpty()) {
            return null;
        }

        return userClubs.get(0);
    }

    /**
     * Valida una respuesta estándar que debe contener datos en {@code data}.
     *
     * @param response respuesta recibida desde la API
     * @param defaultMessage mensaje por defecto en caso de error
     */
    private void validateResponse(ApiResponse response, String defaultMessage) {
        validateResponseWithoutData(response, defaultMessage);

        if (response.getData() == null || response.getData().isNull()) {
            throw new RuntimeException(defaultMessage + ": respuesta sin datos");
        }
    }

    /**
     * Valida una respuesta estándar sin exigir datos en {@code data}.
     *
     * @param response respuesta recibida desde la API
     * @param defaultMessage mensaje por defecto en caso de error
     */
    private void validateResponseWithoutData(ApiResponse response, String defaultMessage) {
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