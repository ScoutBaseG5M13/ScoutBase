package es.dimecresalessis.scoutbase.infrastructure.user.persistence;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import es.dimecresalessis.scoutbase.infrastructure.user.persistence.mapper.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Infrastructure implementation of the {@link UserRepository} interface.
 * <p>
 * Aacts as a Secondary Adapter and it coordinates persistence operations by delegating to the {@link JpaUserRepository}
 * and using the {@link UserEntityMapper} to maintain domain model purity.
 * </p>
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    /**
     * Spring Data JPA repository for table-level access.
     */
    private final JpaUserRepository jpaUserRepository;

    /**
     * Mapper to translate between the {@link User} domain model and {@link UserEntity}.
     */
    private final UserEntityMapper mapper;

    /**
     * Retrieves all users and converts them to domain models.
     * @return A {@link List} of all {@link User} domain objects.
     */
    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    /**
     * Finds a specific user by their unique identifier.
     * @param id The {@link UUID} of the user.
     * @return An {@link Optional} containing the domain {@link User} if found.
     */
    @Override
    public Optional<User> findById(UUID id) {
        return jpaUserRepository.findById(id).map(mapper::toDomain);
    }

    /**
     * Retrieves a user by their username, typically used for authentication.
     * @param username The unique username to search for.
     * @return An {@link Optional} domain {@link User}.
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return jpaUserRepository.findByUsername(username).map(mapper::toDomain);
    }

    /**
     * Retrieves a user by their username, typically used for authentication.
     * Keeps only the first result.
     * @param username The unique username to search for.
     * @return An {@link Optional} domain {@link User}.
     */
    @Override
    public Optional<User> findFirstByUsername(String username) {
        return jpaUserRepository.findByUsername(username).stream().findFirst().map(mapper::toDomain);
    }

    /**
     * Persists or updates a user in the database.
     * <p>
     * This method fetches the existing entity if present to preserve state,
     * updates the fields from the domain model, and returns the persisted
     * result mapped back to the domain.
     * </p>
     * @param user The {@link User} domain model to save.
     * @return The saved {@link User} domain model.
     */
    @Override
    public User save(User user) {
        return jpaUserRepository.findById(user.getId())
                .map(existingEntity -> {
                    mapper.updateEntityFromDomain(user, existingEntity);
                    return mapper.toDomain(jpaUserRepository.save(existingEntity));
                })
                .orElseGet(() -> {
                    UserEntity newEntity = mapper.toEntity(user);
                    return mapper.toDomain(jpaUserRepository.save(newEntity));
                });
    }

    /**
     * Removes a user from the system by their ID.
     * @param id The {@link UUID} of the user to delete.
     */
    @Override
    public void deleteById(UUID id) {
        jpaUserRepository.deleteById(id);
    }
}