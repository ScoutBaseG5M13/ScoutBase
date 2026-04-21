package es.dimecresalessis.scoutbase.application.team.find;

import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Use case for finding all {@link Team} a {@link User} is part of.
 */
@Service
@AllArgsConstructor
public class FindAllTeamsByUserUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindAllTeamsByUserUseCase.class);
    private final TeamRepository teamRepository;

    /**
     * Executes the search for all Teams linked to a specific user ID.
     *
     * @param userId The unique identifier ({@link UUID}) of the user.
     * @return A {@link List} of {@link Team} objects found for the user.
     * Returns an empty list if the user is not part of any team.
     */
    public List<Team> execute(UUID userId) {
        List<Team> teams = teamRepository.findAllByUserId(userId);
        logger.info("[FIND] Found {} teams", teams.size());
        return teams;
    }
}
