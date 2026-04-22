package es.dimecresalessis.scoutbase.application.stat;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.stat.exception.StatException;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Use case for finding if a {@link Stat} already exists in a {@link Player}.
 */
@Service
@AllArgsConstructor
public class CheckIfStatAlreadyExistsOnPlayer {

    private static final Logger logger = LoggerFactory.getLogger(CheckIfStatAlreadyExistsOnPlayer.class);
    private final StatRepository statRepository;

    /**
     * Checks if the given Stat already exists for the player associated with it.
     *
     * @param stat The {@link Stat} object containing the playerId and the code to verify.
     * @return {@code true} if a statistic with the same code already exists for the player;
     * {@code false} otherwise.
     * @throws StatException if an error occurs during the validation process.
     */
    public boolean execute(Stat stat) throws StatException {
        List<Stat> playerStats = statRepository.findAllByPlayerId(stat.getPlayerId());
        for (Stat playerStat : playerStats) {
            if (playerStat.getCode().equals(stat.getCode())) {
                return true;
            }
        }
        return false;
    }
}
