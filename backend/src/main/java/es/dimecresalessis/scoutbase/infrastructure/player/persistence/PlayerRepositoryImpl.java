package es.dimecresalessis.scoutbase.infrastructure.player.persistence;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import es.dimecresalessis.scoutbase.infrastructure.player.persistence.mapper.PlayerEntityMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Infrastructure implementation of the {@link PlayerRepository} interface.
 */
@Repository
@RequiredArgsConstructor
public class PlayerRepositoryImpl implements PlayerRepository {

    private final JpaPlayerRepository jpaPlayerRepository;
    private final PlayerEntityMapper mapper;

    /**
     * Retrieves all players from the database and maps them to domain entities.
     *
     * @return A {@link List} of all {@link Player} entities found.
     */
    @Override
    public List<Player> findAll() {
        return jpaPlayerRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    /**
     * Finds a player by their unique identifier.
     *
     * @param id The {@link UUID} of the player.
     * @return An {@link Optional} containing the {@link Player} if found, or empty otherwise.
     */
    @Override
    public Optional<Player> findById(UUID id) {
        return jpaPlayerRepository.findById(id).map(mapper::toDomain);
    }

    /**
     * Persists a player entity. If the player already exists, the existing record
     * is updated; otherwise, a new record is created.
     * <p>
     * This method is {@link Transactional} to ensure database consistency during
     * the find-and-update lifecycle.
     *
     * @param player The {@link Player} domain object to save.
     * @return The saved {@link Player} domain object.
     */
    @Override
    @Transactional
    public Player save(Player player) {
        PlayerEntity playerEntity = jpaPlayerRepository.findById(player.getId())
                .orElseGet(PlayerEntity::new);
        mapper.updateEntityFromDomain(player, playerEntity);
        jpaPlayerRepository.saveAndFlush(playerEntity);
        return player;
    }

    /**
     * Removes a player record from the database by its ID.
     *
     * @param id The {@link UUID} of the player to delete.
     */
    @Override
    public void deleteById(UUID id) {
        jpaPlayerRepository.deleteById(id);
    }
}