package es.dimecresalessis.scoutbase.application.userteam.find;

import es.dimecresalessis.scoutbase.application.userclub.find.FindUserClubByIdUseCase;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Use case for finding all {@link UserTeam} of a {@link UserClub}.
 */
@Service
@AllArgsConstructor
public class FindAllUserTeamsByUserClubUseCase {

    private final UserTeamRepository userTeamRepository;
    private final FindUserClubByIdUseCase findUserClubByIdUseCase;

    /**
     * Executes the retrieval of all {@link UserTeam} entities associated with a specific {@link UserClub}.
     *
     * @param clubId The {@link UUID} of the user club whose teams are to be retrieved.
     * @return A {@link List} of {@link UserTeam} objects belonging to the specified club.
     */
    public List<UserTeam> execute(UUID clubId) {
        List<UserTeam> userTeams = new ArrayList<>();
        UserClub userClub = findUserClubByIdUseCase.execute(clubId);
        if (userClub.getUserTeams() != null) {
            for (UUID teamId: userClub.getUserTeams()) {
                UserTeam userTeam = userTeamRepository.findById(teamId).orElse(null);
                if (userTeam != null) {
                    userTeams.add(userTeam);
                }
            }
        }
        return userTeams;
    }
}
