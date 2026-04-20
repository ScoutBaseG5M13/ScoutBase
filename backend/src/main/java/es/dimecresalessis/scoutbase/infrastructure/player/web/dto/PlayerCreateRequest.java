package es.dimecresalessis.scoutbase.infrastructure.player.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerCreateRequest {

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
