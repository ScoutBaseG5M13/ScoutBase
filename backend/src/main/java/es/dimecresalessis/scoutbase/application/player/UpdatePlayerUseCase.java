package es.dimecresalessis.scoutbase.application.player;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdatePlayerUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UpdatePlayerUseCase.class);
    private final PlayerRepository playerRepository;

    public Player execute(Player player) {
        playerRepository.findById(player.getId()).orElseThrow();
        playerRepository.save(player);
        logger.info("[UPDATE] Updated Player with id '{}'", player.getId());
        return player;
    }
}
