package scoutbase.auth;

/**
 * Representa la solicitud de autenticación enviada al backend de ScoutBase.
 *
 * <p>Esta clase encapsula las credenciales necesarias para realizar
 * el proceso de inicio de sesión mediante el endpoint de autenticación
 * de la API REST.</p>
 *
 * <p>El objeto se serializa automáticamente a formato JSON antes de ser
 * enviado en el cuerpo de una petición HTTP POST al endpoint:</p>
 *
 * <pre>
 * /api/v1/users/auth/login
 * </pre>
 *
 * <p>La estructura generada coincide con la documentación OpenAPI
 * definida por el backend.</p>
 *
 * <p>Ejemplo JSON generado:</p>
 *
 * <pre>
 * {
 *   "username": "myuser",
 *   "password": "password123"
 * }
 * </pre>
 */
public class LoginRequest {

    /**
     * Nombre de usuario utilizado para autenticarse en el sistema.
     */
    private String username;

    /**
     * Contraseña asociada al usuario.
     */
    private String password;

    /**
     * Constructor vacío requerido por bibliotecas de serialización
     * y deserialización JSON como Jackson.
     */
    public LoginRequest() {
    }

    /**
     * Crea una nueva solicitud de autenticación con las credenciales indicadas.
     *
     * @param username nombre de usuario introducido en el formulario de login
     * @param password contraseña introducida en el formulario de login
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Devuelve el nombre de usuario configurado en la solicitud.
     *
     * @return nombre de usuario utilizado para autenticarse
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario de la solicitud.
     *
     * @param username nombre de usuario a establecer
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Devuelve la contraseña asociada a la solicitud.
     *
     * @return contraseña utilizada para autenticarse
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña de la solicitud.
     *
     * @param password contraseña a establecer
     */
    public void setPassword(String password) {
        this.password = password;
    }
}