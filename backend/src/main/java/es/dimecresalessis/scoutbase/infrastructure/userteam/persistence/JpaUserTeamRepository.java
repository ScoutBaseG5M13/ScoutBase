package es.dimecresalessis.scoutbase.infrastructure.userteam.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Spring Data JPA repository for {@link UserTeamEntity}.
 */
public interface JpaUserTeamRepository extends JpaRepository<UserTeamEntity, UUID> {

}
