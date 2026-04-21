package scoutbase.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Objeto de transferencia de datos (DTO) que representa a un jugador.
 *
 * <p>Se utiliza para mapear la información de jugadores recibida desde el backend
 * y para enviar datos relacionados con jugadores en las peticiones HTTP.</p>
 *
 * <p>Incluye los atributos básicos de un jugador como identificación, datos personales,
 * posición en el campo y relación con su equipo.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerDTO {

    /**
     * Identificador único del jugador.
     */
    private String id;

    /**
     * Nombre del jugador.
     */
    private String name;

    /**
     * Apellidos del jugador.
     */
    private String surname;

    /**
     * Edad del jugador.
     */
    private int age;

    /**
     * Correo electrónico del jugador.
     */
    private String email;

    /**
     * Número dorsal del jugador.
     */
    private int number;

    /**
     * Posición en el campo (por ejemplo: delantero, defensa, etc.).
     */
    private String position;

    /**
     * Prioridad o nivel asignado al jugador dentro del sistema.
     */
    private int priority;

    /**
     * Identificador del equipo al que pertenece el jugador.
     */
    private String teamId;

    /**
     * Constructor vacío necesario para la deserialización desde JSON.
     */
    public PlayerDTO() {
    }

    /**
     * Devuelve el identificador del jugador.
     *
     * @return identificador del jugador
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador del jugador.
     *
     * @param id identificador a establecer
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre del jugador.
     *
     * @return nombre del jugador
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del jugador.
     *
     * @param name nombre a establecer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devuelve los apellidos del jugador.
     *
     * @return apellidos del jugador
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Establece los apellidos del jugador.
     *
     * @param surname apellidos a establecer
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Devuelve la edad del jugador.
     *
     * @return edad del jugador
     */
    public int getAge() {
        return age;
    }

    /**
     * Establece la edad del jugador.
     *
     * @param age edad a establecer
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Devuelve el correo electrónico del jugador.
     *
     * @return correo electrónico
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del jugador.
     *
     * @param email correo electrónico a establecer
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Devuelve el número dorsal del jugador.
     *
     * @return número del jugador
     */
    public int getNumber() {
        return number;
    }

    /**
     * Establece el número dorsal del jugador.
     *
     * @param number número a establecer
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Devuelve la posición del jugador en el campo.
     *
     * @return posición del jugador
     */
    public String getPosition() {
        return position;
    }

    /**
     * Establece la posición del jugador en el campo.
     *
     * @param position posición a establecer
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Devuelve la prioridad asignada al jugador.
     *
     * @return prioridad del jugador
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Establece la prioridad del jugador.
     *
     * @param priority prioridad a establecer
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Devuelve el identificador del equipo asociado al jugador.
     *
     * @return identificador del equipo
     */
    public String getTeamId() {
        return teamId;
    }

    /**
     * Establece el identificador del equipo asociado al jugador.
     *
     * @param teamId identificador del equipo
     */
    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}