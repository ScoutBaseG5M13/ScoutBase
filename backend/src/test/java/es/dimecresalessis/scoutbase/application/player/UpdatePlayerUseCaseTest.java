package es.dimecresalessis.scoutbase.application.player;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.PositionEnum;
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
class UpdatePlayerUseCaseTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private UpdatePlayerUseCase updatePlayerUseCase;

    private UUID playerId;
    private UUID teamId;
    private Player player;

    @BeforeEach
    void setUp() {
        playerId = UUID.randomUUID();
        teamId = UUID.randomUUID();
        player = Player.builder()
                .id(playerId)
                .teamId(teamId)
                .name("Ronald")
                .surname("McGallahan")
                .age(14)
                .email("ronald@mcgall.es")
                .number(16)
                .position(PositionEnum.PORTERO.name())
                .category(CategoryEnum.BENJAMIN.name())
                .priority(8)
                .build();
    }

    @Test
    @DisplayName("Should update player successfully when IDs match and exist")
    void shouldUpdatePlayer() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        Player result = updatePlayerUseCase.execute(player, playerId);

        assertNotNull(result);
        assertEquals(playerId, result.getId());
        verify(playerRepository, times(2)).findById(playerId);
        verify(playerRepository, times(1)).save(player);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when path ID and body ID do not match")
    void shouldThrowException_WhenIdsDoNotMatch() {
        UUID differentId = UUID.randomUUID();
        Player differentPlayer = Player.builder().id(differentId).build();

        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        when(playerRepository.findById(differentId)).thenReturn(Optional.of(differentPlayer));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            updatePlayerUseCase.execute(differentPlayer, playerId);
        });

        assertEquals("Player id does not match", exception.getMessage());
        verify(playerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw NoSuchElementException when player does not exist")
    void shouldThrowException_WhenPlayerNotFound() {
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            updatePlayerUseCase.execute(player, playerId);
        });

        verify(playerRepository, times(1)).findById(playerId);
        verify(playerRepository, never()).save(any());
    }
}