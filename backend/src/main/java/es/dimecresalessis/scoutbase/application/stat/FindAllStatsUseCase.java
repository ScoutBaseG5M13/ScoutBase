package es.dimecresalessis.scoutbase.application.stat;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Use case for retrieving all {@link Stat} entities from the system.
 */
@Service
@AllArgsConstructor
public class FindAllStatsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindAllStatsUseCase.class);
    private final StatRepository statRepository;

    /**
     * Executes the operation for getting all stats from the repository.
     *
     * @return A list of all {@link Stat} entities.
     */
    public List<Stat> execute() {
        List<Stat> stats = statRepository.findAll();
        logger.info("[FIND] Found {} stats", stats.size());
        return stats;
    }
}
