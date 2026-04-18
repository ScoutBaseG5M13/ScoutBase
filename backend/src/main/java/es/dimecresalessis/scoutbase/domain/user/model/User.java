package es.dimecresalessis.scoutbase.domain.user.model;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import lombok.*;

import java.util.Random;
import java.util.UUID;

@Setter
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {

    UUID id;
    String username;
    String password;
    String role;
    String name;
    String surname;
    String email;

    public static class UserBuilder {
        public UserBuilder id(UUID id) {
            // Apply your custom logic here
            if (id == null) {
                this.id = UUID.randomUUID();
            } else {
                this.id = id;
            }
            return this;
        }
    }

    public void setId(UUID id) {
        if (id == null || id.toString().isEmpty()) {
            this.id = UUID.randomUUID();
        }
    }

    /**
     * Static factory method for creating a new `User` instance.
     * It still exists for now for testing purposes.
     *
     * @param username The username for the new user.
     * @param password The password for the new user.
     * @param role The {@link RoleEnum} (string form) assigned to the user.
     * @return A new {@link User} instance with a unique identifier.
     * @throws UserException if any of the parameters are null or invalid.
     */
    public static User getNewInstance(String username, String password, String role) {
        if (username == null || password == null || role == null) {
            throw new UserException(ErrorEnum.USER_IS_NULL);
        }
        if (RoleEnum.fromName(role) == null) {
            throw new UserException(ErrorEnum.ROLE_NOT_FOUND, role);
        }

        return new User(UUID.randomUUID(), username, password, RoleEnum.fromName(role).getName(),
                "Name " + new Random().nextInt(100), "Surname " + new Random().nextInt(100),
                username + "@test.com");
    }
}