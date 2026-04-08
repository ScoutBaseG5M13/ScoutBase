package scoutbase;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Servicio encargado de gestionar la autenticación contra el backend.
 *
 * <p>Permite iniciar sesión, obtener datos del usuario autenticado,
 * recuperar el rol del usuario actual y extraer el token devuelto por la API.</p>
 */
public class AuthService {

    /**
     * URL base de la API backend.
     */
    private static final String BASE_URL =
            "https://scoutbase-dev-6r6d.onrender.com/api/v1/users";

    /**
     * URL del endpoint de login del backend.
     */
    private static final String LOGIN_URL =
            BASE_URL + "/auth/login";

    /**
     * URL del endpoint para obtener el rol del usuario autenticado.
     */
    private static final String ROLE_URL =
            BASE_URL + "/role";

    /**
     * Cliente HTTP usado para comunicarse con la API.
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
     * y el mapper necesario para trabajar con respuestas JSON.</p>
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
     * <p>Este método puede requerir permisos específicos según la configuración
     * del backend. No debe usarse como parte obligatoria del flujo de login.</p>
     *
     * @param username nombre de usuario a consultar
     * @param token token de autenticación necesario para acceder al endpoint
     * @return objeto {@link UserDto} con los datos del usuario
     * @throws IOException si ocurre un error durante la comunicación
     * @throws InterruptedException si la petición es interrumpida
     * @throws RuntimeException si la respuesta no contiene datos válidos o se produce un error HTTP
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
     * Obtiene el rol del usuario autenticado a partir del token JWT.
     *
     * <p>Este método utiliza el endpoint específico del backend destinado
     * a recuperar el rol del usuario actual autenticado.</p>
     *
     * @param token token de autenticación
     * @return rol del usuario autenticado
     * @throws IOException si ocurre un error durante la comunicación
     * @throws InterruptedException si la petición es interrumpida
     * @throws RuntimeException si la respuesta no contiene un rol válido o se produce un error HTTP
     */
    public String getCurrentUserRole(String token) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ROLE_URL))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("GET ROLE URL: " + ROLE_URL);
        System.out.println("GET ROLE STATUS: " + response.statusCode());
        System.out.println("GET ROLE BODY: " + response.body());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            ApiResponse apiResponse = objectMapper.readValue(response.body(), ApiResponse.class);
            JsonNode data = apiResponse.getData();

            if (data == null || data.isNull()) {
                throw new RuntimeException("La respuesta no contiene el rol del usuario");
            }

            if (data.isTextual()) {
                return data.asText();
            }

            if (data.isObject()) {
                if (data.has("role") && !data.get("role").isNull()) {
                    return data.get("role").asText();
                }

                if (data.has("authority") && !data.get("authority").isNull()) {
                    return data.get("authority").asText();
                }

                if (data.has("name") && !data.get("name").isNull()) {
                    return data.get("name").asText();
                }
            }

            throw new RuntimeException("Formato de rol no reconocido: " + data.toPrettyString());
        }

        if (response.statusCode() == 401) {
            throw new RuntimeException("No autorizado para obtener el rol del usuario");
        }

        if (response.statusCode() == 403) {
            throw new RuntimeException("Acceso denegado al obtener el rol del usuario");
        }

        throw new RuntimeException("Error obteniendo rol: HTTP " + response.statusCode() + " -> " + response.body());
    }

    /**
     * Realiza el login del usuario contra el backend.
     *
     * @param username nombre de usuario introducido
     * @param password contraseña introducida
     * @return respuesta de la API convertida a {@link ApiResponse}
     * @throws IOException si ocurre un error durante la comunicación
     * @throws InterruptedException si la petición es interrumpida
     * @throws RuntimeException si las credenciales son incorrectas o se produce un error HTTP
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
     * Construye una excepción a partir del mensaje devuelto por la API.
     *
     * <p>Si la respuesta contiene un mensaje de error válido, se usa ese texto.
     * En caso contrario, se devuelve una excepción con un mensaje por defecto.</p>
     *
     * @param responseBody cuerpo de la respuesta HTTP
     * @param defaultMessage mensaje por defecto si no se puede leer el error de la API
     * @return excepción con el mensaje más adecuado
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
     * <p>Se contemplan varios nombres posibles del campo para adaptarse
     * a distintas respuestas del backend.</p>
     *
     * @param response respuesta devuelta por la API
     * @return token de autenticación, o {@code null} si no se encuentra
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