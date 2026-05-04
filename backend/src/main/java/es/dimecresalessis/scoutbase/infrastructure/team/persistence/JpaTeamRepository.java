package es.dimecresalessis.scoutbase.infrastructure.team.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Spring Data JPA repository for {@link TeamEntity}.
 */
public interface JpaTeamRepository extends JpaRepository<TeamEntity, UUID> {
}
