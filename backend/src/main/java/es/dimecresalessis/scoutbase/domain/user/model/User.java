package es.dimecresalessis.scoutbase.domain.user.model;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.infrastructure.web.persistence.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class User extends CommonEntity {
    UUID id;
    String username;
    String password;
    String role;

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