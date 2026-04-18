package es.dimecresalessis.scoutbase.application.stat;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.stat.exception.StatException;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Use case for creating {@link Stat} entities in the system.
 */
@Service
@AllArgsConstructor
public class CreateStatUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateStatUseCase.class);
    private final StatRepository statRepository;

    /**
     * Executes the operation to save a new {@link Stat} entity in the DB.
     *
     * @param stat The {@link Stat} object containing the stat's details.
     * @return The created {@link Stat} object after being persisted.
     * @throws StatException If the provided stat object is null.
     */
    public Stat execute(Stat stat) throws StatException {
        if (stat == null) {
            throw new StatException(ErrorEnum.STAT_IS_NULL);
        }
        if (stat.getId() == null) {
            throw new StatException(ErrorEnum.STAT_ID_IS_NULL);
        }
        Optional<Stat> optStat = statRepository.findById(stat.getId());
        if (optStat.isPresent() && !optStat.get().getCode().equals(stat.getCode())) {
            throw new StatException(ErrorEnum.STAT_ID_ALREADY_EXISTS, stat.getId().toString());
        }
        statRepository.save(stat);
        logger.info("[CREATE] Created Stat with id '{}'", stat.getId());
        return stat;
    }
}
