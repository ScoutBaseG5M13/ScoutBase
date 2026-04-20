package es.dimecresalessis.scoutbase.infrastructure.player.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for Player entities.
 */
@Data
@Setter
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerDTO {

    private UUID id;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    private int age;

    @NotBlank
    private String email;

    private int number;
    
    private UUID teamId;

    private String position;

    private int priority;
}
