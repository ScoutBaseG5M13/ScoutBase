package es.dimecresalessis.scoutbase.application.stat;

import es.dimecresalessis.scoutbase.domain.stat.exception.StatException;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CheckIfStatAlreadyExistsOnPlayer {

    private static final Logger logger = LoggerFactory.getLogger(CheckIfStatAlreadyExistsOnPlayer.class);
    private final StatRepository statRepository;

    public boolean execute(Stat stat) throws StatException {
        List<Stat> playerStats = statRepository.findAllByPlayerId(stat.getPlayerId());
        for (Stat playerStat : playerStats) {
            if (playerStat.getCode().equals(stat.getCode())) {
                return true;
            }
        }
        return false;
    }
}
