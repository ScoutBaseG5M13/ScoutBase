package es.dimecresalessis.scoutbase.application.stat.create;

import es.dimecresalessis.scoutbase.application.stat.CheckIfStatAlreadyExistsOnPlayer;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import es.dimecresalessis.scoutbase.domain.stat.exception.StatException;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Use case for creating {@link Stat}.
 */
@Service
@AllArgsConstructor
public class CreateStatUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateStatUseCase.class);
    private final StatRepository statRepository;
    private final PlayerRepository playerRepository;
    private final CheckIfStatAlreadyExistsOnPlayer checkIfStatAlreadyExistsOnPlayer;

    /**
     * Executes the operation to save a new {@link Stat} entity in the DB.
     *
     * @param stat The {@link Stat} object containing the stat's details.
     * @return The created {@link Stat} object after being persisted.
     * @throws StatException If the provided stat object is null.
     */
    public Stat execute(Stat stat, UUID playerId) throws StatException {
        if (stat == null) {
            throw new StatException(ErrorEnum.STAT_IS_NULL);
        }
        if (stat.getId() == null) {
            throw new StatException(ErrorEnum.STAT_ID_IS_NULL);
        }
        if (!stat.getPlayerId().equals(playerId)) {
            throw new StatException(ErrorEnum.USER_ID_DOES_NOT_MATCH, stat.getPlayerId().toString(), playerId.toString());
        }
        if (checkIfStatAlreadyExistsOnPlayer.execute(stat)) {
            throw new StatException(ErrorEnum.STAT_CODE_ALREADY_EXISTS, stat.getCode(), stat.getPlayerId().toString());
        }
        stat.setPlayerId(playerId);
        statRepository.save(stat);
        logger.info("[CREATE] Created Stat with id '{}'", stat.getId());

        playerRepository.findById(stat.getPlayerId()).ifPresent(player -> {
            if (player.getStats() == null) {
                player.setStats(new ArrayList<>());
            }
            player.getStats().add(stat.getId());
            playerRepository.save(player);
            logger.info("[CREATE] Added Stat '{}' to Player '{}'", stat.getId(), player.getName());
        });

        return stat;
    }
}
