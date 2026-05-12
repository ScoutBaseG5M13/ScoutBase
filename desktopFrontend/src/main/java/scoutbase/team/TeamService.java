package scoutbase.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import scoutbase.common.ApiClient;
import scoutbase.common.ApiResponse;

import java.text.Normalizer;
import java.util.List;
import java.util.Map;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con equipos.
 */
public class TeamService {

    private static final String TEAMS_ENDPOINT = "/teams";
    private static final String CLUBS_ENDPOINT = "/clubs";

    private final ApiClient apiClient = new ApiClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<TeamDTO> getTeamsByClubId(String clubId) {
        try {
            String responseJson = apiClient.get(TEAMS_ENDPOINT + "/clubs/" + clubId);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo equipos del club");

            return response.dataAs(new TypeReference<List<TeamDTO>>() {});

        } catch (Exception e) {
            String message = e.getMessage() != null ? e.getMessage() : "";

            if (message.contains("Club.getTeams()")
                    || message.contains("getTeams()\" is null")
                    || message.contains("NullPointerException")) {
                return List.of();
            }

            throw new RuntimeException("Error obteniendo equipos del club", e);
        }
    }

    public TeamDTO getTeamById(String teamId) {
        try {
            String responseJson = apiClient.get(TEAMS_ENDPOINT + "/" + teamId);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo equipo por ID");

            return response.dataAs(TeamDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo equipo por ID", e);
        }
    }

    public List<TeamCategoryDTO> getCategories() {
        try {
            String responseJson = apiClient.get(TEAMS_ENDPOINT + "/categories");
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo categorías de equipos");

            return response.dataAs(new TypeReference<List<TeamCategoryDTO>>() {});

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo categorías de equipos", e);
        }
    }

    public TeamDTO createTeam(String clubId, String name, String category, String subcategory) {
        try {
            String normalizedCategory = normalizeCategory(category);
            String normalizedSubcategory = normalizeSubcategory(subcategory);

            Map<String, String> body = Map.of(
                    "name", name,
                    "category", normalizedCategory,
                    "subcategory", normalizedSubcategory
            );

            String jsonBody = objectMapper.writeValueAsString(body);

            String responseJson = apiClient.post(
                    CLUBS_ENDPOINT + "/" + clubId + "/teams",
                    jsonBody
            );

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error creando equipo");

            return response.dataAs(TeamDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error creando equipo", e);
        }
    }

    public void deleteTeam(String teamId) {
        try {
            String responseJson = apiClient.delete(TEAMS_ENDPOINT + "/" + teamId);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponseWithoutData(response, "Error eliminando equipo");

        } catch (Exception e) {
            throw new RuntimeException("Error eliminando equipo", e);
        }
    }

    private String normalizeCategory(String category) {
        if (category == null) {
            return null;
        }

        String normalized = Normalizer.normalize(category, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toUpperCase();

        return normalized;
    }

    private String normalizeSubcategory(String subcategory) {
        if (subcategory == null) {
            return null;
        }

        return subcategory
                .trim()
                .toUpperCase()
                .replace("-", "")
                .replace("_", "");
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TeamCategoryDTO {

        private String name;
        private List<String> subcategories;

        public TeamCategoryDTO() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getSubcategories() {
            return subcategories;
        }

        public void setSubcategories(List<String> subcategories) {
            this.subcategories = subcategories;
        }

        @Override
        public String toString() {
            return name != null ? name : "";
        }
    }
}