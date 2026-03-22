package es.dimecresalessis.scoutbase.infrastructure.user.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Entity representing a user in the database.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class UserEntity {

    /**
     * Primary key for the user, represented as a {@link UUID}.
     */
    @Id
    private UUID id;

    /**
     * User's username.
     */
    @Column(nullable = false)
    private String username;

    /**
     * Hashed security credential for authentication.
     */
    @Column(nullable = false)
    private String password;

    /**
     * The security role assigned to the user (e.g., "ROLE_USER", "ROLE_ADMIN").
     */
    @Column(nullable = false)
    private String role;
}