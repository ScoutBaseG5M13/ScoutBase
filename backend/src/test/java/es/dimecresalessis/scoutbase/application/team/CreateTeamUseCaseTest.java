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
class CreateTeamUseCaseTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private CreateTeamUseCase createTeamUseCase;

    private Team team;
    private UUID teamId;

    @BeforeEach
    void setUp() {
        teamId = UUID.randomUUID();
        team = Team.builder()
                .id(teamId)
                .name("Team A")
                .build();
    }

    @Test
    void execute_ShouldCreateTeamSuccessfully() {
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());
        when(teamRepository.save(team)).thenReturn(team);

        Team result = createTeamUseCase.execute(team);

        assertNotNull(result);
        assertEquals(teamId, result.getId());
        verify(teamRepository).save(team);
    }

    @Test
    void execute_ShouldThrowException_WhenTeamIsNull() {
        TeamException exception = assertThrows(TeamException.class, () ->
                createTeamUseCase.execute(null)
        );
        assertEquals(ErrorEnum.TEAM_IS_NULL, exception.getErrorEnum());
    }

    @Test
    void execute_ShouldThrowException_WhenTeamIdIsNull() {
        Team teamNoId = new Team();
        teamNoId.setId(null);

        TeamException exception = assertThrows(TeamException.class, () ->
                createTeamUseCase.execute(teamNoId)
        );
        assertEquals(ErrorEnum.TEAM_ID_IS_NULL, exception.getErrorEnum());
    }

    @Test
    void execute_ShouldThrowException_WhenTeamAlreadyExists() {
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        TeamException exception = assertThrows(TeamException.class, () ->
                createTeamUseCase.execute(team)
        );
        assertEquals(ErrorEnum.TEAM_ALREADY_EXISTS, exception.getErrorEnum());
        verify(teamRepository, never()).save(any());
    }
}