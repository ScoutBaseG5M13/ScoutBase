package scoutbase.app;

/**
 * Gestiona la información de sesión del usuario autenticado.
 *
 * <p>Almacena en memoria los datos necesarios para mantener la sesión
 * activa durante la ejecución de la aplicación, como el token de acceso,
 * el identificador de sesión, el nombre de usuario y su rol.</p>
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
     * Constructor privado para evitar instanciación.
     */
    private SessionManager() {
    }

    /**
     * Guarda los datos de la sesión tras un login correcto.
     *
     * @param token token de autenticación
     * @param session identificador de sesión
     * @param user nombre de usuario
     * @param userRole rol del usuario
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
     * @return token de autenticación actual
     */
    public static String getAuthToken() {
        return authToken;
    }

    /**
     * Devuelve el identificador de sesión actual.
     *
     * @return identificador de sesión actual
     */
    public static String getSessionId() {
        return sessionId;
    }

    /**
     * Devuelve el nombre de usuario de la sesión activa.
     *
     * @return nombre de usuario de la sesión activa
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Devuelve el rol del usuario de la sesión activa.
     *
     * @return rol del usuario de la sesión activa
     */
    public static String getRole() {
        return role;
    }

    /**
     * Indica si existe una sesión válida.
     *
     * @return {@code true} si hay un token almacenado; {@code false} en caso contrario
     */
    public static boolean isLoggedIn() {
        return authToken != null && !authToken.isBlank();
    }

    /**
     * Elimina todos los datos de la sesión.
     *
     * <p>Debe llamarse al cerrar sesión para evitar conservar información
     * del usuario autenticado en memoria.</p>
     */
    public static void clear() {
        authToken = null;
        sessionId = null;
        username = null;
        role = null;
    }
}