package es.dimecresalessis.scoutbase.application.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO representing a user's authentication credentials.
 * <p>
 * This DTO is used by {@link AuthService} to verify identity and
 * generate security tokens. It is then hashed and saved into the DB.
 * </p>
 */
@Data
public class LoginRequest {

    /**
     * The {@code username} provided by the user to login.
     *
     */
    @Schema(example = "myuser", description = "Username used to authenticate")
    private String username;

    /**
     * The {@code password} provided by the user to login.
     *
     */
    @Schema(example = "password123", description = "Password used to authenticate")
    private String password;
}