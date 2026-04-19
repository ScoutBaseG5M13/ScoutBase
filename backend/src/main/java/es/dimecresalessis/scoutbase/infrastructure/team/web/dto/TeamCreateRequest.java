package es.dimecresalessis.scoutbase.infrastructure.team.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Data
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamCreateRequest {

    @NotBlank
    private String name;

    private String category;

    private String subcategory;

    private List<UUID> players;

    private List<UUID> trainers;

    private List<UUID> scouters;
}
