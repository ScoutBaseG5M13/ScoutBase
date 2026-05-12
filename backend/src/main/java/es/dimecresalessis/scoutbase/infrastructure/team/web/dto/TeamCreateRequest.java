package es.dimecresalessis.scoutbase.infrastructure.team.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request DTO for creating a new {@link Team} entity.
 */
@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String category;

    @NotBlank
    private String subcategory;
}
