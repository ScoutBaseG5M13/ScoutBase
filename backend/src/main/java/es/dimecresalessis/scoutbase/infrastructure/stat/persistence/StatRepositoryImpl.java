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

    @Override
    public List<Stat> findAll() {
        return jpaStatRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Stat> findById(UUID id) {
        return jpaStatRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Stat> findAllByPlayerId(UUID playerId) {
        return jpaStatRepository.findAllByPlayerId(playerId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

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

    @Override
    @Transactional
    public Stat update(Stat stat) {
        StatEntity savedStatEntity = jpaStatRepository.findById(stat.getId())
                .orElseGet(StatEntity::new);
        mapper.updateEntityFromDomain(stat, savedStatEntity);
        jpaStatRepository.saveAndFlush(savedStatEntity);
        return stat;
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        jpaStatRepository.deleteById(id);
        jpaStatRepository.flush();
    }
}