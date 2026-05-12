package es.dimecresalessis.scoutbase.application.team.update;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.team.exception.TeamException;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
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
class UpdateTeamUseCaseTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private UpdateTeamUseCase updateTeamUseCase;

    private UUID teamId;
    private Team team;
    private Team savedTeam;

    @BeforeEach
    void setUp() {
        teamId = UUID.randomUUID();
        team = Team.builder()
                .id(teamId)
                .name("Updated Team Name")
                .build();
        savedTeam = Team.builder()
                .id(teamId)
                .name("Original Team Name")
                .build();
    }

    @Test
    void execute_ShouldUpdateAndReturnTeam_WhenValidRequest() {
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(savedTeam));

        Team result = updateTeamUseCase.execute(team, teamId);

        assertNotNull(result);
        assertEquals(teamId, result.getId());
        verify(teamRepository, times(3)).findById(teamId);
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    void execute_ShouldThrowIllegalArgumentException_WhenIdsInBodyAndPathDoNotMatch() {
        UUID pathId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () ->
                updateTeamUseCase.execute(team, pathId)
        );

        verify(teamRepository, never()).save(any());
    }

    @Test
    void execute_ShouldThrowTeamException_WhenTeamDoesNotExistInRepository() {
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        TeamException exception = assertThrows(TeamException.class, () ->
                updateTeamUseCase.execute(team, teamId)
        );

        assertEquals(ErrorEnum.TEAM_NOT_FOUND, exception.getErrorEnum());
        verify(teamRepository, never()).save(any());
    }

    @Test
    void execute_ShouldCallMatchWithObject_WhenTeamIsFound() {
        Team incomingTeam = mock(Team.class);
        when(incomingTeam.getId()).thenReturn(teamId);
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(savedTeam));

        updateTeamUseCase.execute(incomingTeam, teamId);

        verify(incomingTeam).matchWithObject(savedTeam);
        verify(teamRepository).save(incomingTeam);
    }
}