package es.dimecresalessis.scoutbase.infrastructure.team.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link TeamEntity}.
 */
public interface JpaTeamRepository extends JpaRepository<TeamEntity, UUID> {
    /**
     * Retrieves all teams where a specific user has an active role.
     *
     * @param userId The {@link UUID} of the user.
     * @return A {@link List} of {@link TeamEntity} objects matching the criteria.
     */
    @Query(value = """
        SELECT DISTINCT t.* FROM team t 
        LEFT JOIN club c ON t.id = ANY(c.teams) 
        WHERE t.trainer = :userId 
           OR t.second_trainer = :userId 
           OR :userId = ANY(t.scouters) 
           OR :userId = ANY(c.admin_user_ids)
        """, nativeQuery = true)
    List<TeamEntity> findAllByUserId(@Param("userId") UUID userId);

    /**
     * Finds the first team associated with a specific player.
     *
     * @param playerId The {@link UUID} of the player.
     * @return An {@link Optional} containing the {@link TeamEntity} if the player belongs to one.
     */
    @Query(value = "SELECT * FROM team t WHERE :playerId = ANY(t.players) LIMIT 1",
            nativeQuery = true)
    Optional<TeamEntity> findByPlayerId(@Param("playerId") UUID playerId);

}
