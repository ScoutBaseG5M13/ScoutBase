package es.dimecresalessis.scoutbase.infrastructure.team.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaTeamRepository extends JpaRepository<TeamEntity, UUID> {
}
