package es.dimecresalessis.scoutbase.application.userteam.create;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import es.dimecresalessis.scoutbase.domain.userteam.exception.UserTeamException;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserTeamUseCaseTest {

    @Mock
    private UserTeamRepository userTeamRepository;

    @Mock
    private UserClubRepository userClubRepository;

    @InjectMocks
    private CreateUserTeamUseCase createUserTeamUseCase;

    private UUID teamId;
    private UserTeam userTeam;
    private UserClub userClub;

    @BeforeEach
    void setUp() {
        teamId = UUID.randomUUID();
        userTeam = UserTeam.builder()
                .id(teamId)
                .name("Cadete A")
                .build();

        userClub = UserClub.builder()
                .id(UUID.randomUUID())
                .name("Scout Club")
                .userTeams(new ArrayList<>())
                .build();
    }

    @Test
    void execute_ShouldCreateTeamAndLinkToClub_WhenDataIsValid() {
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.empty());

        UserTeam result = createUserTeamUseCase.execute(userTeam, userClub);

        assertNotNull(result);
        assertEquals(teamId, result.getId());
        assertTrue(userClub.getUserTeams().contains(teamId));

        verify(userTeamRepository).save(userTeam);
        verify(userClubRepository).save(userClub);
    }

    @Test
    void execute_ShouldInitializeListAndLinkToClub_WhenClubTeamsListIsNull() {
        userClub.setUserTeams(null);
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.empty());

        createUserTeamUseCase.execute(userTeam, userClub);

        assertNotNull(userClub.getUserTeams());
        assertEquals(1, userClub.getUserTeams().size());
        verify(userClubRepository).save(userClub);
    }

    @Test
    void execute_ShouldThrowException_WhenUserTeamIsNull() {
        UserTeamException exception = assertThrows(UserTeamException.class, () ->
                createUserTeamUseCase.execute(null, userClub)
        );

        assertEquals(ErrorEnum.USER_TEAM_IS_NULL, exception.getErrorEnum());
        verifyNoInteractions(userTeamRepository);
    }

    @Test
    void execute_ShouldThrowException_WhenIdIsNull() {
        userTeam.setId(null);

        UserTeamException exception = assertThrows(UserTeamException.class, () ->
                createUserTeamUseCase.execute(userTeam, userClub)
        );

        assertEquals(ErrorEnum.USER_TEAM_ID_IS_NULL, exception.getErrorEnum());
        verify(userTeamRepository, never()).save(any());
    }

    @Test
    void execute_ShouldThrowException_WhenTeamAlreadyExists() {
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.of(userTeam));

        UserTeamException exception = assertThrows(UserTeamException.class, () ->
                createUserTeamUseCase.execute(userTeam, userClub)
        );

        assertEquals(ErrorEnum.USER_TEAM_ALREADY_EXISTS, exception.getErrorEnum());
        verify(userTeamRepository, never()).save(any());
        verifyNoInteractions(userClubRepository);
    }
}