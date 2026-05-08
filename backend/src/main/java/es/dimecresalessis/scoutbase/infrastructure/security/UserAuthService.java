package es.dimecresalessis.scoutbase.infrastructure.security;

import es.dimecresalessis.scoutbase.application.user.find.FindUserRoleInClubUseCase;
import es.dimecresalessis.scoutbase.application.user.find.FindUserRoleInTeamUseCase;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import lombok.AllArgsConstructor;
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

    private final UserClubRepository userClubRepository;
    private final FindUserRoleInTeamUseCase findUserRoleInTeamUseCase;
    private final FindUserRoleInClubUseCase  findUserRoleInClubUseCase;

    public void hasMinimumTeamAuthorization(UUID teamId, RoleEnum minRole) {
        if (!isAuthorizedByTeam(teamId, minRole)) {
            throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, minRole.name());
        }
    }

    public void hasMinimumClubAuthorization(UUID clubId, RoleEnum minRole) {
        if (!isAuthorizedByClub(clubId, minRole)) {
            throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, minRole.name());
        }
    }

    public void hasSuperadminAuthorization() {
        User user = Session.getSessionUser();
        if (!user.isSuperAdmin()) {
            throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, RoleEnum.SUPERADMIN.name());
        }
    }

    public boolean isOwnUser(UUID userId) {
        return Session.getSessionUser().getId().equals(userId);
    }

    /**
     * Checks if a user is authorized for a specific userteam given a minimum required role.
     * Authorization is granted if the user holds the required role either at the
     * owning userclub level or directly within the userteam.
     *
     * @param teamId The unique identifier of the userteam.
     * @param minRole The minimum role level required to perform the action.
     * @return {@code true} if the user meets the role requirements; {@code false} otherwise.
     */
    public boolean isAuthorizedByTeam(UUID teamId, RoleEnum minRole) {
        User user = Session.getSessionUser();
        if (isSuperadmin()) {
            return true;
        }
        Optional<UserClub> club = userClubRepository.findUserClubByTeam(teamId);
        if (club.isPresent()) {
            RoleEnum clubRole = findUserRoleInClubUseCase.execute(user, club.get().getId());
            if (clubRole != null && RoleEnum.isEqualsOrHigher(clubRole, minRole)) {
                return true;
            }
        }
        RoleEnum teamRole = findUserRoleInTeamUseCase.execute(user, teamId);
        if (teamRole != null && RoleEnum.isEqualsOrHigher(teamRole, minRole)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if a user is authorized for actions at the userclub level.
     *
     * @param clubId The unique identifier of the userclub.
     * @param minRole The minimum role level required to perform the action.
     * @return {@code true} if the user holds a sufficient role in the userclub; {@code false} otherwise.
     */
    public boolean isAuthorizedByClub(UUID clubId, RoleEnum minRole) {
        User user = Session.getSessionUser();
        if (isSuperadmin()) {
            return true;
        }
        Optional<UserClub> club = userClubRepository.findUserClubById(clubId);
        if (club.isPresent()) {
            RoleEnum userRole = findUserRoleInClubUseCase.execute(user, club.get().getId());
            if (userRole != null && RoleEnum.isEqualsOrHigher(minRole, userRole)) {
                return true;
            }
        }
        return false;
    }

    public boolean isSuperadmin() {
        User user = Session.getSessionUser();
        return user.isSuperAdmin();
    }
}
