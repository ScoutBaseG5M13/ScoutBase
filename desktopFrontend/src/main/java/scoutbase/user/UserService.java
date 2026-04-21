package scoutbase.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import scoutbase.common.ApiClient;
import scoutbase.common.ApiResponse;

import java.util.List;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con usuarios.
 *
 * <p>Permite obtener usuarios desde el backend y realizar operaciones
 * de consulta o creación según los endpoints disponibles.</p>
 */
public class UserService {

    /**
     * URL base del endpoint de usuarios en el backend.
     */
    private static final String BASE_URL =
            "https://scoutbase-dev-6r6d.onrender.com/api/v1/users";

    /**
     * Cliente HTTP utilizado para comunicarse con la API.
     */
    private final ApiClient apiClient = new ApiClient();

    /**
     * Objeto encargado de la serialización y deserialización de JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Obtiene todos los usuarios disponibles desde el backend.
     *
     * @return lista de usuarios
     * @throws RuntimeException si ocurre un error al obtener o mapear los datos
     */
    public List<UserDto> getAllUsers() {
        try {
            String responseJson = apiClient.get(BASE_URL);

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            if (!response.isSuccess()) {
                throw new RuntimeException(response.getMessage());
            }

            JsonNode data = response.getData();

            return objectMapper
                    .readerForListOf(UserDto.class)
                    .readValue(data);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error obteniendo usuarios", e);
        }
    }
}