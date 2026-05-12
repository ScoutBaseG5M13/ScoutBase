package es.dimecresalessis.scoutbase.application.userteam.delete;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.userteam.exception.UserTeamException;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
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
class RemoveSecondTrainerUseCaseTest {

    @Mock
    private UserTeamRepository userTeamRepository;

    @InjectMocks
    private RemoveSecondTrainerUseCase removeSecondTrainerUseCase;

    @Test
    void execute_ShouldRemoveSecondTrainer_WhenDataIsValid() {
        UUID teamId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UserTeam userTeam = UserTeam.builder()
                .id(teamId)
                .secondTrainer(userId)
                .build();

        when(userTeamRepository.findById(teamId)).thenReturn(Optional.of(userTeam));

        boolean result = removeSecondTrainerUseCase.execute(teamId, userId);

        assertTrue(result);
        assertNull(userTeam.getSecondTrainer());
        verify(userTeamRepository).save(userTeam);
    }

    @Test
    void execute_ShouldThrowException_WhenTeamIdIsNull() {
        UUID userId = UUID.randomUUID();

        UserTeamException exception = assertThrows(UserTeamException.class, () ->
                removeSecondTrainerUseCase.execute(null, userId)
        );

        assertEquals(ErrorEnum.USER_TEAM_ID_IS_NULL, exception.getErrorEnum());
        verifyNoInteractions(userTeamRepository);
    }

    @Test
    void execute_ShouldThrowException_WhenUserIdIsNull() {
        UUID teamId = UUID.randomUUID();

        UserTeamException exception = assertThrows(UserTeamException.class, () ->
                removeSecondTrainerUseCase.execute(teamId, null)
        );

        assertEquals(ErrorEnum.USER_ID_IS_NULL, exception.getErrorEnum());
        verifyNoInteractions(userTeamRepository);
    }

    @Test
    void execute_ShouldThrowException_WhenTeamNotFound() {
        UUID teamId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        when(userTeamRepository.findById(teamId)).thenReturn(Optional.empty());

        UserTeamException exception = assertThrows(UserTeamException.class, () ->
                removeSecondTrainerUseCase.execute(teamId, userId)
        );

        assertEquals(ErrorEnum.USER_TEAM_NOT_FOUND, exception.getErrorEnum());
        verify(userTeamRepository, never()).save(any());
    }
}