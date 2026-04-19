package es.dimecresalessis.scoutbase.infrastructure.player.web;

import es.dimecresalessis.scoutbase.application.player.create.CreatePlayerUseCase;
import es.dimecresalessis.scoutbase.application.player.delete.DeletePlayerUseCase;
import es.dimecresalessis.scoutbase.application.player.find.FindAllPlayersUseCase;
import es.dimecresalessis.scoutbase.application.player.find.FindPlayerByIdUseCase;
import es.dimecresalessis.scoutbase.application.player.update.UpdatePlayerUseCase;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDTO;
import es.dimecresalessis.scoutbase.infrastructure.web.annotation.ApiCommonResponses;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.player.exception.PlayerException;
import es.dimecresalessis.scoutbase.infrastructure.player.web.mapper.PlayerMapper;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static es.dimecresalessis.scoutbase.infrastructure.web.dto.ResponseFactory.handleResponse;

/**
 * REST Controller for managing player-related operations.
 */
@RestController
@AllArgsConstructor
@ApiCommonResponses
@Tag(name = "Players", description = "Player management endpoints")
@RequestMapping(Routes.API_ROOT + Routes.PLAYERS)
public class PlayerController {

    private final PlayerMapper playerMapper;
    private final FindAllPlayersUseCase findAllPlayersUseCase;
    private final FindPlayerByIdUseCase findPlayerByIdUseCase;
    private final UpdatePlayerUseCase updatePlayerUseCase;
    private final CreatePlayerUseCase createPlayerUseCase;
    private final DeletePlayerUseCase deletePlayerUseCase;

    /**
     * Finds all players.
     *
     * @return {@link ApiResponse} containing a list of all {@link Player}.
     */
    @GetMapping
    @Operation(summary = "Find all players", description = "Retrieves all registered players in the DB")
    public ResponseEntity<ApiResponse<List<PlayerDTO>>> findAll() {
        List<Player> players = findAllPlayersUseCase.execute();
        List<PlayerDTO> playersDto = players.stream().map(playerMapper::toDto).toList();
        return handleResponse(playersDto).ok();
    }

    /**
     * Fetches a single player record by ID.
     *
     * @param id The ID of the player.
     * @return {@link ApiResponse} containing the player details.
     * @throws PlayerException If the requested player does not exist.
     */
    @GetMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Find player by ID", description = "Finds and returns a player by their ID")
    public ResponseEntity<ApiResponse<PlayerDTO>> findById(@PathVariable UUID id) throws PlayerException {
        try {
            Player player = findPlayerByIdUseCase.execute(id);
            PlayerDTO playerDto = playerMapper.toDto(player);
            return handleResponse(playerDto).ok();
        } catch (NoSuchElementException ex) {
            throw new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, id.toString());
        }
    }

    /**
     * Creates a new player record.
     *
     * @param playerRequest The player details submitted by the client.
     * @return {@link ApiResponse} containing the created player's details.
     * @throws PlayerException If an error occurs during player creation.
     */
    @PostMapping
    @Operation(summary = "Create a new player", description = "Adds a new player to the DB")
    public ResponseEntity<ApiResponse<PlayerDTO>> create(@Valid @RequestBody PlayerCreateRequest playerRequest) throws PlayerException {
        Player player = playerMapper.createToDomain(playerRequest);
        Player createdPlayer = createPlayerUseCase.execute(player);
        PlayerDTO createdPlayerDTO = playerMapper.toDto(createdPlayer);
        return handleResponse(createdPlayerDTO).created();
    }

    /**
     * Updates an existing player record.
     *
     * @param playerDto The updated player details.
     * @param id The ID of the player to be updated.
     * @return {@link ApiResponse} containing the updated player's details.
     * @throws PlayerException If the player is not found.
     */
    @PutMapping(value = Routes.ID_PATHVAR)
    @Operation(summary = "Update player by ID", description = "Updates the data for a specific player")
    public ResponseEntity<ApiResponse<PlayerDTO>> update(@Valid @RequestBody PlayerDTO playerDto, @PathVariable UUID id) {
        try {
            Player player = playerMapper.dtoToDomain(playerDto);
            Player updatedPlayer = updatePlayerUseCase.execute(player, id);
            PlayerDTO updatedPlayerDTO = playerMapper.toDto(updatedPlayer);
            return handleResponse(updatedPlayerDTO).ok();
        } catch (NoSuchElementException ex) {
            throw new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, ex.getMessage());
        }
    }

    /**
     * Deletes a player record by ID.
     *
     * @param id The ID of the player to be deleted.
     * @return {@link ApiResponse} containing {@code true} if the player was deleted successfully.
     * @throws PlayerException If the player is not found.
     */
    @DeleteMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Delete player by ID", description = "Deletes a specific player by their ID")
    public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable UUID id) {
        try {
            boolean isDeleted = deletePlayerUseCase.execute(id);
            return handleResponse(isDeleted).ok();
        } catch (NoSuchElementException ex) {
            throw new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, id.toString());
        }
    }
}
