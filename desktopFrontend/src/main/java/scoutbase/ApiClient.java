package scoutbase;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Clase encargada de realizar peticiones HTTP al backend.
 *
 * <p>Actualmente solo implementa peticiones GET autenticadas
 * mediante el token almacenado en {@link SessionManager}.</p>
 */
public class ApiClient {

    /**
     * Cliente HTTP utilizado para enviar las peticiones al servidor.
     */
    private final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     * Realiza una petición GET a la URL indicada.
     *
     * @param url dirección del endpoint al que se quiere acceder
     * @return cuerpo de la respuesta del servidor
     * @throws IOException si ocurre un error durante la comunicación
     * @throws InterruptedException si la petición es interrumpida
     */
    public String get(String url) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + SessionManager.getAuthToken())
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}