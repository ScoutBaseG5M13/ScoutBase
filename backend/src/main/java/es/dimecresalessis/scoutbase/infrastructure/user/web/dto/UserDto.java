package es.dimecresalessis.scoutbase.infrastructure.user.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Random;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDto {
    private UUID id;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String role;

    public static UserDto getRandomInstance(String role) {
        return new UserDto(UUID.randomUUID(), shuffleAndReturnRandomString(9), shuffleAndReturnRandomString(9), role);
    }

    private static String shuffleAndReturnRandomString(int length) {
        char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            str.append(chars[new Random().nextInt(chars.length)]);
        }
        return str.toString();
    }
}
