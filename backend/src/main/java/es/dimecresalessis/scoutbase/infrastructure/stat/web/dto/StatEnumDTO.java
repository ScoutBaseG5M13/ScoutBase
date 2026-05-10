package es.dimecresalessis.scoutbase.infrastructure.stat.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatEnumDTO {
    
    private String code;

    private String name;

    private String type;
}
