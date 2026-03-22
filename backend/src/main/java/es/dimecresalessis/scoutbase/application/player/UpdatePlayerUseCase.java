package es.dimecresalessis.scoutbase.application.player;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for updating an existing {@link Player} entity in the system.
 */
@Service
@AllArgsConstructor
public class UpdatePlayerUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UpdatePlayerUseCase.class);
    private final PlayerRepository playerRepository;

    /**
     * Updates the details of a {@link Player} identified by their unique ID.
     *
     * @param player The updated {@link Player} object with the new details.
     * @param id The ID of the player to be updated.
     * @return The updated {@link Player} object after being persisted.
     */
    public Player execute(Player player, UUID id) {
        Player idPlayer = playerRepository.findById(id).orElseThrow();
        Player bodyPlayer = playerRepository.findById(player.getId()).orElseThrow();
        if (!idPlayer.getId().toString().equals(bodyPlayer.getId().toString())) {
            throw new IllegalArgumentException("Player id does not match");
        }
        playerRepository.save(player);
        logger.info("[UPDATE] Updated Player with id '{}'", player.getId());
        return player;
    }
}
