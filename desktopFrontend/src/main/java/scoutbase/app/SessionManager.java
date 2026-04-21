package scoutbase.app;

/**
 * Clase utilitaria que gestiona la información de sesión del usuario autenticado.
 *
 * <p>Almacena temporalmente en memoria los datos necesarios para mantener
 * la sesión activa durante la ejecución de la aplicación, como el token
 * de autenticación, el identificador de sesión, el nombre de usuario
 * y el rol asociado.</p>
 *
 * <p>Todos los datos se gestionan mediante miembros estáticos, por lo que
 * no es necesario crear instancias de esta clase.</p>
 */
public class SessionManager {

    /**
     * Token de autenticación utilizado para acceder a la API.
     */
    private static String authToken;

    /**
     * Identificador de sesión proporcionado por el backend.
     */
    private static String sessionId;

    /**
     * Nombre de usuario del usuario autenticado.
     */
    private static String username;

    /**
     * Rol del usuario autenticado.
     */
    private static String role;

    /**
     * Constructor privado para evitar la instanciación de la clase.
     */
    private SessionManager() {
    }

    /**
     * Guarda los datos de la sesión tras un inicio de sesión correcto.
     *
     * @param token token de autenticación
     * @param session identificador de sesión
     * @param user nombre de usuario autenticado
     * @param userRole rol asignado al usuario
     */
    public static void saveSession(String token, String session, String user, String userRole) {
        authToken = token;
        sessionId = session;
        username = user;
        role = userRole;
    }

    /**
     * Devuelve el token de autenticación actual.
     *
     * @return token de autenticación almacenado
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
     * Devuelve el nombre de usuario de la sesión activa.
     *
     * @return nombre de usuario autenticado
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Devuelve el rol del usuario de la sesión activa.
     *
     * @return rol del usuario autenticado
     */
    public static String getRole() {
        return role;
    }

    /**
     * Indica si existe una sesión activa válida.
     *
     * @return {@code true} si hay un token almacenado y no vacío;
     * {@code false} en caso contrario
     */
    public static boolean isLoggedIn() {
        return authToken != null && !authToken.isBlank();
    }

    /**
     * Elimina todos los datos almacenados de la sesión actual.
     *
     * <p>Este método debe invocarse al cerrar sesión para evitar que
     * permanezca información del usuario autenticado en memoria.</p>
     */
    public static void clear() {
        authToken = null;
        sessionId = null;
        username = null;
        role = null;
    }
}