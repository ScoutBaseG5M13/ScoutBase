package es.dimecresalessis.scoutbase.domain.shared.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PositionEnum {
    PORTERO ("Portero"),
    LATERAL_DERECHO ("Lateral derecho"),
    LATERAL_IZQUIERDO ("Lateral izquierdo"),
    DEFENSA_CENTRAL ("Defensa central"),
    DEFENSA_CENTRAL_DERECHO ("Defensa central derecho"),
    DEFENSA_CENTRAL_IZQUIERDO ("Defensa central izquierdo"),
    CARRILERO_DERECHO ("Carrilero derecho"),
    CARRILERO_IZQUIERDO ("Carrilero izquierdo"),
    MEDIOCENTRO ("Mediocentro"),
    MEDIOCENTRO_DEFENSIVO ("Mediocentro defensivo"),
    MEDIOCENTRO_OFENSIVO ("Mediocentro ofensivo"),
    INTERIOR_DERECHO ("Interior derecho"),
    INTERIOR_IZQUIERDO ("Interior izquierdo"),
    EXTREMO_DERECHO ("Extremo derecho"),
    EXTREMO_IZQUIERDO ("Extremo izquierdo"),
    MEDIAPUNTA ("Mediapunta"),
    DELANTERO_CENTRO ("Delantero centro"),
    SEGUNDO_DELANTERO ("Segundo delantero");

    private String positionName;

    public static PositionEnum fromValue(String value) {
        return Arrays.stream(PositionEnum.values())
                .filter(category -> category.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid 'Position.name': '" + value + "'"));
    }

    public static PositionEnum fromPositionName(String name) {
        return Arrays.stream(PositionEnum.values())
                .filter(category -> category.positionName.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid 'Position.positionName': '" + name + "'"));
    }
}
