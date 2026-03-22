package es.dimecresalessis.scoutbase.application.player;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for deleting a {@link Player} entity from the system.
 */
@Service
@AllArgsConstructor
public class DeletePlayerUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeletePlayerUseCase.class);
    private final PlayerRepository playerRepository;

    /**
     * Executes the operation to delete a {@link Player} from the repository.
     *
     * @param id The ID of the player to be deleted.
     * @return {@code true} if the player was successfully deleted, {@code false} otherwise.
     */
    public boolean execute(UUID id) {
        playerRepository.findById(id).orElseThrow();
        playerRepository.deleteById(id);
        logger.info("[DELETE] Deleted Player with id '{}'", id);
        return true;
    }
}
