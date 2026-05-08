package es.dimecresalessis.scoutbase.application.user.find;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FindUserRoleInTeamUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindUserRoleInTeamUseCase.class);
    private final UserClubRepository userClubRepository;
    private final UserTeamRepository userTeamRepository;
    private final FindUserRoleInClubUseCase findUserRoleInClubUseCase;

    /**
     * Resolves the specific role of a user within a userteam context.
     * This considers the userclub hierarchy (e.g., userclub admins inherit permissions in the userteam).
     *
     * @param user The user to evaluate.
     * @param teamId The unique identifier of the userteam.
     * @return The highest {@link RoleEnum} the user holds in the userteam context,
     * or {@code null} if no relationship exists.
     */

    public RoleEnum execute(User user, UUID teamId) {
        RoleEnum teamRole = null;
        Optional<UserTeam> team = userTeamRepository.findById(teamId);
        if (team.isPresent()) {
            Optional<UserClub> club = userClubRepository.findUserClubByTeam(teamId);
            if (club.isPresent()) {
                RoleEnum clubRole = findUserRoleInClubUseCase.execute(user, club.get().getId());
                if (clubRole != null) {
                    return clubRole;
                }
            }

            if (team.get().getTrainer() != null && team.get().getTrainer().equals(user.getId())) {
                teamRole = RoleEnum.TRAINER;
            }

            if (team.get().getSecondTrainer() != null && team.get().getSecondTrainer().equals(user.getId())) {
                teamRole = RoleEnum.SECOND_TRAINER;
            }

            if (team.get().getScouters() != null && team.get().getScouters().stream().anyMatch(s -> s.equals(user.getId()))) {
                teamRole = RoleEnum.SCOUTER;
            }
            logger.info("[AUTH] User '{}' has role {} in userteam {}, of userclub '{}'", user.getUsername(), teamRole, team.get().getName(), club.isPresent() ? club.get().getName() : null);
        }
        return null;
    }
}
