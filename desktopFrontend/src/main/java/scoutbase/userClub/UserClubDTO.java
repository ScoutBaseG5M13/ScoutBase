package scoutbase.userClub;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Objeto de transferencia de datos (DTO) que representa un UserClub dentro de ScoutBase.
 *
 * <p>Un UserClub representa una agrupación o espacio de gestión de clubes
 * en el que participa el usuario autenticado.</p>
 *
 * <p>Esta entidad sirve como punto de entrada para acceder a los clubes
 * gestionados mediante endpoints como {@code /clubs/user-clubs/{id}}.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserClubDTO {

    /**
     * Identificador único del UserClub en formato UUID.
     */
    private String id;

    /**
     * Nombre del UserClub.
     */
    private String name;

    /**
     * Lista opcional de identificadores de clubes asociados.
     */
    private List<String> clubs;

    /**
     * Lista opcional de identificadores de usuarios administradores.
     */
    private List<String> admins;

    /**
     * Constructor vacío requerido por Jackson para la deserialización JSON.
     */
    public UserClubDTO() {
    }

    /**
     * Devuelve el identificador único del UserClub.
     *
     * @return identificador UUID del UserClub
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el identificador único del UserClub.
     *
     * @param id identificador UUID a establecer
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Devuelve el nombre del UserClub.
     *
     * @return nombre del UserClub
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del UserClub.
     *
     * @param name nombre a establecer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devuelve la lista de clubes asociados.
     *
     * @return lista de identificadores de clubes
     */
    public List<String> getClubs() {
        return clubs;
    }

    /**
     * Establece la lista de clubes asociados.
     *
     * @param clubs lista de identificadores de clubes
     */
    public void setClubs(List<String> clubs) {
        this.clubs = clubs;
    }

    /**
     * Devuelve la lista de administradores asociados al UserClub.
     *
     * @return lista de identificadores de usuarios administradores
     */
    public List<String> getAdmins() {
        return admins;
    }

    /**
     * Establece la lista de administradores asociados al UserClub.
     *
     * @param admins lista de identificadores de usuarios administradores
     */
    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    /**
     * Devuelve una representación textual legible del UserClub.
     *
     * @return nombre del UserClub o texto genérico si no existe
     */
    @Override
    public String toString() {
        return name != null && !name.isBlank() ? name : "UserClub";
    }
}