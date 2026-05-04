package es.dimecresalessis.scoutbase.infrastructure.userteam.web.dto;

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
public class UserTeamDTO {
    @NotBlank
    private UUID id;

    @NotBlank
    private String name;

    private String category;

    private String subcategory;

    private UUID trainer;

    private UUID secondTrainer;

    private List<UUID> scouters;
}
