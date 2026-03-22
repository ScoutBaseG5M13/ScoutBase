package es.dimecresalessis.scoutbase.application.player;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for finding a {@link Player} entity by their unique ID.
 */
@Service
@AllArgsConstructor
public class FindPlayerByIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindPlayerByIdUseCase.class);
    private final PlayerRepository playerRepository;

    /**
     * Retrieves a player by their unique ID.
     *
     * @param id The ID of the player.
     * @return The {@link Player} entity corresponding to the provided ID.
     */
    public Player execute(UUID id)  {
        Player player = playerRepository.findById(id).orElseThrow();
        logger.info("[FIND] Found Player with id '{}'", id);
        return player;
    }
}