package es.dimecresalessis.scoutbase.player.domain.repository;

import es.dimecresalessis.scoutbase.player.domain.model.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository {
    List<Player> findAll();
    Optional<Player> findById(UUID id);
    Player save(Player player);
    void deleteById(UUID id);
}
