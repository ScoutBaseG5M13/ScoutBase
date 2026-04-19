package es.dimecresalessis.scoutbase.application.player;

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
                .name("Ronald")
                .build();
    }

    @Test
    void execute_ShouldCreatePlayerSuccessfully() throws PlayerException {
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        Player result = createPlayerUseCase.execute(player);

        assertNotNull(result);
        assertEquals(playerId, result.getId());
        verify(playerRepository).findById(playerId);
        verify(playerRepository).save(player);
    }

    @Test
    void execute_ShouldThrowException_WhenPlayerIsNull() {
        PlayerException exception = assertThrows(PlayerException.class, () ->
                createPlayerUseCase.execute(null)
        );

        assertEquals(ErrorEnum.PLAYER_IS_NULL, exception.getErrorEnum());
        verifyNoInteractions(playerRepository);
    }

    @Test
    void execute_ShouldThrowException_WhenPlayerIdIsNull() {
        Player playerNoId = new Player();
        playerNoId.setId(null); // Forzamos el null que el constructor/builder evitan

        PlayerException exception = assertThrows(PlayerException.class, () ->
                createPlayerUseCase.execute(playerNoId)
        );

        assertEquals(ErrorEnum.PLAYER_ID_IS_NULL, exception.getErrorEnum());
        verifyNoInteractions(playerRepository);
    }

    @Test
    void execute_ShouldThrowException_WhenPlayerAlreadyExists() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        PlayerException exception = assertThrows(PlayerException.class, () ->
                createPlayerUseCase.execute(player)
        );

        assertEquals(ErrorEnum.PLAYER_ALREADY_EXISTS, exception.getErrorEnum());
        verify(playerRepository).findById(playerId);
        verify(playerRepository, never()).save(any());
    }
}