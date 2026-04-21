package es.dimecresalessis.scoutbase.application.team;

import es.dimecresalessis.scoutbase.application.team.delete.DeleteTeamUseCase;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
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
class DeleteTeamUseCaseTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private DeleteTeamUseCase deleteTeamUseCase;

    @Test
    void execute_ShouldDelete_WhenIdExists() {
        UUID id = UUID.randomUUID();
        when(teamRepository.findById(id)).thenReturn(Optional.of(new Team()));

        boolean result = deleteTeamUseCase.execute(id);

        assertTrue(result);
        verify(teamRepository).deleteById(id);
    }

    @Test
    void execute_ShouldThrowException_WhenIdNotFound() {
        UUID id = UUID.randomUUID();
        when(teamRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> deleteTeamUseCase.execute(id));
    }
}