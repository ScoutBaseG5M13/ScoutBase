package scoutbase.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Representa la estructura estándar de respuesta devuelta por la API REST de ScoutBase.
 *
 * <p>Todas las respuestas del backend siguen un formato común encapsulado
 * dentro de esta clase, independientemente del endpoint utilizado.</p>
 *
 * <p>La respuesta contiene:</p>
 * <ul>
 *     <li>El estado de éxito o error de la operación.</li>
 *     <li>Un mensaje descriptivo proporcionado por el backend.</li>
 *     <li>Los datos devueltos en formato JSON dinámico.</li>
 *     <li>Metadatos adicionales como el identificador de sesión y timestamp.</li>
 * </ul>
 *
 * <p>La propiedad {@code data} se almacena como {@link JsonNode} para permitir
 * trabajar tanto con objetos individuales como con listas o estructuras JSON complejas.</p>
 *
 * <p>Además, la clase proporciona métodos auxiliares para convertir automáticamente
 * el contenido de {@code data} a DTOs o colecciones tipadas mediante Jackson.</p>
 */
public class ApiResponse {

    /**
     * Instancia reutilizable de {@link ObjectMapper} utilizada para convertir
     * el contenido JSON de {@code data} a objetos Java tipados.
     */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Indica si la operación realizada por el backend fue exitosa.
     */
    private boolean success;

    /**
     * Mensaje descriptivo asociado a la respuesta.
     *
     * <p>Puede contener información de éxito, advertencias o detalles de error.</p>
     */
    private String message;

    /**
     * Contenido principal devuelto por el backend en formato JSON.
     *
     * <p>Puede representar:</p>
     * <ul>
     *     <li>Un objeto DTO individual.</li>
     *     <li>Una lista de elementos.</li>
     *     <li>Un valor simple.</li>
     *     <li>Una estructura JSON compleja.</li>
     * </ul>
     */
    private JsonNode data;

    /**
     * Identificador único de sesión asociado a la petición.
     *
     * <p>Este valor es generado por el backend y puede utilizarse
     * para trazabilidad o depuración.</p>
     */
    private String sessionId;

    /**
     * Marca temporal generada por el backend indicando cuándo
     * se creó la respuesta.
     */
    private String timestamp;

    /**
     * Constructor vacío requerido por Jackson para la deserialización automática.
     */
    public ApiResponse() {
    }

    /**
     * Indica si la operación se completó correctamente.
     *
     * @return {@code true} si la operación fue exitosa;
     *         {@code false} en caso contrario
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Establece el estado de éxito de la operación.
     *
     * @param success valor de éxito a establecer
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Devuelve el mensaje descriptivo de la respuesta.
     *
     * @return mensaje proporcionado por el backend
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
     * Devuelve el contenido JSON asociado a la respuesta.
     *
     * @return datos devueltos por la API en formato {@link JsonNode}
     */
    public JsonNode getData() {
        return data;
    }

    /**
     * Establece los datos JSON de la respuesta.
     *
     * @param data contenido JSON recibido desde el backend
     */
    public void setData(JsonNode data) {
        this.data = data;
    }

    /**
     * Devuelve el identificador de sesión asociado a la petición.
     *
     * @return identificador de sesión
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * Establece el identificador de sesión.
     *
     * @param sessionId identificador de sesión generado por el backend
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * Devuelve la marca temporal de generación de la respuesta.
     *
     * @return timestamp de la respuesta
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Establece la marca temporal de la respuesta.
     *
     * @param timestamp fecha y hora asociadas a la respuesta
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Convierte el contenido de {@code data} a una instancia tipada.
     *
     * <p>Este método facilita transformar automáticamente el JSON recibido
     * desde el backend en un DTO concreto.</p>
     *
     * <pre>
     * ClubDTO club = response.dataAs(ClubDTO.class);
     * </pre>
     *
     * @param clazz clase destino de conversión
     * @param <T> tipo del objeto resultante
     * @return objeto convertido al tipo especificado
     */
    public <T> T dataAs(Class<T> clazz) {
        return mapper.convertValue(data, clazz);
    }

    /**
     * Convierte el contenido de {@code data} a una estructura genérica tipada.
     *
     * <p>Especialmente útil para listas o colecciones complejas.</p>
     *
     * <pre>
     * List&lt;ClubDTO&gt; clubs =
     *     response.dataAs(new TypeReference&lt;List&lt;ClubDTO&gt;&gt;() {});
     * </pre>
     *
     * @param typeReference referencia del tipo objetivo
     * @param <T> tipo resultante
     * @return estructura convertida al tipo especificado
     */
    public <T> T dataAs(TypeReference<T> typeReference) {
        return mapper.convertValue(data, typeReference);
    }
}