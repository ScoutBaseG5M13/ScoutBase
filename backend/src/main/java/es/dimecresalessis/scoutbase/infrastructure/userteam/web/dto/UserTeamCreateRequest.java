package es.dimecresalessis.scoutbase.infrastructure.userteam.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTeamCreateRequest {

    @NotBlank
    private String name;

    private String category;

    private String subcategory;
}
