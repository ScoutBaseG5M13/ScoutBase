package es.dimecresalessis.scoutbase.player.application.usecases;

import es.dimecresalessis.scoutbase.player.domain.model.Player;
import es.dimecresalessis.scoutbase.player.domain.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FindAllPlayersUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindAllPlayersUseCase.class);
    private final PlayerRepository playerRepository;

    public List<Player> execute() {
        List<Player> players = playerRepository.findAll();
        logger.info("[FIND] Found {} players", players.size());
        return players;
    }
}
