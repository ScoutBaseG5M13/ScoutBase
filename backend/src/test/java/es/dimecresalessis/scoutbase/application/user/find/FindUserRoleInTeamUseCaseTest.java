package es.dimecresalessis.scoutbase.application.user.find;

import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserRoleInTeamUseCaseTest {

    @Mock
    private UserClubRepository userClubRepository;

    @Mock
    private UserTeamRepository userTeamRepository;

    @InjectMocks
    private FindUserRoleInTeamUseCase findUserRoleInTeamUseCase;

    private User user;
    private UUID teamId;
    private UserTeam userTeam;

    @BeforeEach
    void setUp() {
        user = User.builder().id(UUID.randomUUID()).username("testuser").build();
        teamId = UUID.randomUUID();
        userTeam = UserTeam.builder()
                .id(teamId)
                .name("Test Team")
                .scouters(List.of())
                .build();
    }

    @Test
    void execute_ShouldReturnTrainer_WhenUserIsTrainer() {
        userTeam.setTrainer(user.getId());
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.of(userTeam));

        RoleEnum result = findUserRoleInTeamUseCase.execute(user, teamId);

        assertEquals(RoleEnum.TRAINER, result);
    }

    @Test
    void execute_ShouldReturnSecondTrainer_WhenUserIsSecondTrainer() {
        userTeam.setSecondTrainer(user.getId());
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.of(userTeam));

        RoleEnum result = findUserRoleInTeamUseCase.execute(user, teamId);

        assertEquals(RoleEnum.SECOND_TRAINER, result);
    }

    @Test
    void execute_ShouldReturnScouter_WhenUserIsInScoutersList() {
        userTeam.setScouters(List.of(user.getId()));
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.of(userTeam));

        RoleEnum result = findUserRoleInTeamUseCase.execute(user, teamId);

        assertEquals(RoleEnum.SCOUTER, result);
    }

    @Test
    void execute_ShouldReturnNull_WhenTeamDoesNotExist() {
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.empty());

        RoleEnum result = findUserRoleInTeamUseCase.execute(user, teamId);

        assertNull(result);
    }

    @Test
    void execute_ShouldReturnNull_WhenUserHasNoRoleInTeam() {
        userTeam.setTrainer(UUID.randomUUID());
        userTeam.setSecondTrainer(UUID.randomUUID());
        userTeam.setScouters(List.of(UUID.randomUUID()));
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.of(userTeam));

        RoleEnum result = findUserRoleInTeamUseCase.execute(user, teamId);

        assertNull(result);
    }
}