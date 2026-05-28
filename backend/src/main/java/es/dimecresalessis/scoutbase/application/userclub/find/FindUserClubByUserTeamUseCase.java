package es.dimecresalessis.scoutbase.application.userclub.find;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.userclub.exception.UserClubException;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.userteam.exception.UserTeamException;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Use case for finding a {@link List<Club>} by {@link User} {@link UUID}.
 */
@Service
@AllArgsConstructor
public class FindUserClubByUserTeamUseCase {

    private final UserClubRepository userClubRepository;
    private final UserTeamRepository userTeamRepository;

    /**
     * Executes the search for a specific {@link UserClub} that contains a given team ID.
     *
     * @param teamId The {@link UUID} of the team used to find the associated club.
     * @return The {@link UserClub} that contains the specified team in its associations.
     * @throws UserClubException If no club is found that matches the provided team ID.
     */
    public UserClub execute(UUID teamId) {
        UserTeam userTeam = userTeamRepository.findById(teamId)
                .orElseThrow(() -> new UserTeamException(ErrorEnum.USER_TEAM_NOT_FOUND, teamId.toString()));
        Optional<UserClub> userClub = userClubRepository.findById(userTeam.getUserClub());
        if (userClub.isPresent()) {
            return userClub.get();
        }
        throw new UserClubException(ErrorEnum.NO_USER_CLUB_HAS_BEEN_FOUND);
    }
}
