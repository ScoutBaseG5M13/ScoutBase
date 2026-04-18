package es.dimecresalessis.scoutbase.infrastructure.stat.persistence;

import es.dimecresalessis.scoutbase.infrastructure.web.persistence.CommonEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * Entity representing a group of Statistics of a Player in the system.
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(name = "stat")
public class StatEntity extends CommonEntity {

    @Column(nullable = false)
    private UUID playerId;

    private String name;

    private String code;

    private int value;
}
