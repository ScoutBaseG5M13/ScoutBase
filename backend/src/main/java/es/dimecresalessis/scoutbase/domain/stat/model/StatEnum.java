package es.dimecresalessis.scoutbase.domain.stat.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatEnum {
    CONDUCCION("Conducción", "CON"),
    PASE("Pase", "PAS"),
    RESISTENCIA("Resistencia", "RES"),
    POTENCIA("Potencia", "POT"),
    VELOCIDAD("Velocidad", "VEL");

    String name;
    String code;

    public static StatEnum fromName(String name) {
        if (isValid(name, "name")) {
            for (StatEnum statEnum : StatEnum.values()) {
                if (statEnum.name.equals(name)) {
                    return statEnum;
                }
            }
        }
        throw new IllegalArgumentException("Invalid 'Stat.name': '" + name + "'");
    }

    public static StatEnum fromCode(String code) {
        if (isValid(code, "code")) {
            for (StatEnum statEnum : StatEnum.values()) {
                if (statEnum.code.equals(code)) {
                    return statEnum;
                }
            }
        }
        throw new IllegalArgumentException("Invalid 'Stat.code': '" + code + "'");
    }

    public static boolean isValid(String value, String field) {
        for (StatEnum stat : StatEnum.values()) {
            switch (field) {
                case "name":
                    if (stat.name.equalsIgnoreCase(value)) {
                        return true;
                    }
                    break;
                case "code":
                    if (stat.code.equalsIgnoreCase(value)) {
                        return true;
                    }
                    break;
                default:
                    return false;
            }
        }
        return false;
    }
}
