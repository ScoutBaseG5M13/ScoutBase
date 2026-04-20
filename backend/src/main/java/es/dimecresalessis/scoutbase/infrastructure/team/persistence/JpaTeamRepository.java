package es.dimecresalessis.scoutbase.infrastructure.team.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaTeamRepository extends JpaRepository<TeamEntity, UUID> {
    @Query(value = """
        SELECT DISTINCT t.* FROM team t 
        LEFT JOIN club c ON t.id = ANY(c.teams) 
        WHERE t.trainer = :userId 
           OR t.second_trainer = :userId 
           OR :userId = ANY(t.scouters) 
           OR :userId = ANY(c.admin_user_ids)
        """, nativeQuery = true)
    List<TeamEntity> findAllByUserId(@Param("userId") UUID userId);

    @Query(value = "SELECT * FROM team t WHERE :playerId = ANY(t.players) LIMIT 1",
            nativeQuery = true)
    Optional<TeamEntity> findByPlayerId(@Param("playerId") UUID playerId);

}
