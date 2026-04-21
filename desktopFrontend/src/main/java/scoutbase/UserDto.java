package scoutbase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Objeto de transferencia de datos (DTO) que representa un usuario
 * obtenido desde el backend.
 *
 * <p>Se utiliza para mapear la información devuelta por la API,
 * incluyendo identificador, nombre de usuario, contraseña,
 * rol y datos personales básicos.</p>
 *
 * <p>La anotación {@code @JsonIgnoreProperties(ignoreUnknown = true)}
 * permite ignorar campos adicionales que pueda devolver el backend,
 * evitando errores durante la deserialización.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    /**
     * Identificador único del usuario.
     */
    private String id;

    /**
     * Nombre de usuario.
     */
    private String username;

    /**
     * Contraseña del usuario (generalmente en formato cifrado).
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
     * Constructor vacío necesario para la deserialización.
     */
    public UserDto() {
    }

    /**
     * @return identificador del usuario
     */
    public String getId() {
        return id;
    }

    /**
     * @param id identificador a establecer
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return nombre de usuario
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
     * @return contraseña del usuario
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

    /**
     * @return rol del usuario
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role rol a establecer
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return nombre real del usuario
     */
    public String getName() {
        return name;
    }

    /**
     * @param name nombre real a establecer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return apellidos del usuario
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname apellidos a establecer
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return correo electrónico del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email correo electrónico a establecer
     */
    public void setEmail(String email) {
        this.email = email;
    }
}