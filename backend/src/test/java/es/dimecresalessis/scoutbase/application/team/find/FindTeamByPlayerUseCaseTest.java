package es.dimecresalessis.scoutbase.application.team.find;

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
class FindTeamByPlayerUseCaseTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private FindTeamByPlayerUseCase findTeamByPlayerUseCase;

    private UUID playerId;
    private UUID teamId;
    private Team team;

    @BeforeEach
    void setUp() {
        playerId = UUID.randomUUID();
        teamId = UUID.randomUUID();
        team = Team.builder()
                .id(teamId)
                .name("Scout Team")
                .build();
    }

    @Test
    void execute_ShouldReturnTeam_WhenPlayerIsFoundInATeam() {
        when(teamRepository.findByPlayerId(playerId)).thenReturn(Optional.of(team));

        Team result = findTeamByPlayerUseCase.execute(playerId);

        assertNotNull(result);
        assertEquals(teamId, result.getId());
        verify(teamRepository).findByPlayerId(playerId);
    }

    @Test
    void execute_ShouldReturnNull_WhenPlayerIsNotInAnyTeam() {
        when(teamRepository.findByPlayerId(playerId)).thenReturn(Optional.empty());

        Team result = findTeamByPlayerUseCase.execute(playerId);

        assertNull(result);
        verify(teamRepository).findByPlayerId(playerId);
    }
}