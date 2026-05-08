package es.dimecresalessis.scoutbase.application.userteam.find;

import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Use case for finding all {@link UserTeam}.
 */
@Service
@AllArgsConstructor
public class FindAllUserTeamsUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindAllUserTeamsUseCase.class);
    private final UserTeamRepository userTeamRepository;

    /**
     * Executes the operation to fetch all Teams.
     *
     * @return A {@link List} containing all {@link UserTeam} entities.
     * Returns an empty list if no teams are registered.
     */
    public List<UserTeam> execute() {
        List<UserTeam> userTeams = userTeamRepository.findAll();
        logger.info("[FIND] Found {} teams", userTeams.size());
        return userTeams;
    }

}
