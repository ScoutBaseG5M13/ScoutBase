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
class DeletePlayerUseCaseTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private DeletePlayerUseCase deletePlayerUseCase;

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
    @DisplayName("Should delete player successfully")
    void shouldDeletePlayer() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        boolean result = deletePlayerUseCase.execute(playerId);

        assertTrue(result);
        verify(playerRepository, times(1)).findById(playerId);
        verify(playerRepository, times(1)).deleteById(playerId);
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when player does not exist")
    void shouldThrowException_WhenPlayerNotFound() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            deletePlayerUseCase.execute(playerId);
        });

        verify(playerRepository, times(1)).findById(playerId);
        verify(playerRepository, never()).deleteById(any());
    }
}