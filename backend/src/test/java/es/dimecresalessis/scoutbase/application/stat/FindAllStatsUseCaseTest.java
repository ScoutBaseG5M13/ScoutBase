package es.dimecresalessis.scoutbase.application.stat;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
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
class FindAllStatsUseCaseTest {

    @Mock
    private StatRepository statRepository;

    @InjectMocks
    private FindAllStatsUseCase findAllStatsUseCase;

    @Test
    void execute_ShouldReturnAllStats() {
        List<Stat> stats = List.of(
                Stat.builder().id(UUID.randomUUID()).playerId(UUID.randomUUID()).code("PAS").value(80).build(),
                Stat.builder().id(UUID.randomUUID()).playerId(UUID.randomUUID()).code("VEL").value(90).build()
        );
        when(statRepository.findAll()).thenReturn(stats);

        List<Stat> result = findAllStatsUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(statRepository).findAll();
    }

    @Test
    void execute_ShouldReturnEmptyList_WhenNoStatsExist() {
        when(statRepository.findAll()).thenReturn(List.of());

        List<Stat> result = findAllStatsUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(statRepository).findAll();
    }
}