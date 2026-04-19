package es.dimecresalessis.scoutbase.application.stat;

import es.dimecresalessis.scoutbase.application.stat.find.FindAllStatsByPlayerIdUseCase;
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
class FindAllStatsByPlayerIdUseCaseTest {

    @Mock
    private StatRepository statRepository;

    @InjectMocks
    private FindAllStatsByPlayerIdUseCase findAllStatsByPlayerIdUseCase;

    @Test
    void execute_ShouldReturnStats() {
        UUID playerId = UUID.randomUUID();
        Stat s1 = Stat.builder().playerId(playerId).code("PAS").build();
        Stat s2 = Stat.builder().playerId(playerId).code("VEL").build();

        when(statRepository.findAllByPlayerId(playerId)).thenReturn(List.of(s1, s2));

        List<Stat> result = findAllStatsByPlayerIdUseCase.execute(playerId);

        assertEquals(2, result.size());
        verify(statRepository).findAllByPlayerId(playerId);
    }
}