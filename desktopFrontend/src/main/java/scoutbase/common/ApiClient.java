package scoutbase.common;

import scoutbase.app.SessionManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Cliente HTTP para comunicarse con el backend.
 */
public class ApiClient {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    private HttpRequest.Builder baseRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + SessionManager.getAuthToken());
    }

    public String get(String url) throws IOException, InterruptedException {
        HttpRequest request = baseRequest(url)
                .GET()
                .build();

        return send(request);
    }

    public String post(String url, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = baseRequest(url)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return send(request);
    }

    public String put(String url, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = baseRequest(url)
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return send(request);
    }

    public String delete(String url) throws IOException, InterruptedException {
        HttpRequest request = baseRequest(url)
                .DELETE()
                .build();

        return send(request);
    }

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