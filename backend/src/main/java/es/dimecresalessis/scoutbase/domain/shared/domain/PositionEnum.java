package es.dimecresalessis.scoutbase.domain.shared.domain;

public enum PositionEnum {
    PORTERO,
    LATERAL_DERECHO,
    LATERAL_IZQUIERDO,
    DEFENSA_CENTRAL,
    DEFENSA_CENTRAL_DERECHO,
    DEFENSA_CENTRAL_IZQUIERDO,
    CARRILERO_DERECHO,
    CARRILERO_IZQUIERDO,
    MEDIOCENTRO,
    MEDIOCENTRO_DEFENSIVO,
    MEDIOCENTRO_OFENSIVO,
    INTERIOR_DERECHO,
    INTERIOR_IZQUIERDO,
    EXTREMO_DERECHO,
    EXTREMO_IZQUIERDO,
    MEDIAPUNTA,
    DELANTERO_CENTRO,
    SEGUNDO_DELANTERO;

    public static PositionEnum fromName(String name) {
        if (isValid(name)) {
            return PositionEnum.valueOf(name.toUpperCase());
        }
        throw new IllegalArgumentException("Invalid 'Position' name: '" + name + "'");
    }

    public static boolean isValid(String value) {
        for (PositionEnum position : PositionEnum.values()) {
            if (position.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
