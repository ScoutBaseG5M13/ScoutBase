package es.dimecresalessis.scoutbase.infrastructure.player.web;

import es.dimecresalessis.scoutbase.application.player.*;
import es.dimecresalessis.scoutbase.domain.player.exception.PlayerException;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDTO;
import es.dimecresalessis.scoutbase.infrastructure.player.web.mapper.PlayerMapper;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerControllerTest {

    @Mock
    private PlayerMapper playerMapper;
    @Mock
    private FindAllPlayersUseCase findAllPlayersUseCase;
    @Mock
    private FindPlayerByIdUseCase findPlayerByIdUseCase;
    @Mock
    private UpdatePlayerUseCase updatePlayerUseCase;
    @Mock
    private CreatePlayerUseCase createPlayerUseCase;
    @Mock
    private DeletePlayerUseCase deletePlayerUseCase;

    @InjectMocks
    private PlayerController playerController;

    private UUID playerId;
    private Player player;
    private PlayerDTO playerDto;

    @BeforeEach
    void setUp() {
        playerId = UUID.randomUUID();
        player = Player.builder().id(playerId).name("Ronald").build();
        playerDto = new PlayerDTO(playerId, UUID.randomUUID(), "Ronald", "Scoutbase FC", 25, "ronald@scoutbase.es", 15, "PORTERO", "ALEVIN");
    }

    @Test
    @DisplayName("findAll - Should return list of all players")
    void findAll_ShouldReturnOk() {
        when(findAllPlayersUseCase.execute()).thenReturn(List.of(player));
        when(playerMapper.toDto(any())).thenReturn(playerDto);

        ResponseEntity<ApiResponse<List<PlayerDTO>>> response = playerController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().data().size());
        verify(findAllPlayersUseCase).execute();
    }

    @Test
    @DisplayName("findById - Should return player")
    void findById_ShouldReturnPlayer() throws PlayerException {
        when(findPlayerByIdUseCase.execute(playerId)).thenReturn(player);
        when(playerMapper.toDto(player)).thenReturn(playerDto);

        ResponseEntity<ApiResponse<PlayerDTO>> response = playerController.findById(playerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(playerId, response.getBody().data().getId());
    }

    @Test
    @DisplayName("findById - Should throw PlayerException when player does not exist")
    void findById_ShouldThrowException() {
        when(findPlayerByIdUseCase.execute(playerId)).thenThrow(new NoSuchElementException());

        assertThrows(PlayerException.class, () -> playerController.findById(playerId));
    }

    @Test
    @DisplayName("create - Should return created player")
    void create_ShouldReturnCreated() throws PlayerException {
        when(playerMapper.toDomain(playerDto)).thenReturn(player);
        when(createPlayerUseCase.execute(player)).thenReturn(player);
        when(playerMapper.toDto(player)).thenReturn(playerDto);

        ResponseEntity<ApiResponse<PlayerDTO>> response = playerController.create(playerDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(createPlayerUseCase).execute(any());
    }

    @Test
    @DisplayName("update - Should return updated player")
    void update_ShouldReturnOk() throws PlayerException {
        when(findPlayerByIdUseCase.execute(playerId)).thenReturn(player);
        when(playerMapper.toDomain(playerDto)).thenReturn(player);
        when(updatePlayerUseCase.execute(player, playerId)).thenReturn(player);
        when(playerMapper.toDto(player)).thenReturn(playerDto);

        ResponseEntity<ApiResponse<PlayerDTO>> response = playerController.update(playerDto, playerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(updatePlayerUseCase).execute(player, playerId);
    }

    @Test
    @DisplayName("delete - Should return true on success")
    void delete_ShouldReturnOk() {
        when(deletePlayerUseCase.execute(playerId)).thenReturn(true);

        ResponseEntity<ApiResponse<Boolean>> response = playerController.delete(playerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().data());
    }
}