package es.dimecresalessis.scoutbase.infrastructure.team.persistence;

import es.dimecresalessis.scoutbase.infrastructure.web.persistence.CommonEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

/**
 * Entity representing a team in the system.
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "team")
public class TeamEntity extends CommonEntity {

    @Column(nullable = false)
    private String name;

    @Column
    private String category;

    @Column
    private String subcategory;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(nullable = true, columnDefinition = "uuid[]")
    private List<UUID> players;
}
