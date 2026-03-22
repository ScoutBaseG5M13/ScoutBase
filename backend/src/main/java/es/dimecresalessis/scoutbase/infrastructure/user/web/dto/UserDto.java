package es.dimecresalessis.scoutbase.infrastructure.user.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Random;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for User entities.
 */
@Data
@AllArgsConstructor
public class UserDto {

    /**
     * ID for the user.
     * If not provided during creation, a random UUID will be generated server-side.
     */
    @Schema(
            description = "Unique identifier. Optional for creation (server will generate one if null).",
            example = "550e8400-e29b-41d4-a716-446655440000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED
    )
    private UUID id;

    /**
     * Username of the user.
     */
    @NotBlank
    private String username;

    /**
     * Password of the user.
     */
    @NotBlank
    private String password;

    /**
     * Role of the user, such as 'ADMIN' or 'USER'.
     */
    @NotBlank
    private String role;

    /**
     * Generates a random instance for testing purposes.
     *
     * @param role The role to be assigned to the random user.
     * @return A random {@link UserDto} instance.
     */
    public static UserDto getRandomInstance(String role) {
        return new UserDto(UUID.randomUUID(), shuffleAndReturnRandomString(9), shuffleAndReturnRandomString(9), role);
    }

    /**
     * Utility method for generating random alphanumeric strings, for testing purposes.
     *
     * @param length Length of the string to generate.
     * @return A randomly generated string of the specified length.
     */
    private static String shuffleAndReturnRandomString(int length) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            str.append(chars[new Random().nextInt(chars.length)]);
        }
        return str.toString();
    }
}
