package scoutbase.scout;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import scoutbase.common.ApiClient;
import scoutbase.common.ApiResponse;
import scoutbase.user.UserDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScoutService {

    private static final String USERS_ENDPOINT = "/users";
    private static final String USER_TEAMS_ENDPOINT = "/user-teams";
    private static final String SCOUTER_ROLE_ID = "SCOUTER";

    private final ApiClient apiClient = new ApiClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<ScoutDTO> getAllScouts() {
        try {
            String responseJson = apiClient.get(USERS_ENDPOINT + "/role/" + SCOUTER_ROLE_ID);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo scouts");

            List<UserDto> users = response.dataAs(new TypeReference<List<UserDto>>() {});

            return users.stream()
                    .map(this::toScoutDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo scouts", e);
        }
    }

    public ScoutDTO createScout(String username,
                                String password,
                                String name,
                                String surname,
                                String email) {
        try {
            Map<String, String> body = Map.of(
                    "username", username,
                    "password", password,
                    "name", name,
                    "surname", surname,
                    "email", email
            );

            String jsonBody = objectMapper.writeValueAsString(body);
            String responseJson = apiClient.post(USERS_ENDPOINT, jsonBody);

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);
            validateResponseWithoutData(response, "Error creando scout");

            UserDto createdUser = findCreatedUserFromAllUsers(username);

            if (createdUser == null || createdUser.getId() == null || createdUser.getId().isBlank()) {
                throw new RuntimeException("Scout creado, pero no se pudo recuperar su ID");
            }

            return toScoutDTO(createdUser);

        } catch (Exception e) {
            throw new RuntimeException("Error creando scout", e);
        }
    }

    public void addScoutToUserTeam(String userTeamId, String userId) {
        try {
            String responseJson = apiClient.post(
                    USER_TEAMS_ENDPOINT + "/" + userTeamId + "/scouter/" + userId,
                    "{}"
            );

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);
            validateResponseWithoutData(response, "Error asignando scout al equipo");

        } catch (Exception e) {
            throw new RuntimeException("Error asignando scout al equipo", e);
        }
    }

    public void removeScoutFromUserTeam(String userTeamId, String userId) {
        try {
            String responseJson = apiClient.delete(
                    USER_TEAMS_ENDPOINT + "/" + userTeamId + "/scouter/" + userId
            );

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);
            validateResponseWithoutData(response, "Error eliminando scout del equipo");

        } catch (Exception e) {
            throw new RuntimeException("Error eliminando scout del equipo", e);
        }
    }

    private UserDto findCreatedUserFromAllUsers(String username) {
        try {
            String responseJson = apiClient.get(USERS_ENDPOINT);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error buscando usuario creado");

            List<UserDto> users = response.dataAs(new TypeReference<List<UserDto>>() {});

            return users.stream()
                    .filter(user -> user.getUsername() != null
                            && user.getUsername().equalsIgnoreCase(username))
                    .findFirst()
                    .orElse(null);

        } catch (Exception e) {
            throw new RuntimeException("Error buscando usuario creado", e);
        }
    }

    private ScoutDTO toScoutDTO(UserDto user) {
        ScoutDTO scout = new ScoutDTO();
        scout.setId(user.getId());
        scout.setUsername(user.getUsername());
        scout.setName(user.getName());
        scout.setSurname(user.getSurname());
        scout.setEmail(user.getEmail());
        scout.setRole("SCOUTER");
        return scout;
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