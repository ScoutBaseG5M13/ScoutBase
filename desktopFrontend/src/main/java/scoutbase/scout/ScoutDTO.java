package scoutbase.scout;

/**
 * Objeto de transferencia de datos (DTO) que representa un scout.
 *
 * <p>En esta implementación, un scout se modela como un usuario
 * con rol específico dentro del sistema.</p>
 */
public class ScoutDTO {

    private String id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String role;

    public ScoutDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}