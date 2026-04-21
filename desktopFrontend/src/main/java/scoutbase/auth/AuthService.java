package scoutbase.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import scoutbase.common.ApiResponse;
import scoutbase.user.UserDto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Servicio encargado de gestionar la autenticación y la obtención
 * de datos del usuario contra el backend de ScoutBase.
 *
 * <p>Esta clase encapsula las llamadas HTTP relacionadas con el inicio
 * de sesión y la recuperación de información del usuario autenticado,
 * así como la extracción del token devuelto por la API.</p>
 */
public class AuthService {

    /**
     * URL base de la API backend para la gestión de usuarios.
     */
    private static final String BASE_URL =
            "https://scoutbase-dev-6r6d.onrender.com/api/v1/users";

    /**
     * URL del endpoint de autenticación para el inicio de sesión.
     */
    private static final String LOGIN_URL =
            BASE_URL + "/auth/login";

    /**
     * Cliente HTTP utilizado para comunicarse con la API.
     */
    private final HttpClient httpClient;

    /**
     * Objeto encargado de convertir JSON a objetos Java y viceversa.
     */
    private final ObjectMapper objectMapper;

    /**
     * Crea una nueva instancia del servicio de autenticación.
     *
     * <p>Inicializa el cliente HTTP con un tiempo de espera para la conexión
     * y el objeto necesario para serializar y deserializar respuestas JSON.</p>
     */
    public AuthService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Obtiene los datos de un usuario a partir de su nombre de usuario.
     *
     * <p>Este método realiza una petición autenticada al backend para recuperar
     * la información asociada a un usuario concreto. Puede requerir permisos
     * específicos según la configuración del servidor.</p>
     *
     * @param username nombre de usuario a consultar
     * @param token token de autenticación necesario para acceder al endpoint
     * @return objeto {@link UserDto} con los datos del usuario solicitado
     * @throws IOException si ocurre un error durante la comunicación con la API
     * @throws InterruptedException si la petición HTTP es interrumpida
     * @throws RuntimeException si la respuesta no contiene datos válidos
     *                          o si se produce un error HTTP
     */
    public UserDto getUserByUsername(String username, String token) throws IOException, InterruptedException {
        String url = BASE_URL + "/username/" + username;

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

    /**
     * Obtiene los datos del usuario autenticado a partir del token actual.
     *
     * <p>Este método consulta el endpoint {@code /me} para recuperar
     * la información del usuario en sesión, incluyendo datos que pueden
     * ser necesarios en otras operaciones de la aplicación.</p>
     *
     * @param token token de autenticación del usuario actual
     * @return objeto {@link UserDto} con los datos del usuario autenticado
     * @throws IOException si ocurre un error durante la comunicación con la API
     * @throws InterruptedException si la petición HTTP es interrumpida
     * @throws RuntimeException si la respuesta no contiene datos válidos
     *                          o si se produce un error HTTP
     */
    public UserDto getCurrentUser(String token) throws IOException, InterruptedException {
        String url = BASE_URL + "/me";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("GET ME URL: " + url);
        System.out.println("GET ME STATUS: " + response.statusCode());
        System.out.println("GET ME BODY: " + response.body());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);

            if (apiResponse.getData() != null && !apiResponse.getData().isNull()) {
                return objectMapper.treeToValue(apiResponse.getData(), UserDto.class);
            }

            throw new RuntimeException("La respuesta no contiene datos del usuario actual");
        }

        throw new RuntimeException("Error obteniendo usuario actual: HTTP "
                + response.statusCode() + " -> " + response.body());
    }

    /**
     * Realiza el inicio de sesión del usuario contra el backend.
     *
     * <p>Envía las credenciales al endpoint de autenticación y devuelve
     * la respuesta procesada como un objeto {@link ApiResponse} si la
     * operación se completa correctamente.</p>
     *
     * @param username nombre de usuario introducido
     * @param password contraseña introducida
     * @return respuesta de la API convertida a {@link ApiResponse}
     * @throws IOException si ocurre un error durante la comunicación con la API
     * @throws InterruptedException si la petición HTTP es interrumpida
     * @throws RuntimeException si las credenciales son incorrectas
     *                          o si se produce un error HTTP
     */
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

    /**
     * Construye una excepción a partir del mensaje de error devuelto por la API.
     *
     * <p>Si la respuesta contiene un mensaje válido, se utiliza dicho texto
     * como descripción de la excepción. En caso contrario, se emplea
     * el mensaje por defecto proporcionado.</p>
     *
     * @param responseBody cuerpo de la respuesta HTTP
     * @param defaultMessage mensaje por defecto si no puede extraerse uno válido
     * @return excepción con el mensaje más adecuado según la respuesta recibida
     */
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

    /**
     * Extrae el token de autenticación contenido en la respuesta de login.
     *
     * <p>Este método contempla distintos nombres posibles del campo que
     * almacena el token, para adaptarse a posibles variaciones en la
     * estructura de respuesta del backend.</p>
     *
     * @param response respuesta devuelta por la API tras el login
     * @return token de autenticación extraído, o {@code null} si no se encuentra
     */
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