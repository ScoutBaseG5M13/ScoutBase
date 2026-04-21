package scoutbase;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

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
            e.printStackTrace();
            throw new RuntimeException("Error obteniendo jugadores", e);
        }
    }
    public void createPlayer(String name, String surname, int age, String position, String teamId) {
        try {
            String jsonBody = """
                {
                  "name": "%s",
                  "surname": "%s",
                  "age": %d,
                  "position": "%s",
                  "teamId": "%s"
                }
                """.formatted(name, surname, age, position, teamId);

            String responseJson = apiClient.post(BASE_URL, jsonBody);

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            if (!response.isSuccess()) {
                throw new RuntimeException(response.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creando jugador", e);
        }
    }
}