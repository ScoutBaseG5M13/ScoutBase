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
     * Locates all clubs where the user is in.
     *
     * @param userId The clubs of the user {@link UUID} to find.
     * @return An {@link Optional} containing clubs, or empty if no match is found.
     */
    List<Club> findAllByUserId(UUID userId);

    Optional<Club> findClubByTeam(UUID teamId);

    List<Club> findAllClubsByUserId(UUID userId);

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

//    /**
//     * Updates a player's club into the system.
//     * <p>
//     * This method handles both the updating of new club and the updating of existing ones.
//     * </p>
//     *
//     * @param club The {@link Club} domain object to be saved. Must not be {@code null}.
//     * @return The saved version of the club entity.
//     */
//    Club update(Club club);

    /**
     * Removes a club from the system using their ID.
     *
     * @param id The unique {@link UUID} of the club to be deleted.
     */
    void deleteById(UUID id);
}
