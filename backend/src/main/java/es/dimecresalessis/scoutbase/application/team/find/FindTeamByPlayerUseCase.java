package es.dimecresalessis.scoutbase.application.team.find;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Use case for finding a {@link Team} by {@link Player} {@link UUID}.
 */
@Service
@AllArgsConstructor
public class FindTeamByPlayerUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindTeamByPlayerUseCase.class);
    private final TeamRepository teamRepository;

    /**
     * Executes the operation for getting the team where the Player plays.
     *
     * @return A list of all {@link Team} entities.
     */
    public Team execute(UUID playerId) {
        Optional<Team> teams = teamRepository.findByPlayerId(playerId);
        logger.info("[FIND] Found {} teams", teams.isPresent() ? 1 : 0);
        return teams.orElse(null);
    }
}
