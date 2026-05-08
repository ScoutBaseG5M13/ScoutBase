package es.dimecresalessis.scoutbase.application.player.update;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.player.exception.PlayerException;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for updating a {@link Player}.
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
     * @param playerId The ID of the player to be updated.
     * @return The updated {@link Player} object after being persisted.
     */
    public Player execute(Player player, UUID playerId) {
        validateAndRetrievePlayer(player, playerId);
        Player matchedPlayer = matchWithSavedInfo(player, playerId);
        playerRepository.save(matchedPlayer);
        logger.info("[UPDATE] Updated Player '{}'", matchedPlayer.getId());
        return matchedPlayer;
    }

    private void validateAndRetrievePlayer(Player player, UUID id) {
        if (!player.getId().equals(id)) {
            throw new IllegalArgumentException("Player id '" + player.getId() + "' does not match the path variable '" + id + "'");
        }

        Player bodyPlayer = playerRepository.findById(player.getId()).orElseThrow(
                () -> new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, player.getId().toString())
        );

        Player idPlayer = playerRepository.findById(id).orElseThrow(
                () -> new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, id.toString())
        );

        if (!bodyPlayer.getId().toString().equals(idPlayer.getId().toString())) {
            throw new IllegalArgumentException("Body player id '" + bodyPlayer.getId() + "' does not match '" + idPlayer.getId() + "'");
        }
    }

    private Player matchWithSavedInfo(Player player, UUID playerId) {
        Player newPlayer = player;
        Player savedPlayer = playerRepository.findById(playerId).orElse(null);
        if (savedPlayer == null) {
            return newPlayer;
        }
        newPlayer.matchWithObject(savedPlayer);
        return newPlayer;
    }
}
