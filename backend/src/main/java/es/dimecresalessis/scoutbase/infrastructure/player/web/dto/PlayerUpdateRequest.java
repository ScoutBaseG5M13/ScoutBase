package es.dimecresalessis.scoutbase.infrastructure.player.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.util.UUID;

@Data
@Setter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerUpdateRequest {

    @NotNull
    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    private int birthYear;

    @NotBlank
    private String email;

    private int number;

    private String position;
}
