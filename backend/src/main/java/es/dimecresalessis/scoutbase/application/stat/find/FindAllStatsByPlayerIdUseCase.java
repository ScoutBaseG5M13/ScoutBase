package es.dimecresalessis.scoutbase.application.stat.find;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Use case for finding a {@link Stat} by {@link Player} {@link UUID}.
 */
@Service
@AllArgsConstructor
public class FindAllStatsByPlayerIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindAllStatsByPlayerIdUseCase.class);
    private final StatRepository statRepository;

    /**
     * Executes the search for Stats belonging to a player.
     *
     * @param playerId The unique identifier ({@link UUID}) of the player whose stats are being requested.
     * @return A {@link List} of {@link Stat} objects found. Returns an empty list if no stats exist for the player.
     */
    public List<Stat> execute(UUID playerId) {
        List<Stat> stats = statRepository.findAllByPlayerId(playerId);
        logger.info("[FIND] Found {} stats", stats.size());
        return stats;
    }
}
