package es.dimecresalessis.scoutbase.application.player;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
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
class FindPlayerByIdUseCaseTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private FindPlayerByIdUseCase findPlayerByIdUseCase;

    @Test
    void execute_ShouldReturnPlayer_WhenIdExists() {
        UUID id = UUID.randomUUID();
        Player player = Player.builder().id(id).build();
        when(playerRepository.findById(id)).thenReturn(Optional.of(player));

        Player result = findPlayerByIdUseCase.execute(id);

        assertEquals(id, result.getId());
    }

    @Test
    void execute_ShouldThrowException_WhenIdNotFound() {
        UUID id = UUID.randomUUID();
        when(playerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> findPlayerByIdUseCase.execute(id));
    }
}