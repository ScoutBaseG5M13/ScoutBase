package es.dimecresalessis.scoutbase.application.player.delete;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for deleting a {@link Player}.
 */
@Service
@AllArgsConstructor
public class DeletePlayerUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeletePlayerUseCase.class);
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    /**
     * Executes the operation to delete a {@link Player} from the repository.
     *
     * @param id The ID of the player to be deleted.
     * @return {@code true} if the player was successfully deleted, {@code false} otherwise.
     */
    public boolean execute(UUID id) {
        Player player = playerRepository.findById(id).orElseThrow();
        playerRepository.deleteById(id);
        logger.info("[DELETE] Deleted Player '{}'", id);

        teamRepository.findByPlayerId(id).ifPresent(team -> {
            team.getPlayers().remove(id);
            teamRepository.save(team);
            logger.info("[DELETE] Removed Player '{}' from Team '{}'", id, team.getName());
        });

        return true;
    }
}
