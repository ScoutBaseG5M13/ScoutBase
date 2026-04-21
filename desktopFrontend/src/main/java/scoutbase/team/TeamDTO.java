package scoutbase.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Objeto de transferencia de datos (DTO) que representa un equipo.
 *
 * <p>Se utiliza para mapear la información de equipos recibida desde el backend,
 * incluyendo sus datos básicos, la relación con un club y las listas de
 * jugadores, entrenadores y scouts asociados.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamDTO {

    /**
     * Identificador único del equipo.
     */
    private String id;

    /**
     * Nombre del equipo.
     */
    private String name;

    /**
     * Categoría principal del equipo (por ejemplo: juvenil, senior, etc.).
     */
    private String category;

    /**
     * Subcategoría del equipo.
     */
    private String subcategory;

    /**
     * Identificador del club al que pertenece el equipo.
     */
    private String clubId;

    /**
     * Referencia al objeto club cuando viene embebido en la respuesta del backend.
     */
    private ClubRef club;

    /**
     * Lista de identificadores de jugadores asociados al equipo.
     */
    private List<String> players;

    /**
     * Lista de identificadores de entrenadores asociados al equipo.
     */
    private List<String> trainers;

    /**
     * Lista de identificadores de scouts asociados al equipo.
     */
    private List<String> scouters;

    /**
     * Constructor vacío necesario para la deserialización desde JSON.
     */
    public TeamDTO() {}

    /**
     * Devuelve el identificador del club asociado al equipo.
     *
     * <p>Prioriza el campo {@code clubId} si está presente. En caso contrario,
     * intenta obtener el identificador desde el objeto {@link ClubRef} embebido.</p>
     *
     * @return identificador del club o {@code null} si no se puede resolver
     */
    public String getResolvedClubId() {
        if (clubId != null && !clubId.isBlank()) {
            return clubId;
        }
        if (club != null) {
            return club.getId();
        }
        return null;
    }

    /**
     * Devuelve el identificador del equipo.
     *
     * @return identificador del equipo
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador del equipo.
     *
     * @param id identificador a establecer
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre del equipo.
     *
     * @return nombre del equipo
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del equipo.
     *
     * @param name nombre a establecer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devuelve la categoría del equipo.
     *
     * @return categoría del equipo
     */
    public String getCategory() {
        return category;
    }

    /**
     * Establece la categoría del equipo.
     *
     * @param category categoría a establecer
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Devuelve la subcategoría del equipo.
     *
     * @return subcategoría del equipo
     */
    public String getSubcategory() {
        return subcategory;
    }

    /**
     * Establece la subcategoría del equipo.
     *
     * @param subcategory subcategoría a establecer
     */
    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    /**
     * Devuelve el identificador del club asociado.
     *
     * @return identificador del club
     */
    public String getClubId() {
        return clubId;
    }

    /**
     * Establece el identificador del club asociado.
     *
     * @param clubId identificador del club
     */
    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    /**
     * Devuelve la referencia al club embebido.
     *
     * @return objeto {@link ClubRef}
     */
    public ClubRef getClub() {
        return club;
    }

    /**
     * Establece la referencia al club embebido.
     *
     * @param club objeto club a establecer
     */
    public void setClub(ClubRef club) {
        this.club = club;
    }

    /**
     * Devuelve la lista de identificadores de jugadores.
     *
     * @return lista de jugadores
     */
    public List<String> getPlayers() {
        return players;
    }

    /**
     * Establece la lista de identificadores de jugadores.
     *
     * @param players lista de jugadores
     */
    public void setPlayers(List<String> players) {
        this.players = players;
    }

    /**
     * Devuelve la lista de identificadores de entrenadores.
     *
     * @return lista de entrenadores
     */
    public List<String> getTrainers() {
        return trainers;
    }

    /**
     * Establece la lista de identificadores de entrenadores.
     *
     * @param trainers lista de entrenadores
     */
    public void setTrainers(List<String> trainers) {
        this.trainers = trainers;
    }

    /**
     * Devuelve la lista de identificadores de scouts.
     *
     * @return lista de scouts
     */
    public List<String> getScouters() {
        return scouters;
    }

    /**
     * Establece la lista de identificadores de scouts.
     *
     * @param scouters lista de scouts
     */
    public void setScouters(List<String> scouters) {
        this.scouters = scouters;
    }

    /**
     * Clase interna que representa una referencia simplificada a un club.
     *
     * <p>Se utiliza cuando el backend incluye información parcial del club
     * dentro de la respuesta del equipo.</p>
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ClubRef {

        /**
         * Identificador del club.
         */
        private String id;

        /**
         * Constructor vacío necesario para la deserialización.
         */
        public ClubRef() {}

        /**
         * Devuelve el identificador del club.
         *
         * @return identificador del club
         */
        public String getId() {
            return id;
        }

        /**
         * Establece el identificador del club.
         *
         * @param id identificador a establecer
         */
        public void setId(String id) {
            this.id = id;
        }
    }
}