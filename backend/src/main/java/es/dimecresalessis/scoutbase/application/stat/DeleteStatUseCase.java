package es.dimecresalessis.scoutbase.application.stat;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for deleting a {@link Stat} entity from the system.
 */
@Service
@AllArgsConstructor
public class DeleteStatUseCase {

    private static final Logger logger = LoggerFactory.getLogger(DeleteStatUseCase.class);
    private final StatRepository statRepository;

    /**
     * Executes the operation to delete a {@link Stat} from the repository.
     *
     * @param id The ID of the stat to be deleted.
     * @return {@code true} if the stat was successfully deleted, {@code false} otherwise.
     */
    public boolean execute(UUID id) {
        statRepository.findById(id).orElseThrow();
        statRepository.deleteById(id);
        logger.info("[DELETE] Deleted Stat with id '{}'", id);
        return true;
    }
}
