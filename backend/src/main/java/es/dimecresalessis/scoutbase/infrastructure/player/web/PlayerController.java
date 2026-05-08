package es.dimecresalessis.scoutbase.infrastructure.player.web;

import es.dimecresalessis.scoutbase.application.player.delete.DeletePlayerUseCase;
import es.dimecresalessis.scoutbase.application.player.find.FindAllPlayersByTeamIdUseCase;
import es.dimecresalessis.scoutbase.application.player.find.FindPlayerByIdUseCase;
import es.dimecresalessis.scoutbase.application.player.update.UpdatePlayerUseCase;
import es.dimecresalessis.scoutbase.application.stat.create.CreateStatUseCase;
import es.dimecresalessis.scoutbase.application.team.find.FindTeamByIdUseCase;
import es.dimecresalessis.scoutbase.application.team.find.FindTeamByPlayerUseCase;
import es.dimecresalessis.scoutbase.application.team.update.UpdateTeamUseCase;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.player.exception.PlayerException;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.stat.exception.StatException;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.team.exception.TeamException;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDTO;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerUpdateRequest;
import es.dimecresalessis.scoutbase.infrastructure.player.web.mapper.PlayerMapper;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.infrastructure.security.UserAuthService;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatDTO;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.mapper.StatMapper;
import es.dimecresalessis.scoutbase.infrastructure.web.annotation.ApiCommonResponses;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static es.dimecresalessis.scoutbase.infrastructure.web.dto.ResponseFactory.handleResponse;

/**
 * REST Controller for managing player-related operations.
 */
@RestController
@AllArgsConstructor
@ApiCommonResponses
@Tag(name = "Player", description = "Player management endpoints")
@RequestMapping(Routes.API_ROOT + Routes.PLAYERS)
public class PlayerController {

    private final PlayerMapper playerMapper;
    private final StatMapper statMapper;
    private final UserAuthService userAuthService;
    private final FindPlayerByIdUseCase findPlayerByIdUseCase;
    private final UpdatePlayerUseCase updatePlayerUseCase;
    private final DeletePlayerUseCase deletePlayerUseCase;
    private final FindAllPlayersByTeamIdUseCase findAllPlayersByTeamIdUseCase;
    private final UpdateTeamUseCase updateTeamUseCase;
    private final FindTeamByPlayerUseCase findTeamByPlayerUseCase;
    private final FindTeamByIdUseCase findTeamByIdUseCase;
    private final CreateStatUseCase createStatUseCase;

    /**
     * Finds all players.
     *
     * @return {@link ApiResponse} containing a list of all {@link Player}.
     */
    @GetMapping(Routes.TEAMS + Routes.ID_PATHVAR)
    @Operation(summary = "Find all players of team [Auth SCOUTER]", description = "Find all Players from Team")
    public ResponseEntity<ApiResponse<List<PlayerDTO>>> findAllByTeam(@PathVariable("id") UUID teamId) {
        userAuthService.hasMinimumTeamAuthorization(teamId, RoleEnum.SCOUTER);
        List<Player> players = findAllPlayersByTeamIdUseCase.execute(teamId);
        List<PlayerDTO> playersDto = players.stream().map(playerMapper::toDto).toList();
        return handleResponse(playersDto).ok();
    }

    /**
     * Fetches a single player record by ID.
     *
     * @param playerId The ID of the player.
     * @return {@link ApiResponse} containing the player details.
     * @throws PlayerException If the requested player does not exist.
     */
    @GetMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Find Player by ID [Auth SCOUTER]", description = "Finds a Player")
    public ResponseEntity<ApiResponse<PlayerDTO>> findById(@PathVariable(value = "id") UUID playerId) {
        Player player = findPlayerByIdUseCase.execute(playerId);
        if (player == null) {
            throw new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, playerId.toString());
        }

        Team team = findTeamByIdUseCase.execute(player.getTeamId());
        if (team == null) {
            throw new TeamException(ErrorEnum.TEAM_NOT_FOUND, player.getTeamId().toString());
        }

        userAuthService.hasMinimumTeamAuthorization(team.getId(), RoleEnum.SCOUTER);
        PlayerDTO playerDto = playerMapper.toDto(player);
        return handleResponse(playerDto).ok();
    }

    /**
     * Updates an existing player record.
     *
     * @param updateRequest The updated data request.
     * @param playerId The ID of the player to be updated.
     * @return {@link ApiResponse} containing the updated player's details.
     * @throws PlayerException If the player is not found.
     */
    @PutMapping(value = Routes.ID_PATHVAR)
    @Operation(summary = "Update Player [Auth SCOUTER]", description = "Updates a Player")
    public ResponseEntity<ApiResponse<PlayerDTO>> update(@Valid @RequestBody PlayerUpdateRequest updateRequest, @PathVariable("id") UUID playerId) {
        Player player = playerMapper.updateToDomain(updateRequest);
        Team team = findTeamByPlayerUseCase.execute(playerId);
        if (team == null) {
            throw new TeamException(ErrorEnum.TEAM_BY_PLAYER_NOT_FOUND, playerId.toString());
        }
        userAuthService.hasMinimumTeamAuthorization(team.getId(), RoleEnum.SCOUTER);
        Player updatedPlayer = updatePlayerUseCase.execute(player, playerId);
        PlayerDTO updatedPlayerDTO = playerMapper.toDto(updatedPlayer);
        return handleResponse(updatedPlayerDTO).ok();
    }

    /**
     * Deletes a player record by ID.
     *
     * @param playerId The ID of the player to be deleted.
     * @return {@link ApiResponse} containing {@code true} if the player was deleted successfully.
     * @throws PlayerException If the player is not found.
     */
    @DeleteMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Delete player [Auth SCOUTER]", description = "Deletes a Player")
    public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable("id") UUID playerId) {
        Team userTeam = findTeamByPlayerUseCase.execute(playerId);
        if (userTeam == null) {
            throw new TeamException(ErrorEnum.USER_TEAM_BY_PLAYER_NOT_FOUND, playerId.toString());
        }
        userAuthService.hasMinimumTeamAuthorization(userTeam.getId(), RoleEnum.SCOUTER);
        boolean isDeleted = deletePlayerUseCase.execute(playerId);
        userTeam.getPlayers().remove(playerId);
        updateTeamUseCase.execute(userTeam, userTeam.getId());
        return handleResponse(isDeleted).ok();
    }

    /**
     * Creates a new stat in a player.
     *
     * @param statRequest The stat details to create.
     * @return {@link ApiResponse} containing the created stat's details.
     * @throws StatException If an error occurs during stat creation.
     */
    @PostMapping(Routes.ID_PATHVAR + Routes.STATS)
    @Operation(summary = "Create and assign a stat to a player [Auth SCOUTER]", description = "Creates a new Stat and assigns it to a Player")
    public ResponseEntity<ApiResponse<StatDTO>> createStat(@Valid @RequestBody StatCreateRequest statRequest, @PathVariable(value = "id") UUID playerId) {
        Player player = findPlayerByIdUseCase.execute(playerId);
        if (player == null) {
            throw new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, playerId.toString());
        }
        Team team = findTeamByPlayerUseCase.execute(player.getId());
        userAuthService.hasMinimumTeamAuthorization(team.getId(), RoleEnum.SCOUTER);

        Stat stat = statMapper.createToDomain(statRequest, playerId);
        Stat createdStat = createStatUseCase.execute(stat, playerId);
        StatDTO createdStatDTO = statMapper.toDto(createdStat);
        return handleResponse(createdStatDTO).created();
    }
}
