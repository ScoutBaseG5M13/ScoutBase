package es.dimecresalessis.scoutbase.application.stat.delete;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import es.dimecresalessis.scoutbase.domain.stat.exception.StatException;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for deleting a {@link Stat}.
 */
@Service
@AllArgsConstructor
public class DeleteStatUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeleteStatUseCase.class);
    private final StatRepository statRepository;
    private final PlayerRepository playerRepository;

    /**
     * Executes the operation to delete a {@link Stat} from the repository.
     *
     * @param id The ID of the stat to be deleted.
     * @return {@code true} if the stat was successfully deleted, {@code false} otherwise.
     */
    public boolean execute(UUID id) {
        Stat stat = statRepository.findById(id).orElseThrow(
                () -> new StatException(ErrorEnum.STAT_NOT_FOUND, id.toString())
        );

        statRepository.findById(id).orElseThrow();
        statRepository.deleteById(id);
        logger.info("[DELETE] Deleted Stat with id '{}'", id);

        playerRepository.findById(stat.getPlayerId()).ifPresent(player -> {
            player.getStats().remove(id);
            playerRepository.save(player);
            logger.info("[DELETE] Removed Stat '{}' from Player '{}'", id, player.getName());
        });

        return true;
    }
}
