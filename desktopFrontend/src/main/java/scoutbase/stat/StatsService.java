package scoutbase.stat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import scoutbase.common.ApiClient;
import scoutbase.common.ApiResponse;

import java.util.List;
import java.util.Map;

/**
 * Servicio encargado de consumir los endpoints de estadísticas.
 */
public class StatsService {

    private static final String STATS_ENDPOINT = "/stats";
    private static final String PLAYERS_ENDPOINT = "/players";

    private final ApiClient apiClient = new ApiClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<StatDTO> getAllStats() {
        try {
            String responseJson = apiClient.get(STATS_ENDPOINT);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo estadísticas");

            return response.dataAs(new TypeReference<List<StatDTO>>() {});

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo estadísticas", e);
        }
    }

    public List<StatDTO> getStatsByPlayerId(String playerId) {
        try {
            String responseJson = apiClient.get(STATS_ENDPOINT + "/players/" + playerId);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo estadísticas del jugador");

            return response.dataAs(new TypeReference<List<StatDTO>>() {});

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo estadísticas del jugador", e);
        }
    }

    public StatDTO createStatForPlayer(String playerId, String code, int value) {
        try {
            Map<String, Object> body = Map.of(
                    "code", code,
                    "value", value
            );

            String jsonBody = objectMapper.writeValueAsString(body);

            String responseJson = apiClient.post(
                    PLAYERS_ENDPOINT + "/" + playerId + "/stats",
                    jsonBody
            );

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error creando estadística");

            return response.dataAs(StatDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error creando estadística", e);
        }
    }

    public StatDTO updateStat(String statId, String code, int value) {
        try {
            Map<String, Object> body = Map.of(
                    "id", statId,
                    "code", code,
                    "value", value
            );

            String jsonBody = objectMapper.writeValueAsString(body);

            String responseJson = apiClient.put(
                    STATS_ENDPOINT + "/" + statId,
                    jsonBody
            );

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error actualizando estadística");

            return response.dataAs(StatDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error actualizando estadística", e);
        }
    }

    public void deleteStat(String statId) {
        try {
            String responseJson = apiClient.delete(STATS_ENDPOINT + "/" + statId);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponseWithoutData(response, "Error eliminando estadística");

        } catch (Exception e) {
            throw new RuntimeException("Error eliminando estadística", e);
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