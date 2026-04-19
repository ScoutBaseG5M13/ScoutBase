package es.dimecresalessis.scoutbase.infrastructure.club.web.dto;

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
public class ClubCreateRequest {

    @NotEmpty
    private List<UUID> adminUserIds;

    @NotBlank
    private String name;

    private List<UUID> teams;
}