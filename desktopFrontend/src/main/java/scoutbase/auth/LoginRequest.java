package scoutbase.auth;

/**
 * Solicitud de login enviada al backend.
 *
 * <p>Contiene el nombre de usuario y la contraseña
 * necesarios para iniciar sesión.</p>
 */
public class LoginRequest {

    /**
     * Nombre de usuario introducido en el login.
     */
    private String username;

    /**
     * Contraseña introducida en el login.
     */
    private String password;

    /**
     * Constructor vacío necesario para trabajar con serialización y deserialización.
     */
    public LoginRequest() {
    }

    /**
     * Crea una solicitud de login con los datos introducidos por el usuario.
     *
     * @param username nombre de usuario
     * @param password contraseña
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @return nombre de usuario de la solicitud
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username nombre de usuario a establecer
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return contraseña de la solicitud
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password contraseña a establecer
     */
    public void setPassword(String password) {
        this.password = password;
    }
}