package es.dimecresalessis.scoutbase.application.stat;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.stat.exception.StatException;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for updating an existing {@link Stat} entity in the system.
 */
@Service
@AllArgsConstructor
public class UpdateStatUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UpdateStatUseCase.class);
    private final StatRepository statRepository;
    private final CheckIfStatAlreadyExistsOnPlayer checkIfStatAlreadyExistsOnPlayer;

    /**
     * Updates the details of a {@link Stat} identified by their unique ID.
     *
     * @param stat The updated {@link Stat} object with the new details.
     * @param id The ID of the stat to be updated.
     * @return The updated {@link Stat} object after being persisted.
     */
    public Stat execute(Stat stat, UUID id) {
        validateAndRetrieveStat(stat, id);
        statRepository.update(stat);
        logger.info("[UPDATE] Updated Stat with id '{}'", stat.getId());
        return stat;
    }

    private void validateAndRetrieveStat(Stat stat, UUID id) {
        Stat bodyStat = statRepository.findById(stat.getId()).orElseThrow(
                () -> new StatException(ErrorEnum.STAT_NOT_FOUND, stat.getId().toString())
        );

        Stat idStat = statRepository.findById(id).orElseThrow(
                () -> new StatException(ErrorEnum.STAT_NOT_FOUND, id.toString())
        );

        if (checkIfStatAlreadyExistsOnPlayer.execute(stat)) {
            throw new StatException(ErrorEnum.STAT_CODE_ALREADY_EXISTS, stat.getId().toString(), stat.getPlayerId().toString());
        }

        if (!bodyStat.getId().toString().equals(idStat.getId().toString())) {
            throw new IllegalArgumentException("Stat id " + bodyStat.getId() + " does not match " + idStat.getId());
        }
    }
}
