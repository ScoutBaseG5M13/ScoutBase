package es.dimecresalessis.scoutbase.infrastructure.stat.web;

import es.dimecresalessis.scoutbase.application.player.find.FindPlayerByIdUseCase;
import es.dimecresalessis.scoutbase.application.stat.delete.DeleteStatUseCase;
import es.dimecresalessis.scoutbase.application.stat.find.FindAllStatsByPlayerIdUseCase;
import es.dimecresalessis.scoutbase.application.stat.find.FindStatByIdUseCase;
import es.dimecresalessis.scoutbase.application.stat.update.UpdateStatUseCase;
import es.dimecresalessis.scoutbase.application.team.find.FindTeamByPlayerUseCase;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.player.exception.PlayerException;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.stat.exception.StatException;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.infrastructure.security.UserAuthService;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatDTO;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatUpdateRequest;
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
 * REST Controller for managing stat-related operations.
 */
@RestController
@AllArgsConstructor
@ApiCommonResponses
@Tag(name = "Stat", description = "Stat management endpoints")
@RequestMapping(Routes.API_ROOT + Routes.STATS)
public class StatController {

    private final StatMapper statMapper;
    private final FindAllStatsByPlayerIdUseCase findAllStatsByPlayerIdUseCase;
    private final FindStatByIdUseCase findStatByIdUseCase;
    private final UpdateStatUseCase updateStatUseCase;
    private final DeleteStatUseCase deleteStatUseCase;
    private final UserAuthService userAuthService;
    private final FindPlayerByIdUseCase findPlayerByIdUseCase;
    private final FindTeamByPlayerUseCase findTeamByPlayerUseCase;

    /**
     * Finds all stats of a player.
     *
     * @param playerId The {@link UUID} of the player.
     * @return {@link ApiResponse} containing a list of all {@link Stat}.
     */
    @GetMapping(Routes.PLAYERS + Routes.ID_PATHVAR)
    @Operation(summary = "Find all stats of player [Auth SCOUTER]", description = "Finds all Stats from a Player")
    public ResponseEntity<ApiResponse<List<StatDTO>>> findAllByPlayerId(@PathVariable(name = "id") UUID playerId) {
        Player player = findPlayerByIdUseCase.execute(playerId);
        if (player == null) {
            throw new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, playerId.toString());
        }
        Team team = findTeamByPlayerUseCase.execute(player.getId());
        userAuthService.hasMinimumTeamAuthorization(team.getId(), RoleEnum.SCOUTER);

        List<Stat> stats = findAllStatsByPlayerIdUseCase.execute(playerId);
        List<StatDTO> statsDto = stats.stream().map(statMapper::toDto).toList();
        return handleResponse(statsDto).ok();
    }

    /**
     * Retrieves a specific statistic by its unique identifier.
     *
     * @param statId The {@link UUID} of the statistic.
     * @return {@link ApiResponse} containing the {@link StatDTO} if found.
     */
    @GetMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Find stat by ID [Auth SCOUTER]", description = "Finds a Stat")
    public ResponseEntity<ApiResponse<StatDTO>> findById(@PathVariable("id") UUID statId) {
        Stat stat = findStatByIdUseCase.execute(statId);
        Player player = findPlayerByIdUseCase.execute(stat.getPlayerId());
        Team team = findTeamByPlayerUseCase.execute(player.getId());
        userAuthService.hasMinimumTeamAuthorization(team.getId(), RoleEnum.SCOUTER);

        StatDTO statDto = statMapper.toDto(stat);
        return handleResponse(statDto).ok();
    }

    /**
     * Updates an existing stat.
     *
     * @param updateRequest The updated stat details.
     * @param statId The ID of the stat to be updated.
     * @return {@link ApiResponse} containing the updated stat's details.
     * @throws StatException If the stat is not found.
     */
    @PutMapping(value = Routes.ID_PATHVAR)
    @Operation(summary = "Updates stat [Auth SCOUTER]", description = "Update  Stat")
    public ResponseEntity<ApiResponse<StatDTO>> update(@Valid @RequestBody StatUpdateRequest updateRequest, @PathVariable(value = "id") UUID statId) {
        Stat stat = findStatByIdUseCase.execute(statId);
        if (stat == null) {
            throw new StatException(ErrorEnum.STAT_NOT_FOUND, statId.toString());
        }
        Player player = findPlayerByIdUseCase.execute(stat.getPlayerId());
        Team team = findTeamByPlayerUseCase.execute(player.getId());
        userAuthService.hasMinimumTeamAuthorization(team.getId(), RoleEnum.SCOUTER);

        Stat newStat = statMapper.updateToDomain(updateRequest);
        Stat updatedStat = updateStatUseCase.execute(newStat, statId);
        StatDTO updatedStatDTO = statMapper.toDto(updatedStat);
        return handleResponse(updatedStatDTO).ok();
    }

    /**
     * Deletes a stat record by ID.
     *
     * @param statId The ID of the stat to be deleted.
     * @return {@link ApiResponse} containing {@code true} if the stat was deleted successfully.
     * @throws StatException If the stat is not found.
     */
    @DeleteMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Deletes stat [Auth SCOUTER]", description = "Delete Stat")
    public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable(value = "id") UUID statId) {
        Stat stat = findStatByIdUseCase.execute(statId);
        if (stat == null) {
            throw new StatException(ErrorEnum.STAT_NOT_FOUND, statId.toString());
        }
        Player player = findPlayerByIdUseCase.execute(stat.getPlayerId());
        Team team = findTeamByPlayerUseCase.execute(player.getId());
        userAuthService.hasMinimumTeamAuthorization(team.getId(), RoleEnum.SCOUTER);
        boolean isDeleted = deleteStatUseCase.execute(statId);
        return handleResponse(isDeleted).ok();
    }
}
