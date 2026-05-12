package es.dimecresalessis.scoutbase.application.user.find;

import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllUsersByRoleUseCaseTest {

    @Mock
    private UserTeamRepository userTeamRepository;

    @Mock
    private FindUserByIdUseCase findUserByIdUseCase;

    @Mock
    private UserClubRepository userClubRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FindAllUsersByRoleUseCase findAllUsersByRoleUseCase;

    @Test
    void execute_ShouldReturnSuperAdmins_WhenRoleIsSuperAdmin() {
        UUID adminId = UUID.randomUUID();
        UserClub userClub = UserClub.builder()
                .adminUserIds(List.of(adminId))
                .build();
        User superAdmin = User.builder()
                .id(adminId)
                .superAdmin(true)
                .build();

        when(userClubRepository.findAll()).thenReturn(List.of(userClub));
        when(userTeamRepository.findAll()).thenReturn(Collections.emptyList());
        when(userRepository.findById(adminId)).thenReturn(Optional.of(superAdmin));

        List<User> result = findAllUsersByRoleUseCase.execute(RoleEnum.SUPERADMIN);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(adminId, result.get(0).getId());
    }

    @Test
    void execute_ShouldReturnAdmins_WhenRoleIsAdmin() {
        UUID adminId = UUID.randomUUID();
        UserClub userClub = UserClub.builder()
                .adminUserIds(List.of(adminId))
                .build();
        User adminUser = User.builder().id(adminId).build();

        when(userClubRepository.findAll()).thenReturn(List.of(userClub));
        when(userTeamRepository.findAll()).thenReturn(Collections.emptyList());
        when(findUserByIdUseCase.execute(adminId)).thenReturn(adminUser);

        List<User> result = findAllUsersByRoleUseCase.execute(RoleEnum.ADMIN);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(findUserByIdUseCase).execute(adminId);
    }

    @Test
    void execute_ShouldReturnTrainers_WhenRoleIsTrainer() {
        UUID trainerId = UUID.randomUUID();
        UserTeam userTeam = UserTeam.builder()
                .trainer(trainerId)
                .build();
        User trainerUser = User.builder().id(trainerId).build();

        when(userClubRepository.findAll()).thenReturn(Collections.emptyList());
        when(userTeamRepository.findAll()).thenReturn(List.of(userTeam));
        when(findUserByIdUseCase.execute(trainerId)).thenReturn(trainerUser);

        List<User> result = findAllUsersByRoleUseCase.execute(RoleEnum.TRAINER);

        assertEquals(1, result.size());
        assertEquals(trainerId, result.get(0).getId());
    }

    @Test
    void execute_ShouldReturnScouters_WhenRoleIsScouter() {
        UUID scouterId = UUID.randomUUID();
        UserTeam userTeam = UserTeam.builder()
                .scouters(List.of(scouterId))
                .build();
        User scouterUser = User.builder().id(scouterId).build();

        when(userClubRepository.findAll()).thenReturn(Collections.emptyList());
        when(userTeamRepository.findAll()).thenReturn(List.of(userTeam));
        when(findUserByIdUseCase.execute(scouterId)).thenReturn(scouterUser);

        List<User> result = findAllUsersByRoleUseCase.execute(RoleEnum.SCOUTER);

        assertEquals(1, result.size());
        assertEquals(scouterId, result.get(0).getId());
    }

    @Test
    void execute_ShouldReturnEmpty_WhenNoMatchesFound() {
        when(userClubRepository.findAll()).thenReturn(Collections.emptyList());
        when(userTeamRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = findAllUsersByRoleUseCase.execute(RoleEnum.TRAINER);

        assertNotNull(result);
        assertEquals(0, result.size());
    }
}