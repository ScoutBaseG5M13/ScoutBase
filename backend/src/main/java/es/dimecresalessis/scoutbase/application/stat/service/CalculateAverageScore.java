package es.dimecresalessis.scoutbase.application.stat.service;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.player.exception.PlayerException;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.model.StatEnum;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.PlayerAverageStatScoreDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static es.dimecresalessis.scoutbase.domain.stat.model.StatTypeEnum.*;

/**
 * Service to calculate the average score of the {@link Stat} of the players of a {@link Team}.
 */
@Service
@RequiredArgsConstructor
public class CalculateAverageScore {

    private final Logger logger = LoggerFactory.getLogger(CalculateAverageScore.class);
    private final StatRepository statRepository;
    private final PlayerRepository playerRepository;

    /**
     * Executes the calculation of average scores for a specific {@link Player} based on their recorded {@link Stat}.
     *
     * @param playerId The {@link UUID} of the player whose stats will be calculated.
     * @return A {@link PlayerAverageStatScoreDTO} containing the player's information and calculated averages.
     * @throws PlayerException If no player is found with the provided ID.
     */
    public PlayerAverageStatScoreDTO execute(UUID playerId) {
        Player player = playerRepository.findById(playerId).orElseThrow(
                () -> new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, playerId.toString())
        );
        List<Stat> stats = statRepository.findAllByPlayerId(playerId);

        // SUM
        float offensiveTotalScore = 0f;
        float defensiveTotalScore = 0f;
        float mentalTotalScore = 0f;
        float physicalTotalScore = 0f;

        for (Stat stat : stats) {
            switch (StatEnum.fromStatCode(stat.getCode()).type) {
                case OFENSIVO:
                    offensiveTotalScore += stat.getValue();
                    break;
                case DEFENSIVO:
                    defensiveTotalScore += stat.getValue();
                    break;
                case MENTAL:
                    mentalTotalScore += stat.getValue();
                    break;
                case FISICO:
                    physicalTotalScore += stat.getValue();
                    break;
                default:
                    logger.error("Stat type not recognized: {}", stat.getCode());
                    break;
            }
        }

        // Count
        long globalCount = stats.size();
        long offensiveCount = stats.stream()
                .filter(s -> StatEnum.fromStatCode(s.getCode()).type.equals(OFENSIVO))
                .count();
        long defensiveCount = stats.stream()
                .filter(s -> StatEnum.fromStatCode(s.getCode()).type.equals(DEFENSIVO))
                .count();
        long mentalCount = stats.stream()
                .filter(s -> StatEnum.fromStatCode(s.getCode()).type.equals(MENTAL))
                .count();
        long physicalCount = stats.stream()
                .filter(s -> StatEnum.fromStatCode(s.getCode()).type.equals(FISICO))
                .count();

        // Average
        float offensiveAverageScore = divide(offensiveTotalScore, offensiveCount);
        float defensiveAverageScore = divide(defensiveTotalScore,  defensiveCount);
        float mentalAverageScore = divide(mentalTotalScore, mentalCount);
        float physicalAverageScore = divide(physicalTotalScore, physicalCount);
        float globalAverageScore = divide(offensiveAverageScore + defensiveAverageScore + mentalAverageScore + physicalAverageScore, globalCount);

        return new PlayerAverageStatScoreDTO(
                playerId,
                player.getName(),
                player.getSurname(),
                player.getPosition(),
                globalAverageScore,
                defensiveAverageScore,
                offensiveAverageScore,
                mentalAverageScore,
                physicalAverageScore
        );
    }

    private float divide(float total, long count) {
        return count > 0 ? total / count : 0.0f;
    }
}
