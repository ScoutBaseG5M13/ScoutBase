package scoutbase.app;

/**
 * Clase utilitaria encargada de gestionar la sesión del usuario autenticado
 * dentro de la aplicación ScoutBase Desktop.
 *
 * <p>Esta clase almacena temporalmente en memoria la información necesaria
 * para mantener una sesión activa durante la ejecución de la aplicación,
 * incluyendo:</p>
 *
 * <ul>
 *     <li>El token JWT de autenticación.</li>
 *     <li>El identificador único de sesión.</li>
 *     <li>El nombre de usuario autenticado.</li>
 *     <li>El rol asignado al usuario.</li>
 * </ul>
 *
 * <p>La información almacenada es utilizada principalmente por:</p>
 * <ul>
 *     <li>{@code ApiClient} para adjuntar el token Bearer automáticamente.</li>
 *     <li>Los controladores JavaFX para adaptar la interfaz según el rol.</li>
 *     <li>Los servicios de la aplicación para validar el estado de autenticación.</li>
 * </ul>
 *
 * <p>Todos los miembros y métodos son estáticos, ya que la aplicación
 * únicamente necesita una sesión activa global simultánea.</p>
 */
public class SessionManager {

    /**
     * Token JWT utilizado para autenticar las peticiones HTTP
     * contra la API REST del backend.
     */
    private static String authToken;

    /**
     * Identificador único de sesión generado por el backend.
     *
     * <p>Puede utilizarse para trazabilidad, depuración
     * o auditoría de peticiones.</p>
     */
    private static String sessionId;

    /**
     * Nombre de usuario asociado a la sesión autenticada.
     */
    private static String username;

    /**
     * Rol asignado al usuario autenticado.
     *
     * <p>Este valor puede utilizarse para controlar permisos
     * y adaptar dinámicamente la interfaz de usuario.</p>
     */
    private static String role;

    /**
     * Constructor privado para evitar la instanciación de la clase.
     *
     * <p>La gestión de sesión se realiza exclusivamente mediante
     * miembros y métodos estáticos.</p>
     */
    private SessionManager() {
    }

    /**
     * Guarda en memoria la información de una sesión autenticada.
     *
     * <p>Este método debe invocarse tras un inicio de sesión exitoso
     * para almacenar los datos necesarios durante la ejecución
     * de la aplicación.</p>
     *
     * @param token token JWT devuelto por el backend
     * @param session identificador único de sesión
     * @param user nombre de usuario autenticado
     * @param userRole rol asignado al usuario autenticado
     */
    public static void saveSession(String token,
                                   String session,
                                   String user,
                                   String userRole) {

        authToken = token;
        sessionId = session;
        username = user;
        role = userRole;
    }

    /**
     * Devuelve el token JWT almacenado actualmente.
     *
     * @return token de autenticación activo;
     *         {@code null} si no existe sesión iniciada
     */
    public static String getAuthToken() {
        return authToken;
    }

    /**
     * Devuelve el identificador de sesión actual.
     *
     * @return identificador de sesión almacenado
     */
    public static String getSessionId() {
        return sessionId;
    }

    /**
     * Devuelve el nombre de usuario autenticado.
     *
     * @return username del usuario de la sesión activa
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Devuelve el rol asignado al usuario autenticado.
     *
     * @return rol de la sesión actual
     */
    public static String getRole() {
        return role;
    }

    /**
     * Indica si existe actualmente una sesión autenticada válida.
     *
     * <p>Se considera válida una sesión cuando existe un token JWT
     * almacenado y éste no está vacío.</p>
     *
     * @return {@code true} si existe sesión activa;
     *         {@code false} en caso contrario
     */
    public static boolean isLoggedIn() {
        return authToken != null && !authToken.isBlank();
    }

    /**
     * Elimina completamente la información de la sesión actual.
     *
     * <p>Este método debe ejecutarse al cerrar sesión para garantizar
     * que no permanezcan datos sensibles en memoria.</p>
     *
     * <p>Tras invocar este método:</p>
     * <ul>
     *     <li>El usuario deja de estar autenticado.</li>
     *     <li>Las peticiones protegidas dejarán de incluir JWT válido.</li>
     *     <li>La aplicación deberá redirigir al login.</li>
     * </ul>
     */
    public static void clear() {
        authToken = null;
        sessionId = null;
        username = null;
        role = null;
    }
}