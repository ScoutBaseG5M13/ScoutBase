package es.dimecresalessis.scoutbase.domain.user.model;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.infrastructure.web.persistence.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * Entity Representing Application Users.
 */
@Getter
@AllArgsConstructor
public class User extends CommonEntity {

    /**
     * ID of the user.
     */
    UUID id;

    /**
     * Username of the user.
     */
    String username;

    /**
     * Password of the user.
     */
    String password;

    /**
     * Role of the user.
     */
    String role;

    /**
     * Static factory method for creating a new `User` instance.
     * It still exists for now for testing purposes.
     *
     * @param username The username for the new user.
     * @param password The password for the new user.
     * @param role The {@link Role} (string form) assigned to the user.
     * @return A new {@link User} instance with a unique identifier.
     * @throws UserException if any of the parameters are null or invalid.
     */
    public static User getNewInstance(String username, String password, String role) {
        if (username == null || password == null || role == null) {
            throw new UserException(ErrorEnum.USER_IS_NULL);
        }
        if (Role.fromName(role) == null) {
            throw new UserException(ErrorEnum.ROLE_NOT_FOUND, role);
        }

        return new User(UUID.randomUUID(), username, password, Role.fromName(role).getName());
    }
}