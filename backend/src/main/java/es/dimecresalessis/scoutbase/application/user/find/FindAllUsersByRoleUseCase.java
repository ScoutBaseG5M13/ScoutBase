package es.dimecresalessis.scoutbase.application.user.find;

import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Use case for finding all {@link User} by {@link RoleEnum}.
 */
@Service
@AllArgsConstructor
public class FindAllUsersByRoleUseCase {

    private final UserTeamRepository userTeamRepository;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final UserClubRepository userClubRepository;
    private final UserRepository userRepository;

    /**
     * Executes the retrieval of all {@link User} entities that match a specific {@link RoleEnum}.
     *
     * @param role The {@link RoleEnum} to filter the users by.
     * @return A {@link List} of {@link User} objects assigned to the specified role.
     */
    public List<User> execute(RoleEnum role) {
        List<UserClub> userClubs = userClubRepository.findAll();
        List<UserTeam> userTeams = userTeamRepository.findAll();
        List<User> users = new ArrayList<>();
        for (UserClub userClub : userClubs) {
            switch (role) {
                case SUPERADMIN -> {
                    if (userClub.getAdminUserIds() != null) {
                        for (UUID adminId : userClub.getAdminUserIds()) {
                            userRepository.findById(adminId).ifPresent(user -> {
                                if (user.isSuperAdmin()) {
                                    users.add(user);
                                }
                            });
                        }
                    }
                }
                case ADMIN -> {
                    if (userClub.getAdminUserIds() != null) {
                        for (UUID adminId : userClub.getAdminUserIds()) {
                            users.add(findUserByIdUseCase.execute(adminId));
                        }
                    }
                }
            }
        }
        for (UserTeam userTeam : userTeams) {
            switch (role) {
                case TRAINER:
                    if (userTeam.getTrainer() != null) {
                        users.add(findUserByIdUseCase.execute(userTeam.getTrainer()));
                    }
                    break;
                case SECOND_TRAINER:
                    if (userTeam.getSecondTrainer() != null) {
                        users.add(findUserByIdUseCase.execute(userTeam.getSecondTrainer()));
                    }
                case SCOUTER:
                    if (userTeam.getScouters() != null) {
                        for (UUID scoutId : userTeam.getScouters()) {
                            users.add(findUserByIdUseCase.execute(scoutId));
                        }
                    }
                    break;
            }
        }
        return users;
    }
}
