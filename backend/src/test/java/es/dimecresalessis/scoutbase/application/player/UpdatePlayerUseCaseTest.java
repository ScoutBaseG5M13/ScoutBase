package es.dimecresalessis.scoutbase.application.player;

import es.dimecresalessis.scoutbase.application.player.update.UpdatePlayerUseCase;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.player.exception.PlayerException;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
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
class UpdatePlayerUseCaseTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private UpdatePlayerUseCase updatePlayerUseCase;

    private UUID playerId;
    private Player player;

    @BeforeEach
    void setUp() {
        playerId = UUID.randomUUID();
        player = Player.builder().id(playerId).build();
    }

    @Test
    void execute_ShouldUpdate_WhenIdsMatch() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        Player result = updatePlayerUseCase.execute(player, playerId);

        assertNotNull(result);
        verify(playerRepository, times(2)).findById(playerId);
        verify(playerRepository).save(player);
    }

    @Test
    void execute_ShouldThrowException_WhenIdMismatch() {
        UUID otherId = UUID.randomUUID();
        Player otherPlayer = Player.builder().id(otherId).build();

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(playerRepository.findById(otherId)).thenReturn(Optional.of(otherPlayer));

        assertThrows(IllegalArgumentException.class, () -> updatePlayerUseCase.execute(player, otherId));
    }

    @Test
    void execute_ShouldThrowPlayerException_WhenNotFound() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        PlayerException exception = assertThrows(PlayerException.class, () ->
                updatePlayerUseCase.execute(player, playerId)
        );
        assertEquals(ErrorEnum.PLAYER_NOT_FOUND, exception.getErrorEnum());
    }
}