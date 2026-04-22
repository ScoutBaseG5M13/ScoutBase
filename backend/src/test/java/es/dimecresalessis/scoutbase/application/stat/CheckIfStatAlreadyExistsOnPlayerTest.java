package es.dimecresalessis.scoutbase.application.stat;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckIfStatAlreadyExistsOnPlayerTest {

    @Mock
    private StatRepository statRepository;

    @InjectMocks
    private CheckIfStatAlreadyExistsOnPlayer checkIfStatAlreadyExistsOnPlayer;

    private Stat stat;
    private UUID playerId;

    @BeforeEach
    void setUp() {
        playerId = UUID.randomUUID();
        stat = Stat.builder()
                .playerId(playerId)
                .code("PAS")
                .value(80)
                .build();
    }

    @Test
    void execute_ShouldReturnTrue_WhenStatCodeExists() {
        Stat existingStat = Stat.builder()
                .playerId(playerId)
                .code("PAS")
                .build();

        when(statRepository.findAllByPlayerId(playerId)).thenReturn(List.of(existingStat));

        boolean result = checkIfStatAlreadyExistsOnPlayer.execute(stat);

        assertTrue(result);
        verify(statRepository).findAllByPlayerId(playerId);
    }

    @Test
    void execute_ShouldReturnFalse_WhenStatCodeDoesNotExist() {
        Stat existingStat = Stat.builder()
                .playerId(playerId)
                .code("RES")
                .build();

        when(statRepository.findAllByPlayerId(playerId)).thenReturn(List.of(existingStat));

        boolean result = checkIfStatAlreadyExistsOnPlayer.execute(stat);

        assertFalse(result);
        verify(statRepository).findAllByPlayerId(playerId);
    }
}