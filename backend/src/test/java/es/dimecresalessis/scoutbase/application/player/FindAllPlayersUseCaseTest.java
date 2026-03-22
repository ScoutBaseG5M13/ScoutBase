package es.dimecresalessis.scoutbase.application.player;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllPlayersUseCaseTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private FindAllPlayersUseCase findAllPlayersUseCase;

    @Test
    @DisplayName("Should return a list of players")
    void shouldFindAllPlayers() {
        Player player1 = Player.builder().id(UUID.randomUUID()).name("Ronald McGallahan").build();
        Player player2 = Player.builder().id(UUID.randomUUID()).name("Aitana Bonmatí").build();
        List<Player> expectedPlayers = List.of(player1, player2);

        when(playerRepository.findAll()).thenReturn(expectedPlayers);

        List<Player> result = findAllPlayersUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Ronald McGallahan", result.getFirst().getName());
        verify(playerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no players exist")
    void shouldReturnEmptyList_WhenNoPlayersExist() {
        when(playerRepository.findAll()).thenReturn(List.of());

        List<Player> result = findAllPlayersUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(playerRepository, times(1)).findAll();
    }
}