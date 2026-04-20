package es.dimecresalessis.scoutbase.application.team;

import es.dimecresalessis.scoutbase.application.team.delete.DeleteTeamUseCase;
import es.dimecresalessis.scoutbase.domain.club.repository.ClubRepository;
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

    @Mock
    private ClubRepository clubRepository;

    @InjectMocks
    private DeleteTeamUseCase deleteTeamUseCase;

    @Test
    void execute_ShouldDelete_WhenIdExists() {
        UUID id = UUID.randomUUID();
        when(teamRepository.findById(id)).thenReturn(Optional.of(new Team()));
        when(clubRepository.findClubByTeam(id)).thenReturn(Optional.empty());

        boolean result = deleteTeamUseCase.execute(id);

        assertTrue(result);
        verify(teamRepository).deleteById(id);
    }

    @Test
    void execute_ShouldThrowException_WhenIdNotFound() {
        UUID id = UUID.randomUUID();
        when(teamRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> deleteTeamUseCase.execute(id));

        verify(teamRepository, never()).deleteById(any());
        verify(clubRepository, never()).findClubByTeam(any());
    }
}
