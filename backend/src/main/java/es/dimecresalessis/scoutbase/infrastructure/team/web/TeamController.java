package es.dimecresalessis.scoutbase.infrastructure.team.web;

import es.dimecresalessis.scoutbase.application.club.find.FindClubByTeamUseCase;
import es.dimecresalessis.scoutbase.application.player.create.CreatePlayerUseCase;
import es.dimecresalessis.scoutbase.application.player.find.FindAllPlayersByTeamIdUseCase;
import es.dimecresalessis.scoutbase.application.stat.service.CalculateAverageScore;
import es.dimecresalessis.scoutbase.application.team.delete.DeleteTeamUseCase;
import es.dimecresalessis.scoutbase.application.team.find.FindAllTeamsByClubUseCase;
import es.dimecresalessis.scoutbase.application.team.find.FindTeamByIdUseCase;
import es.dimecresalessis.scoutbase.application.team.find.FindTeamByPlayerUseCase;
import es.dimecresalessis.scoutbase.application.team.update.UpdateTeamUseCase;
import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.player.exception.PlayerException;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.team.exception.TeamException;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDTO;
import es.dimecresalessis.scoutbase.infrastructure.player.web.mapper.PlayerMapper;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.infrastructure.security.UserAuthService;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.PlayerAverageStatScoreDTO;
import es.dimecresalessis.scoutbase.infrastructure.team.web.dto.CategoryEnumDTO;
import es.dimecresalessis.scoutbase.infrastructure.team.web.dto.TeamDTO;
import es.dimecresalessis.scoutbase.infrastructure.team.web.dto.TeamUpdateRequest;
import es.dimecresalessis.scoutbase.infrastructure.team.web.mapper.CategoryMapper;
import es.dimecresalessis.scoutbase.infrastructure.team.web.mapper.TeamMapper;
import es.dimecresalessis.scoutbase.infrastructure.web.annotation.ApiCommonResponses;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static es.dimecresalessis.scoutbase.infrastructure.web.dto.ResponseFactory.handleResponse;

/**
 * REST Controller for managing team-related operations.
 */
@RestController
@AllArgsConstructor
@ApiCommonResponses
@Tag(name = "Team", description = "Team management endpoints")
@RequestMapping(Routes.API_ROOT + Routes.TEAMS)
public class TeamController {

    private final TeamMapper teamMapper;
    private final PlayerMapper playerMapper;
    private final CategoryMapper categoryMapper;
    private final FindTeamByIdUseCase findTeamById;
    private final UpdateTeamUseCase updateTeamUseCase;
    private final DeleteTeamUseCase deleteTeamUseCase;
    private final FindTeamByPlayerUseCase findTeamByPlayerUseCase;
    private final UserAuthService userAuthService;
    private final FindAllTeamsByClubUseCase findAllTeamsByClubUseCase;
    private final FindClubByTeamUseCase findClubByTeamUseCase;
    private final CreatePlayerUseCase createPlayerUseCase;
    private final FindAllPlayersByTeamIdUseCase findAllPlayersByTeamIdUseCase;
    private final CalculateAverageScore calculateAverageScore;

    /**
     * Retrieves all teams belonging to a specific club, filtered by user access.
     *
     * @param clubId The {@link UUID} of the club.
     * @return {@link ApiResponse} containing the filtered list of {@link TeamDTO}.
     */
    @GetMapping(Routes.CLUBS + Routes.ID_PATHVAR)
    @Operation(summary = "Find all Teams by Club [Auth SCOUTER]", description = "Finds all Teams by Club")
    public ResponseEntity<ApiResponse<List<TeamDTO>>> findAll(@PathVariable("id") UUID clubId) {
        userAuthService.hasMinimumClubAuthorization(clubId, RoleEnum.SCOUTER);
        List<Team> teams = findAllTeamsByClubUseCase.execute(clubId);
        List<TeamDTO> teamsDto = teams.stream()
                .map(teamMapper::domainToDTO)
                .toList();

        return handleResponse(teamsDto).ok();
    }

    /**
     * Finds a team by its ID.
     *
     * @param teamId The {@link UUID} of the team.
     * @return {@link ApiResponse} with the {@link TeamDTO}.
     * @throws UserException if the user lacks sufficient permissions.
     */
    @GetMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Find Team by ID [Auth SCOUTER]", description = "Finds a Team")
    public ResponseEntity<ApiResponse<TeamDTO>> findById(@PathVariable(value = "id") UUID teamId) {
        Team team = findTeamById.execute(teamId);
        Club club = findClubByTeamUseCase.execute(teamId);
        userAuthService.hasMinimumClubAuthorization(club.getUserClub(), RoleEnum.SCOUTER);
        TeamDTO teamDto = teamMapper.domainToDTO(team);
        return handleResponse(teamDto).ok();
    }

    /**
     * Retrieves the team associated with a specific player.
     *
     * @param playerId The {@link UUID} of the player.
     * @return {@link ApiResponse} with the {@link TeamDTO}.
     */
    @GetMapping(Routes.PLAYERS + Routes.ID_PATHVAR)
    @Operation(summary = "Find Team by Player [Auth SCOUTER]", description = "Finds the Team by User")
    public ResponseEntity<ApiResponse<TeamDTO>> findByPlayerId(@PathVariable("id") UUID playerId) {
        Team team = findTeamByPlayerUseCase.execute(playerId);
        Club club = findClubByTeamUseCase.execute(team.getId());
        userAuthService.hasMinimumClubAuthorization(club.getUserClub(), RoleEnum.SCOUTER);
        TeamDTO teamDto = teamMapper.domainToDTO(team);
        return handleResponse(teamDto).ok();
    }

    /**
     * Updates an existing team.
     *
     * @param updateRequest The updated data request.
     * @param teamId The {@link UUID} of the team to update.
     * @return {@link ApiResponse} with the updated {@link TeamDTO}.
     */
    @PutMapping(value = Routes.ID_PATHVAR)
    @Operation(summary = "Updates a team [Auth TRAINER]", description = "Updates a Team")
    public ResponseEntity<ApiResponse<TeamDTO>> update(@RequestBody TeamUpdateRequest updateRequest, @PathVariable("id") UUID teamId) {
        Team team = teamMapper.updateToDomain(updateRequest);
        Club club = findClubByTeamUseCase.execute(teamId);
        userAuthService.hasMinimumClubAuthorization(club.getUserClub(), RoleEnum.TRAINER);
        Team updatedTeam = updateTeamUseCase.execute(team, teamId);
        TeamDTO updatedTeamDto = teamMapper.domainToDTO(updatedTeam);
        return handleResponse(updatedTeamDto).ok();
    }

    /**
     * Deletes a team.
     *
     * @param teamId The {@link UUID} of the team to delete.
     * @return {@link ApiResponse} indicating success.
     */
    @DeleteMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Deletes a team [Auth TRAINER]", description = "Deletes a Team")
    public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable("id") UUID teamId) {
        Club club = findClubByTeamUseCase.execute(teamId);
        userAuthService.hasMinimumClubAuthorization(club.getId(), RoleEnum.TRAINER);
        boolean isDeleted = deleteTeamUseCase.execute(teamId);
        return handleResponse(isDeleted).ok();
    }

    /**
     * Creates a Player in the Team.
     *
     * @param createPlayerRequest The player data create request.
     * @return {@link ApiResponse} containing the created player's details.
     * @throws PlayerException If an error occurs during player creation.
     */
    @PostMapping(Routes.ID_PATHVAR + Routes.PLAYERS)
    @Operation(summary = "Creates a player [Auth SCOUTER]", description = "Create Player")
    public ResponseEntity<ApiResponse<PlayerDTO>> createPlayer(@Valid @RequestBody PlayerCreateRequest createPlayerRequest, @PathVariable("id") UUID teamId) {
        Team team = findTeamById.execute(teamId);
        if (team == null) {
            throw new TeamException(ErrorEnum.TEAM_NOT_FOUND, teamId.toString());
        }
        Club club = findClubByTeamUseCase.execute(teamId);

        userAuthService.hasMinimumClubAuthorization(club.getUserClub(), RoleEnum.SCOUTER);
        Player player = playerMapper.createToDomain(createPlayerRequest, teamId);
        Player createdPlayer = createPlayerUseCase.execute(player, teamId);
        team.getPlayers().add(createdPlayer.getId());
        updateTeamUseCase.execute(team, teamId);
        PlayerDTO createdPlayerDTO = playerMapper.toDto(createdPlayer);
        return handleResponse(createdPlayerDTO).created();
    }

    /**
     * Returns the available Categories and its Subcategories
     *
     * @return {@link ApiResponse} containing all categories.
     * @throws PlayerException If an error occurs during category retrieval.
     */
    @GetMapping(Routes.CATEGORIES)
    @Operation(summary = "Gets all categories", description = "Get all CategoryEnum")
    public ResponseEntity<ApiResponse<List<CategoryEnumDTO>>> getCategories() {
        List<CategoryEnumDTO> categories = Arrays.stream(CategoryEnum.values()).map(categoryMapper::domainToDTO).toList();
        return handleResponse(categories).ok();
    }

    /**
     * Returns the available Categories and its Subcategories
     *
     * @return {@link ApiResponse} containing all categories.
     * @throws PlayerException If an error occurs during category retrieval.
     */
    @GetMapping( Routes.ID_PATHVAR + Routes.STATS)
    @Operation(summary = "Gets all player average stats in team", description = "Get all Player average Stats in the Team")
    public ResponseEntity<ApiResponse<List<PlayerAverageStatScoreDTO>>> getAllPlayerAverageStats(@PathVariable("id") UUID teamId) {
        List<Player> players = findAllPlayersByTeamIdUseCase.execute(teamId);
        List<PlayerAverageStatScoreDTO> averageScores = players.stream()
                .map(player -> calculateAverageScore.execute(player.getId()))
                .toList();
        return handleResponse(averageScores).ok();
    }
}
