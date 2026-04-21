package es.dimecresalessis.scoutbase.infrastructure.user.persistence;

import es.dimecresalessis.scoutbase.infrastructure.web.persistence.CommonEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Entity representing a user in the database.
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "sb_user")
public class UserEntity extends CommonEntity {

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    String name;

    @Column
    String surname;

    @Column(nullable = false)
    String email;

    public void update(UserEntity newEntity) {
        this.username = newEntity.getUsername();
        this.password = newEntity.getPassword();
        this.name = newEntity.getName();
        this.surname = newEntity.getSurname();
        this.email = newEntity.getEmail();
    }
}