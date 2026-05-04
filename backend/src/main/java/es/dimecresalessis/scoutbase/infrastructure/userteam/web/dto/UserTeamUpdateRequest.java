package es.dimecresalessis.scoutbase.infrastructure.userteam.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.UUID;

@Data
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTeamUpdateRequest {

    @NotNull
    private UUID id;

    private String name;

    private String category;

    private String subcategory;

}
