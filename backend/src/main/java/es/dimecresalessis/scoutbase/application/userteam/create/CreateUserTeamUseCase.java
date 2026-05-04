package es.dimecresalessis.scoutbase.application.userteam.create;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.userteam.exception.UserTeamException;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Use case for creating a {@link UserTeam}.
 */
@Service
@AllArgsConstructor
public class CreateUserTeamUseCase {

    private static final Logger logger = LoggerFactory.getLogger(CreateUserTeamUseCase.class);
    private final UserTeamRepository userTeamRepository;
    private final UserClubRepository userClubRepository;

    /**
     * Executes the creation of a Team and updates the corresponding userclub.
     * @param userTeam The {@link UserTeam} entity to be created.
     * @param userClub The {@link UserClub} entity that will own the new userteam.
     * @return The persisted {@link UserTeam} entity.
     * @throws UserTeamException if the userteam is null, the ID is missing, or if a userteam with
     * the same ID already exists.
     */
    public UserTeam execute(UserTeam userTeam, UserClub userClub) {
        if (userTeam == null) {
            throw new UserTeamException(ErrorEnum.TEAM_IS_NULL);
        }

        if (userTeam.getId() == null) {
            throw new UserTeamException(ErrorEnum.TEAM_ID_IS_NULL);
        }

        if (userTeamRepository.findById(userTeam.getId()).isPresent()) {
            throw new UserTeamException(ErrorEnum.TEAM_ALREADY_EXISTS, userTeam.getId().toString());
        }

        userTeamRepository.save(userTeam);
        logger.info("[CREATE] Created Team '{}'", userTeam.getId());
        if (userClub.getUserTeams() == null) {
            userClub.setUserTeams(new ArrayList<>());
        }
        userClub.getUserTeams().add(userTeam.getId());
        userClubRepository.save(userClub);
        logger.info("[UPDATE] Updated Club '{}' and added the Team {}", userTeam.getId(), userClub.getId());

        return userTeam;
    }
}
