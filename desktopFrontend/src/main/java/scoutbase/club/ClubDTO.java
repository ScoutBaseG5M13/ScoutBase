package scoutbase.club;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Objeto de transferencia de datos (DTO) que representa un club dentro del sistema ScoutBase.
 *
 * <p>Esta clase se utiliza para mapear la información recibida desde la API REST
 * relacionada con clubes deportivos y transferirla entre las distintas capas
 * de la aplicación desktop.</p>
 *
 * <p>Actualmente el DTO contiene únicamente los datos básicos necesarios
 * para la representación de clubes en la interfaz gráfica, aunque puede
 * ampliarse fácilmente en futuras versiones del backend.</p>
 *
 * <p>La anotación {@link JsonIgnoreProperties} permite ignorar automáticamente
 * propiedades JSON desconocidas durante la deserialización, facilitando la
 * compatibilidad con cambios futuros en la API.</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClubDTO {

    /**
     * Identificador único del club en formato UUID.
     */
    private String id;

    /**
     * Nombre oficial del club.
     */
    private String name;

    /**
     * Constructor vacío requerido por Jackson para la deserialización JSON.
     */
    public ClubDTO() {
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

    /**
     * Devuelve el nombre oficial del club.
     *
     * @return nombre del club
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre oficial del club.
     *
     * @param name nombre del club a establecer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devuelve una representación textual legible del club.
     *
     * <p>Este método resulta útil para componentes JavaFX como
     * ComboBox, ListView o logs de depuración.</p>
     *
     * @return nombre del club
     */
    @Override
    public String toString() {
        return name != null ? name : "Club";
    }
}