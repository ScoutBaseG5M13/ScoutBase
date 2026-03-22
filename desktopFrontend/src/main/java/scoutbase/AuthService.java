package scoutbase;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class AuthService {

    public UserDto getUserByUsername(String username, String token) throws IOException, InterruptedException {
        String url = "https://scoutbase-dev.onrender.com/api/v1/users/username/" + username;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("GET USER URL: " + url);
        System.out.println("GET USER STATUS: " + response.statusCode());
        System.out.println("GET USER BODY: " + response.body());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);

            if (apiResponse.getData() != null && !apiResponse.getData().isNull()) {
                return objectMapper.treeToValue(apiResponse.getData(), UserDto.class);
            }

            throw new RuntimeException("La respuesta no contiene datos de usuario");
        }

        throw new RuntimeException("Error obteniendo usuario: HTTP " + response.statusCode() + " -> " + response.body());
    }
    private static final String LOGIN_URL =
            "https://scoutbase-dev.onrender.com/api/v1/users/auth/login";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public AuthService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public ApiResponse login(String username, String password) throws IOException, InterruptedException {
        LoginRequest loginRequest = new LoginRequest(username, password);
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(LOGIN_URL))
                .timeout(Duration.ofSeconds(15))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        String responseBody = response.body();

        System.out.println("LOGIN URL: " + LOGIN_URL);
        System.out.println("REQUEST BODY: " + requestBody);
        System.out.println("STATUS: " + statusCode);
        System.out.println("BODY: " + responseBody);

        if (statusCode >= 200 && statusCode < 300) {
            return objectMapper.readValue(responseBody, ApiResponse.class);
        }

        if (statusCode == 401) {
            throw buildApiException(responseBody, "Credenciales incorrectas");
        }

        if (statusCode == 404) {
            throw new RuntimeException("Ruta de login no encontrada en backend: " + LOGIN_URL);
        }

        if (statusCode == 500) {
            throw new RuntimeException("Error interno del servidor");
        }

        throw new RuntimeException("Error HTTP " + statusCode + ": " + responseBody);
    }

    private RuntimeException buildApiException(String responseBody, String defaultMessage) {
        try {
            ApiResponse errorResponse = objectMapper.readValue(responseBody, ApiResponse.class);
            String message = errorResponse.getMessage();

            if (message != null && !message.isBlank()) {
                return new RuntimeException(message);
            }
        } catch (Exception ignored) {
        }

        return new RuntimeException(defaultMessage);
    }

    public String extractToken(ApiResponse response) {
        JsonNode data = response.getData();

        if (data == null || data.isNull()) {
            return null;
        }

        if (data.isTextual()) {
            return data.asText();
        }

        if (data.has("token") && !data.get("token").isNull()) {
            return data.get("token").asText();
        }

        if (data.has("accessToken") && !data.get("accessToken").isNull()) {
            return data.get("accessToken").asText();
        }

        if (data.has("jwt") && !data.get("jwt").isNull()) {
            return data.get("jwt").asText();
        }

        return null;
    }
}