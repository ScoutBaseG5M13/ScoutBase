package es.dimecresalessis.scoutbase.infrastructure.userclub.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Spring Data JPA implementation for userclub persistence.
 * <p>
 * It leverages Spring Data's abstraction to provide low-level CRUD operations against
 * the database for {@link UserClubEntity} objects.
 * </p>
 */
public interface JpaUserClubRepository extends JpaRepository<UserClubEntity, UUID> {
}
