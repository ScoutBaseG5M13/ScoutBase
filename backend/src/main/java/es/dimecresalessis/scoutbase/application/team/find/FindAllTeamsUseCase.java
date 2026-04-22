package es.dimecresalessis.scoutbase.application.team.find;

import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Use case for finding all {@link Team}.
 */
@Service
@AllArgsConstructor
public class FindAllTeamsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindAllTeamsUseCase.class);
    private final TeamRepository teamRepository;

    /**
     * Executes the operation to fetch all Teams.
     *
     * @return A {@link List} containing all {@link Team} entities.
     * Returns an empty list if no teams are registered.
     */
    public List<Team> execute() {
        List<Team> teams = teamRepository.findAll();
        logger.info("[FIND] Found {} teams", teams.size());
        return teams;
    }

}
