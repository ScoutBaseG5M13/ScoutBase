package es.dimecresalessis.scoutbase.infrastructure.user.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for UserInfo.
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoDTO {

    private UUID id;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    private String surname;

    @NotBlank
    private String email;

    private String role;

    private boolean superAdmin;
}
