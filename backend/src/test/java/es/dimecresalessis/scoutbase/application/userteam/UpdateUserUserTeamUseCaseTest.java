package es.dimecresalessis.scoutbase.application.userteam;

import es.dimecresalessis.scoutbase.application.userteam.update.UpdateUserTeamUseCase;
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
class UpdateUserUserTeamUseCaseTest {

    @Mock
    private UserTeamRepository userTeamRepository;

    @InjectMocks
    private UpdateUserTeamUseCase updateUserTeamUseCase;

    private UUID teamId;
    private UserTeam userTeam;

    @BeforeEach
    void setUp() {
        teamId = UUID.randomUUID();
        userTeam = UserTeam.builder().id(teamId).name("Updated Name").build();
    }

    @Test
    void execute_ShouldUpdateSuccessfully() {
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.of(userTeam));

        UserTeam result = updateUserTeamUseCase.execute(userTeam, teamId);

        assertNotNull(result);
        verify(userTeamRepository, times(2)).findById(teamId);
        verify(userTeamRepository).save(userTeam);
    }

    @Test
    void execute_ShouldThrowException_WhenTeamNotFound() {
        when(userTeamRepository.findById(teamId)).thenReturn(Optional.empty());

        UserTeamException exception = assertThrows(UserTeamException.class, () ->
                updateUserTeamUseCase.execute(userTeam, teamId)
        );
        assertEquals(ErrorEnum.TEAM_NOT_FOUND, exception.getErrorEnum());
    }

    @Test
    void execute_ShouldThrowException_WhenIdsMismatch() {
        UUID pathId = UUID.randomUUID();
        UserTeam pathUserTeam = UserTeam.builder().id(pathId).build();

        when(userTeamRepository.findById(teamId)).thenReturn(Optional.of(userTeam));
        when(userTeamRepository.findById(pathId)).thenReturn(Optional.of(pathUserTeam));

        assertThrows(IllegalArgumentException.class, () ->
                updateUserTeamUseCase.execute(userTeam, pathId)
        );
    }
}