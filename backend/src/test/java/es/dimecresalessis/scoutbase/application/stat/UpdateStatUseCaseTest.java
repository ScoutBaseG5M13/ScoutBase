package es.dimecresalessis.scoutbase.application.stat;

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
    private Stat stat;

    @BeforeEach
    void setUp() {
        statId = UUID.randomUUID();
        stat = Stat.builder()
                .id(statId)
                .playerId(UUID.randomUUID())
                .code("CON")
                .value(90)
                .build();
    }

    @Test
    void execute_ShouldUpdateSuccessfully() {
        when(statRepository.findById(statId)).thenReturn(Optional.of(stat));
        when(checkIfStatAlreadyExistsOnPlayer.execute(stat)).thenReturn(false);

        Stat result = updateStatUseCase.execute(stat, statId);

        assertNotNull(result);
        verify(statRepository).update(stat);
    }

    @Test
    void execute_ShouldThrowException_WhenStatCodeAlreadyExists() {
        when(statRepository.findById(statId)).thenReturn(Optional.of(stat));
        when(checkIfStatAlreadyExistsOnPlayer.execute(stat)).thenReturn(true);

        StatException exception = assertThrows(StatException.class, () ->
                updateStatUseCase.execute(stat, statId)
        );

        assertEquals(ErrorEnum.STAT_CODE_ALREADY_EXISTS, exception.getErrorEnum());
    }
}