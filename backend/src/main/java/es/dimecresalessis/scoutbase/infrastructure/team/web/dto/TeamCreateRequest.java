package es.dimecresalessis.scoutbase.infrastructure.team.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String category;

    @NotBlank
    private String subcategory;
}
