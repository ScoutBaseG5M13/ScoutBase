package es.dimecresalessis.scoutbase.application.player;

import es.dimecresalessis.scoutbase.domain.player.exception.PlayerException;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import es.dimecresalessis.scoutbase.domain.shared.domain.PositionEnum;
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
        player = Player.builder()
                .id(playerId)
                .name("Ronald")
                .surname("Araujo")
                .age(25)
                .email("ronald@scoutbase.es")
                .number(4)
                .position(PositionEnum.DEFENSA_CENTRAL)
                .priority(1)
                .build();
    }

    @Test
    void execute_ShouldUpdateAndReturnPlayer() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        Player result = updatePlayerUseCase.execute(player, playerId);

        assertNotNull(result);
        assertEquals(playerId, result.getId());
        assertEquals(PositionEnum.DEFENSA_CENTRAL, result.getPosition());
        verify(playerRepository, times(2)).findById(playerId);
        verify(playerRepository).save(player);
    }

    @Test
    void execute_ShouldThrowException_WhenBodyPlayerNotFound() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        assertThrows(PlayerException.class, () -> updatePlayerUseCase.execute(player, playerId));

        verify(playerRepository, never()).save(any());
    }

    @Test
    void execute_ShouldThrowException_WhenIdsMismatch() {
        UUID pathId = UUID.randomUUID();
        Player pathPlayer = Player.builder().id(pathId).build();

        when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));
        when(playerRepository.findById(pathId)).thenReturn(Optional.of(pathPlayer));

        assertThrows(IllegalArgumentException.class, () ->
                updatePlayerUseCase.execute(player, pathId)
        );

        verify(playerRepository, never()).save(any());
    }

    @Test
    void execute_ShouldThrowException_WhenPathIdNotFound() {
        UUID pathId = UUID.randomUUID();
        when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));
        when(playerRepository.findById(pathId)).thenReturn(Optional.empty());

        assertThrows(PlayerException.class, () -> updatePlayerUseCase.execute(player, pathId));

        verify(playerRepository, never()).save(any());
    }
}