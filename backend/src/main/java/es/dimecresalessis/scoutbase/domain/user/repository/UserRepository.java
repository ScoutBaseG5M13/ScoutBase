package es.dimecresalessis.scoutbase.domain.user.repository;

import es.dimecresalessis.scoutbase.domain.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link User} entities.
 */
public interface UserRepository {

    /**
     * Finds all users from the repository.
     *
     * @return A list of all {@link User} entities.
     */
    List<User> findAll();

    /**
     * Finds a user by their ID.
     *
     * @param id The ID of the user.
     * @return An {@link Optional} containing the user, if found.
     */
    Optional<User> findById(UUID id);

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user.
     * @return An {@link Optional} containing the user, if found.
     */
    Optional<User> findByUsername(String username);

    /**
     * Saves a user to the repository.
     *
     * @param user The {@link User} entity to be saved.
     * @return The saved instance of the {@link User}.
     */
    User save(User user);

    /**
     * Deletes a user by their ID.
     *
     * @param id The ID of the user to delete.
     */
    void deleteById(UUID id);
}
