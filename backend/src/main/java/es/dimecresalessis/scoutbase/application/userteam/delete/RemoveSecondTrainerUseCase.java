package es.dimecresalessis.scoutbase.application.userteam.delete;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.userteam.exception.UserTeamException;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RemoveSecondTrainerUseCase {

    private static final Logger logger = LoggerFactory.getLogger(RemoveSecondTrainerUseCase.class);
    private final UserTeamRepository userTeamRepository;

    /**
     * Removes a second trainer from a User Team.
     *
     * @param teamId The ID of the userteam.
     * @param userId The ID of the User.
     * @return The {@link UserTeam} entity corresponding to the provided ID.
     */
    public boolean execute(UUID teamId, UUID userId) {
        if (teamId == null) {
            throw new UserTeamException(ErrorEnum.USER_TEAM_ID_IS_NULL);
        }

        if (userId == null) {
            throw new UserTeamException(ErrorEnum.USER_ID_IS_NULL);
        }

        UserTeam userTeam = userTeamRepository.findById(teamId).orElse(null);
        if (userTeam == null) {
            throw new UserTeamException(ErrorEnum.USER_TEAM_NOT_FOUND, teamId.toString());
        }

        userTeam.setSecondTrainer(null);
        userTeamRepository.save(userTeam);
        logger.info("[DELETE] Removed second trainer from Team '{}'", teamId);
        return true;
    }
}
