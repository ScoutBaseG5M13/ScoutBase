package es.dimecresalessis.scoutbase.application.player;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class FindPlayerByIdUseCaseTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private FindPlayerByIdUseCase findPlayerByIdUseCase;

    private UUID playerId;
    private Player player;

    @BeforeEach
    void setUp() {
        playerId = UUID.randomUUID();
        player = Player.builder()
                .id(playerId)
                .name("Ronald McGallahan")
                .build();
    }

    @Test
    @DisplayName("Should return player when ID exists")
    void shouldFindPlayerById() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        Player result = findPlayerByIdUseCase.execute(playerId);

        assertNotNull(result);
        assertEquals(playerId, result.getId());
        assertEquals("Ronald McGallahan", result.getName());
        verify(playerRepository, times(1)).findById(playerId);
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when player ID does not exist")
    void shouldThrowException_WhenPlayerNotFound() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            findPlayerByIdUseCase.execute(playerId);
        });

        verify(playerRepository, times(1)).findById(playerId);
    }
}