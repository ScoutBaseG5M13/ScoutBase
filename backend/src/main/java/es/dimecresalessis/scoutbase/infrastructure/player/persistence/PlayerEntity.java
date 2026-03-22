package es.dimecresalessis.scoutbase.infrastructure.player.persistence;

import es.dimecresalessis.scoutbase.infrastructure.user.persistence.JpaUserRepository;
import es.dimecresalessis.scoutbase.infrastructure.web.persistence.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Entity representing a player in the system.
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "players")
public class PlayerEntity extends CommonEntity {

    /**
     * Unique identifier for the player entity.
     */
    @Id
    private UUID id;

    /**
     * The player's full name.
     */
    @Column(nullable = false)
    private String name;

    /**
     * The team of the player.
     */
    @Column(nullable = false)
    private String team;

    /**
     * The player's email.
     */
    @Column(nullable = false)
    private String email;
}