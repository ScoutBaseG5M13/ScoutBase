package scoutbase.userTeam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import scoutbase.common.ApiClient;
import scoutbase.common.ApiResponse;
import scoutbase.team.TeamDTO;

import java.text.Normalizer;
import java.util.List;

public class UserTeamService {

    private static final String USER_TEAMS_ENDPOINT = "/user-teams";

    private final ApiClient apiClient = new ApiClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<UserTeamDTO> getUserTeamsByUserClubId(String userClubId) {
        try {
            String responseJson = apiClient.get(USER_TEAMS_ENDPOINT + "/user-clubs/" + userClubId);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo UserTeams del UserClub");

            return response.dataAs(new TypeReference<List<UserTeamDTO>>() {});

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo UserTeams del UserClub", e);
        }
    }

    public UserTeamDTO getOrCreateUserTeamForTeam(String userClubId, TeamDTO team) {
        List<UserTeamDTO> userTeams = getUserTeamsByUserClubId(userClubId);

        if (userTeams == null || userTeams.isEmpty()) {
            return null;
        }

        UserTeamDTO exactMatch = findExactUserTeam(userTeams, team);

        if (exactMatch != null) {
            return exactMatch;
        }

        return userTeams.get(0);
    }

    private UserTeamDTO findExactUserTeam(List<UserTeamDTO> userTeams, TeamDTO team) {
        String targetName = normalize(team.getName());
        String targetCategory = normalize(team.getCategory());
        String targetSubcategory = normalize(team.getSubcategory());

        return userTeams.stream()
                .filter(userTeam ->
                        equalsNormalized(userTeam.getName(), targetName)
                                || equalsNormalized(userTeam.getResolvedTeamId(), team.getId())
                                || (
                                equalsNormalized(userTeam.getCategory(), targetCategory)
                                        && equalsNormalized(userTeam.getSubcategory(), targetSubcategory)
                        )
                )
                .findFirst()
                .orElse(null);
    }

    private boolean equalsNormalized(String value, String target) {
        return normalize(value).equals(target);
    }

    private String normalize(String value) {
        if (value == null) {
            return "";
        }

        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replace("-", "")
                .replace("_", "")
                .trim()
                .toUpperCase();
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