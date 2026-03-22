package es.dimecresalessis.scoutbase.application.player;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.player.exception.PlayerException;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class CreatePlayerUseCaseTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private CreatePlayerUseCase createPlayerUseCase;

    private Player player;
    private UUID playerId;

    @BeforeEach
    void setUp() {
        playerId = UUID.randomUUID();
        player = Player.builder()
                .id(playerId)
                .name("Ronald McGallahan")
                .build();
    }

    @Test
    @DisplayName("Should save player successfully")
    void shouldCreatePlayer() throws PlayerException {
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        Player result = createPlayerUseCase.execute(player);

        assertNotNull(result);
        assertEquals(playerId, result.getId());
        verify(playerRepository, times(1)).findById(playerId);
        verify(playerRepository, times(1)).save(player);
    }

    @Test
    @DisplayName("Should throw PlayerException when player is null")
    void ShouldThrowPlayerException_WhenPlayerIsNull() {
        PlayerException exception = assertThrows(PlayerException.class, () -> {
            createPlayerUseCase.execute(null);
        });

        assertEquals(ErrorEnum.PLAYER_IS_NULL, exception.getErrorEnum());
        verifyNoInteractions(playerRepository);
    }

    @Test
    @DisplayName("Should throw PlayerException when player ID already exists")
    void ShouldThrowPlayerException_WhenPlayerAlreadyExists() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        PlayerException exception = assertThrows(PlayerException.class, () -> {
            createPlayerUseCase.execute(player);
        });

        assertEquals(ErrorEnum.PLAYER_ALREADY_EXISTS, exception.getErrorEnum());
        verify(playerRepository, times(1)).findById(playerId);
        verify(playerRepository, never()).save(any());
    }
}