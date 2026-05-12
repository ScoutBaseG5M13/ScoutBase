package es.dimecresalessis.scoutbase.application.player.update;

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
    private Player savedPlayer;

    @BeforeEach
    void setUp() {
        playerId = UUID.randomUUID();
        player = Player.builder()
                .id(playerId)
                .name("Updated Name")
                .build();
        savedPlayer = Player.builder()
                .id(playerId)
                .name("Old Name")
                .build();
    }

    @Test
    void execute_ShouldUpdateAndReturnPlayer_WhenValidRequest() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(savedPlayer));

        Player result = updatePlayerUseCase.execute(player, playerId);

        assertNotNull(result);
        assertEquals(playerId, result.getId());
        verify(playerRepository, times(3)).findById(playerId);
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    void execute_ShouldThrowIllegalArgumentException_WhenIdsDoNotMatch() {
        UUID differentId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () ->
                updatePlayerUseCase.execute(player, differentId)
        );

        verify(playerRepository, never()).save(any());
    }

    @Test
    void execute_ShouldThrowPlayerException_WhenPlayerNotFound() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        PlayerException exception = assertThrows(PlayerException.class, () ->
                updatePlayerUseCase.execute(player, playerId)
        );

        assertEquals(ErrorEnum.PLAYER_NOT_FOUND, exception.getErrorEnum());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void execute_ShouldMatchWithSavedInfo_WhenPlayerExists() {
        Player incomingPlayer = mock(Player.class);
        when(incomingPlayer.getId()).thenReturn(playerId);
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(savedPlayer));

        updatePlayerUseCase.execute(incomingPlayer, playerId);

        verify(incomingPlayer).matchWithObject(savedPlayer);
        verify(playerRepository).save(incomingPlayer);
    }
}