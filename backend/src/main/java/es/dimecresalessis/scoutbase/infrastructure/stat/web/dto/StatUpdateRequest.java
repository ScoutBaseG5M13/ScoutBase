package es.dimecresalessis.scoutbase.infrastructure.stat.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Request DTO for updating an existing {@link Stat} entity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatUpdateRequest {

    @NotNull
    private UUID id;

    @NotBlank
    @Size(min = 3, max = 3, message = "Code must be exactly 3 characters long [CON, RES...]")
    private String code;

    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    private int value;
}
