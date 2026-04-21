package scoutbase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Objeto de transferencia de datos (DTO) que representa un usuario
 * obtenido desde el backend.
 *
 * <p>Se utiliza para mapear la información devuelta por la API,
 * incluyendo el identificador, nombre de usuario, contraseña
 * (generalmente cifrada) y rol.</p>
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
}