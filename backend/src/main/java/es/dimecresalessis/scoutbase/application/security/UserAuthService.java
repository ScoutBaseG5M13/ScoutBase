package es.dimecresalessis.scoutbase.application.security;

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

/**
 * Application service responsible for user authorization logic.
 * <p>
 * It verifies user permissions for teams and clubs based on the role
 * hierarchy defined in the domain layer.
 */
@Service
@AllArgsConstructor
public class UserAuthService {

    private static final Logger logger = LoggerFactory.getLogger(UserAuthService.class);
    private final TeamRepository teamRepository;
    private final ClubRepository clubRepository;

    /**
     * Checks if a user is authorized for a specific team given a minimum required role.
     * Authorization is granted if the user holds the required role either at the
     * owning club level or directly within the team.
     *
     * @param user        The user requesting authorization.
     * @param teamId      The unique identifier of the team.
     * @param minimumRole The minimum role level required to perform the action.
     * @return {@code true} if the user meets the role requirements; {@code false} otherwise.
     */
    public boolean isAuthorizedByTeam(User user, UUID teamId, RoleEnum minimumRole) {
        Optional<Club> club = clubRepository.findClubByTeam(teamId);
        if (club.isPresent()) {
            RoleEnum clubRole = findClubUserRole(user, club.get().getId());
            if (clubRole != null && RoleEnum.isEqualsOrHigher(clubRole, minimumRole)) {
                return true;
            }
        }
        RoleEnum teamRole = findTeamUserRole(user, teamId);
        if (teamRole != null && RoleEnum.isEqualsOrHigher(teamRole, minimumRole)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if a user is authorized for actions at the club level.
     *
     * @param user        The user requesting authorization.
     * @param clubId      The unique identifier of the club.
     * @param minimumRole The minimum role level required to perform the action.
     * @return {@code true} if the user holds a sufficient role in the club; {@code false} otherwise.
     */
    public boolean isAuthorizedByClub(User user, UUID clubId, RoleEnum minimumRole) {
        Optional<Club> club = clubRepository.findById(clubId);
        if (club.isPresent()) {
            RoleEnum clubRole = findClubUserRole(user, club.get().getId());
            if (clubRole != null && RoleEnum.isEqualsOrHigher(minimumRole, clubRole)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Resolves the specific role of a user within a team context.
     * This considers the club hierarchy (e.g., club admins inherit permissions in the team).
     *
     * @param user   The user to evaluate.
     * @param teamId The unique identifier of the team.
     * @return The highest {@link RoleEnum} the user holds in the team context,
     * or {@code null} if no relationship exists.
     */
    public RoleEnum findTeamUserRole(User user, UUID teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        if (team.isPresent()) {
            Optional<Club> club = clubRepository.findClubByTeam(teamId);
            if (club.isPresent()) {
                RoleEnum role = findClubUserRole(user, club.get().getId());
                if (role != null) {
                    return role;
                }
            }

            if (team.get().getTrainer() != null && team.get().getTrainer().equals(user.getId())) {
                return logAndReturnRole(user, team.get(), club.get(), RoleEnum.TRAINER);
            }

            if (team.get().getSecondTrainer() != null && team.get().getSecondTrainer().equals(user.getId())) {
                return logAndReturnRole(user, team.get(), club.get(), RoleEnum.SECOND_TRAINER);
            }

            if (team.get().getScouters() != null && team.get().getScouters().stream().anyMatch(s -> s.equals(user.getId()))) {
                return logAndReturnRole(user, team.get(), club.get(), RoleEnum.SCOUTER);
            }
        }
        return null;
    }

    /**
     * Resolves the role of a user within a specific club.
     *
     * @param user   The user to evaluate.
     * @param clubId The unique identifier of the club.
     * @return {@link RoleEnum#ADMIN} if the user is in the club's admin list,
     * or {@code null} otherwise.
     */
    public RoleEnum findClubUserRole(User user, UUID clubId) {
        Optional<Club> club = clubRepository.findById(clubId);
        if (club.isPresent()) {
            boolean clubHasUser = club.get().getAdminUserIds()
                    .stream()
                    .anyMatch(t -> t.equals(user.getId()));
            if (clubHasUser) {
                return logAndReturnRole(user, club.get(), RoleEnum.ADMIN);
            }
        }
        return null;
    }

    /**
     * Utility to log the role resolution for a team and return the role.
     */
    private RoleEnum logAndReturnRole(User user, Team team, Club club, RoleEnum role) {
        logger.info("[AUTH] User '{}' has role {} in team {}, of club '{}'", user.getUsername(), role, team.getName(), club.getName());
        return role;
    }

    /**
     * Utility to log the role resolution for a club and return the role.
     */
    private RoleEnum logAndReturnRole(User user, Club club, RoleEnum role) {
        logger.info("[AUTH] User '{}' has role {} in club {}", user.getUsername(), role, club.getName());
        return role;
    }
}
