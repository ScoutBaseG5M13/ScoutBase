package es.dimecresalessis.scoutbase.domain.team.repository;

import es.dimecresalessis.scoutbase.domain.team.model.Team;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link Team} entities.
 */
public interface TeamRepository {
    /**
     * Retrieves all teams currently registered in the system.
     *
     * @return A {@link List} containing all available teams, or an empty list if none exist.
     */
    List<Team> findAll();

    /**
     * Locates a team by their ID.
     *
     * @param id The unique {@link UUID} of the team to find.
     * @return An {@link Optional} containing the found team, or empty if no match is found.
     */
    Optional<Team> findById(UUID id);

    /**
     * Locates a list of teams by their ID.
     *
     * @param userId The unique {@link UUID} of the user teams to find.
     * @return A {@link List} containing the found teams, or empty if no match is found.
     */
    List<Team> findAllByPlayerId(UUID userId);

    /**
     * Persists a team entity into the system.
     * <p>
     * This method handles both the creation of new teams and the updating of existing ones.
     * </p>
     *
     * @param team The {@link Team} domain object to be saved. Must not be {@code null}.
     * @return The saved version of the team entity.
     */
    Team save(Team team);

    /**
     * Removes a team from the system using their ID.
     *
     * @param id The unique {@link UUID} of the team to be deleted.
     */
    void deleteById(UUID id);
}
