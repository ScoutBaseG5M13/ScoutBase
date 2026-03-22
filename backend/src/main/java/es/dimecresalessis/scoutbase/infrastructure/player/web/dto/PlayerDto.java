package es.dimecresalessis.scoutbase.infrastructure.player.web.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for Player entities.
 */
@Data
@AllArgsConstructor
public class PlayerDto {

    /**
     * The player's ID.
     */
    private UUID id;

    /**
     * The player's name.
     */
    @NotBlank
    private String name;

    /**
     * The player's team.
     */
    @NotBlank
    private String team;

    /**
     * The player's email.
     */
    @NotBlank
    private String email;
}
