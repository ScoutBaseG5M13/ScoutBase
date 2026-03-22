package es.dimecresalessis.scoutbase.infrastructure.player.web;

import es.dimecresalessis.scoutbase.infrastructure.web.annotation.ApiCommonResponses;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDto;
import es.dimecresalessis.scoutbase.domain.player.exception.PlayerException;
import es.dimecresalessis.scoutbase.infrastructure.player.web.mapper.PlayerMapper;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.application.player.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Find all players", description = "Retrieves all registered players in the DB")
    public ApiResponse<List<PlayerDto>> findAll() {
        return handleResponse(
                findAllPlayersUseCase.execute()
                .stream()
                .map(playerMapper::toDto)
                .toList()
        ).ok();
    }

    /**
     * Fetches a single player record by ID.
     *
     * @param id The ID of the player.
     * @return {@link ApiResponse} containing the player details.
     * @throws PlayerException If the requested player does not exist.
     */
    @GetMapping(Routes.ID_PATHVAR)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Find player by ID", description = "Finds and returns a player by their ID")
    public ApiResponse<PlayerDto> findById(@PathVariable UUID id) throws PlayerException {
        try {
            return handleResponse(
                    playerMapper.toDto(
                            findPlayerByIdUseCase.execute(id)
                    )
            ).ok();
        } catch (NoSuchElementException ex) {
            throw new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, id.toString());
        }
    }

    /**
     * Creates a new player record.
     *
     * @param playerDto The player details submitted by the client.
     * @return {@link ApiResponse} containing the created player's details.
     * @throws PlayerException If an error occurs during player creation.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Create a new player", description = "Adds a new player to the DB")
    public ApiResponse<PlayerDto> create(@Valid @RequestBody PlayerDto playerDto) throws PlayerException {
        Player player = playerMapper.toDomain(playerDto);
        return handleResponse(
                playerMapper.toDto(
                        createPlayerUseCase.execute(player)
                )
        ).created();
    }

    /**
     * Updates an existing player record.
     *
     * @param playerDto The updated player details.
     * @param id The ID of the player to be updated.
     * @return {@link ApiResponse} containing the updated player's details.
     * @throws PlayerException If the player is not found.
     */
    @PutMapping(value = Routes.ID_PATHVAR, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Update player by ID", description = "Updates the data for a specific player")
    public ApiResponse<PlayerDto> update(@Valid @RequestBody PlayerDto playerDto, @PathVariable UUID id) {
        try {
            if (findPlayerByIdUseCase.execute(id) == null) {
                throw new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, playerDto.getId().toString());
            }
            return handleResponse(
                    playerMapper.toDto(
                            updatePlayerUseCase.execute(
                                    playerMapper.toDomain(playerDto), id
                            )
                    )
            ).ok();
        } catch (NoSuchElementException ex) {
            throw new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, playerDto.getId().toString());
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Delete player by ID", description = "Deletes a specific player by their ID")
    public ApiResponse<Boolean> delete(@PathVariable UUID id) {
        try {
            return handleResponse(
                    deletePlayerUseCase.execute(id)
            ).ok();
        } catch (NoSuchElementException ex) {
            throw new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, id.toString());
        }
    }
}
