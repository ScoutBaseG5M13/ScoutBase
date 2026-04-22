package es.dimecresalessis.scoutbase.application.security;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private UserAuthService userAuthService;

    private User user;
    private UUID userId;
    private UUID teamId;
    private UUID clubId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        teamId = UUID.randomUUID();
        clubId = UUID.randomUUID();
        user = mock(User.class);
    }

    @Test
    void shouldAuthorizeByTeamWhenUserIsClubAdmin() {
        when(user.getId()).thenReturn(userId);
        Club club = mock(Club.class);
        when(club.getId()).thenReturn(clubId);
        when(club.getAdminUserIds()).thenReturn(List.of(userId));

        when(clubRepository.findClubByTeam(teamId)).thenReturn(Optional.of(club));
        when(clubRepository.findById(clubId)).thenReturn(Optional.of(club));

        boolean authorized = userAuthService.isAuthorizedByTeam(user, teamId, RoleEnum.TRAINER);

        assertTrue(authorized);
    }

    @Test
    void shouldAuthorizeByTeamWhenUserIsTrainer() {
        when(user.getId()).thenReturn(userId);
        Team team = mock(Team.class);
        Club club = mock(Club.class);

        when(team.getTrainer()).thenReturn(userId);
        when(team.getName()).thenReturn("Test Team");
        when(club.getName()).thenReturn("Test Club");
        when(club.getId()).thenReturn(clubId);
        when(club.getAdminUserIds()).thenReturn(Collections.emptyList());

        when(clubRepository.findClubByTeam(teamId)).thenReturn(Optional.of(club));
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(clubRepository.findById(clubId)).thenReturn(Optional.of(club));

        boolean authorized = userAuthService.isAuthorizedByTeam(user, teamId, RoleEnum.TRAINER);

        assertTrue(authorized);
    }

    @Test
    void shouldFindClubUserRoleAsAdmin() {
        when(user.getId()).thenReturn(userId);
        Club club = mock(Club.class);
        when(club.getAdminUserIds()).thenReturn(List.of(userId));
        when(club.getName()).thenReturn("Test Club");
        when(clubRepository.findById(clubId)).thenReturn(Optional.of(club));

        RoleEnum role = userAuthService.findClubUserRole(user, clubId);

        assertEquals(RoleEnum.ADMIN, role);
    }

    @Test
    void shouldReturnNullWhenUserIsNotInClub() {
        when(user.getId()).thenReturn(userId);
        Club club = mock(Club.class);
        when(club.getAdminUserIds()).thenReturn(List.of(UUID.randomUUID()));
        when(clubRepository.findById(clubId)).thenReturn(Optional.of(club));

        RoleEnum role = userAuthService.findClubUserRole(user, clubId);

        assertNull(role);
    }

    @Test
    void shouldReturnNullWhenTeamDoesNotExist() {
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        RoleEnum role = userAuthService.findTeamUserRole(user, teamId);

        assertNull(role);
    }
}