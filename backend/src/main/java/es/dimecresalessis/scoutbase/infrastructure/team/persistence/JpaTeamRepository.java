package es.dimecresalessis.scoutbase.infrastructure.team.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaTeamRepository extends JpaRepository<TeamEntity, UUID> {
    @Query("SELECT t FROM TeamEntity t WHERE " +
            "t.trainer = :userId OR " +
            "t.secondTrainer = :userId OR " +
            ":userId MEMBER OF t.scouters")
    List<TeamEntity> findAllByUserId(@Param("userId") UUID userId);

    @Query("SELECT t FROM TeamEntity t WHERE " +
            ":playerId MEMBER OF t.players")
    Optional<TeamEntity> findByPlayerId(@Param("playerId") UUID playerId);

}
