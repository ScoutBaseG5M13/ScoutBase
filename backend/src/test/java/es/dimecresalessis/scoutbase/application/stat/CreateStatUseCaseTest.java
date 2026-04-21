package es.dimecresalessis.scoutbase.application.stat;

import es.dimecresalessis.scoutbase.application.stat.create.CreateStatUseCase;
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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateStatUseCaseTest {

    @Mock
    private StatRepository statRepository;

    @Mock
    private CheckIfStatAlreadyExistsOnPlayer checkIfStatAlreadyExistsOnPlayer;

    @InjectMocks
    private CreateStatUseCase createStatUseCase;

    private Stat stat;
    private UUID statId;

    @BeforeEach
    void setUp() {
        statId = UUID.randomUUID();
        stat = Stat.builder()
                .id(statId)
                .playerId(UUID.randomUUID())
                .code("POT")
                .value(75)
                .build();
    }

    @Test
    void execute_ShouldCreateStatSuccessfully() {
        when(checkIfStatAlreadyExistsOnPlayer.execute(stat)).thenReturn(false);
        when(statRepository.save(stat)).thenReturn(stat);

        Stat result = createStatUseCase.execute(stat);

        assertNotNull(result);
        assertEquals(statId, result.getId());
        verify(statRepository).save(stat);
    }

    @Test
    void execute_ShouldThrowException_WhenStatIdIsNull() {
        Stat statNoId = Stat.builder()
                .playerId(UUID.randomUUID())
                .code("VEL")
                .build();
        statNoId.setId(null);

        StatException exception = assertThrows(StatException.class, () ->
                createStatUseCase.execute(statNoId)
        );

        assertEquals(ErrorEnum.STAT_ID_IS_NULL, exception.getErrorEnum());
    }

    @Test
    void execute_ShouldThrowException_WhenStatCodeAlreadyExists() {
        when(checkIfStatAlreadyExistsOnPlayer.execute(stat)).thenReturn(true);

        StatException exception = assertThrows(StatException.class, () ->
                createStatUseCase.execute(stat)
        );

        assertEquals(ErrorEnum.STAT_CODE_ALREADY_EXISTS, exception.getErrorEnum());
        verify(statRepository, never()).save(any());
    }
}