package es.dimecresalessis.scoutbase.domain.club.repository;

import es.dimecresalessis.scoutbase.domain.club.model.Club;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link Club} entities.
 */
public interface ClubRepository {
    /**
     * Retrieves all clubs currently registered in the system.
     *
     * @return A {@link Club} containing all available clubs, or an empty list if none exist.
     */
    List<Club> findAll();

    /**
     * Locates a club by their ID.
     *
     * @param id The unique {@link UUID} of the club to find.
     * @return An {@link Optional} containing the found club, or empty if no match is found.
     */
    Optional<Club> findById(UUID id);

    /**
     * Locates the clubs where a specific player is in.
     *
     * @param playerId The club of the player {@link UUID} to find.
     * @return An {@link Optional} containing a club, or empty if no match is found.
     */
    Optional<Club> findClubByPlayerId(UUID playerId);

    Optional<Club> findClubByTeam(UUID teamId);

    /**
     * Persists a player's club into the system.
     * <p>
     * This method handles both the creation of new club and the updating of existing ones.
     * </p>
     *
     * @param club The {@link Club} domain object to be saved. Must not be {@code null}.
     * @return The saved version of the club entity.
     */
    Club save(Club club);

    /**
     * Removes a club from the system using their ID.
     *
     * @param id The unique {@link UUID} of the club to be deleted.
     */
    void deleteById(UUID id);
}
