package scoutbase.scout;

/**
 * DTO que representa un scout en la aplicación.
 *
 * <p>Se utiliza para mostrar y gestionar scouts desde la interfaz
 * mientras se define o conecta la integración completa con backend.</p>
 */
public class ScoutDTO {

    /**
     * Identificador del scout.
     */
    private String id;

    /**
     * Nombre del scout.
     */
    private String name;

    /**
     * Apellidos del scout.
     */
    private String surname;

    /**
     * Correo electrónico del scout.
     */
    private String email;

    /**
     * Club asignado al scout.
     */
    private String clubName;

    /**
     * Equipo asignado al scout.
     */
    private String teamName;

    /**
     * Constructor vacío.
     */
    public ScoutDTO() {
    }

    /**
     * Constructor completo.
     *
     * @param id identificador
     * @param name nombre
     * @param surname apellidos
     * @param email correo electrónico
     * @param clubName club asignado
     * @param teamName equipo asignado
     */
    public ScoutDTO(String id, String name, String surname, String email, String clubName, String teamName) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.clubName = clubName;
        this.teamName = teamName;
    }

    /**
     * @return identificador del scout
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
     * @return nombre del scout
     */
    public String getName() {
        return name;
    }

    /**
     * @param name nombre a establecer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return apellidos del scout
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
     * @return correo electrónico del scout
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email correo a establecer
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return nombre del club asignado
     */
    public String getClubName() {
        return clubName;
    }

    /**
     * @param clubName nombre del club a establecer
     */
    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    /**
     * @return nombre del equipo asignado
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * @param teamName nombre del equipo a establecer
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}