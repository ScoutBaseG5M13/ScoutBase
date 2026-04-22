package scoutbase.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Objeto de transferencia de datos (DTO) que representa un usuario.
 *
 * <p>Se utiliza para mapear la información de usuarios obtenida desde el backend,
 * incluyendo datos de identificación, credenciales, rol y datos personales.</p>
 *
 * <p>La anotación {@code @JsonIgnoreProperties(ignoreUnknown = true)} permite
 * ignorar campos adicionales presentes en la respuesta de la API, evitando
 * errores durante la deserialización.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    /**
     * Identificador único del usuario.
     */
    private String id;

    /**
     * Nombre de usuario utilizado para autenticación.
     */
    private String username;

    /**
     * Contraseña del usuario (normalmente en formato cifrado).
     */
    private String password;

    /**
     * Rol del usuario dentro del sistema.
     */
    private String role;

    /**
     * Nombre real del usuario.
     */
    private String name;

    /**
     * Apellidos del usuario.
     */
    private String surname;

    /**
     * Correo electrónico del usuario.
     */
    private String email;

    /**
     * Constructor vacío necesario para la deserialización desde JSON.
     */
    public UserDto() {
    }

    /**
     * Devuelve el identificador del usuario.
     *
     * @return identificador del usuario
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador del usuario.
     *
     * @param id identificador a establecer
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre de usuario.
     *
     * @return nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     *
     * @param username nombre de usuario a establecer
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Devuelve la contraseña del usuario.
     *
     * @return contraseña del usuario
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param password contraseña a establecer
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Devuelve el rol del usuario.
     *
     * @return rol del usuario
     */
    public String getRole() {
        return role;
    }

    /**
     * Establece el rol del usuario.
     *
     * @param role rol a establecer
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Devuelve el nombre real del usuario.
     *
     * @return nombre real
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre real del usuario.
     *
     * @param name nombre a establecer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devuelve los apellidos del usuario.
     *
     * @return apellidos
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Establece los apellidos del usuario.
     *
     * @param surname apellidos a establecer
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Devuelve el correo electrónico del usuario.
     *
     * @return correo electrónico
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param email correo a establecer
     */
    public void setEmail(String email) {
        this.email = email;
    }
}