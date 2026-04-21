package es.dimecresalessis.scoutbase.domain.stat.repository;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link Stat} entities.
 */
public interface StatRepository {

    /**
     * Retrieves all stats currently registered in the system.
     *
     * @return A {@link Stat} containing all available stats, or an empty list if none exist.
     */
    List<Stat> findAll();

    /**
     * Locates a stat by their ID.
     *
     * @param id The unique {@link UUID} of the stat to find.
     * @return An {@link Optional} containing the found stat, or empty if no match is found.
     */
    Optional<Stat> findById(UUID id);

    /**
     * Locates a stat by their stat ID.
     *
     * @param playerId The stats of the player {@link UUID} to find.
     * @return An {@link Optional} containing the found stat, or empty if no match is found.
     */
    List<Stat> findAllByPlayerId(UUID playerId);

    /**
     * Persists a player's stat into the system.
     * <p>
     * This method handles both the creation of new stat and the updating of existing ones.
     * </p>
     *
     * @param stat The {@link Stat} domain object to be saved. Must not be {@code null}.
     * @return The saved version of the stat entity.
     */
    Stat save(Stat stat);

    /**
     * Updates a player's stat into the system.
     * <p>
     * This method handles both the updating of new stat and the updating of existing ones.
     * </p>
     *
     * @param stat The {@link Stat} domain object to be saved. Must not be {@code null}.
     * @return The saved version of the stat entity.
     */
    Stat update(Stat stat);

    /**
     * Removes a stat from the system using their ID.
     *
     * @param id The unique {@link UUID} of the stat to be deleted.
     */
    void deleteById(UUID id);
}
