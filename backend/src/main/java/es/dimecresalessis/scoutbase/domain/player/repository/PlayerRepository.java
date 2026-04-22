package es.dimecresalessis.scoutbase.domain.player.repository;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link Player} entities.
 */
public interface PlayerRepository {

    /**
     * Retrieves all players currently registered in the system.
     *
     * @return A {@link List} containing all available players, or an empty list if none exist.
     */
    List<Player> findAll();

    /**
     * Locates a player by their ID.
     *
     * @param id The unique {@link UUID} of the player to find.
     * @return An {@link Optional} containing the found player, or empty if no match is found.
     */
    Optional<Player> findById(UUID id);

    /**
     * Persists a player entity into the system.
     * <p>
     * This method handles both the creation of new players and the updating of existing ones.
     * </p>
     *
     * @param player The {@link Player} domain object to be saved. Must not be {@code null}.
     * @return The saved version of the player entity.
     */
    Player save(Player player);

    /**
     * Removes a player from the system using their ID.
     *
     * @param id The unique {@link UUID} of the player to be deleted.
     */
    void deleteById(UUID id);
}