package es.dimecresalessis.scoutbase.application.stat;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
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
class FindStatByIdUseCaseTest {

    @Mock
    private StatRepository statRepository;

    @InjectMocks
    private FindStatByIdUseCase findStatByIdUseCase;

    @Test
    void execute_ShouldReturnStat_WhenIdExists() {
        UUID statId = UUID.randomUUID();
        Stat stat = Stat.builder()
                .id(statId)
                .playerId(UUID.randomUUID())
                .code("RES")
                .value(70)
                .build();

        when(statRepository.findById(statId)).thenReturn(Optional.of(stat));

        Stat result = findStatByIdUseCase.execute(statId);

        assertNotNull(result);
        assertEquals(statId, result.getId());
        assertEquals("RES", result.getCode());
        verify(statRepository).findById(statId);
    }

    @Test
    void execute_ShouldReturnNull_WhenIdDoesNotExist() {
        UUID statId = UUID.randomUUID();
        when(statRepository.findById(statId)).thenReturn(Optional.empty());

        Stat result = findStatByIdUseCase.execute(statId);

        assertNull(result);
        verify(statRepository).findById(statId);
    }
}