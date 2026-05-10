package es.dimecresalessis.scoutbase.infrastructure.stat.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import es.dimecresalessis.scoutbase.domain.stat.model.StatEnum;
import jakarta.validation.constraints.*;
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

    private String type;

    @NotBlank
    @Size(min = 3, max = 3, message = "Code must be exactly 3 characters long [CON, RES...]")
    private String code;

    @NotNull
    @Min(value = 0)
    @Max(value = 10)
    private int value;

    String getType() {
        return StatEnum.fromStatCode(code).type;
    }
}
