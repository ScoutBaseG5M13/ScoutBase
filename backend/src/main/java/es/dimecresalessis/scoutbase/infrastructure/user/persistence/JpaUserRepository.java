package es.dimecresalessis.scoutbase.infrastructure.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA implementation for user persistence.
 * <p>
 * It leverages Spring Data's abstraction to provide low-level CRUD operations against
 * the database for {@link UserEntity} objects.
 * </p>
 */
@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {

    /**
     * Retrieves a user by their username.
     * <p>
     * This query is automatically derived by Spring Data JPA based on
     * the method name. It is primarily used during the authentication
     * process to load security credentials.
     * </p>
     *
     * @param username The unique username to search for.
     * @return An {@link Optional} containing the user if found.
     */
    Optional<UserEntity> findByUsername(String username);
}