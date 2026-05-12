package es.dimecresalessis.scoutbase.application.stat.create;

import es.dimecresalessis.scoutbase.application.stat.CheckIfStatAlreadyExistsOnPlayer;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import es.dimecresalessis.scoutbase.domain.stat.exception.StatException;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.model.StatEnum;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateStatUseCaseTest {

    @Mock
    private StatRepository statRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private CheckIfStatAlreadyExistsOnPlayer checkIfStatAlreadyExistsOnPlayer;

    @InjectMocks
    private CreateStatUseCase createStatUseCase;

    private UUID playerId;
    private UUID statId;
    private Stat stat;

    @BeforeEach
    void setUp() {
        playerId = UUID.randomUUID();
        statId = UUID.randomUUID();
        stat = Stat.builder()
                .id(statId)
                .playerId(playerId)
                .code(StatEnum.MED.statCode)
                .build();
    }

    @Test
    void execute_ShouldCreateStatAndLinkToPlayer_WhenDataIsValid() throws StatException {
        Player player = Player.builder()
                .id(playerId)
                .name("Test Player")
                .stats(new ArrayList<>())
                .build();

        when(checkIfStatAlreadyExistsOnPlayer.execute(stat)).thenReturn(false);
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        Stat result = createStatUseCase.execute(stat, playerId);

        assertNotNull(result);
        assertEquals(statId, result.getId());
        verify(statRepository).save(stat);
        verify(playerRepository).save(player);
        assertTrue(player.getStats().contains(statId));
    }

    @Test
    void execute_ShouldThrowException_WhenStatIsNull() {
        StatException exception = assertThrows(StatException.class, () ->
                createStatUseCase.execute(null, playerId)
        );
        assertEquals(ErrorEnum.STAT_IS_NULL, exception.getErrorEnum());
    }

    @Test
    void execute_ShouldThrowException_WhenStatIdIsNull() {
        stat.setId(null);
        StatException exception = assertThrows(StatException.class, () ->
                createStatUseCase.execute(stat, playerId)
        );
        assertEquals(ErrorEnum.STAT_ID_IS_NULL, exception.getErrorEnum());
    }

    @Test
    void execute_ShouldThrowException_WhenPlayerIdDoesNotMatch() {
        UUID wrongPlayerId = UUID.randomUUID();
        StatException exception = assertThrows(StatException.class, () ->
                createStatUseCase.execute(stat, wrongPlayerId)
        );
        assertEquals(ErrorEnum.USER_ID_DOES_NOT_MATCH, exception.getErrorEnum());
    }

    @Test
    void execute_ShouldThrowException_WhenStatAlreadyExists() {
        when(checkIfStatAlreadyExistsOnPlayer.execute(stat)).thenReturn(true);

        StatException exception = assertThrows(StatException.class, () ->
                createStatUseCase.execute(stat, playerId)
        );

        assertEquals(ErrorEnum.STAT_CODE_ALREADY_EXISTS, exception.getErrorEnum());
        verify(statRepository, never()).save(any());
    }

    @Test
    void execute_ShouldInitializeStatsList_WhenPlayerStatsIsNull() throws StatException {
        Player player = Player.builder()
                .id(playerId)
                .stats(null)
                .build();

        when(checkIfStatAlreadyExistsOnPlayer.execute(stat)).thenReturn(false);
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));

        createStatUseCase.execute(stat, playerId);

        assertNotNull(player.getStats());
        assertEquals(1, player.getStats().size());
        verify(playerRepository).save(player);
    }
}