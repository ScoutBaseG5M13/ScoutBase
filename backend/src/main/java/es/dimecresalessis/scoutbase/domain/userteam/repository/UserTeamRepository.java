package es.dimecresalessis.scoutbase.domain.userteam.repository;

import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link UserTeam} entities.
 */
public interface UserTeamRepository {
    /**
     * Retrieves all teams currently registered in the system.
     *
     * @return A {@link List} containing all available teams, or an empty list if none exist.
     */
    List<UserTeam> findAll();

    /**
     * Locates a userteam by their ID.
     *
     * @param id The unique {@link UUID} of the userteam to find.
     * @return An {@link Optional} containing the found userteam, or empty if no match is found.
     */
    Optional<UserTeam> findById(UUID id);

    /**
     * Retrieves a list of teams that contain a specific User.
     *
     * @param userId The unique {@link UUID} of the user teams to find.
     * @return A {@link List} containing the found teams, or empty if no match is found.
     */
    List<UserTeam> findAllByUserId(UUID userId);

    /**
     * Persists a userteam entity into the system.
     * <p>
     * This method handles both the creation of new teams and the updating of existing ones.
     * </p>
     *
     * @param userTeam The {@link UserTeam} domain object to be saved. Must not be {@code null}.
     * @return The saved version of the userteam entity.
     */
    UserTeam save(UserTeam userTeam);

    /**
     * Removes a userteam from the system using their ID.
     *
     * @param id The unique {@link UUID} of the userteam to be deleted.
     */
    void deleteById(UUID id);
}
