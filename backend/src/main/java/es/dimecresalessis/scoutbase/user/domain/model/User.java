package es.dimecresalessis.scoutbase.user.domain.model;

import es.dimecresalessis.scoutbase.infrastructure.web.persistence.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class User extends CommonEntity {
    UUID id;
    String username;
    String password;
    Role role;

    public static User getNewInstance(String username, String password, Role role) {
        return new User(UUID.randomUUID(), username, password, role);
    }
}