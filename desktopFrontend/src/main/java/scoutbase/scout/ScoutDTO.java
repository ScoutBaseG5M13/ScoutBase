package scoutbase.scout;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Objeto de transferencia de datos (DTO) que representa a un scout dentro de ScoutBase.
 *
 * <p>En la estructura actual del backend, un scout se modela como un usuario
 * con un rol específico dentro del sistema o dentro de una relación con un equipo.</p>
 *
 * <p>Esta clase permite mapear la información de usuarios con perfil de scout
 * recibida desde la API REST y mostrarla en la interfaz desktop.</p>
 *
 * <p>La anotación {@link JsonIgnoreProperties} permite ignorar propiedades JSON
 * desconocidas durante la deserialización, manteniendo compatibilidad con futuras
 * ampliaciones del backend.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScoutDTO {

    /**
     * Identificador único del scout en formato UUID.
     */
    private String id;

    /**
     * Nombre de usuario utilizado para autenticación.
     */
    private String username;

    /**
     * Nombre real del scout.
     */
    private String name;

    /**
     * Apellidos del scout.
     */
    private String surname;

    /**
     * Correo electrónico asociado al scout.
     */
    private String email;

    /**
     * Rol asignado al usuario dentro del sistema o del contexto deportivo.
     */
    private String role;

    /**
     * Constructor vacío requerido por Jackson para la deserialización JSON.
     */
    public ScoutDTO() {
    }

    /**
     * Devuelve el identificador único del scout.
     *
     * @return identificador UUID del scout
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador único del scout.
     *
     * @param id identificador UUID a establecer
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre de usuario del scout.
     *
     * @return nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario del scout.
     *
     * @param username nombre de usuario a establecer
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Devuelve el nombre real del scout.
     *
     * @return nombre real
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre real del scout.
     *
     * @param name nombre real a establecer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devuelve los apellidos del scout.
     *
     * @return apellidos del scout
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Establece los apellidos del scout.
     *
     * @param surname apellidos a establecer
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Devuelve el correo electrónico asociado al scout.
     *
     * @return dirección de correo electrónico
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico asociado al scout.
     *
     * @param email correo electrónico a establecer
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Devuelve el rol asignado al scout.
     *
     * @return rol del scout
     */
    public String getRole() {
        return role;
    }

    /**
     * Establece el rol asignado al scout.
     *
     * @param role rol a establecer
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Devuelve el nombre completo del scout concatenando nombre y apellidos.
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

    /**
     * Devuelve una representación textual legible del scout.
     *
     * @return nombre completo, username o texto genérico si no hay datos disponibles
     */
    @Override
    public String toString() {
        String fullName = getFullName();

        if (!fullName.isBlank()) {
            return fullName;
        }

        return username != null && !username.isBlank() ? username : "Scout";
    }
}