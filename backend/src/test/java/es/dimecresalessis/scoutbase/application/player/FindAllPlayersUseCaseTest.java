package es.dimecresalessis.scoutbase.application.player;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllPlayersUseCaseTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private FindAllPlayersUseCase findAllPlayersUseCase;

    @Test
    void execute_ShouldReturnList() {
        when(playerRepository.findAll()).thenReturn(List.of(new Player()));

        List<Player> result = findAllPlayersUseCase.execute();

        assertEquals(1, result.size());
        verify(playerRepository).findAll();
    }
}