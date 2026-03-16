package es.dimecresalessis.scoutbase.application.player;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import es.dimecresalessis.scoutbase.domain.player.exception.PlayerException;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreatePlayerUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreatePlayerUseCase.class);
    private final PlayerRepository playerRepository;

    public Player execute(Player player) throws PlayerException {
        if (player == null) {
            throw new PlayerException(ErrorEnum.PLAYER_IS_NULL);
        }
        playerRepository.save(player);
        logger.info("[CREATE] Created Player with id '{}'", player.getId());
        return player;
    }
}
