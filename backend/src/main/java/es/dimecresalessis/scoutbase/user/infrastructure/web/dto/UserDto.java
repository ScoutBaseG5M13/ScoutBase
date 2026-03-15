package es.dimecresalessis.scoutbase.user.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDto {
    private UUID id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String role;
}
