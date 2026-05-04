package es.dimecresalessis.scoutbase.domain.userclub.repository;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link UserClub} entities.
 */
public interface UserClubRepository {
    /**
     * Retrieves all clubs currently registered in the system.
     *
     * @return A {@link UserClub} containing all available clubs, or an empty list if none exist.
     */
    List<UserClub> findAll();

    /**
     * Locates a userclub by their ID.
     *
     * @param id The unique {@link UUID} of the userclub to find.
     * @return An {@link Optional} containing the found userclub, or empty if no match is found.
     */
    Optional<UserClub> findUserClubById(UUID id);

    Optional<UserClub> findUserClubByTeam(UUID teamId);

    /**
     * Locates all clubs where the user is in.
     *
     * @param userId The clubs of the user {@link UUID} to find.
     * @return An {@link Optional} containing clubs, or empty if no match is found.
     */
    List<UserClub> findAllUserClubsByUserId(UUID userId);

    Optional<UserClub> findUserClubByClubId(UUID clubId);

    /**
     * Persists a player's userclub into the system.
     * <p>
     * This method handles both the creation of new userclub and the updating of existing ones.
     * </p>
     *
     * @param userClub The {@link UserClub} domain object to be saved. Must not be {@code null}.
     * @return The saved version of the userclub entity.
     */
    UserClub save(UserClub userClub);

    /**
     * Removes a userclub from the system using their ID.
     *
     * @param id The unique {@link UUID} of the userclub to be deleted.
     */
    void deleteById(UUID id);
}
