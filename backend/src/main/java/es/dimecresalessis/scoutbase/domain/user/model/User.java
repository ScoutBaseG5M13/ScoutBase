package es.dimecresalessis.scoutbase.domain.user.model;

import lombok.*;

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
    String name;
    String surname;
    String email;
    boolean isSuperAdmin = false;

    public static class UserBuilder {
        public UserBuilder id(UUID id) {
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
}