package es.dimecresalessis.scoutbase.infrastructure.team.web;

import es.dimecresalessis.scoutbase.application.team.*;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.team.exception.TeamException;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
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
import java.util.NoSuchElementException;
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
    private final FindAllTeamsUseCase findAllTeamsUseCase;
    private final FindTeamByIdUseCase findTeamById;
    private final CreateTeamUseCase createTeamUseCase;
    private final UpdateTeamUseCase updateTeamUseCase;
    private final DeleteTeamUseCase deleteTeamUseCase;
    private final FindAllTeamsByUserId findAllTeamsByUserId;

    @GetMapping
    @Operation(summary = "Find all teams", description = "Retrieves all registered teams in the DB")
    public ResponseEntity<ApiResponse<List<TeamDTO>>> findAll() {
        List<Team> teams = findAllTeamsUseCase.execute();
        List<TeamDTO> teamsDto = teams.stream().map(teamMapper::toDto).toList();
        return handleResponse(teamsDto).ok();
    }

    @GetMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Find a team by ID", description = "Retrieves a teams registred in the DB by ID")
    public ResponseEntity<ApiResponse<TeamDTO>> findById(@PathVariable UUID id) {
        Team team = findTeamById.execute(id);
        TeamDTO teamDto = teamMapper.toDto(team);
        return handleResponse(teamDto).ok();
    }

    @GetMapping(Routes.USERS + Routes.ID_PATHVAR)
    @Operation(summary = "Find all teams by user", description = "Retrieves all registered teams by user in the DB")
    public ResponseEntity<ApiResponse<List<TeamDTO>>> findAllTeamsByUserId(@PathVariable("id") UUID userId) {
        List<Team> teams = findAllTeamsByUserId.execute(userId);
        List<TeamDTO> teamsDto = teams.stream().map(teamMapper::toDto).toList();
        return handleResponse(teamsDto).ok();
    }

    @PostMapping
    @Operation(summary = "Create a team", description = "Creates a team in the DB")
    public ResponseEntity<ApiResponse<TeamDTO>> create(@RequestBody TeamCreateRequest teamRequest) {
        Team team = teamMapper.createToDomain(teamRequest);
        Team createdTeam = createTeamUseCase.execute(team);
        TeamDTO createdTeamDto = teamMapper.toDto(createdTeam);
        return handleResponse(createdTeamDto).created();
    }

    @PutMapping(value = Routes.ID_PATHVAR)
    @Operation(summary = "Update a team", description = "Updates a team in the DB")
    public ResponseEntity<ApiResponse<TeamDTO>> update(@RequestBody TeamDTO teamDto, @PathVariable UUID id) {
        try {
            Team team = teamMapper.dtoToDomain(teamDto);
            Team updatedTeam = updateTeamUseCase.execute(team, id);
            TeamDTO updatedTeamDto = teamMapper.toDto(updatedTeam);
            return handleResponse(updatedTeamDto).ok();
        } catch (NoSuchElementException ex) {
            throw new TeamException(ErrorEnum.TEAM_NOT_FOUND, ex.getMessage());
        }
    }

    @DeleteMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Delete team by ID", description = "Deletes a specific team by their ID")
    public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable UUID id) {
        try {
            boolean isDeleted = deleteTeamUseCase.execute(id);
            return handleResponse(isDeleted).ok();
        } catch (NoSuchElementException ex) {
            throw new TeamException(ErrorEnum.TEAM_NOT_FOUND, id.toString());
        }
    }
}
