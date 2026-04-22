package scoutbase.common;

import scoutbase.app.SessionManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Cliente HTTP encargado de gestionar la comunicación con el backend.
 *
 * <p>Proporciona métodos básicos para realizar peticiones HTTP (GET, POST, PUT, DELETE)
 * incluyendo automáticamente las cabeceras necesarias, como el token de autenticación
 * almacenado en {@link SessionManager}.</p>
 */
public class ApiClient {

    /**
     * Cliente HTTP utilizado para enviar las peticiones.
     */
    private final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Construye una petición base con las cabeceras comunes.
     *
     * <p>Incluye el tipo de contenido en formato JSON y el token de autorización
     * necesario para acceder a los endpoints protegidos del backend.</p>
     *
     * @param url URL del endpoint al que se realizará la petición
     * @return builder de la petición HTTP configurado
     */
    private HttpRequest.Builder baseRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getAuthToken());
    }

    /**
     * Realiza una petición HTTP GET.
     *
     * @param url URL del endpoint
     * @return cuerpo de la respuesta en formato String
     * @throws IOException si ocurre un error de entrada/salida
     * @throws InterruptedException si la petición es interrumpida
     */
    public String get(String url) throws IOException, InterruptedException {
        HttpRequest request = baseRequest(url)
                .GET()
                .build();

        return send(request);
    }

    /**
     * Realiza una petición HTTP POST.
     *
     * @param url URL del endpoint
     * @param jsonBody cuerpo de la petición en formato JSON
     * @return cuerpo de la respuesta en formato String
     * @throws IOException si ocurre un error de entrada/salida
     * @throws InterruptedException si la petición es interrumpida
     */
    public String post(String url, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = baseRequest(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return send(request);
    }

    /**
     * Realiza una petición HTTP PUT.
     *
     * @param url URL del endpoint
     * @param jsonBody cuerpo de la petición en formato JSON
     * @return cuerpo de la respuesta en formato String
     * @throws IOException si ocurre un error de entrada/salida
     * @throws InterruptedException si la petición es interrumpida
     */
    public String put(String url, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = baseRequest(url)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return send(request);
    }

    /**
     * Realiza una petición HTTP DELETE.
     *
     * @param url URL del endpoint
     * @return cuerpo de la respuesta en formato String
     * @throws IOException si ocurre un error de entrada/salida
     * @throws InterruptedException si la petición es interrumpida
     */
    public String delete(String url) throws IOException, InterruptedException {
        HttpRequest request = baseRequest(url)
                .DELETE()
                .build();

        return send(request);
    }

    /**
     * Envía una petición HTTP y gestiona la respuesta.
     *
     * <p>Si el código de estado está en el rango 2xx, devuelve el cuerpo
     * de la respuesta. En caso contrario, lanza una excepción con la
     * información del error.</p>
     *
     * @param request petición HTTP a enviar
     * @return cuerpo de la respuesta en formato String
     * @throws IOException si ocurre un error de entrada/salida
     * @throws InterruptedException si la petición es interrumpida
     * @throws RuntimeException si la respuesta HTTP indica error
     */
    private String send(HttpRequest request) throws IOException, InterruptedException {
        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int status = response.statusCode();

        if (status >= 200 && status < 300) {
            return response.body();
        } else {
            throw new RuntimeException(
                    "HTTP Error: " + status + " - " + response.body()
            );
        }
    }
}