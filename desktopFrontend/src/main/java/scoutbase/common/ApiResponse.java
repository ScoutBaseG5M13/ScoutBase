package scoutbase.common;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Representa la respuesta estándar devuelta por la API del backend.
 *
 * <p>Encapsula la información común de todas las respuestas HTTP,
 * incluyendo el estado de la operación, un mensaje descriptivo,
 * los datos devueltos en formato JSON y metadatos adicionales
 * como el identificador de sesión y la marca temporal.</p>
 *
 * <p>Esta clase se utiliza para deserializar las respuestas JSON
 * recibidas desde el backend y facilitar su manejo en la aplicación.</p>
 */
public class ApiResponse {

    /**
     * Indica si la operación se realizó con éxito.
     */
    private boolean success;

    /**
     * Mensaje asociado a la respuesta (informativo o de error).
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
     * Constructor vacío necesario para la deserialización desde JSON.
     */
    public ApiResponse() {
    }

    /**
     * Indica si la operación fue exitosa.
     *
     * @return {@code true} si la operación fue correcta; {@code false} en caso contrario
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Establece el estado de la operación.
     *
     * @param success valor que indica si la operación fue exitosa
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Devuelve el mensaje descriptivo de la respuesta.
     *
     * @return mensaje de la respuesta
     */
    public String getMessage() {
        return message;
    }

    /**
     * Establece el mensaje descriptivo de la respuesta.
     *
     * @param message mensaje a asociar a la respuesta
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Devuelve los datos proporcionados por la API.
     *
     * @return datos en formato {@link JsonNode}
     */
    public JsonNode getData() {
        return data;
    }

    /**
     * Establece los datos de la respuesta.
     *
     * @param data datos en formato JSON
     */
    public void setData(JsonNode data) {
        this.data = data;
    }

    /**
     * Devuelve el identificador de sesión asociado a la respuesta.
     *
     * @return identificador de sesión
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Establece el identificador de sesión.
     *
     * @param sessionId identificador de sesión a establecer
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Devuelve la marca temporal de la respuesta.
     *
     * @return fecha y hora en formato texto
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Establece la marca temporal de la respuesta.
     *
     * @param timestamp fecha y hora en formato texto
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}