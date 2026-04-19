package es.dimecresalessis.scoutbase.application.player.find;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Use case for retrieving all {@link Player} entities from the system.
 */
@Service
@AllArgsConstructor
public class FindAllPlayersUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindAllPlayersUseCase.class);
    private final PlayerRepository playerRepository;

    /**
     * Executes the operation for getting all players from the repository.
     *
     * @return A list of all {@link Player} entities.
     */
    public List<Player> execute() {
        List<Player> players = playerRepository.findAll();
        logger.info("[FIND] Found {} players", players.size());
        return players;
    }
}
