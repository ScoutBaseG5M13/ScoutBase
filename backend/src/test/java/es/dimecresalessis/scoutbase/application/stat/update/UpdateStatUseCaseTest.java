package es.dimecresalessis.scoutbase.application.stat.update;

import es.dimecresalessis.scoutbase.application.stat.CheckIfStatAlreadyExistsOnPlayer;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.stat.exception.StatException;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
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
class UpdateStatUseCaseTest {

    @Mock
    private StatRepository statRepository;

    @Mock
    private CheckIfStatAlreadyExistsOnPlayer checkIfStatAlreadyExistsOnPlayer;

    @InjectMocks
    private UpdateStatUseCase updateStatUseCase;

    private UUID statId;
    private UUID playerId;
    private Stat stat;
    private Stat savedStat;

    @BeforeEach
    void setUp() {
        statId = UUID.randomUUID();
        playerId = UUID.randomUUID();
        stat = Stat.builder()
                .id(statId)
                .playerId(playerId)
                .code("DR1")
                .value(4)
                .build();
        savedStat = Stat.builder()
                .id(statId)
                .playerId(playerId)
                .code("DR1")
                .value(2)
                .build();
    }

    @Test
    void execute_ShouldUpdateStatSuccessfully() {
        when(statRepository.findById(statId)).thenReturn(Optional.of(savedStat));
        when(checkIfStatAlreadyExistsOnPlayer.execute(stat)).thenReturn(false);

        Stat result = updateStatUseCase.execute(stat, statId);

        assertNotNull(result);
        assertEquals(statId, result.getId());
        verify(statRepository, times(3)).findById(statId);
        verify(statRepository).update(stat);
        verify(checkIfStatAlreadyExistsOnPlayer).execute(stat);
    }

    @Test
    void execute_ShouldThrowIllegalArgumentException_WhenIdsDoNotMatch() {
        UUID differentId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class, () ->
                updateStatUseCase.execute(stat, differentId)
        );

        verifyNoInteractions(statRepository);
        verifyNoInteractions(checkIfStatAlreadyExistsOnPlayer);
    }

    @Test
    void execute_ShouldThrowStatException_WhenStatNotFound() {
        when(statRepository.findById(statId)).thenReturn(Optional.empty());

        StatException exception = assertThrows(StatException.class, () ->
                updateStatUseCase.execute(stat, statId)
        );

        assertEquals(ErrorEnum.STAT_NOT_FOUND, exception.getErrorEnum());
        verify(statRepository).findById(statId);
        verify(statRepository, never()).update(any());
    }

    @Test
    void execute_ShouldThrowStatException_WhenStatAlreadyExistsOnPlayer() {
        when(statRepository.findById(statId)).thenReturn(Optional.of(savedStat));
        when(checkIfStatAlreadyExistsOnPlayer.execute(stat)).thenReturn(true);

        StatException exception = assertThrows(StatException.class, () ->
                updateStatUseCase.execute(stat, statId)
        );

        assertEquals(ErrorEnum.STAT_CODE_ALREADY_EXISTS, exception.getErrorEnum());
        verify(statRepository, never()).update(any());
    }

    @Test
    void execute_ShouldCallMatchWithObject_DuringUpdate() {
        Stat spyStat = spy(stat);
        when(statRepository.findById(statId)).thenReturn(Optional.of(savedStat));
        when(checkIfStatAlreadyExistsOnPlayer.execute(spyStat)).thenReturn(false);

        updateStatUseCase.execute(spyStat, statId);

        verify(spyStat).matchWithObject(savedStat);
        verify(statRepository).update(spyStat);
    }
}