package es.dimecresalessis.scoutbase.infrastructure.club.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClubDTO {

    private UUID id;

    @NotBlank
    private String name;

    private List<UUID> teams;
}
