package es.dimecresalessis.scoutbase.application.team;

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

    @BeforeEach
    void setUp() {
        teamId = UUID.randomUUID();
        team = Team.builder().id(teamId).name("Updated Name").build();
    }

    @Test
    void execute_ShouldUpdateSuccessfully() {
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        Team result = updateTeamUseCase.execute(team, teamId);

        assertNotNull(result);
        verify(teamRepository, times(2)).findById(teamId);
        verify(teamRepository).save(team);
    }

    @Test
    void execute_ShouldThrowException_WhenTeamNotFound() {
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        TeamException exception = assertThrows(TeamException.class, () ->
                updateTeamUseCase.execute(team, teamId)
        );
        assertEquals(ErrorEnum.TEAM_NOT_FOUND, exception.getErrorEnum());
    }

    @Test
    void execute_ShouldThrowException_WhenIdsMismatch() {
        UUID pathId = UUID.randomUUID();
        Team pathTeam = Team.builder().id(pathId).build();

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(teamRepository.findById(pathId)).thenReturn(Optional.of(pathTeam));

        assertThrows(IllegalArgumentException.class, () ->
                updateTeamUseCase.execute(team, pathId)
        );
    }
}