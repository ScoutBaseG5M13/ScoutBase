package scoutbase.auth;

/**
 * Representa la solicitud de inicio de sesión enviada al backend.
 *
 * <p>Contiene las credenciales necesarias para autenticar a un usuario,
 * incluyendo el nombre de usuario y la contraseña.</p>
 *
 * <p>Esta clase se utiliza para serializar los datos a formato JSON
 * antes de enviarlos en una petición HTTP.</p>
 */
public class LoginRequest {

    /**
     * Nombre de usuario introducido en el formulario de login.
     */
    private String username;

    /**
     * Contraseña introducida en el formulario de login.
     */
    private String password;

    /**
     * Constructor vacío necesario para procesos de serialización
     * y deserialización (por ejemplo, con Jackson).
     */
    public LoginRequest() {
    }

    /**
     * Crea una nueva solicitud de login con las credenciales proporcionadas.
     *
     * @param username nombre de usuario
     * @param password contraseña
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Devuelve el nombre de usuario de la solicitud.
     *
     * @return nombre de usuario
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
     * Devuelve la contraseña de la solicitud.
     *
     * @return contraseña
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