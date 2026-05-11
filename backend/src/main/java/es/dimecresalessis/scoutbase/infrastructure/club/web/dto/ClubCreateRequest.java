package es.dimecresalessis.scoutbase.infrastructure.club.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import es.dimecresalessis.scoutbase.domain.club.model.Club;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Request DTO for creating a new {@link Club} entity.
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClubCreateRequest {

    @NotBlank
    private String name;

}