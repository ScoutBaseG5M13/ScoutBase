package es.dimecresalessis.scoutbase.infrastructure.stat.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.UUID;

@Data
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatDTO {

    private UUID id;

    private UUID playerId;

    private String name;

    private String code;

    private int value;
}
