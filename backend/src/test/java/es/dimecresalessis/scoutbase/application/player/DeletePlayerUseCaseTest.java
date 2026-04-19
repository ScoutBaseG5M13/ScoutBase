package es.dimecresalessis.scoutbase.application.player;

import es.dimecresalessis.scoutbase.application.player.delete.DeletePlayerUseCase;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
        playerId = UUID.randomUUID();
    }

    @Test
    void execute_ShouldDeletePlayer_WhenIdExists() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(new Player()));

        boolean result = deletePlayerUseCase.execute(playerId);

        assertTrue(result);
        verify(playerRepository).deleteById(playerId);
    }

    @Test
    void execute_ShouldThrowException_WhenIdDoesNotExist() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> deletePlayerUseCase.execute(playerId));
        verify(playerRepository, never()).deleteById(any());
    }
}