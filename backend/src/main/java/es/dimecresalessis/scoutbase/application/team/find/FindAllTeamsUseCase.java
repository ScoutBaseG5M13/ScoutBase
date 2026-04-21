package es.dimecresalessis.scoutbase.application.team.find;

import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FindAllTeamsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindAllTeamsUseCase.class);
    private final TeamRepository teamRepository;

    /**
     * Executes the operation for getting all teams from the repository.
     *
     * @return A list of all {@link Team} entities.
     */
    public List<Team> execute() {
        List<Team> teams = teamRepository.findAll();
        logger.info("[FIND] Found {} teams", teams.size());
        return teams;
    }

}
