package es.dimecresalessis.scoutbase.infrastructure.stat.persistence;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.stat.exception.StatException;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
import es.dimecresalessis.scoutbase.infrastructure.stat.persistence.mapper.StatEntityMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Infrastructure implementation of the {@link StatRepository} interface.
 */
@Repository
@RequiredArgsConstructor
public class StatRepositoryImpl implements StatRepository {

    private final JpaStatRepository jpaStatRepository;
    private final StatEntityMapper mapper;

    /**
     * Finds all Stat.
     *
     * @return A {@link List} of all {@link Stat} domain entities.
     */
    @Override
    public List<Stat> findAll() {
        return jpaStatRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    /**
     * Finds a specific Stat by its ID.
     *
     * @param id The {@link UUID} of the Stat.
     * @return An {@link Optional} containing the found {@link Stat}, or empty if not found.
     */
    @Override
    public Optional<Stat> findById(UUID id) {
        return jpaStatRepository.findById(id).map(mapper::toDomain);
    }

    /**
     * Finds all Stat from a specific player.
     *
     * @param playerId The {@link UUID} of the player.
     * @return A {@link List} of {@link Stat} entities belonging to the player.
     */
    @Override
    public List<Stat> findAllByPlayerId(UUID playerId) {
        return jpaStatRepository.findAllByPlayerId(playerId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    /**
     * Persists a new Stat.
     *
     * @param stat The {@link Stat} domain entity to save.
     * @return The persisted {@link Stat}.
     * @throws StatException if a Stat with the same code already exists for the player.
     */
    @Override
    @Transactional
    public Stat save(Stat stat) {
        StatEntity savedStatEntity = jpaStatRepository.findById(stat.getId())
                .orElseGet(StatEntity::new);

        List<Stat> savedStats = findAllByPlayerId(stat.getPlayerId());
        if (savedStats.stream().anyMatch(s -> s.getCode().equals(stat.getCode()))) {
            String statId = stat.getPlayerId() != null ? stat.getPlayerId().toString() : "null";
            throw new StatException(ErrorEnum.STAT_CODE_ALREADY_EXISTS, stat.getCode(), statId);
        }

        mapper.updateEntityFromDomain(stat, savedStatEntity);
        jpaStatRepository.saveAndFlush(savedStatEntity);
        return stat;
    }

    /**
     * Updates an existing Stat.
     *
     * @param stat The {@link Stat} entity containing updated data.
     * @return The updated {@link Stat}.
     */
    @Override
    @Transactional
    public Stat update(Stat stat) {
        StatEntity savedStatEntity = jpaStatRepository.findById(stat.getId())
                .orElseGet(StatEntity::new);
        mapper.updateEntityFromDomain(stat, savedStatEntity);
        jpaStatRepository.saveAndFlush(savedStatEntity);
        return stat;
    }

    /**
     * Deletes a Stat.
     *
     * @param id The {@link UUID} of the Stat to delete.
     */
    @Override
    @Transactional
    public void deleteById(UUID id) {
        jpaStatRepository.deleteById(id);
        jpaStatRepository.flush();
    }
}