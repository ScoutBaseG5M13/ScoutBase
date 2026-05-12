package es.dimecresalessis.scoutbase.application.player.find;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.team.exception.TeamException;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllPlayersByTeamIdUseCaseTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private FindPlayerByIdUseCase findPlayerByIdUseCase;

    @InjectMocks
    private FindAllPlayersByTeamIdUseCase findAllPlayersByTeamIdUseCase;

    @Test
    void execute_ShouldReturnPlayers_WhenTeamExistsWithPlayers() {
        UUID teamId = UUID.randomUUID();
        UUID playerId1 = UUID.randomUUID();
        UUID playerId2 = UUID.randomUUID();
        Team team = Team.builder()
                .id(teamId)
                .players(List.of(playerId1, playerId2))
                .build();
        Player player1 = Player.builder().id(playerId1).build();
        Player player2 = Player.builder().id(playerId2).build();

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(findPlayerByIdUseCase.execute(playerId1)).thenReturn(player1);
        when(findPlayerByIdUseCase.execute(playerId2)).thenReturn(player2);

        List<Player> result = findAllPlayersByTeamIdUseCase.execute(teamId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(findPlayerByIdUseCase, times(2)).execute(any(UUID.class));
    }

    @Test
    void execute_ShouldThrowTeamException_WhenTeamDoesNotExist() {
        UUID teamId = UUID.randomUUID();
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        TeamException exception = assertThrows(TeamException.class, () ->
                findAllPlayersByTeamIdUseCase.execute(teamId)
        );

        assertEquals(ErrorEnum.TEAM_NOT_FOUND, exception.getErrorEnum());
        verify(findPlayerByIdUseCase, never()).execute(any());
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenTeamHasNoPlayers() {
        UUID teamId = UUID.randomUUID();
        Team team = Team.builder()
                .id(teamId)
                .players(Collections.emptyList())
                .build();

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        List<Player> result = findAllPlayersByTeamIdUseCase.execute(teamId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(findPlayerByIdUseCase, never()).execute(any());
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenPlayersListIsNull() {
        UUID teamId = UUID.randomUUID();
        Team team = Team.builder()
                .id(teamId)
                .players(null)
                .build();

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        List<Player> result = findAllPlayersByTeamIdUseCase.execute(teamId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}