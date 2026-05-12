package es.dimecresalessis.scoutbase.application.user.find;

import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserRoleInClubUseCaseTest {

    @Mock
    private UserClubRepository userClubRepository;

    @Mock
    private FindUserRoleInTeamUseCase findUserRoleInTeamUseCase;

    @InjectMocks
    private FindUserRoleInClubUseCase findUserRoleInClubUseCase;

    private User user;
    private UUID clubId;
    private UserClub userClub;

    @BeforeEach
    void setUp() {
        user = User.builder().id(UUID.randomUUID()).username("testuser").build();
        clubId = UUID.randomUUID();
        userClub = UserClub.builder()
                .id(clubId)
                .name("Test Club")
                .adminUserIds(List.of())
                .userTeams(List.of())
                .build();
    }

    @Test
    void execute_ShouldReturnAdmin_WhenUserIsInAdminList() {
        userClub.setAdminUserIds(List.of(user.getId()));
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.of(userClub));

        RoleEnum result = findUserRoleInClubUseCase.execute(user, clubId);

        assertEquals(RoleEnum.ADMIN, result);
        verify(findUserRoleInTeamUseCase, never()).execute(any(), any());
    }

    @Test
    void execute_ShouldReturnMaxRoleFromTeams_WhenUserIsNotAdmin() {
        UUID teamId1 = UUID.randomUUID();
        UUID teamId2 = UUID.randomUUID();
        userClub.setAdminUserIds(List.of(UUID.randomUUID()));
        userClub.setUserTeams(List.of(teamId1, teamId2));

        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.of(userClub));
        when(findUserRoleInTeamUseCase.execute(user, teamId1)).thenReturn(RoleEnum.SCOUTER);
        when(findUserRoleInTeamUseCase.execute(user, teamId2)).thenReturn(RoleEnum.TRAINER);

        RoleEnum result = findUserRoleInClubUseCase.execute(user, clubId);

        assertEquals(RoleEnum.TRAINER, result);
    }

    @Test
    void execute_ShouldReturnNull_WhenClubNotFound() {
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.empty());

        RoleEnum result = findUserRoleInClubUseCase.execute(user, clubId);

        assertNull(result);
    }

    @Test
    void execute_ShouldReturnNull_WhenUserHasNoRoleInTeams() {
        UUID teamId = UUID.randomUUID();
        userClub.setAdminUserIds(List.of(UUID.randomUUID()));
        userClub.setUserTeams(List.of(teamId));

        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.of(userClub));
        when(findUserRoleInTeamUseCase.execute(user, teamId)).thenReturn(null);

        RoleEnum result = findUserRoleInClubUseCase.execute(user, clubId);

        assertNull(result);
    }

    @Test
    void execute_ShouldReturnCorrectRole_WhenOnlyOneTeamHasRole() {
        UUID teamId1 = UUID.randomUUID();
        UUID teamId2 = UUID.randomUUID();
        userClub.setUserTeams(List.of(teamId1, teamId2));

        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.of(userClub));
        when(findUserRoleInTeamUseCase.execute(user, teamId1)).thenReturn(null);
        when(findUserRoleInTeamUseCase.execute(user, teamId2)).thenReturn(RoleEnum.SECOND_TRAINER);

        RoleEnum result = findUserRoleInClubUseCase.execute(user, clubId);

        assertEquals(RoleEnum.SECOND_TRAINER, result);
    }
}