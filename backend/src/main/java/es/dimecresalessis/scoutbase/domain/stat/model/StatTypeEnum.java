package es.dimecresalessis.scoutbase.domain.stat.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatTypeEnum {
    OFENSIVO("OFENSIVO"),
    DEFENSIVO("DEFENSIVO"),
    MENTAL("MENTAL"),
    FISICO("FÍSICO");

    String name;
}
