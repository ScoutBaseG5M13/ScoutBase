package es.dimecresalessis.scoutbase.infrastructure.userclub.persistence;

import es.dimecresalessis.scoutbase.infrastructure.web.persistence.CommonEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Entity representing a userclub in the system.
 */
@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_club")
public class UserClubEntity extends CommonEntity {

    @Column(nullable = false)
    private String name;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(nullable = false, columnDefinition = "uuid ARRAY")
    private List<UUID> adminUserIds = Collections.emptyList();

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "uuid ARRAY")
    private List<UUID> userTeams = Collections.emptyList();

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "uuid ARRAY")
    private List<UUID> managedClubs = Collections.emptyList();
}
