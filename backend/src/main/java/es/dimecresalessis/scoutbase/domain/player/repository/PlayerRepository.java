package es.dimecresalessis.scoutbase.domain.player.repository;

import es.dimecresalessis.scoutbase.domain.player.model.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository {
    List<Player> findAll();
    Optional<Player> findById(UUID id);
    Player save(Player player);
    void deleteById(UUID id);
}
