package es.dimecresalessis.scoutbase.infrastructure.player.persistence;

import es.dimecresalessis.scoutbase.infrastructure.web.persistence.CommonEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String teamId;

    @Column(nullable = false)
    private String surname;

    private int birthYear;

    @Column(nullable = false)
    private String email;

    private int number;

    private String position;

    private int priority;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(nullable = true, columnDefinition = "uuid[]")
    private List<UUID> stats;
}