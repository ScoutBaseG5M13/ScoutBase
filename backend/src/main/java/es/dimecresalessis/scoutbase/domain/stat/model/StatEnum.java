package es.dimecresalessis.scoutbase.domain.stat.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatEnum {
    // CAPACIDAD OFENSIVA
    CSL("Conducción para superar línea o fijar rivales", "CSL", "OFENSIVA"),
    DR1("Dribling-Regate-1x1", "DR1", "OFENSIVA"),
    TAP("Timing apropiado para progresar", "TAP", "OFENSIVA"),
    CTR("Centros", "CTR", "OFENSIVA"),
    PCA("Pase corto-asociación", "PCA", "OFENSIVA"),
    PLG("Pase largo", "PLG", "OFENSIVA"),
    COR("Cambio de orientación", "COR", "OFENSIVA"),
    VJU("Visión de juego", "VJU", "OFENSIVA"),
    UPA("Último pase", "UPA", "OFENSIVA"),
    MOV("Movilidad", "MOV", "OFENSIVA"),
    CTE("Cobertura técnica", "CTE", "OFENSIVA"),
    COO("Control orientado", "COO", "OFENSIVA"),
    REM("Remate", "REM", "OFENSIVA"),
    TIR("Tiro", "TIR", "OFENSIVA"),
    DAP("Desmarque apoyo", "DAP", "OFENSIVA"),
    JES("Juego de espaldas", "JES", "OFENSIVA"),
    PDA("Pausa dentro del área", "PDA", "OFENSIVA"),
    DED("Desmarque espalda de la defensa", "DED", "OFENSIVA"),
    CBA("Caída en banda", "CBA", "OFENSIVA"),
    JZI("Juego zonas interiores", "JZI", "OFENSIVA"),
    JZE("Juego zonas exteriores", "JZE", "OFENSIVA"),
    JAE("Juego aéreo", "JAE", "OFENSIVA"),
    PND("Dominio pierna no dominante", "PND", "OFENSIVA"),

    // CAPACIDAD DEFENSIVA
    OMA("Orientación marcaje", "OMA", "DEFENSIVA"),
    TEE("Técnica entrada", "TEE", "DEFENSIVA"),
    TPD("Temporización-presencia defensiva", "TPD", "DEFENSIVA"),
    SAN("Sentido de la anticipación", "SAN", "DEFENSIVA"),
    CTD("Colaboración trabajo defensivo", "CTD", "DEFENSIVA"),
    MAR("Marcaje", "MAR", "DEFENSIVA"),
    OPR("Orientación presión", "OPR", "DEFENSIVA"),
    CON("Contundencia", "CON", "DEFENSIVA"),
    EDF("Equilibrio defensivo", "EDF", "DEFENSIVA"),
    IPP("Intercepción por pase", "IPP", "DEFENSIVA"),
    DOR("Despejes orientados", "DOR", "DEFENSIVA"),
    JAD("Juego aéreo defensivo", "JAD", "DEFENSIVA"),
    MLD("Mandar la línea defensiva", "MLD", "DEFENSIVA"),
    MED("Mandar al equipo en defensa", "MED", "DEFENSIVA"),
    CCO("Cobertura a compañero", "CCO", "DEFENSIVA"),
    CLI("Cobertura a línea", "CLI", "DEFENSIVA"),
    PER("Permutas", "PER", "DEFENSIVA"),
    REP("Repliegue", "REP", "DEFENSIVA"),

    // CAPACIDAD MENTAL/VOLITIVA
    TDE("Toma de decisión", "TDE", "MENTAL/VOLITIVA"),
    PET("Percepción espacio-temporal", "PET", "MENTAL/VOLITIVA"),
    ACO("Atención y concentración", "ACO", "MENTAL/VOLITIVA"),
    PCV("Personalidad: Carácter y valentía", "PCV", "MENTAL/VOLITIVA"),
    CPT("Competitividad", "CPT", "MENTAL/VOLITIVA"),
    CMU("Comunicación", "CMU", "MENTAL/VOLITIVA"),
    AGR("Agresividad", "AGR", "MENTAL/VOLITIVA"),
    LID("Liderazgo", "LID", "MENTAL/VOLITIVA"),
    SAC("Sacrificio", "SAC", "MENTAL/VOLITIVA"),
    COM("Compañerismo", "COM", "MENTAL/VOLITIVA"),

    // CAPACIDAD CONDICIONAL/FÍSICA
    CAR("Coordinación, agilidad, reflejos", "CAR", "CONDICIONAL/FÍSICA"),
    FUE("Fuerza", "FUE", "CONDICIONAL/FÍSICA"),
    VEC("Velocidad espacios cortos", "VEC", "CONDICIONAL/FÍSICA"),
    VRC("Velocidad de reacción compleja", "VRC", "CONDICIONAL/FÍSICA"),
    VEL("Velocidad espacios largos", "VEL", "CONDICIONAL/FÍSICA"),
    POT("Potencia", "POT", "CONDICIONAL/FÍSICA"),
    SIJ("Soporta intensidad de juego todo el partido", "SIJ", "CONDICIONAL/FÍSICA"),
    CRI("Cambio de ritmo", "CRI", "CONDICIONAL/FÍSICA"),
    RAE("Resistencia Aeróbica", "RAE", "CONDICIONAL/FÍSICA"),
    RVE("Resistencia a la velocidad", "RVE", "CONDICIONAL/FÍSICA");

    public String statName;
    public String statCode;
    public String type;

    public static StatEnum fromName(String name) {
        if (isValid(name, "name")) {
            for (StatEnum statEnum : StatEnum.values()) {
                if (statEnum.statName.equals(name)) {
                    return statEnum;
                }
            }
        }
        throw new IllegalArgumentException("Invalid 'Stat.name': '" + name + "'");
    }

    public static StatEnum fromStatCode(String code) {
        if (isValid(code, "code")) {
            for (StatEnum statEnum : StatEnum.values()) {
                if (statEnum.statCode.equals(code)) {
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
                    if (stat.statName.equalsIgnoreCase(value)) {
                        return true;
                    }
                    break;
                case "code":
                    if (stat.statCode.equalsIgnoreCase(value)) {
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
