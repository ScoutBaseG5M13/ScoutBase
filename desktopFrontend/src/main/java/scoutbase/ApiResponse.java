package scoutbase;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Respuestas devueltas por la API del backend.
 *
 * <p>Incluye información sobre si la operación fue correcta,
 * un mensaje descriptivo, los datos devueltos y metadatos
 * de la sesión.</p>
 */
public class ApiResponse {

    /**
     * Indica si la operación se realizó con éxito.
     */
    private boolean success;

    /**
     * Mensaje asociado a la respuesta (error o información).
     */
    private String message;

    /**
     * Datos devueltos por el backend en formato JSON.
     */
    private JsonNode data;

    /**
     * Identificador de la sesión asociada a la petición.
     */
    private String sessionId;

    /**
     * Marca temporal en la que se generó la respuesta.
     */
    private String timestamp;

    /**
     * Constructor vacío necesario para la deserialización JSON.
     */
    public ApiResponse() {
    }

    /**
     * @return true si la operación fue correcta
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success valor que indica si la operación fue exitosa
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * @return mensaje descriptivo de la respuesta
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message mensaje a asociar a la respuesta
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return datos devueltos por la API en formato JSON
     */
    public JsonNode getData() {
        return data;
    }

    /**
     * @param data datos en formato JSON a almacenar
     */
    public void setData(JsonNode data) {
        this.data = data;
    }

    /**
     * @return identificador de sesión asociado a la respuesta
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId identificador de sesión a establecer
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return marca temporal de la respuesta
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp fecha y hora en formato texto
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}