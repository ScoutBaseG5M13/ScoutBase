package scoutbase.common;

import scoutbase.app.SessionManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Cliente HTTP común encargado de gestionar la comunicación con la API REST de ScoutBase.
 *
 * <p>Centraliza la construcción y envío de peticiones HTTP para evitar repetir
 * configuración en los diferentes servicios de la aplicación.</p>
 *
 * <p>Sus responsabilidades principales son:</p>
 * <ul>
 *     <li>Aplicar la URL base del backend.</li>
 *     <li>Configurar cabeceras comunes como {@code Content-Type} y {@code Accept}.</li>
 *     <li>Añadir automáticamente el token JWT cuando la petición lo requiera.</li>
 *     <li>Enviar peticiones GET, POST, PUT y DELETE.</li>
 *     <li>Validar códigos HTTP y devolver el cuerpo de la respuesta.</li>
 * </ul>
 */
public class ApiClient {

    /**
     * URL base de la API REST de ScoutBase.
     *
     * <p>Todos los servicios deben enviar endpoints relativos, por ejemplo:</p>
     *
     * <pre>
     * /users/me
     * /clubs/{id}/teams
     * /players/teams/{id}
     * </pre>
     */
    private static final String BASE_URL = "https://scoutbase-dev-6r6d.onrender.com/api/v1";

    /**
     * Cliente HTTP reutilizable para enviar peticiones al backend.
     */
    private final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Construye una petición HTTP base con las cabeceras comunes.
     *
     * <p>Si {@code requiresAuth} es {@code true}, añade automáticamente
     * la cabecera {@code Authorization: Bearer <token>} usando el token
     * almacenado en {@link SessionManager}.</p>
     *
     * @param endpoint endpoint relativo de la API o URL completa
     * @param requiresAuth indica si la petición requiere token JWT
     * @return builder HTTP configurado con URI y cabeceras comunes
     */
    private HttpRequest.Builder baseRequest(String endpoint, boolean requiresAuth) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(resolveUrl(endpoint)))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");

        String token = SessionManager.getAuthToken();

        if (requiresAuth && token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }

        return builder;
    }

    /**
     * Resuelve la URL final de una petición.
     *
     * <p>Si se recibe una URL absoluta, se utiliza tal cual. Si se recibe
     * un endpoint relativo, se concatena con la URL base del backend.</p>
     *
     * @param endpoint endpoint relativo o URL absoluta
     * @return URL final de la petición
     */
    private String resolveUrl(String endpoint) {
        if (endpoint == null || endpoint.isBlank()) {
            throw new IllegalArgumentException("El endpoint no puede estar vacío");
        }

        if (endpoint.startsWith("http://") || endpoint.startsWith("https://")) {
            return endpoint;
        }

        if (!endpoint.startsWith("/")) {
            endpoint = "/" + endpoint;
        }

        return BASE_URL + endpoint;
    }

    /**
     * Realiza una petición HTTP GET autenticada.
     *
     * @param endpoint endpoint relativo o URL completa
     * @return cuerpo de la respuesta en formato texto
     * @throws IOException si ocurre un error de entrada/salida
     * @throws InterruptedException si la petición es interrumpida
     */
    public String get(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = baseRequest(endpoint, true)
                .GET()
                .build();

        return send(request);
    }

    /**
     * Realiza una petición HTTP POST autenticada.
     *
     * @param endpoint endpoint relativo o URL completa
     * @param jsonBody cuerpo JSON de la petición
     * @return cuerpo de la respuesta en formato texto
     * @throws IOException si ocurre un error de entrada/salida
     * @throws InterruptedException si la petición es interrumpida
     */
    public String post(String endpoint, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = baseRequest(endpoint, true)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return send(request);
    }

    /**
     * Realiza una petición HTTP POST sin autenticación.
     *
     * <p>Este método se utiliza principalmente para endpoints públicos
     * como el login, donde todavía no existe un token JWT disponible.</p>
     *
     * @param endpoint endpoint relativo o URL completa
     * @param jsonBody cuerpo JSON de la petición
     * @return cuerpo de la respuesta en formato texto
     * @throws IOException si ocurre un error de entrada/salida
     * @throws InterruptedException si la petición es interrumpida
     */
    public String postWithoutAuth(String endpoint, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = baseRequest(endpoint, false)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return send(request);
    }

    /**
     * Realiza una petición HTTP PUT autenticada.
     *
     * @param endpoint endpoint relativo o URL completa
     * @param jsonBody cuerpo JSON de la petición
     * @return cuerpo de la respuesta en formato texto
     * @throws IOException si ocurre un error de entrada/salida
     * @throws InterruptedException si la petición es interrumpida
     */
    public String put(String endpoint, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = baseRequest(endpoint, true)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return send(request);
    }

    /**
     * Realiza una petición HTTP DELETE autenticada.
     *
     * @param endpoint endpoint relativo o URL completa
     * @return cuerpo de la respuesta en formato texto
     * @throws IOException si ocurre un error de entrada/salida
     * @throws InterruptedException si la petición es interrumpida
     */
    public String delete(String endpoint) throws IOException, InterruptedException {
        HttpRequest request = baseRequest(endpoint, true)
                .DELETE()
                .build();

        return send(request);
    }

    /**
     * Envía una petición HTTP y procesa su respuesta.
     *
     * <p>Si el código HTTP se encuentra entre 200 y 299, devuelve el cuerpo
     * de la respuesta. En caso contrario, lanza una excepción con el código
     * y el cuerpo devuelto por el backend.</p>
     *
     * @param request petición HTTP ya construida
     * @return cuerpo de la respuesta
     * @throws IOException si ocurre un error de entrada/salida
     * @throws InterruptedException si la petición es interrumpida
     * @throws RuntimeException si el backend devuelve un código HTTP de error
     */
    private String send(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int status = response.statusCode();

        if (status >= 200 && status < 300) {
            return response.body();
        }

        throw new RuntimeException("HTTP Error " + status + ": " + response.body());
    }
}