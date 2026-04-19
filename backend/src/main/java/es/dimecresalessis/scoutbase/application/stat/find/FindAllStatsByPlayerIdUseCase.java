package es.dimecresalessis.scoutbase.application.stat.find;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FindAllStatsByPlayerIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindAllStatsByPlayerIdUseCase.class);
    private final StatRepository statRepository;

    public List<Stat> execute(UUID playerId) {
        List<Stat> stats = statRepository.findAllByPlayerId(playerId);
        logger.info("[FIND] Found {} stats", stats.size());
        return stats;
    }
}
