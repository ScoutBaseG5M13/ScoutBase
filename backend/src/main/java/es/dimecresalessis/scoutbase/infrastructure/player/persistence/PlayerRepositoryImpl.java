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

    /**
     * The Spring Data JPA repository providing low-level database access.
     */
    private final JpaPlayerRepository jpaPlayerRepository;

    /**
     * The mapper responsible for converting domain objects to/from persistence entities.
     */
    private final PlayerEntityMapper mapper;

    /**
     * Retrieves all players from the database and maps them to Domain models.
     * * @return A {@link List} of {@link Player} domain objects.
     */
    @Override
    public List<Player> findAll() {
        return jpaPlayerRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    /**
     * Finds a player in the system by their ID.
     * @param id The unique {@link UUID} of the player.
     * @return An {@link Optional} containing the domain {@link Player} if found.
     */
    @Override
    public Optional<Player> findById(UUID id) {
        return jpaPlayerRepository.findById(id).map(mapper::toDomain);
    }

    /**
     * Saves or updates a player in the system.
     * @param player The domain {@link Player} to persist.
     * @return The updated domain {@link Player} after persistence.
     */
    @Override
    @Transactional
    public Player save(Player player) {
        PlayerEntity entity = jpaPlayerRepository.findById(player.getId())
                .orElse(new PlayerEntity());

        entity.setId(player.getId());
        entity.setName(player.getName());
        entity.setTeam(player.getTeam());
        entity.setEmail(player.getEmail());

        return mapper.toDomain(jpaPlayerRepository.save(entity));
    }

    /**
     * Deletes a player from the system by their unique ID.
     * * @param id The {@link UUID} of the player to remove.
     */
    @Override
    public void deleteById(UUID id) {
        jpaPlayerRepository.deleteById(id);
    }
}