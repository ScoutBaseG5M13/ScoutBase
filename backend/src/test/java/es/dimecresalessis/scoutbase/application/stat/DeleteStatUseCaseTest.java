package es.dimecresalessis.scoutbase.application.stat;

import es.dimecresalessis.scoutbase.application.stat.delete.DeleteStatUseCase;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.repository.StatRepository;
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
class DeleteStatUseCaseTest {

    @Mock
    private StatRepository statRepository;

    @InjectMocks
    private DeleteStatUseCase deleteStatUseCase;

    @Test
    void execute_ShouldDeleteStat_WhenIdExists() {
        UUID statId = UUID.randomUUID();
        Stat stat = Stat.builder().id(statId).playerId(UUID.randomUUID()).code("CON").build();

        when(statRepository.findById(statId)).thenReturn(Optional.of(stat));
        doNothing().when(statRepository).deleteById(statId);

        boolean result = deleteStatUseCase.execute(statId);

        assertTrue(result);
        verify(statRepository).findById(statId);
        verify(statRepository).deleteById(statId);
    }

    @Test
    void execute_ShouldThrowException_WhenIdDoesNotExist() {
        UUID statId = UUID.randomUUID();
        when(statRepository.findById(statId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                deleteStatUseCase.execute(statId)
        );

        verify(statRepository).findById(statId);
        verify(statRepository, never()).deleteById(any());
    }
}