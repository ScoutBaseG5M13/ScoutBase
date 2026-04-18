package es.dimecresalessis.scoutbase.infrastructure.player.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for Player entities.
 */
@Data
@Setter
@AllArgsConstructor
public class PlayerDTO {

    private UUID id;

    private UUID teamId;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    private int age;

    @NotBlank
    private String email;

    private int number; // "Dorsal"

    private String position;

    private String category;

    private int priority;
}
