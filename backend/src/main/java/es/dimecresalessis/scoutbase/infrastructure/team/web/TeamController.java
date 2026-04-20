package es.dimecresalessis.scoutbase.infrastructure.team.web;

import es.dimecresalessis.scoutbase.application.club.find.FindAllClubsByUserUseCase;
import es.dimecresalessis.scoutbase.application.club.find.FindClubByIdUseCase;
import es.dimecresalessis.scoutbase.application.security.UserAuthService;
import es.dimecresalessis.scoutbase.application.team.create.CreateTeamUseCase;
import es.dimecresalessis.scoutbase.application.team.delete.DeleteTeamUseCase;
import es.dimecresalessis.scoutbase.application.team.find.FindAllTeamsByUserUseCase;
import es.dimecresalessis.scoutbase.application.team.find.FindTeamByIdUseCase;
import es.dimecresalessis.scoutbase.application.team.find.FindTeamByPlayerUseCase;
import es.dimecresalessis.scoutbase.application.team.update.UpdateTeamUseCase;
import es.dimecresalessis.scoutbase.domain.club.exception.ClubException;
import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.infrastructure.security.Session;
import es.dimecresalessis.scoutbase.infrastructure.team.web.dto.TeamCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.team.web.dto.TeamDTO;
import es.dimecresalessis.scoutbase.infrastructure.team.web.mapper.TeamMapper;
import es.dimecresalessis.scoutbase.infrastructure.web.annotation.ApiCommonResponses;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static es.dimecresalessis.scoutbase.infrastructure.web.dto.ResponseFactory.handleResponse;

/**
 * REST Controller for managing team-related operations.
 */
@RestController
@AllArgsConstructor
@ApiCommonResponses
@Tag(name = "Teams", description = "Team management endpoints")
@RequestMapping(Routes.API_ROOT + Routes.TEAMS)
public class TeamController {

    private final TeamMapper teamMapper;
    private final FindTeamByIdUseCase findTeamById;
    private final CreateTeamUseCase createTeamUseCase;
    private final UpdateTeamUseCase updateTeamUseCase;
    private final DeleteTeamUseCase deleteTeamUseCase;
    private final FindAllTeamsByUserUseCase findAllTeamsByUserUseCase;
    private final FindTeamByPlayerUseCase findTeamByPlayerUseCase;
    private final FindClubByIdUseCase findClubByIdUseCase;
    private final UserAuthService userAuthService;
    private final FindAllClubsByUserUseCase findAllClubsByUserUseCase;

    /**
     * Retrieves all teams associated with the currently authenticated user.
     *
     * @return {@link ApiResponse} containing the list of {@link TeamDTO}.
     */
    @GetMapping
    @Operation(summary = "Find all teams [Auth SCOUTER]", description = "Finds all Teams")
    public ResponseEntity<ApiResponse<List<TeamDTO>>> findAllTeamsByUser() {
        List<Team> teams = findAllTeamsByUserUseCase.execute(Session.getSessionUser().getId());
        List<TeamDTO> teamsDto = teams.stream().map(teamMapper::toDto).toList();
        return handleResponse(teamsDto).ok();
    }

    /**
     * Retrieves all teams belonging to a specific club, filtered by user access.
     *
     * @param clubId The {@link UUID} of the club.
     * @return {@link ApiResponse} containing the filtered list of {@link TeamDTO}.
     */
    @GetMapping(Routes.CLUBS + Routes.ID_PATHVAR)
    @Operation(summary = "Find all Teams by Club [Auth SCOUTER]", description = "Finds all Teams by Club")
    public ResponseEntity<ApiResponse<List<TeamDTO>>> findAllTeamsByClub(@PathVariable(value = "id") UUID clubId) {
        List<Club> userClubs = findAllClubsByUserUseCase.execute(Session.getSessionUser().getId());

        Optional<Club> targetClub = userClubs.stream()
                .filter(c -> c.getId().equals(clubId))
                .findFirst();

        if (targetClub.isEmpty()) {
            return handleResponse(Collections.<TeamDTO>emptyList()).ok();
        }

        List<Team> allTeams = findAllTeamsByUserUseCase.execute(Session.getSessionUser().getId());

        List<TeamDTO> teamsDto = allTeams.stream()
                .filter(team -> targetClub.get().getTeams().contains(team.getId()))
                .map(teamMapper::toDto)
                .toList();

        return handleResponse(teamsDto).ok();
    }

    /**
     * Finds a team by its ID.
     *
     * @param id The {@link UUID} of the team.
     * @return {@link ApiResponse} with the {@link TeamDTO}.
     * @throws UserException if the user lacks sufficient permissions.
     */
    @GetMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Find Team by ID [Auth SCOUTER]", description = "Finds a Team")
    public ResponseEntity<ApiResponse<TeamDTO>> findTeamById(@PathVariable UUID id) {
        Team team = findTeamById.execute(id);
        if (!userAuthService.isAuthorizedByTeam(Session.getSessionUser(), team.getId(), RoleEnum.SCOUTER)) {
            throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, RoleEnum.SCOUTER.name());
        }
        TeamDTO teamDto = teamMapper.toDto(team);
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
    public ResponseEntity<ApiResponse<TeamDTO>> findTeamByPlayerId(@PathVariable("id") UUID playerId) {
        Team team = findTeamByPlayerUseCase.execute(playerId);
        TeamDTO teamDto = teamMapper.toDto(team);
        return handleResponse(teamDto).ok();
    }

    /**
     * Creates a new team within a club.
     *
     * @param teamRequest The creation details.
     * @return {@link ApiResponse} with the created {@link TeamDTO}.
     * @throws UserException if the user is not a club admin.
     * @throws ClubException if the specified club does not exist.
     */
    @PostMapping
    @Operation(summary = "Create a Team [Auth ADMIN]", description = "Creates a Team")
    public ResponseEntity<ApiResponse<TeamDTO>> createTeam(@RequestBody TeamCreateRequest teamRequest) {
        if (!userAuthService.isAuthorizedByClub(Session.getSessionUser(), teamRequest.getClubId(), RoleEnum.ADMIN)) {
            throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, RoleEnum.ADMIN.name());
        }

        Club club = findClubByIdUseCase.execute(teamRequest.getClubId());
        if (club == null) {
            throw new ClubException(ErrorEnum.CLUB_NOT_FOUND, teamRequest.getClubId().toString());
        }

        Team team = teamMapper.createToDomain(teamRequest);
        Team createdTeam = createTeamUseCase.execute(team, club);
        TeamDTO createdTeamDto = teamMapper.toDto(createdTeam);
        return handleResponse(createdTeamDto).created();
    }

    /**
     * Updates an existing team.
     *
     * @param teamDto The updated data.
     * @param id The {@link UUID} of the team to update.
     * @return {@link ApiResponse} with the updated {@link TeamDTO}.
     */
    @PutMapping(value = Routes.ID_PATHVAR)
    @Operation(summary = "Update Team [Auth TRAINER]", description = "Updates a Team")
    public ResponseEntity<ApiResponse<TeamDTO>> updateTeam(@RequestBody TeamDTO teamDto, @PathVariable UUID id) {
        if (!userAuthService.isAuthorizedByTeam(Session.getSessionUser(), teamDto.getId(), RoleEnum.TRAINER)) {
            throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, RoleEnum.TRAINER.name());
        }
        Team team = teamMapper.dtoToDomain(teamDto);
        Team updatedTeam = updateTeamUseCase.execute(team, id);
        TeamDTO updatedTeamDto = teamMapper.toDto(updatedTeam);
        return handleResponse(updatedTeamDto).ok();
    }

    /**
     * Deletes a team.
     *
     * @param id The {@link UUID} of the team to delete.
     * @return {@link ApiResponse} indicating success.
     */
    @DeleteMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Delete Team [Auth ADMIN]", description = "Deletes a Team")
    public ResponseEntity<ApiResponse<Boolean>> deleteTeam(@PathVariable UUID id) {
        if (!userAuthService.isAuthorizedByTeam(Session.getSessionUser(), id, RoleEnum.ADMIN)) {
            throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, RoleEnum.ADMIN.name());
        }
        boolean isDeleted = deleteTeamUseCase.execute(id);
        return handleResponse(isDeleted).ok();
    }
}
