package scoutbase.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Objeto de transferencia de datos (DTO) que representa un usuario del sistema ScoutBase.
 *
 * <p>Esta clase se utiliza para mapear la información de usuarios recibida
 * desde la API REST del backend y transferirla entre las diferentes capas
 * de la aplicación desktop.</p>
 *
 * <p>En la estructura actual del backend, el usuario puede tener un indicador
 * global de superadministrador mediante {@code superAdmin}. El resto de roles
 * funcionales pueden depender del contexto de un UserClub o UserTeam concreto.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    /**
     * Identificador único del usuario en formato UUID.
     */
    private String id;

    /**
     * Nombre de usuario utilizado para autenticación.
     */
    private String username;

    /**
     * Contraseña del usuario.
     *
     * <p>Normalmente este campo no debería utilizarse desde el frontend.</p>
     */
    private String password;

    /**
     * Rol directo del usuario si el backend lo informa.
     *
     * <p>Puede venir vacío o nulo si el rol se calcula según el UserClub
     * o UserTeam seleccionado.</p>
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
     * Dirección de correo electrónico asociada al usuario.
     */
    private String email;

    /**
     * Indica si el usuario tiene permisos globales de superadministrador.
     */
    private boolean superAdmin;

    /**
     * Constructor vacío requerido por Jackson para la deserialización JSON.
     */
    public UserDto() {
    }

    /**
     * Devuelve el identificador único del usuario.
     *
     * @return identificador UUID del usuario
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador único del usuario.
     *
     * @param id identificador UUID a establecer
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre de usuario utilizado para autenticación.
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
     * Devuelve el rol directo del usuario si el backend lo informa.
     *
     * @return rol directo del usuario
     */
    public String getRole() {
        return role;
    }

    /**
     * Establece el rol directo del usuario.
     *
     * @param role rol a establecer
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Devuelve el nombre real del usuario.
     *
     * @return nombre real del usuario
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
     * @return apellidos del usuario
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
     * Devuelve el correo electrónico asociado al usuario.
     *
     * @return dirección de correo electrónico
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param email correo electrónico a establecer
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Indica si el usuario es superadministrador global.
     *
     * @return {@code true} si el usuario es SUPERADMIN; {@code false} en caso contrario
     */
    public boolean isSuperAdmin() {
        return superAdmin;
    }

    /**
     * Establece si el usuario es superadministrador global.
     *
     * @param superAdmin valor de superadministrador
     */
    public void setSuperAdmin(boolean superAdmin) {
        this.superAdmin = superAdmin;
    }

    /**
     * Devuelve el rol global legible del usuario.
     *
     * <p>Si el usuario es superadministrador, devuelve {@code SUPERADMIN}.
     * Si el backend informa un rol directo, devuelve ese rol. En caso contrario,
     * indica que no existe un rol global asignado.</p>
     *
     * @return rol global formateado para mostrar en interfaz
     */
    public String getDisplayRole() {
        if (superAdmin) {
            return "SUPERADMIN";
        }

        if (role != null && !role.isBlank()) {
            return role.replace("ROLE_", "");
        }

        return "SIN ROL GLOBAL";
    }

    /**
     * Indica si el usuario tiene algún rol global conocido.
     *
     * <p>Esto no contempla roles contextuales de UserClub o UserTeam,
     * únicamente permisos globales presentes directamente en el usuario.</p>
     *
     * @return {@code true} si tiene rol global; {@code false} en caso contrario
     */
    public boolean hasGlobalRole() {
        return superAdmin || (role != null && !role.isBlank());
    }

    /**
     * Devuelve el nombre completo del usuario concatenando nombre y apellidos.
     *
     * @return nombre completo formateado
     */
    public String getFullName() {
        String fullName = "";

        if (name != null) {
            fullName += name;
        }

        if (surname != null && !surname.isBlank()) {
            fullName += " " + surname;
        }

        return fullName.trim();
    }
}