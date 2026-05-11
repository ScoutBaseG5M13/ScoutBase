package es.dimecresalessis.scoutbase.infrastructure.stat.web.dto;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.shared.domain.PositionEnum;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

/**
 * Response DTO representing the calculated average {@link Stat} scores of a {@link Player}.
 */
@Getter
@AllArgsConstructor
public class PlayerAverageStatScoreDTO {

    private UUID playerId;

    private String name;

    private String surname;

    private PositionEnum position;

    private float globalAverageScore;

    private float offensiveAverageScore;

    private float defensiveAverageScore;

    private float mentalAverageScore;

    private float physicalAverageScore;
}
