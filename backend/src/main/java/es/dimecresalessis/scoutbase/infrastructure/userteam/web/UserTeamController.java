package es.dimecresalessis.scoutbase.infrastructure.userteam.web;

import es.dimecresalessis.scoutbase.application.userclub.find.FindUserClubByIdUseCase;
import es.dimecresalessis.scoutbase.application.userclub.find.FindUserClubByUserTeamUseCase;
import es.dimecresalessis.scoutbase.application.userclub.update.UpdateUserClubUseCase;
import es.dimecresalessis.scoutbase.application.userteam.find.FindAllUserTeamsByUserClubUseCase;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.domain.userteam.exception.UserTeamException;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.infrastructure.security.UserAuthService;
import es.dimecresalessis.scoutbase.application.userteam.create.CreateUserTeamUseCase;
import es.dimecresalessis.scoutbase.application.userteam.delete.DeleteUserTeamUseCase;
import es.dimecresalessis.scoutbase.application.userteam.find.FindAllUserTeamsByUserUseCase;
import es.dimecresalessis.scoutbase.application.userteam.find.FindUserTeamByIdUseCase;
import es.dimecresalessis.scoutbase.application.userteam.update.UpdateUserTeamUseCase;
import es.dimecresalessis.scoutbase.domain.userclub.exception.UserClubException;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.infrastructure.security.Session;
import es.dimecresalessis.scoutbase.infrastructure.userteam.web.dto.UserTeamCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.userteam.web.dto.UserTeamDTO;
import es.dimecresalessis.scoutbase.infrastructure.userteam.web.dto.UserTeamUpdateRequest;
import es.dimecresalessis.scoutbase.infrastructure.userteam.web.mapper.UserTeamMapper;
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
 * REST Controller for managing UserTeam-related operations.
 */
@RestController
@AllArgsConstructor
@ApiCommonResponses
@Tag(name = "Teams", description = "Team management endpoints")
@RequestMapping(Routes.API_ROOT + Routes.USER_TEAMS)
public class UserTeamController {

    private final UserTeamMapper userTeamMapper;
    private final FindUserTeamByIdUseCase findTeamById;
    private final CreateUserTeamUseCase createUserTeamUseCase;
    private final UpdateUserTeamUseCase updateUserTeamUseCase;
    private final DeleteUserTeamUseCase deleteUserTeamUseCase;
    private final FindAllUserTeamsByUserUseCase findAllUserTeamsByUserUseCase;
    private final FindUserClubByIdUseCase findUserClubByIdUseCase;
    private final UserAuthService userAuthService;
    private final UpdateUserClubUseCase updateUserClubUseCase;
    private final FindAllUserTeamsByUserClubUseCase findAllUserTeamsByUserClubUseCase;
    private final FindUserClubByUserTeamUseCase findUserClubByUserTeamUseCase;
    private final FindUserTeamByIdUseCase findUserTeamByIdUseCase;

    /**
     * Retrieves all teams associated with the currently authenticated user.
     *
     * @return {@link ApiResponse} containing the list of {@link UserTeamDTO}.
     */
    @GetMapping
    @Operation(summary = "Find all user teams [Auth SUPERADMIN]", description = "Finds all UserTeams in the system")
    public ResponseEntity<ApiResponse<List<UserTeamDTO>>> findAll() {
        if (userAuthService.isSuperadmin()) {
            List<UserTeam> userTeams = findAllUserTeamsByUserUseCase.execute(Session.getSessionUser().getId());
            List<UserTeamDTO> teamsDto = userTeams.stream().map(userTeamMapper::toDto).toList();
            return handleResponse(teamsDto).ok();
        } else {
            throw new UserException(ErrorEnum.USER_HAS_NOT_AUTHORIZATION, RoleEnum.SUPERADMIN.name());
        }
    }

    /**
     * Retrieves all teams belonging to a specific userclub, filtered by user access.
     *
     * @param clubId The {@link UUID} of the userclub.
     * @return {@link ApiResponse} containing the filtered list of {@link UserTeamDTO}.
     */
    @GetMapping(Routes.USER_CLUBS + Routes.ID_PATHVAR)
    @Operation(summary = "Find all user teams by user club [Auth SCOUTER]", description = "Finds all UserTeams by UserClub")
    public ResponseEntity<ApiResponse<List<UserTeamDTO>>> findAllByUserClub(@PathVariable("id") UUID clubId) {
        userAuthService.hasMinimumClubAuthorization(clubId, RoleEnum.SCOUTER);
        List<UserTeam> userTeams = findAllUserTeamsByUserClubUseCase.execute(clubId);
        List<UserTeamDTO> userTeamsDto = userTeams.stream().map(userTeamMapper::toDto).toList();
        return handleResponse(userTeamsDto).ok();
    }

    /**
     * Finds a userteam by its ID.
     *
     * @param teamId The {@link UUID} of the userteam.
     * @return {@link ApiResponse} with the {@link UserTeamDTO}.
     * @throws UserException if the user lacks sufficient permissions.
     */
    @GetMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Find UserTeam by ID [Auth SCOUTER]", description = "Finds a UserTeam")
    public ResponseEntity<ApiResponse<UserTeamDTO>> findById(@PathVariable(value = "id") UUID teamId) {
        UserTeam userTeam = findTeamById.execute(teamId);
        userAuthService.hasMinimumTeamAuthorization(teamId, RoleEnum.SCOUTER);
        UserTeamDTO userTeamDto = userTeamMapper.toDto(userTeam);
        return handleResponse(userTeamDto).ok();
    }

    /**
     * Creates a new userteam within a userclub.
     *
     * @param teamRequest The creation details.
     * @return {@link ApiResponse} with the created {@link UserTeamDTO}.
     * @throws UserException if the user is not a userclub admin.
     * @throws UserClubException if the specified userclub does not exist.
     */
    @PostMapping(Routes.USER_CLUBS + Routes.ID_PATHVAR)
    @Operation(summary = "Create a user team [Auth SCOUTER]", description = "Creates a UserTeam")
    public ResponseEntity<ApiResponse<UserTeamDTO>> create(@PathVariable("id") UUID clubId, @RequestBody UserTeamCreateRequest teamRequest) {
        UserClub userClub = findUserClubByIdUseCase.execute(clubId);
        if (userClub == null) {
            throw new UserClubException(ErrorEnum.CLUB_NOT_FOUND, clubId.toString());
        }
        userAuthService.hasMinimumClubAuthorization(userClub.getId(), RoleEnum.SCOUTER);
        UserTeam userTeam = userTeamMapper.createToDomain(teamRequest);
        UserTeam createdUserTeam = createUserTeamUseCase.execute(userTeam, userClub);
        UserTeamDTO createdUserTeamDto = userTeamMapper.toDto(createdUserTeam);
        return handleResponse(createdUserTeamDto).created();
    }

    /**
     * Updates an existing userteam.
     *
     * @param userTeamUpdateRequest The updated data request.
     * @param teamId The {@link UUID} of the userteam to update.
     * @return {@link ApiResponse} with the updated {@link UserTeamDTO}.
     */
    @PutMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Updates a user team [Auth TRAINER]", description = "Updates a UserTeam")
    public ResponseEntity<ApiResponse<UserTeamDTO>> update(@PathVariable("id") UUID teamId, @RequestBody UserTeamUpdateRequest userTeamUpdateRequest) {
        UserTeam userTeam = userTeamMapper.updateToDomain(userTeamUpdateRequest);
        userAuthService.hasMinimumTeamAuthorization(teamId, RoleEnum.TRAINER);
        UserTeam updatedUserTeam = updateUserTeamUseCase.execute(userTeam, teamId);
        UserTeamDTO updatedUserTeamDto = userTeamMapper.toDto(updatedUserTeam);
        return handleResponse(updatedUserTeamDto).ok();
    }

    /**
     * Deletes a user team.
     *
     * @param teamId The {@link UUID} of the userteam to delete.
     * @return {@link ApiResponse} indicating success.
     */
    @DeleteMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Delete Team [Auth ADMIN]", description = "Deletes a Team")
    public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable("id") UUID teamId) {
        UserClub userClub = findUserClubByUserTeamUseCase.execute(teamId);
        userAuthService.hasMinimumClubAuthorization(userClub.getId(), RoleEnum.ADMIN);
        boolean isDeleted = deleteUserTeamUseCase.execute(teamId);
        userClub.getUserTeams().remove(teamId);
        updateUserClubUseCase.execute(userClub, userClub.getId());
        return handleResponse(isDeleted).ok();
    }

    /**
     * Sets a User as trainer of a user team.
     *
     * @param teamId The team to be modified.
     * @param userId The user to be assigned as trainer.
     * @return {@link ApiResponse} with the updated {@link UserTeamDTO}.
     */
    @PutMapping(Routes.ID_PATHVAR + Routes.TRAINER_PATH + Routes.ID_TWO_PATHVAR)
    @Operation(summary = "Set a trainer to the user team [Auth ADMIN]", description = "Set trainer to a UserTeam")
    public ResponseEntity<ApiResponse<UserTeamDTO>> setTrainer(@PathVariable("id") UUID teamId, @PathVariable(value = "id2") UUID userId) {
        UserTeam userTeam = findUserTeamByIdUseCase.execute(teamId);
        if (userTeam == null) {
            throw new UserTeamException(ErrorEnum.TEAM_NOT_FOUND, teamId.toString());
        }
        userAuthService.hasMinimumTeamAuthorization(teamId, RoleEnum.ADMIN);
        if (findUserTeamByIdUseCase.execute(teamId) == null) {
            throw new UserException(ErrorEnum.USER_NOT_FOUND, userId.toString());
        }

        userTeam.setTrainer(userId);
        UserTeam updatedUserTeam = updateUserTeamUseCase.execute(userTeam, teamId);
        UserTeamDTO updatedUserTeamDto = userTeamMapper.toDto(updatedUserTeam);
        return handleResponse(updatedUserTeamDto).ok();
    }

    /**
     * Sets a User as second trainer of a user team.
     *
     * @param teamId The team to be modified.
     * @param userId The user to be assigned as second trainer.
     * @return {@link ApiResponse} with the updated {@link UserTeamDTO}.
     */
    @PutMapping(Routes.ID_PATHVAR + Routes.SECOND_TRAINER_PATH + Routes.ID_TWO_PATHVAR)
    @Operation(summary = "Set a second trainer to the user team [Auth ADMIN]", description = "Set second trainer to a UserTeam")
    public ResponseEntity<ApiResponse<UserTeamDTO>> setSecondTrainer(@PathVariable("id") UUID teamId, @PathVariable(value = "id2") UUID userId) {
        UserTeam userTeam = findUserTeamByIdUseCase.execute(teamId);
        if (userTeam == null) {
            throw new UserTeamException(ErrorEnum.TEAM_NOT_FOUND, teamId.toString());
        }
        userAuthService.hasMinimumTeamAuthorization(teamId, RoleEnum.ADMIN);
        if (findUserTeamByIdUseCase.execute(teamId) == null) {
            throw new UserException(ErrorEnum.USER_NOT_FOUND, userId.toString());
        }

        userTeam.setSecondTrainer(userId);
        UserTeam updatedUserTeam = updateUserTeamUseCase.execute(userTeam, teamId);
        UserTeamDTO updatedUserTeamDto = userTeamMapper.toDto(updatedUserTeam);
        return handleResponse(updatedUserTeamDto).ok();
    }

    /**
     * Adds a User as scouter of a user team.
     *
     * @param teamId The team to be modified.
     * @param userId The user to be assigned as scouter.
     * @return {@link ApiResponse} with the updated {@link UserTeamDTO}.
     */
    @PostMapping(Routes.ID_PATHVAR + Routes.SCOUTER_PATH + Routes.ID_TWO_PATHVAR)
    @Operation(summary = "Adds a scouter to the user team [Auth ADMIN]", description = "Adds a scouter to a UserTeam")
    public ResponseEntity<ApiResponse<UserTeamDTO>> addScouter(@PathVariable("id") UUID teamId, @PathVariable(value = "id2") UUID userId) {
        UserTeam userTeam = findUserTeamByIdUseCase.execute(teamId);
        if (userTeam == null) {
            throw new UserTeamException(ErrorEnum.TEAM_NOT_FOUND, teamId.toString());
        }
        userAuthService.hasMinimumTeamAuthorization(teamId, RoleEnum.ADMIN);
        if (findUserTeamByIdUseCase.execute(teamId) == null) {
            throw new UserException(ErrorEnum.USER_NOT_FOUND, userId.toString());
        }
        userTeam.getScouters().add(userId);
        UserTeam updatedUserTeam = updateUserTeamUseCase.execute(userTeam, teamId);
        UserTeamDTO updatedUserTeamDto = userTeamMapper.toDto(updatedUserTeam);
        return handleResponse(updatedUserTeamDto).ok();
    }

    /**
     * Removes a User as scouter of a user team.
     *
     * @param teamId The team to be modified.
     * @param userId The user to be removed as scouter.
     * @return {@link ApiResponse} with the updated {@link UserTeamDTO}.
     */
    @DeleteMapping(Routes.ID_PATHVAR + Routes.SCOUTER_PATH + Routes.ID_TWO_PATHVAR)
    @Operation(summary = "Removes a scouter to the user team [Auth ADMIN]", description = "Removes a scouter to a UserTeam")
    public ResponseEntity<ApiResponse<UserTeamDTO>> removeScouter(@PathVariable("id") UUID teamId, @PathVariable(value = "id2") UUID userId) {
        UserTeam userTeam = findUserTeamByIdUseCase.execute(teamId);
        if (userTeam == null) {
            throw new UserTeamException(ErrorEnum.TEAM_NOT_FOUND, teamId.toString());
        }
        userAuthService.hasMinimumTeamAuthorization(teamId, RoleEnum.ADMIN);
        if (findUserTeamByIdUseCase.execute(teamId) == null) {
            throw new UserException(ErrorEnum.USER_NOT_FOUND, userId.toString());
        }
        userTeam.getScouters().remove(userId);
        UserTeam updatedUserTeam = updateUserTeamUseCase.execute(userTeam, teamId);
        UserTeamDTO updatedUserTeamDto = userTeamMapper.toDto(updatedUserTeam);
        return handleResponse(updatedUserTeamDto).ok();
    }
}
