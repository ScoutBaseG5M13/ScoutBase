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

@Repository
@RequiredArgsConstructor
public class PlayerRepositoryImpl implements PlayerRepository {

    private final JpaPlayerRepository jpaPlayerRepository;
    private final PlayerEntityMapper mapper;

    @Override
    public List<Player> findAll() {
        return jpaPlayerRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Player> findById(UUID id) {
        return jpaPlayerRepository.findById(id).map(mapper::toDomain);
    }

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

    @Override
    public void deleteById(UUID id) {
        jpaPlayerRepository.deleteById(id);
    }
}