package es.dimecresalessis.scoutbase.player.infrastructure.port;

import es.dimecresalessis.scoutbase.player.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {
}
