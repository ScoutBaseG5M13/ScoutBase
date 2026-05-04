package es.dimecresalessis.scoutbase.infrastructure.user.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Random;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for User.
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {

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

    private boolean superAdmin;

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
