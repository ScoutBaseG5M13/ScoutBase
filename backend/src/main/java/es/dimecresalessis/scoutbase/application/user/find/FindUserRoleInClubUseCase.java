package es.dimecresalessis.scoutbase.application.user.find;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.Optional;
import java.util.UUID;

/**
 * Use case for finding the {@link Role} of a {@link User} in their {@link UserClub}.
 */
@Service
@AllArgsConstructor
public class FindUserRoleInClubUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindUserRoleInClubUseCase.class);
    private final UserClubRepository userClubRepository;
    private final FindUserRoleInTeamUseCase findUserRoleInTeamUseCase;

    /**
     * Resolves the role of a user within a specific userclub.
     *
     * @param user The user to evaluate.
     * @param clubId The unique identifier of the userclub.
     * @return {@link RoleEnum#ADMIN} if the user is in the userclub's admin list,
     * or {@code null} otherwise.
     */
    public RoleEnum execute(User user, UUID clubId) {
        if (user.isSuperAdmin()) {
            return RoleEnum.SUPERADMIN;
        }
        Optional<UserClub> userClub = userClubRepository.findUserClubById(clubId);
        if (userClub.isPresent()) {
            boolean clubHasUser = userClub.get().getAdminUserIds()
                    .stream()
                    .anyMatch(t -> t.equals(user.getId()));
            if (clubHasUser) {
                logger.info("[AUTH] User '{}' has ROLE '{}' in CLUB '{}'", user.getUsername(), RoleEnum.ADMIN, userClub.get().getName());
                return RoleEnum.ADMIN;
            } else {
                RoleEnum maxRole = null;
                for (UUID userTeamId : userClub.get().getUserTeams()) {
                    RoleEnum role = findUserRoleInTeamUseCase.execute(user, userTeamId);
                    if (maxRole == null) {
                        maxRole = role;
                    } else if (role != null && maxRole.getRoleAuthLevel() < role.getRoleAuthLevel()) {
                        maxRole = role;
                    }
                }
                if (maxRole != null) {
                    logger.info("[AUTH] User '{}' has ROLE '{}' in CLUB '{}'", user.getUsername(), maxRole, userClub.get().getName());
                    return maxRole;
                }
            }
        }
        return null;
    }
}
