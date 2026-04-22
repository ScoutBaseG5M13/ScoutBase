package scoutbase.club;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import scoutbase.common.ApiClient;
import scoutbase.common.ApiResponse;

import java.util.List;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con clubes.
 *
 * <p>Proporciona métodos para obtener la lista de clubes desde el backend
 * y para crear nuevos clubes mediante peticiones HTTP.</p>
 */
public class ClubService {

    /**
     * URL base del endpoint de clubes en el backend.
     */
    private static final String BASE_URL = "https://scoutbase-dev-6r6d.onrender.com/api/v1/clubs";

    /**
     * Cliente HTTP encargado de realizar las peticiones a la API.
     */
    private final ApiClient apiClient = new ApiClient();

    /**
     * Objeto utilizado para la serialización y deserialización de JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Obtiene la lista completa de clubes desde el backend.
     *
     * <p>Realiza una petición GET al endpoint de clubes, procesa la respuesta
     * y convierte los datos recibidos en una lista de objetos {@link ClubDTO}.</p>
     *
     * @return lista de clubes
     * @throws RuntimeException si ocurre un error durante la petición
     *                          o la respuesta no es válida
     */
    public List<ClubDTO> getAllClubs() {
        try {
            String responseJson = apiClient.get(BASE_URL);

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            if (!response.isSuccess()) {
                throw new RuntimeException(response.getMessage());
            }

            JsonNode data = response.getData();

            return objectMapper
                    .readerForListOf(ClubDTO.class)
                    .readValue(data);

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo clubs", e);
        }
    }

    /**
     * Crea un nuevo club con el nombre indicado.
     *
     * <p>Envía una petición POST al backend con el nombre del club.
     * Este método no asigna administradores explícitamente.</p>
     *
     * @param name nombre del nuevo club
     * @throws RuntimeException si ocurre un error durante la creación
     */
    public void createClub(String name) {
        try {
            String jsonBody = """
                {
                  "name": "%s"
                }
                """.formatted(name);

            String responseJson = apiClient.post(BASE_URL, jsonBody);

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            if (!response.isSuccess()) {
                throw new RuntimeException(response.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creando club", e);
        }
    }

    /**
     * Crea un nuevo club asignando un usuario administrador.
     *
     * <p>Envía una petición POST al backend incluyendo el identificador
     * del usuario administrador, el nombre del club y una lista inicial
     * vacía de equipos.</p>
     *
     * @param name nombre del nuevo club
     * @param adminUserId identificador del usuario que será administrador del club
     * @throws RuntimeException si ocurre un error durante la creación
     */
    public void createClub(String name, String adminUserId) {
        try {
            String jsonBody = """
                {
                  "adminUserIds": ["%s"],
                  "name": "%s",
                  "teams": []
                }
                """.formatted(adminUserId, name);

            System.out.println("CREATE CLUB BODY: " + jsonBody);

            String responseJson = apiClient.post(BASE_URL, jsonBody);

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            if (!response.isSuccess()) {
                throw new RuntimeException(response.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creando club", e);
        }
    }
}