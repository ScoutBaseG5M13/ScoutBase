package es.dimecresalessis.scoutbase.infrastructure.team.web;

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

import java.util.List;
import java.util.UUID;

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

    @GetMapping
    @Operation(summary = "Find all teams related to User", description = "Retrieves all teams that the user takes part in")
    public ResponseEntity<ApiResponse<List<TeamDTO>>> findAllTeamsOfUser() {
        List<Team> teams = findAllTeamsByUserUseCase.execute(Session.getSessionUser().getId());
        List<TeamDTO> teamsDto = teams.stream().map(teamMapper::toDto).toList();
        return handleResponse(teamsDto).ok();
    }

    @GetMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Find team by ID", description = "Retrieves a team. Needs a SCOUTER authorization")
    public ResponseEntity<ApiResponse<TeamDTO>> findTeamById(@PathVariable UUID id) {
        Team team = findTeamById.execute(id);
        if (!userAuthService.isAuthorizedByTeam(Session.getSessionUser(), team.getId(), RoleEnum.SCOUTER)) {
            throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, RoleEnum.SCOUTER.name());
        }
        TeamDTO teamDto = teamMapper.toDto(team);
        return handleResponse(teamDto).ok();
    }

    @GetMapping(Routes.PLAYERS + Routes.ID_PATHVAR)
    @Operation(summary = "Find a Team by Player", description = "Retrieves the team where a player takes part")
    public ResponseEntity<ApiResponse<TeamDTO>> findTeamByPlayerId(@PathVariable("id") UUID playerId) {
        Team team = findTeamByPlayerUseCase.execute(playerId);
        TeamDTO teamDto = teamMapper.toDto(team);
        return handleResponse(teamDto).ok();
    }

    @PostMapping
    @Operation(summary = "Create a team", description = "Creates a team in the DB. Needs an ADMIN authorization")
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

    @PutMapping(value = Routes.ID_PATHVAR)
    @Operation(summary = "Update a team", description = "Updates a team in the DB. Needs a TRAINER authorization")
    public ResponseEntity<ApiResponse<TeamDTO>> updateTeam(@RequestBody TeamDTO teamDto, @PathVariable UUID id) {
        if (!userAuthService.isAuthorizedByTeam(Session.getSessionUser(), teamDto.getId(), RoleEnum.TRAINER)) {
            throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, RoleEnum.TRAINER.name());
        }
        Team team = teamMapper.dtoToDomain(teamDto);
        Team updatedTeam = updateTeamUseCase.execute(team, id);
        TeamDTO updatedTeamDto = teamMapper.toDto(updatedTeam);
        return handleResponse(updatedTeamDto).ok();
    }

    @DeleteMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Delete team by ID", description = "Deletes a specific team by their ID. Needs an ADMIN authorization.")
    public ResponseEntity<ApiResponse<Boolean>> deleteTeam(@PathVariable UUID id) {
        if (!userAuthService.isAuthorizedByTeam(Session.getSessionUser(), id, RoleEnum.ADMIN)) {
            throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, RoleEnum.ADMIN.name());
        }
        boolean isDeleted = deleteTeamUseCase.execute(id);
        return handleResponse(isDeleted).ok();
    }
}
