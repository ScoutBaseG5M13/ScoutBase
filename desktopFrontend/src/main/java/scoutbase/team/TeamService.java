package scoutbase.team;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import scoutbase.common.ApiClient;
import scoutbase.common.ApiResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con equipos.
 *
 * <p>Permite obtener equipos desde el backend, filtrarlos por club
 * y crear nuevos equipos. Mientras el backend no tenga estable
 * la creación y el listado de equipos, incorpora un fallback local
 * en memoria mediante {@link TeamCache}.</p>
 */
public class TeamService {

    /**
     * URL base del endpoint de equipos en el backend.
     */
    private static final String BASE_URL =
            "https://scoutbase-dev-6r6d.onrender.com/api/v1/teams";

    /**
     * Cliente HTTP utilizado para comunicarse con la API.
     */
    private final ApiClient apiClient = new ApiClient();

    /**
     * Objeto encargado de la serialización y deserialización de JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Obtiene todos los equipos disponibles.
     *
     * <p>Intenta recuperar los equipos desde el backend y fusionarlos
     * con los equipos creados localmente en caché. Si el backend falla,
     * devuelve únicamente los equipos del caché sin propagar la excepción.</p>
     *
     * @return lista de equipos visibles
     */
    public List<TeamDTO> getAllTeams() {
        try {
            String responseJson = apiClient.get(BASE_URL);
            System.out.println("GET TEAMS RAW: " + responseJson);

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            if (!response.isSuccess()) {
                System.out.println("GET TEAMS API ERROR: " + response.getMessage());
                return TeamCache.getTeams();
            }

            JsonNode data = response.getData();

            List<TeamDTO> backendTeams = objectMapper
                    .readerForListOf(TeamDTO.class)
                    .readValue(data);

            List<TeamDTO> mergedTeams = new ArrayList<>(backendTeams);

            for (TeamDTO localTeam : TeamCache.getTeams()) {
                boolean exists = mergedTeams.stream()
                        .anyMatch(team -> team.getId() != null
                                && localTeam.getId() != null
                                && team.getId().equals(localTeam.getId()));

                if (!exists) {
                    mergedTeams.add(localTeam);
                }
            }

            for (TeamDTO team : mergedTeams) {
                System.out.println("TEAM -> id=" + team.getId()
                        + ", name=" + team.getName()
                        + ", clubId=" + team.getResolvedClubId());
            }

            return mergedTeams;

        } catch (Exception e) {
            System.out.println("GET TEAMS FALLBACK TO CACHE: " + e.getMessage());
            return TeamCache.getTeams();
        }
    }

    /**
     * Obtiene los equipos filtrados por el identificador de un club.
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
     * Crea un nuevo equipo.
     *
     * <p>Intenta crear el equipo en backend. Si el endpoint de creación
     * no está soportado o falla por el problema actual conocido,
     * genera un equipo local y lo almacena en {@link TeamCache} para
     * mantener operativa la demo.</p>
     *
     * @param name nombre del equipo
     * @param category categoría del equipo
     * @param subcategory subcategoría del equipo
     * @param clubId identificador del club al que pertenece el equipo
     * @param userId identificador del usuario autenticado
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
            String message = e.getMessage() != null ? e.getMessage() : "";

            if (message.contains("Request method 'POST' is not supported")
                    || message.contains("HTTP Error: 400")
                    || message.contains("HTTP Error: 500")) {
                TeamDTO localTeam = createLocalTeam(name, category, subcategory, clubId, userId);
                TeamCache.addTeam(localTeam);
                System.out.println("TEAM CREADO EN CACHE LOCAL: " + localTeam.getName());
                return;
            }

            throw new RuntimeException("Error creando equipo", e);
        }
    }

    /**
     * Crea un equipo únicamente en memoria como fallback temporal.
     *
     * @param name nombre del equipo
     * @param category categoría del equipo
     * @param subcategory subcategoría del equipo
     * @param clubId identificador del club
     * @param userId identificador del usuario autenticado que queda asignado como scouter
     * @return equipo creado localmente
     */
    public TeamDTO createLocalTeam(String name, String category, String subcategory, String clubId, String userId) {
        TeamDTO team = new TeamDTO();
        team.setId(UUID.randomUUID().toString());
        team.setName(name);
        team.setCategory(category);
        team.setSubcategory(subcategory);
        team.setClubId(clubId);
        team.setPlayers(new ArrayList<>());
        team.setTrainers(new ArrayList<>());
        team.setScouters(new ArrayList<>(List.of(userId)));
        return team;
    }
}