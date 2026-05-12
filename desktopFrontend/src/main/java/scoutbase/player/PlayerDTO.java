package scoutbase.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Objeto de transferencia de datos (DTO) que representa a un jugador dentro de ScoutBase.
 *
 * <p>Esta clase se utiliza para mapear la información de jugadores recibida
 * desde la API REST del backend y transferirla entre la capa de servicios
 * y los controladores JavaFX.</p>
 *
 * <p>El DTO contiene información básica relacionada con:</p>
 * <ul>
 *     <li>Identificación del jugador.</li>
 *     <li>Datos personales y de contacto.</li>
 *     <li>Información deportiva.</li>
 *     <li>Relación con el equipo al que pertenece.</li>
 * </ul>
 *
 * <p>La anotación {@link JsonIgnoreProperties} permite ignorar automáticamente
 * propiedades JSON desconocidas durante la deserialización, manteniendo
 * compatibilidad con futuras versiones del backend.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerDTO {

    /**
     * Identificador único del jugador en formato UUID.
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
     * Correo electrónico asociado al jugador.
     */
    private String email;

    /**
     * Número dorsal del jugador.
     */
    private int number;

    /**
     * Posición principal del jugador en el terreno de juego.
     *
     * <p>Ejemplos posibles: portero, defensa, centrocampista,
     * delantero u otras posiciones definidas por el sistema.</p>
     */
    private String position;

    /**
     * Prioridad o valoración asignada al jugador dentro del sistema.
     *
     * <p>Este valor puede utilizarse para clasificaciones internas,
     * seguimiento de talento o sistemas de scouting.</p>
     */
    private int priority;

    /**
     * Identificador UUID del equipo al que pertenece el jugador.
     */
    private String teamId;

    /**
     * Constructor vacío requerido por Jackson para la deserialización JSON.
     */
    public PlayerDTO() {
    }

    /**
     * Devuelve el identificador único del jugador.
     *
     * @return identificador UUID del jugador
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador único del jugador.
     *
     * @param id identificador UUID a establecer
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
     * Devuelve el correo electrónico asociado al jugador.
     *
     * @return dirección de correo electrónico
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
     * @return número dorsal
     */
    public int getNumber() {
        return number;
    }

    /**
     * Establece el número dorsal del jugador.
     *
     * @param number dorsal a establecer
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Devuelve la posición principal del jugador.
     *
     * @return posición del jugador
     */
    public String getPosition() {
        return position;
    }

    /**
     * Establece la posición principal del jugador.
     *
     * @param position posición a establecer
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * Devuelve la prioridad o valoración asignada al jugador.
     *
     * @return prioridad del jugador
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Establece la prioridad o valoración del jugador.
     *
     * @param priority prioridad a establecer
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Devuelve el identificador del equipo asociado al jugador.
     *
     * @return identificador UUID del equipo
     */
    public String getTeamId() {
        return teamId;
    }

    /**
     * Establece el identificador del equipo asociado al jugador.
     *
     * @param teamId identificador UUID del equipo
     */
    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    /**
     * Devuelve el nombre completo del jugador concatenando nombre y apellidos.
     *
     * <p>Este método resulta útil para tablas, listados y componentes
     * visuales de JavaFX.</p>
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
     * Devuelve una representación textual legible del jugador.
     *
     * @return nombre completo del jugador o un texto genérico si no existe
     */
    @Override
    public String toString() {
        String fullName = getFullName();
        return !fullName.isBlank() ? fullName : "Jugador";
    }
}