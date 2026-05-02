package es.dimecresalessis.scoutbase.application.user.find;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
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
    private final ClubRepository clubRepository;
    private final TeamRepository teamRepository;
    private final FindUserRoleInClubUseCase findUserRoleInClubUseCase;

    /**
     * Resolves the specific role of a user within a team context.
     * This considers the club hierarchy (e.g., club admins inherit permissions in the team).
     *
     * @param user The user to evaluate.
     * @param teamId The unique identifier of the team.
     * @return The highest {@link RoleEnum} the user holds in the team context,
     * or {@code null} if no relationship exists.
     */

    public RoleEnum execute(User user, UUID teamId) {
        RoleEnum teamRole = null;
        Optional<Team> team = teamRepository.findById(teamId);
        if (team.isPresent()) {
            Optional<Club> club = clubRepository.findClubByTeam(teamId);
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
            logger.info("[AUTH] User '{}' has role {} in team {}, of club '{}'", user.getUsername(), teamRole, team.get().getName(), club.get().getName());
        }
        return null;
    }
}
