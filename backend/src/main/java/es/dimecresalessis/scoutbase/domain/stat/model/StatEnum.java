package es.dimecresalessis.scoutbase.domain.stat.model;

import lombok.AllArgsConstructor;

import static es.dimecresalessis.scoutbase.domain.stat.model.StatTypeEnum.*;

@AllArgsConstructor
public enum StatEnum {
    // CAPACIDAD OFENSIVA
    CSL("Conducción para superar línea o fijar rivales", "CSL", OFENSIVO),
    DR1("Dribling-Regate-1x1", "DR1", OFENSIVO),
    TAP("Timing apropiado para progresar", "TAP", OFENSIVO),
    CTR("Centros", "CTR", OFENSIVO),
    PCA("Pase corto-asociación", "PCA", OFENSIVO),
    PLG("Pase largo", "PLG", OFENSIVO),
    COR("Cambio de orientación", "COR", OFENSIVO),
    VJU("Visión de juego", "VJU", OFENSIVO),
    UPA("Último pase", "UPA", OFENSIVO),
    MOV("Movilidad", "MOV", OFENSIVO),
    CTE("Cobertura técnica", "CTE", OFENSIVO),
    COO("Control orientado", "COO", OFENSIVO),
    REM("Remate", "REM", OFENSIVO),
    TIR("Tiro", "TIR", OFENSIVO),
    DAP("Desmarque apoyo", "DAP", OFENSIVO),
    JES("Juego de espaldas", "JES", OFENSIVO),
    PDA("Pausa dentro del área", "PDA", OFENSIVO),
    DED("Desmarque espalda de la defensa", "DED", OFENSIVO),
    CBA("Caída en banda", "CBA", OFENSIVO),
    JZI("Juego zonas interiores", "JZI", OFENSIVO),
    JZE("Juego zonas exteriores", "JZE", OFENSIVO),
    JAE("Juego aéreo", "JAE", OFENSIVO),
    PND("Dominio pierna no dominante", "PND", OFENSIVO),

    // CAPACIDAD DEFENSIVA
    OMA("Orientación marcaje", "OMA", DEFENSIVO),
    TEE("Técnica entrada", "TEE", DEFENSIVO),
    TPD("Temporización-presencia defensiva", "TPD", DEFENSIVO),
    SAN("Sentido de la anticipación", "SAN", DEFENSIVO),
    CTD("Colaboración trabajo defensivo", "CTD", DEFENSIVO),
    MAR("Marcaje", "MAR", DEFENSIVO),
    OPR("Orientación presión", "OPR", DEFENSIVO),
    CON("Contundencia", "CON", DEFENSIVO),
    EDF("Equilibrio defensivo", "EDF", DEFENSIVO),
    IPP("Intercepción por pase", "IPP", DEFENSIVO),
    DOR("Despejes orientados", "DOR", DEFENSIVO),
    JAD("Juego aéreo defensivo", "JAD", DEFENSIVO),
    MLD("Mandar la línea defensiva", "MLD", DEFENSIVO),
    MED("Mandar al equipo en defensa", "MED", DEFENSIVO),
    CCO("Cobertura a compañero", "CCO", DEFENSIVO),
    CLI("Cobertura a línea", "CLI", DEFENSIVO),
    PER("Permutas", "PER", DEFENSIVO),
    REP("Repliegue", "REP", DEFENSIVO),

    // CAPACIDAD MENTAL
    TDE("Toma de decisión", "TDE", MENTAL),
    PET("Percepción espacio-temporal", "PET", MENTAL),
    ACO("Atención y concentración", "ACO", MENTAL),
    PCV("Personalidad: Carácter y valentía", "PCV", MENTAL),
    CPT("Competitividad", "CPT", MENTAL),
    CMU("Comunicación", "CMU", MENTAL),
    AGR("Agresividad", "AGR", MENTAL),
    LID("Liderazgo", "LID", MENTAL),
    SAC("Sacrificio", "SAC", MENTAL),
    COM("Compañerismo", "COM", MENTAL),

    // CAPACIDAD FÍSICA
    CAR("Coordinación, agilidad, reflejos", "CAR", FISICO),
    FUE("Fuerza", "FUE", FISICO),
    VEC("Velocidad espacios cortos", "VEC", FISICO),
    VRC("Velocidad de reacción compleja", "VRC", FISICO),
    VEL("Velocidad espacios largos", "VEL", FISICO),
    POT("Potencia", "POT", FISICO),
    SIJ("Soporta intensidad de juego todo el partido", "SIJ", FISICO),
    CRI("Cambio de ritmo", "CRI", FISICO),
    RAE("Resistencia Aeróbica", "RAE", FISICO),
    RVE("Resistencia a la velocidad", "RVE", FISICO);

    public String statName;
    public String statCode;
    public StatTypeEnum type;

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
