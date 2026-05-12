package es.dimecresalessis.scoutbase.application.userteam.update;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.userteam.exception.UserTeamException;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserTeamUseCaseTest {

    @Mock
    private UserTeamRepository userTeamRepository;

    @InjectMocks
    private UpdateUserTeamUseCase updateUserTeamUseCase;

    private UUID teamId;
    private UserTeam userTeam;
    private UserTeam savedUserTeam;

    @BeforeEach
    void setUp() {
        teamId = UUID.randomUUID();
        userTeam = UserTeam.builder()
                .id(teamId)
                .name("Updated Team Name")
                .build();
        savedUserTeam = UserTeam.builder()
                .id(teamId)
                .name("Old Team Name")
                .build();
    }

    @Test
    void execute_ShouldUpdateAndReturnUserTeam_WhenValidRequest() {
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.of(savedUserTeam));

        UserTeam result = updateUserTeamUseCase.execute(userTeam, teamId);

        assertNotNull(result);
        assertEquals(teamId, result.getId());
        verify(userTeamRepository, times(3)).findById(teamId);
        verify(userTeamRepository).save(any(UserTeam.class));
    }

    @Test
    void execute_ShouldThrowIllegalArgumentException_WhenIdsMismatch() {
        UUID differentId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () ->
                updateUserTeamUseCase.execute(userTeam, differentId)
        );

        verify(userTeamRepository, never()).save(any());
    }

    @Test
    void execute_ShouldThrowUserTeamException_WhenTeamNotFound() {
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.empty());

        UserTeamException exception = assertThrows(UserTeamException.class, () ->
                updateUserTeamUseCase.execute(userTeam, teamId)
        );

        assertEquals(ErrorEnum.USER_TEAM_NOT_FOUND, exception.getErrorEnum());
        verify(userTeamRepository, never()).save(any());
    }

    @Test
    void execute_ShouldCallMatchWithObject_WhenTeamExists() {
        UserTeam incomingTeam = mock(UserTeam.class);
        when(incomingTeam.getId()).thenReturn(teamId);
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.of(savedUserTeam));

        updateUserTeamUseCase.execute(incomingTeam, teamId);

        verify(incomingTeam).matchWithObject(savedUserTeam);
        verify(userTeamRepository).save(incomingTeam);
    }
}