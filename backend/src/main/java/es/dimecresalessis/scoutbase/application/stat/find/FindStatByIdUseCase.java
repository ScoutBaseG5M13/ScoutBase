package es.dimecresalessis.scoutbase.application.stat.find;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class FindStatByIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindStatByIdUseCase.class);
    private final StatRepository statRepository;

    public Stat execute(UUID statId) {
        Stat stats = statRepository.findById(statId).orElse(null);
        logger.info("[FIND] Found {} stats", 1);
        return stats;
    }
}
