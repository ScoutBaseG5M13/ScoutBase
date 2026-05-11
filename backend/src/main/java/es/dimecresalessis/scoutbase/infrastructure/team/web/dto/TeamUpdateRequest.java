package es.dimecresalessis.scoutbase.infrastructure.team.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.UUID;

/**
 * Request DTO for updating an existing {@link Team} entity.
 */
@Data
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamUpdateRequest {

    @NotNull
    private UUID id;

    private String name;

    private String category;

    private String subcategory;
}
