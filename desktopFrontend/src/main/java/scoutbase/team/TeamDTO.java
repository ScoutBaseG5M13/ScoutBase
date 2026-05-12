package scoutbase.team;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Objeto de transferencia de datos (DTO) que representa un equipo dentro de ScoutBase.
 *
 * <p>Esta clase se utiliza para mapear la información de equipos recibida
 * desde la API REST del backend y transferirla entre la capa de servicios
 * y los controladores JavaFX.</p>
 *
 * <p>El DTO contempla tanto campos simples del equipo como referencias
 * opcionales a entidades relacionadas. Esto permite trabajar correctamente
 * aunque el backend devuelva algunas relaciones como identificadores planos
 * o como objetos embebidos.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamDTO {

    /**
     * Identificador único del equipo en formato UUID.
     */
    private String id;

    /**
     * Nombre del equipo.
     */
    private String name;

    /**
     * Categoría principal del equipo.
     *
     * <p>Ejemplos posibles: benjamín, alevín, infantil, cadete, juvenil,
     * senior u otras categorías definidas por el backend.</p>
     */
    private String category;

    /**
     * Subcategoría del equipo.
     *
     * <p>Permite diferenciar equipos dentro de una misma categoría,
     * por ejemplo A, B, C o similares.</p>
     */
    private String subcategory;

    /**
     * Identificador del club al que pertenece el equipo.
     *
     * <p>Puede venir informado directamente por el backend o resolverse
     * desde el objeto {@link ClubRef} embebido.</p>
     */
    private String clubId;

    /**
     * Referencia simplificada al club cuando el backend devuelve
     * la relación embebida dentro del equipo.
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
     * Constructor vacío requerido por Jackson para la deserialización JSON.
     */
    public TeamDTO() {
    }

    /**
     * Devuelve el identificador del club asociado al equipo.
     *
     * <p>Este método prioriza el campo {@code clubId} si está presente.
     * Si no existe, intenta obtener el identificador desde el objeto
     * {@link ClubRef} embebido en la respuesta.</p>
     *
     * @return identificador UUID del club, o {@code null} si no se puede resolver
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
     * Devuelve el identificador único del equipo.
     *
     * @return identificador UUID del equipo
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador único del equipo.
     *
     * @param id identificador UUID a establecer
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
     * @param name nombre del equipo a establecer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devuelve la categoría principal del equipo.
     *
     * @return categoría del equipo
     */
    public String getCategory() {
        return category;
    }

    /**
     * Establece la categoría principal del equipo.
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
     * Devuelve el identificador directo del club asociado.
     *
     * @return identificador UUID del club
     */
    public String getClubId() {
        return clubId;
    }

    /**
     * Establece el identificador directo del club asociado.
     *
     * @param clubId identificador UUID del club
     */
    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    /**
     * Devuelve la referencia embebida al club asociado.
     *
     * @return referencia simplificada del club
     */
    public ClubRef getClub() {
        return club;
    }

    /**
     * Establece la referencia embebida al club asociado.
     *
     * @param club referencia simplificada del club
     */
    public void setClub(ClubRef club) {
        this.club = club;
    }

    /**
     * Devuelve la lista de identificadores de jugadores asociados al equipo.
     *
     * @return lista de identificadores de jugadores
     */
    public List<String> getPlayers() {
        return players;
    }

    /**
     * Establece la lista de identificadores de jugadores asociados al equipo.
     *
     * @param players lista de identificadores de jugadores
     */
    public void setPlayers(List<String> players) {
        this.players = players;
    }

    /**
     * Devuelve la lista de identificadores de entrenadores asociados al equipo.
     *
     * @return lista de identificadores de entrenadores
     */
    public List<String> getTrainers() {
        return trainers;
    }

    /**
     * Establece la lista de identificadores de entrenadores asociados al equipo.
     *
     * @param trainers lista de identificadores de entrenadores
     */
    public void setTrainers(List<String> trainers) {
        this.trainers = trainers;
    }

    /**
     * Devuelve la lista de identificadores de scouts asociados al equipo.
     *
     * @return lista de identificadores de scouts
     */
    public List<String> getScouters() {
        return scouters;
    }

    /**
     * Establece la lista de identificadores de scouts asociados al equipo.
     *
     * @param scouters lista de identificadores de scouts
     */
    public void setScouters(List<String> scouters) {
        this.scouters = scouters;
    }

    /**
     * Devuelve una representación textual legible del equipo.
     *
     * <p>Resulta útil para componentes JavaFX como ComboBox, ListView
     * o mensajes de depuración.</p>
     *
     * @return nombre del equipo
     */
    @Override
    public String toString() {
        return name != null ? name : "Equipo";
    }

    /**
     * Clase interna que representa una referencia simplificada a un club.
     *
     * <p>Se utiliza cuando el backend incluye información parcial del club
     * dentro de la respuesta del equipo en lugar de devolver únicamente
     * un identificador plano.</p>
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ClubRef {

        /**
         * Identificador único del club en formato UUID.
         */
        private String id;

        /**
         * Constructor vacío requerido por Jackson para la deserialización JSON.
         */
        public ClubRef() {
        }

        /**
         * Devuelve el identificador único del club.
         *
         * @return identificador UUID del club
         */
        public String getId() {
            return id;
        }

        /**
         * Establece el identificador único del club.
         *
         * @param id identificador UUID a establecer
         */
        public void setId(String id) {
            this.id = id;
        }
    }
}