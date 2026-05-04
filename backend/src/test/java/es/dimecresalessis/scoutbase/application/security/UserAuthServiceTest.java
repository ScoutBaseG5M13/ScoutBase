package es.dimecresalessis.scoutbase.application.security;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.security.UserAuthService;
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
    private UserTeamRepository userTeamRepository;

    @Mock
    private UserClubRepository userClubRepository;

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
        UserClub userClub = mock(UserClub.class);
        when(userClub.getId()).thenReturn(clubId);
        when(userClub.getAdminUserIds()).thenReturn(List.of(userId));

        when(userClubRepository.findUserClubByTeam(teamId)).thenReturn(Optional.of(userClub));
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.of(userClub));

        boolean authorized = userAuthService.isAuthorizedByTeam(teamId, RoleEnum.TRAINER);

        assertTrue(authorized);
    }

    @Test
    void shouldAuthorizeByTeamWhenUserIsTrainer() {
        when(user.getId()).thenReturn(userId);
        UserTeam userTeam = mock(UserTeam.class);
        UserClub userClub = mock(UserClub.class);

        when(userTeam.getTrainer()).thenReturn(userId);
        when(userTeam.getName()).thenReturn("Test Team");
        when(userClub.getName()).thenReturn("Test Club");
        when(userClub.getId()).thenReturn(clubId);
        when(userClub.getAdminUserIds()).thenReturn(Collections.emptyList());

        when(userClubRepository.findUserClubByTeam(teamId)).thenReturn(Optional.of(userClub));
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.of(userTeam));
        when(userClubRepository.findUserClubById(clubId)).thenReturn(Optional.of(userClub));

        boolean authorized = userAuthService.isAuthorizedByTeam(teamId, RoleEnum.TRAINER);

        assertTrue(authorized);
    }
}