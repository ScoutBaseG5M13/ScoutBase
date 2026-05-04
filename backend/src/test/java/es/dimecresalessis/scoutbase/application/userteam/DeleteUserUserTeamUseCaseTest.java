package es.dimecresalessis.scoutbase.application.userteam;

import es.dimecresalessis.scoutbase.application.userteam.delete.DeleteUserTeamUseCase;
import es.dimecresalessis.scoutbase.domain.userclub.repository.UserClubRepository;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.domain.userteam.repository.UserTeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserUserTeamUseCaseTest {

    @Mock
    private UserTeamRepository userTeamRepository;

    @Mock
    private UserClubRepository userClubRepository;

    @InjectMocks
    private DeleteUserTeamUseCase deleteUserTeamUseCase;

    @Test
    void execute_ShouldDelete_WhenIdExists() {
        UUID id = UUID.randomUUID();
        when(userTeamRepository.findById(id)).thenReturn(Optional.of(new UserTeam()));
        when(userClubRepository.findUserClubByTeam(id)).thenReturn(Optional.empty());

        boolean result = deleteUserTeamUseCase.execute(id);

        assertTrue(result);
        verify(userTeamRepository).deleteById(id);
    }

    @Test
    void execute_ShouldThrowException_WhenIdNotFound() {
        UUID id = UUID.randomUUID();
        when(userTeamRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> deleteUserTeamUseCase.execute(id));

        verify(userTeamRepository, never()).deleteById(any());
        verify(userClubRepository, never()).findUserClubByTeam(any());
    }
}
