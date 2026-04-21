package scoutbase.team;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import scoutbase.common.ApiClient;
import scoutbase.common.ApiResponse;

import java.util.List;
import java.util.stream.Collectors;

public class TeamService {

    private static final String BASE_URL =
            "https://scoutbase-dev-6r6d.onrender.com/api/v1/teams";

    private final ApiClient apiClient = new ApiClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Obtiene todos los equipos del backend.
     *
     * @return lista de equipos relacionados con el usuario autenticado
     * @throws RuntimeException si ocurre un error al obtener o mapear los datos
     */
    public List<TeamDTO> getAllTeams() {
        try {
            String responseJson = apiClient.get(BASE_URL);
            System.out.println("GET TEAMS RAW: " + responseJson);

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            if (!response.isSuccess()) {
                throw new RuntimeException(response.getMessage());
            }

            JsonNode data = response.getData();

            List<TeamDTO> teams = objectMapper
                    .readerForListOf(TeamDTO.class)
                    .readValue(data);

            for (TeamDTO team : teams) {
                System.out.println("TEAM -> id=" + team.getId()
                        + ", name=" + team.getName()
                        + ", clubId=" + team.getResolvedClubId());
            }

            return teams;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error obteniendo equipos", e);
        }
    }

    /**
     * Obtiene los equipos filtrados por el identificador del club.
     *
     * @param clubId identificador del club seleccionado
     * @return lista de equipos pertenecientes al club indicado
     */
    public List<TeamDTO> getTeamsByClubId(String clubId) {
        return getAllTeams().stream()
                .filter(team -> clubId.equals(team.getResolvedClubId()))
                .collect(Collectors.toList());
    }

    /**
     * Crea un nuevo equipo asociado a un club y relacionado con el usuario actual como scouter.
     *
     * @param name nombre del equipo
     * @param category categoría del equipo
     * @param subcategory subcategoría del equipo
     * @param clubId identificador del club al que pertenece
     * @param userId identificador del usuario autenticado
     * @throws RuntimeException si ocurre un error al crear el equipo
     */
    public void createTeam(String name, String category, String subcategory, String clubId, String userId) {
        try {
            String jsonBody = """
                {
                  "name": "%s",
                  "category": "%s",
                  "subcategory": "%s",
                  "players": [],
                  "trainers": [],
                  "scouters": ["%s"],
                  "clubId": "%s"
                }
                """.formatted(name, category, subcategory, userId, clubId);

            System.out.println("CREATE TEAM BODY: " + jsonBody);

            String responseJson = apiClient.post(BASE_URL, jsonBody);
            System.out.println("CREATE TEAM RESPONSE: " + responseJson);

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            if (!response.isSuccess()) {
                throw new RuntimeException(response.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creando equipo", e);
        }
    }
}