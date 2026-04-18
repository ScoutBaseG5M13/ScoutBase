package es.dimecresalessis.scoutbase.infrastructure.player.persistence;

import es.dimecresalessis.scoutbase.infrastructure.web.persistence.CommonEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Entity representing a player in the system.
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "player")
public class PlayerEntity extends CommonEntity {

    @Column
    private UUID teamId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    private int age;

    @Column(nullable = false)
    private String email;

    private int number;

    private String position;

    private String category;

    private int priority;
}