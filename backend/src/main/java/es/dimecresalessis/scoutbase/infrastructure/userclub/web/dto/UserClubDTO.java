package es.dimecresalessis.scoutbase.infrastructure.userclub.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserClubDTO {

    private UUID id;

    @NotBlank
    private String name;

    @NotEmpty
    private List<UUID> adminUserIds;

    private List<UUID> userTeams;

    private List<UUID> managedClubs;
}
