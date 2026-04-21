package scoutbase;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class ClubService {

    private static final String BASE_URL = "https://scoutbase-dev-6r6d.onrender.com/api/v1/clubs";

    private final ApiClient apiClient = new ApiClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

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