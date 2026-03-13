package es.dimecresalessis.scoutbase.player.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
public class PlayerDto {
    private UUID id;
    @NotBlank
    private String name;
    @NotBlank
    private String team;
    @NotBlank
    private String email;
}
