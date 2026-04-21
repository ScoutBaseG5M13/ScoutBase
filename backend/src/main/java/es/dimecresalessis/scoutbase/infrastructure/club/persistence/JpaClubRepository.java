package es.dimecresalessis.scoutbase.infrastructure.club.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Spring Data JPA implementation for club persistence.
 * <p>
 * It leverages Spring Data's abstraction to provide low-level CRUD operations against
 * the database for {@link ClubEntity} objects.
 * </p>
 */
public interface JpaClubRepository extends JpaRepository<ClubEntity, UUID> {
}
