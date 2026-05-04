package es.dimecresalessis.scoutbase.infrastructure.userteam.persistence;

import es.dimecresalessis.scoutbase.infrastructure.web.persistence.CommonEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

/**
 * Entity representing a userteam in the system.
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_team")
public class UserTeamEntity extends CommonEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String subcategory;

    @Column
    private UUID trainer;

    @Column
    private UUID secondTrainer;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(nullable = true, columnDefinition = "uuid[]")
    private List<UUID> scouters;
}
