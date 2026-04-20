package es.dimecresalessis.scoutbase.application.player.create;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import es.dimecresalessis.scoutbase.domain.player.exception.PlayerException;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Use case for creating {@link Player}.
 */
@Service
@AllArgsConstructor
public class CreatePlayerUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreatePlayerUseCase.class);
    private final PlayerRepository playerRepository;

    /**
     * Executes the operation to save a new {@link Player} entity in the DB.
     *
     * @param player The {@link Player} object containing the player's details.
     * @return The created {@link Player} object after being persisted.
     * @throws PlayerException If the provided player object is null.
     */
    public Player execute(Player player) throws PlayerException {
        if (player == null) {
            throw new PlayerException(ErrorEnum.PLAYER_IS_NULL);
        }
        if (player.getId() == null) {
            throw new PlayerException(ErrorEnum.PLAYER_ID_IS_NULL);
        }
        if (playerRepository.findById(player.getId()).isPresent()) {
            throw new PlayerException(ErrorEnum.PLAYER_ALREADY_EXISTS, player.getId().toString());
        }
        playerRepository.save(player);
        logger.info("[CREATE] Created Player '{}'", player.getId());
        return player;
    }
}
