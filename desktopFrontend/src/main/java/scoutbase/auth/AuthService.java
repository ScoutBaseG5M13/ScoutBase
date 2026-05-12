package scoutbase.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import scoutbase.common.ApiResponse;
import scoutbase.common.ApiClient;
import scoutbase.user.UserDto;

import java.io.IOException;

/**
 * Servicio encargado de gestionar la autenticación de usuarios en ScoutBase.
 *
 * <p>Esta clase centraliza las operaciones relacionadas con el inicio de sesión
 * y la recuperación de información del usuario autenticado contra la API REST
 * del backend.</p>
 *
 * <p>Sus responsabilidades principales son:</p>
 * <ul>
 *     <li>Enviar las credenciales del usuario al endpoint de login.</li>
 *     <li>Procesar la respuesta estándar {@link ApiResponse} devuelta por la API.</li>
 *     <li>Extraer el token JWT utilizado posteriormente por el cliente HTTP.</li>
 *     <li>Obtener los datos del usuario autenticado mediante el endpoint {@code /users/me}.</li>
 *     <li>Consultar usuarios concretos por nombre de usuario cuando sea necesario.</li>
 * </ul>
 *
 * <p>El servicio utiliza {@link ApiClient} para reutilizar la configuración común
 * de comunicación HTTP, incluyendo la URL base del backend y la gestión de
 * cabeceras de autenticación.</p>
 */
public class AuthService {

    /**
     * Endpoint relativo utilizado para autenticar un usuario.
     *
     * <p>Este endpoint no requiere token previo, ya que se utiliza precisamente
     * para obtener el JWT inicial.</p>
     */
    private static final String LOGIN_ENDPOINT = "/users/auth/login";

    /**
     * Endpoint relativo utilizado para recuperar el usuario autenticado.
     */
    private static final String CURRENT_USER_ENDPOINT = "/users/me";

    /**
     * Endpoint base relativo para buscar usuarios por nombre de usuario.
     */
    private static final String USERNAME_ENDPOINT = "/users/username/";

    /**
     * Cliente común encargado de realizar las peticiones HTTP al backend.
     */
    private final ApiClient apiClient;

    /**
     * Objeto encargado de serializar y deserializar datos JSON.
     */
    private final ObjectMapper objectMapper;

    /**
     * Crea una nueva instancia del servicio de autenticación.
     *
     * <p>Inicializa el cliente HTTP común y el conversor JSON utilizado
     * para transformar las peticiones y respuestas entre objetos Java
     * y estructuras JSON.</p>
     */
    public AuthService() {
        this.apiClient = new ApiClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Realiza el inicio de sesión de un usuario contra el backend.
     *
     * <p>Construye un {@link LoginRequest} con las credenciales recibidas,
     * lo serializa a JSON y lo envía al endpoint de autenticación. Si la
     * petición se completa correctamente, devuelve la respuesta estándar
     * de la API.</p>
     *
     * <p>Este método no adjunta cabecera Authorization, ya que el usuario
     * todavía no dispone de token JWT en el momento de autenticarse.</p>
     *
     * @param username nombre de usuario introducido en el formulario de login
     * @param password contraseña introducida en el formulario de login
     * @return respuesta estándar de la API con el resultado del login
     * @throws IOException si ocurre un error de entrada/salida durante la petición
     * @throws InterruptedException si la petición HTTP es interrumpida
     * @throws RuntimeException si las credenciales no son válidas o el backend devuelve error
     */
    public ApiResponse login(String username, String password) throws IOException, InterruptedException {
        LoginRequest loginRequest = new LoginRequest(username, password);
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        try {
            String responseBody = apiClient.postWithoutAuth(LOGIN_ENDPOINT, requestBody);
            return objectMapper.readValue(responseBody, ApiResponse.class);
        } catch (RuntimeException e) {
            throw buildApiException(e.getMessage(), "Credenciales incorrectas");
        }
    }

    /**
     * Obtiene los datos del usuario autenticado actualmente.
     *
     * <p>Consulta el endpoint {@code /users/me}, que devuelve la información
     * del usuario asociado al token JWT incluido en la sesión activa.</p>
     *
     * <p>Este método debe utilizarse después de un login correcto y una vez
     * guardado el token en sesión.</p>
     *
     * @return objeto {@link UserDto} con los datos del usuario autenticado
     * @throws IOException si ocurre un error de comunicación con el backend
     * @throws InterruptedException si la petición HTTP es interrumpida
     * @throws RuntimeException si la respuesta no contiene datos válidos
     */
    public UserDto getCurrentUser() throws IOException, InterruptedException {
        String responseBody = apiClient.get(CURRENT_USER_ENDPOINT);
        ApiResponse apiResponse = objectMapper.readValue(responseBody, ApiResponse.class);

        if (apiResponse.getData() == null || apiResponse.getData().isNull()) {
            throw new RuntimeException("La respuesta no contiene datos del usuario autenticado");
        }

        return apiResponse.dataAs(UserDto.class);
    }

    /**
     * Obtiene los datos de un usuario a partir de su nombre de usuario.
     *
     * <p>Realiza una petición autenticada al endpoint
     * {@code /users/username/{username}}. Según la documentación del backend,
     * este endpoint puede estar restringido a usuarios con permisos de administrador.</p>
     *
     * @param username nombre de usuario que se desea consultar
     * @return objeto {@link UserDto} con los datos del usuario encontrado
     * @throws IOException si ocurre un error de comunicación con el backend
     * @throws InterruptedException si la petición HTTP es interrumpida
     * @throws RuntimeException si la respuesta no contiene datos válidos
     */
    public UserDto getUserByUsername(String username) throws IOException, InterruptedException {
        String responseBody = apiClient.get(USERNAME_ENDPOINT + username);
        ApiResponse apiResponse = objectMapper.readValue(responseBody, ApiResponse.class);

        if (apiResponse.getData() == null || apiResponse.getData().isNull()) {
            throw new RuntimeException("La respuesta no contiene datos del usuario solicitado");
        }

        return apiResponse.dataAs(UserDto.class);
    }

    /**
     * Extrae el token JWT incluido en la respuesta de login.
     *
     * <p>El backend documenta que el endpoint de login devuelve un token JWT
     * dentro de una respuesta estándar {@link ApiResponse}. Este método contempla
     * diferentes nombres habituales para el campo del token con el objetivo de
     * hacer la integración más tolerante ante pequeños cambios de estructura.</p>
     *
     * <p>Formatos contemplados:</p>
     * <ul>
     *     <li>{@code data} como texto plano.</li>
     *     <li>{@code data.token}</li>
     *     <li>{@code data.accessToken}</li>
     *     <li>{@code data.jwt}</li>
     * </ul>
     *
     * @param response respuesta devuelta por el endpoint de login
     * @return token JWT extraído; {@code null} si no se encuentra ningún token válido
     */
    public String extractToken(ApiResponse response) {
        if (response == null || response.getData() == null || response.getData().isNull()) {
            return null;
        }

        JsonNode data = response.getData();

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

    /**
     * Construye una excepción legible a partir de una respuesta de error.
     *
     * <p>Intenta extraer un mensaje útil desde una estructura {@link ApiResponse}.
     * Si no es posible interpretar el contenido recibido, devuelve una excepción
     * con el mensaje por defecto indicado.</p>
     *
     * @param responseBody cuerpo o mensaje recibido al producirse el error
     * @param defaultMessage mensaje alternativo si no se puede extraer uno mejor
     * @return excepción preparada para ser lanzada por el servicio
     */
    private RuntimeException buildApiException(String responseBody, String defaultMessage) {
        try {
            ApiResponse errorResponse = objectMapper.readValue(responseBody, ApiResponse.class);
            String message = errorResponse.getMessage();

            if (message != null && !message.isBlank()) {
                return new RuntimeException(message);
            }
        } catch (Exception ignored) {
            if (responseBody != null && !responseBody.isBlank()) {
                return new RuntimeException(responseBody);
            }
        }

        return new RuntimeException(defaultMessage);
    }
}