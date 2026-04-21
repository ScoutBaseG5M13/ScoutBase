package scoutbase.club;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Objeto de transferencia de datos (DTO) que representa un club.
 *
 * <p>Se utiliza para mapear la información de un club recibida desde el backend,
 * permitiendo trabajar con sus datos dentro de la aplicación.</p>
 *
 * <p>Incluye únicamente los campos necesarios para la capa cliente.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClubDTO {

    /**
     * Identificador único del club.
     */
    private String id;

    /**
     * Nombre del club.
     */
    private String name;

    /**
     * Constructor vacío necesario para la deserialización desde JSON.
     */
    public ClubDTO() {
    }

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

    /**
     * Devuelve el nombre del club.
     *
     * @return nombre del club
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre del club.
     *
     * @param name nombre a establecer
     */
    public void setName(String name) {
        this.name = name;
    }
}