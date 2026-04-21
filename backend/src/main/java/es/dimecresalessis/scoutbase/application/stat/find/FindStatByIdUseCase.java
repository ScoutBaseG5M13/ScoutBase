package es.dimecresalessis.scoutbase.application.stat.find;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for finding a {@link Stat}.
 */
@Service
@AllArgsConstructor
public class FindStatByIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindStatByIdUseCase.class);
    private final StatRepository statRepository;

    /**
     * Executes the search for a single Stat.
     *
     * @param statId The unique identifier ({@link UUID}) of the statistic to find.
     * @return The found {@link Stat} entity, or {@code null} if no record exists for the given ID.
     */
    public Stat execute(UUID statId) {
        Stat stats = statRepository.findById(statId).orElse(null);
        logger.info("[FIND] Found {} stats", 1);
        return stats;
    }
}
