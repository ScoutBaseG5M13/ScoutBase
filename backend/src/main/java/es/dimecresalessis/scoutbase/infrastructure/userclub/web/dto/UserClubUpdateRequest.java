package es.dimecresalessis.scoutbase.infrastructure.userclub.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserClubUpdateRequest {

    @NotNull
    private UUID id;

    @NotBlank
    private String name;

    @NotEmpty
    private List<UUID> adminUserIds;

}
