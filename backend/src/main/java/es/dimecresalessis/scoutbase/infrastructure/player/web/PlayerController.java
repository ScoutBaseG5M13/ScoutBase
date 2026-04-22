package es.dimecresalessis.scoutbase.infrastructure.player.web;

import es.dimecresalessis.scoutbase.application.player.create.CreatePlayerUseCase;
import es.dimecresalessis.scoutbase.application.player.delete.DeletePlayerUseCase;
import es.dimecresalessis.scoutbase.application.player.find.FindAllPlayersByTeamIdUseCase;
import es.dimecresalessis.scoutbase.application.player.find.FindPlayerByIdUseCase;
import es.dimecresalessis.scoutbase.application.player.update.UpdatePlayerUseCase;
import es.dimecresalessis.scoutbase.application.security.UserAuthService;
import es.dimecresalessis.scoutbase.application.team.find.FindTeamByIdUseCase;
import es.dimecresalessis.scoutbase.application.team.find.FindTeamByPlayerUseCase;
import es.dimecresalessis.scoutbase.application.team.update.UpdateTeamUseCase;
import es.dimecresalessis.scoutbase.domain.team.exception.TeamException;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDTO;
import es.dimecresalessis.scoutbase.infrastructure.security.Session;
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
    private final FindPlayerByIdUseCase findPlayerByIdUseCase;
    private final UpdatePlayerUseCase updatePlayerUseCase;
    private final CreatePlayerUseCase createPlayerUseCase;
    private final DeletePlayerUseCase deletePlayerUseCase;
    private final FindAllPlayersByTeamIdUseCase findAllPlayersByTeamIdUseCase;
    private final FindTeamByPlayerUseCase findTeamByPlayerUseCase;
    private final UserAuthService userAuthService;
    private final FindTeamByIdUseCase findTeamByIdUseCase;
    private final UpdateTeamUseCase updateTeamUseCase;

    /**
     * Finds all players.
     *
     * @return {@link ApiResponse} containing a list of all {@link Player}.
     */
    @GetMapping(Routes.TEAMS + Routes.ID_PATHVAR)
    @Operation(summary = "Find all players of team [Auth SCOUTER]", description = "Find all Players of the requested Team")
    public ResponseEntity<ApiResponse<List<PlayerDTO>>> findAllPlayersByTeam(@PathVariable("id") UUID teamId) {
        if (!userAuthService.isAuthorizedByTeam(Session.getSessionUser(), teamId, RoleEnum.SCOUTER)) {
            throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, RoleEnum.SCOUTER.name());
        }
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
    public ResponseEntity<ApiResponse<PlayerDTO>> findPlayerById(@PathVariable(value = "id") UUID playerId) throws PlayerException {
        try {
            Team team = findTeamByPlayerUseCase.execute(playerId);
            if (team == null) {
                throw new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, playerId.toString());
            }
            if (!userAuthService.isAuthorizedByTeam(Session.getSessionUser(), team.getId(), RoleEnum.SCOUTER)) {
                throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, RoleEnum.SCOUTER.name());
            }
            Player player = findPlayerByIdUseCase.execute(playerId);
            PlayerDTO playerDto = playerMapper.toDto(player);
            return handleResponse(playerDto).ok();
        } catch (NoSuchElementException ex) {
            throw new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, playerId.toString());
        }
    }

    /**
     * Creates a new player record.
     *
     * @param playerRequest The player details submitted by the client.
     * @return {@link ApiResponse} containing the created player's details.
     * @throws PlayerException If an error occurs during player creation.
     */
    @PostMapping(Routes.TEAMS + Routes.ID_PATHVAR)
    @Operation(summary = "Create Player [Auth SCOUTER]", description = "Creates a new Player")
    public ResponseEntity<ApiResponse<PlayerDTO>> createPlayer(@PathVariable("id") UUID teamId, @Valid @RequestBody PlayerCreateRequest playerRequest) throws PlayerException {
        Team team = findTeamByIdUseCase.execute(teamId);
        if (team == null) {
            throw new TeamException(ErrorEnum.TEAM_NOT_FOUND, teamId.toString());
        }
        if (!userAuthService.isAuthorizedByTeam(Session.getSessionUser(), teamId, RoleEnum.SCOUTER)) {
            throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, RoleEnum.SCOUTER.name());
        }
        Player player = playerMapper.createToDomain(playerRequest);
        Player createdPlayer = createPlayerUseCase.execute(player);
        team.getPlayers().add(createdPlayer.getId());
        updateTeamUseCase.execute(team, teamId);
        PlayerDTO createdPlayerDTO = playerMapper.toDto(createdPlayer);
        return handleResponse(createdPlayerDTO).created();
    }

    /**
     * Updates an existing player record.
     *
     * @param playerDto The updated player details.
     * @param playerId The ID of the player to be updated.
     * @return {@link ApiResponse} containing the updated player's details.
     * @throws PlayerException If the player is not found.
     */
    @PutMapping(value = Routes.ID_PATHVAR)
    @Operation(summary = "Update Player", description = "Updates a Player")
    public ResponseEntity<ApiResponse<PlayerDTO>> updatePlayer(@PathVariable("id") UUID playerId, @Valid @RequestBody PlayerDTO playerDto) {
        try {
            Team team = findTeamByPlayerUseCase.execute(playerDto.getId());
            if (team == null) {
                throw new TeamException(ErrorEnum.TEAM_IS_NULL);
            }
            if (!userAuthService.isAuthorizedByTeam(Session.getSessionUser(), team.getId(), RoleEnum.SCOUTER)) {
                throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, RoleEnum.SCOUTER.name());
            }
            Player player = playerMapper.dtoToDomain(playerDto);
            Player updatedPlayer = updatePlayerUseCase.execute(player, playerId);
            PlayerDTO updatedPlayerDTO = playerMapper.toDto(updatedPlayer);
            return handleResponse(updatedPlayerDTO).ok();
        } catch (NoSuchElementException ex) {
            throw new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, ex.getMessage());
        }
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
    public ResponseEntity<ApiResponse<Boolean>> deletePlayer(@PathVariable("id") UUID playerId) {
        try {
            Team team = findTeamByPlayerUseCase.execute(playerId);
            if (team == null) {
                throw new TeamException(ErrorEnum.TEAM_BY_PLAYER_NOT_FOUND, playerId.toString());
            }
            if (!userAuthService.isAuthorizedByTeam(Session.getSessionUser(), team.getId(), RoleEnum.SCOUTER)) {
                throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, RoleEnum.SCOUTER.name());
            }
            boolean isDeleted = deletePlayerUseCase.execute(playerId);
            team.getPlayers().remove(playerId);
            updateTeamUseCase.execute(team, team.getId());
            return handleResponse(isDeleted).ok();
        } catch (NoSuchElementException ex) {
            throw new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, playerId.toString());
        }
    }
}
