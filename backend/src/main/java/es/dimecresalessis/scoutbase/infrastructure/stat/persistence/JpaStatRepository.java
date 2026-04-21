package es.dimecresalessis.scoutbase.infrastructure.stat.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA implementation for stat persistence.
 * <p>
 * It leverages Spring Data's abstraction to provide low-level CRUD operations against
 * the database for {@link StatEntity} objects.
 * </p>
 */
@Repository
public interface JpaStatRepository extends JpaRepository<StatEntity, UUID> {
    List<StatEntity> findAllByPlayerId(UUID playerId);
}
