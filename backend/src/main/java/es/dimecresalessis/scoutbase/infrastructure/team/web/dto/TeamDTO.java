package es.dimecresalessis.scoutbase.infrastructure.team.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Data
@Setter
@AllArgsConstructor
public class TeamDTO {
    @NotBlank
    private UUID id;

    @NotBlank
    private String name;

    private String category;

    private String subcategory;

    private List<UUID> players;

    private List<UUID> trainers;

    private List<UUID> scouters;
}
