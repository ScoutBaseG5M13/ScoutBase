package scoutbase.stat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO que representa una estadística de jugador.
 *
 * <p>El backend trabaja con estadísticas simples formadas por:</p>
 * <ul>
 *     <li>id: identificador de la estadística</li>
 *     <li>code: código de 3 caracteres, por ejemplo PAC, SHO, PAS</li>
 *     <li>value: valor numérico entre 0 y 5</li>
 * </ul>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatDTO {

    private String id;
    private String code;
    private Integer value;

    public StatDTO() {
    }

    public StatDTO(String id, String code, Integer value) {
        this.id = id;
        this.code = code;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}