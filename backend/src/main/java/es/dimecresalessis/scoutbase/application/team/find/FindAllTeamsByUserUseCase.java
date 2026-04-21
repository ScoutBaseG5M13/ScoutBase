package es.dimecresalessis.scoutbase.application.team.find;

import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FindAllTeamsByUserUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindAllTeamsByUserUseCase.class);
    private final TeamRepository teamRepository;

    /**
     * Executes the operation for getting all teams from the repository.
     *
     * @return A list of all {@link Team} entities.
     */
    public List<Team> execute(UUID userId) {
        List<Team> teams = teamRepository.findAllByUserId(userId);
        logger.info("[FIND] Found {} teams", teams.size());
        return teams;
    }
}
