package es.dimecresalessis.scoutbase.infrastructure.player.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data JPA implementation for player persistence.
 * <p>
 * It leverages Spring Data's abstraction to provide low-level CRUD operations against
 * the database for {@link PlayerEntity} objects.
 * </p>
 */
@Repository
public interface JpaPlayerRepository extends JpaRepository<PlayerEntity, UUID> {
}
