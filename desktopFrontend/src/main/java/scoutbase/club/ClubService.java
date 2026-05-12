package scoutbase.club;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import scoutbase.common.ApiClient;
import scoutbase.common.ApiResponse;

import java.util.List;
import java.util.Map;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con clubes.
 */
public class ClubService {

    private static final String CLUBS_ENDPOINT = "/clubs";
    private static final String USER_CLUBS_ENDPOINT = "/user-clubs";

    private final ApiClient apiClient = new ApiClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<ClubDTO> getAllClubs() {
        try {
            String responseJson = apiClient.get(CLUBS_ENDPOINT);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo clubes");

            return response.dataAs(new TypeReference<List<ClubDTO>>() {});

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo clubes", e);
        }
    }

    public List<ClubDTO> getClubsByUserClub(String userClubId) {
        try {
            String responseJson = apiClient.get(CLUBS_ENDPOINT + "/user-clubs/" + userClubId);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo clubes del UserClub");

            return response.dataAs(new TypeReference<List<ClubDTO>>() {});

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo clubes del UserClub", e);
        }
    }

    public ClubDTO getClubById(String clubId) {
        try {
            String responseJson = apiClient.get(CLUBS_ENDPOINT + "/" + clubId);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo club por ID");

            return response.dataAs(ClubDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo club por ID", e);
        }
    }

    public ClubDTO createClub(String userClubId, String name) {
        try {
            Map<String, String> body = Map.of("name", name);
            String jsonBody = objectMapper.writeValueAsString(body);

            String responseJson = apiClient.post(
                    USER_CLUBS_ENDPOINT + "/" + userClubId + "/clubs",
                    jsonBody
            );

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error creando club");

            return response.dataAs(ClubDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error creando club", e);
        }
    }

    public ClubDTO updateClub(String clubId, String name) {
        try {
            Map<String, String> body = Map.of(
                    "id", clubId,
                    "name", name
            );

            String jsonBody = objectMapper.writeValueAsString(body);
            String responseJson = apiClient.put(CLUBS_ENDPOINT + "/" + clubId, jsonBody);

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error actualizando club");

            return response.dataAs(ClubDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error actualizando club", e);
        }
    }

    public void deleteClub(String clubId) {
        try {
            String responseJson = apiClient.delete(CLUBS_ENDPOINT + "/" + clubId);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponseWithoutData(response, "Error eliminando club");

        } catch (Exception e) {
            throw new RuntimeException("Error eliminando club", e);
        }
    }

    private void validateResponse(ApiResponse response, String defaultMessage) {
        validateResponseWithoutData(response, defaultMessage);

        if (response.getData() == null || response.getData().isNull()) {
            throw new RuntimeException(defaultMessage + ": respuesta sin datos");
        }
    }

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