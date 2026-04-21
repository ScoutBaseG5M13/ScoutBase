package scoutbase.player;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import scoutbase.common.ApiClient;
import scoutbase.common.ApiResponse;

import java.util.List;
import java.util.UUID;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con jugadores.
 *
 * <p>Permite obtener los jugadores de un equipo y crear nuevos jugadores
 * asociados a un equipo concreto.</p>
 */
public class PlayerService {

    private static final String BASE_URL =
            "https://scoutbase-dev-6r6d.onrender.com/api/v1/players";

    private final ApiClient apiClient = new ApiClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<PlayerDTO> getPlayersByTeamId(String teamId) {
        try {
            String responseJson = apiClient.get(BASE_URL + "/teams/" + teamId);

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            if (!response.isSuccess()) {
                throw new RuntimeException(response.getMessage());
            }

            JsonNode data = response.getData();

            return objectMapper
                    .readerForListOf(PlayerDTO.class)
                    .readValue(data);

        } catch (Exception e) {
            String message = e.getMessage() != null ? e.getMessage() : "";

            // Apaño temporal: si backend devuelve "No value present" para teams vacíos,
            // tratamos la respuesta como una lista vacía.
            if (message.contains("No value present")) {
                return List.of();
            }

            e.printStackTrace();
            throw new RuntimeException("Error obteniendo jugadores", e);
        }
    }

    public PlayerDTO createPlayer(String name,
                                  String surname,
                                  int age,
                                  String email,
                                  int number,
                                  String position,
                                  int priority,
                                  String teamId) {
        try {
            String jsonBody = """
                {
                  "name": "%s",
                  "surname": "%s",
                  "age": %d,
                  "email": "%s",
                  "number": %d,
                  "position": "%s",
                  "priority": %d,
                  "teamId": "%s"
                }
                """.formatted(name, surname, age, email, number, position, priority, teamId);

            System.out.println("CREATE PLAYER URL: " + BASE_URL + "/teams/" + teamId);
            System.out.println("CREATE PLAYER BODY: " + jsonBody);

            String responseJson = apiClient.post(BASE_URL + "/teams/" + teamId, jsonBody);

            System.out.println("CREATE PLAYER RESPONSE: " + responseJson);

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            if (!response.isSuccess()) {
                throw new RuntimeException(response.getMessage());
            }

            return objectMapper.treeToValue(response.getData(), PlayerDTO.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creando jugador", e);
        }
    }

    /**
     * Crea un jugador solo en memoria para la demo cuando backend falla
     * con teams nuevos sin jugadores previos.
     */
    public PlayerDTO createLocalPlayer(String name,
                                       String surname,
                                       int age,
                                       String email,
                                       int number,
                                       String position,
                                       int priority,
                                       String teamId) {
        PlayerDTO player = new PlayerDTO();
        player.setId(UUID.randomUUID().toString());
        player.setName(name);
        player.setSurname(surname);
        player.setAge(age);
        player.setEmail(email);
        player.setNumber(number);
        player.setPosition(position);
        player.setPriority(priority);
        player.setTeamId(teamId);
        return player;
    }
}