package es.dimecresalessis.scoutbase.application.userteam.find;

import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Use case for finding all {@link UserTeam} a {@link User} is part of.
 */
@Service
@AllArgsConstructor
public class FindAllUserTeamsByUserUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindAllUserTeamsByUserUseCase.class);
    private final UserTeamRepository userTeamRepository;

    /**
     * Executes the search for all Teams linked to a specific user ID.
     *
     * @param userId The unique identifier ({@link UUID}) of the user.
     * @return A {@link List} of {@link UserTeam} objects found for the user.
     * Returns an empty list if the user is not part of any userteam.
     */
    public List<UserTeam> execute(UUID userId) {
        List<UserTeam> userTeams = userTeamRepository.findAllByUserId(userId);
        logger.info("[FIND] Found {} teams", userTeams.size());
        return userTeams;
    }
}
