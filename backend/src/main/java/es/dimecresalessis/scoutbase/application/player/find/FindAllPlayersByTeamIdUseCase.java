package es.dimecresalessis.scoutbase.application.player.find;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Use case for finding a {@link List<Player>} by {@link Team} {@link UUID}.
 */
@Service
@AllArgsConstructor
public class FindAllPlayersByTeamIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindAllPlayersByTeamIdUseCase.class);
    private final TeamRepository teamRepository;
    private final FindPlayerByIdUseCase findPlayerByIdUseCase;

    /**
     * Executes the operation for getting all players from the repository, filtering by Team id.
     *
     * @return A list of all {@link Player} entities.
     */
    public List<Player> execute(UUID teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        if (!team.isPresent()) {
           return null;
        }
        List<UUID> playerIds = team.get().getPlayers();
        List<Player> players = new ArrayList<>();
        for (UUID playerId : playerIds) {
            players.add(findPlayerByIdUseCase.execute(playerId));
        }
        logger.info("[FIND] Found {} players", players.size());
        return players;
    }
}
