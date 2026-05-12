package es.dimecresalessis.scoutbase.infrastructure.club.web;

import es.dimecresalessis.scoutbase.application.club.delete.DeleteClubUseCase;
import es.dimecresalessis.scoutbase.application.club.find.FindAllClubsByUserClubIdUseCase;
import es.dimecresalessis.scoutbase.application.club.find.FindClubByIdUseCase;
import es.dimecresalessis.scoutbase.application.club.update.UpdateClubUseCase;
import es.dimecresalessis.scoutbase.application.team.create.CreateTeamUseCase;
import es.dimecresalessis.scoutbase.application.userclub.find.FindUserClubByIdUseCase;
import es.dimecresalessis.scoutbase.domain.club.exception.ClubException;
import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.domain.userclub.exception.UserClubException;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.infrastructure.club.web.dto.ClubDTO;
import es.dimecresalessis.scoutbase.infrastructure.club.web.dto.ClubUpdateRequest;
import es.dimecresalessis.scoutbase.infrastructure.club.web.mapper.ClubMapper;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.infrastructure.security.UserAuthService;
import es.dimecresalessis.scoutbase.infrastructure.team.web.dto.TeamCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.team.web.dto.TeamDTO;
import es.dimecresalessis.scoutbase.infrastructure.team.web.mapper.TeamMapper;
import es.dimecresalessis.scoutbase.infrastructure.web.annotation.ApiCommonResponses;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
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
 * REST Controller for managing club-related operations.
 */
@RestController
@AllArgsConstructor
@ApiCommonResponses
@Tag(name = "Club", description = "Club management endpoints")
@RequestMapping(Routes.API_ROOT + Routes.CLUBS)
public class ClubController {

    private final ClubMapper clubMapper;
    private final TeamMapper teamMapper;
    private final FindClubByIdUseCase findClubByIdUseCase;
    private final UpdateClubUseCase updateClubUseCase;
    private final DeleteClubUseCase deleteClubUseCase;
    private final UserAuthService userAuthService;
    private final FindAllClubsByUserClubIdUseCase findAllClubsByUserClubIdUseCase;
    private final FindUserClubByIdUseCase findUserClubByIdUseCase;
    private final CreateTeamUseCase createTeamUseCase;

    /**
     * Finds all clubs.
     *
     * @return {@link ApiResponse} containing a list of all {@link Club}.
     */
    @GetMapping(Routes.USER_CLUBS + Routes.ID_PATHVAR)
    @Operation(summary = "Find all clubs from a user club [Auth SCOUTER]", description = "Finds all Clubs created by a User Club")
    public ResponseEntity<ApiResponse<List<ClubDTO>>> findAll(@PathVariable(value = "id") UUID clubId) {
        userAuthService.hasMinimumClubAuthorization(clubId, RoleEnum.SCOUTER);
        List<Club> clubs = findAllClubsByUserClubIdUseCase.execute(clubId);
        List<ClubDTO> clubDtos = clubs.stream().map(clubMapper::domainToDTO).toList();
        return handleResponse(clubDtos).ok();
    }

    /**
     * Fetches a single club record by ID.
     *
     * @param clubId The ID of the club.
     * @return {@link ApiResponse} containing the club details.
     * @throws ClubException If the requested club does not exist.
     */
    @GetMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Find Club by ID [Auth SCOUTER]", description = "Finds and returns a Club by ID")
    public ResponseEntity<ApiResponse<ClubDTO>> findById(@PathVariable("id") UUID clubId) throws ClubException {
        userAuthService.hasMinimumClubAuthorization(clubId, RoleEnum.SCOUTER);
        try {
            Club club = findClubByIdUseCase.execute(clubId);
            ClubDTO clubDto = clubMapper.domainToDTO(club);
            return handleResponse(clubDto).ok();
        } catch (NoSuchElementException ex) {
            throw new ClubException(ErrorEnum.CLUB_NOT_FOUND, clubId.toString());
        }
    }

    /**
     * Updates an existing club record.
     *
     * @param updateRequest The updated club details.
     * @param id The ID of the club to be updated.
     * @return {@link ApiResponse} containing the updated club's details.
     * @throws ClubException If the club is not found.
     */
    @PutMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Update club by ID [Auth ADMIN]", description = "Updates a Club")
    public ResponseEntity<ApiResponse<ClubDTO>> update(@Valid @RequestBody ClubUpdateRequest updateRequest, @PathVariable(value = "id") UUID id) {
        userAuthService.hasMinimumClubAuthorization(id, RoleEnum.ADMIN);
        try {
            Club updatedClub = updateClubUseCase.execute(clubMapper.updateToDomain(updateRequest), id);
            ClubDTO updatedClubDto = clubMapper.domainToDTO(updatedClub);
            return handleResponse(updatedClubDto).ok();
        } catch (NoSuchElementException ex) {
            throw new ClubException(ErrorEnum.CLUB_NOT_FOUND, ex.getMessage());
        }
    }

    /**
     * Deletes a club record by ID.
     *
     * @param id The ID of the club to be deleted.
     * @return {@link ApiResponse} containing {@code true} if the club was deleted successfully.
     * @throws ClubException If the club is not found.
     */
    @DeleteMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Delete club by ID [Auth ADMIN]", description = "Deletes a Club")
    public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable UUID id) {
        userAuthService.hasMinimumClubAuthorization(id, RoleEnum.ADMIN);
        try {
            boolean isDeleted = deleteClubUseCase.execute(id);
            return handleResponse(isDeleted).ok();
        } catch (NoSuchElementException ex) {
            throw new ClubException(ErrorEnum.CLUB_NOT_FOUND, id.toString());
        }
    }

    /**
     * Creates a new Team
     *
     * @param clubId The ID of the Club to be modified.
     * @param request ClubCreateRequest.
     * @return {@link ApiResponse} containing {@code true} if the UserClub was updated successfully.
     * @throws UserClubException If the userclub is not found.
     */
    @PostMapping(Routes.ID_PATHVAR + Routes.TEAMS)
    @Operation(summary = "Creates a new Team under this Club [Auth SCOUTER]", description = "Creates a new Team under this Club")
    public ResponseEntity<ApiResponse<TeamDTO>> createTeam(@PathVariable(value = "id") UUID clubId, @RequestBody TeamCreateRequest request) {
        Club club = findClubByIdUseCase.execute(clubId);
        if (club == null) {
            throw new UserClubException(ErrorEnum.CLUB_NOT_FOUND, clubId.toString());
        }
        UserClub userClub = findUserClubByIdUseCase.execute(club.getUserClub());
        userAuthService.hasMinimumClubAuthorization(userClub.getId(), RoleEnum.SCOUTER);
        Team team = teamMapper.createToDomain(request);
        Team createdTeam = createTeamUseCase.execute(team, club.getId());
        return handleResponse(teamMapper.domainToDTO(createdTeam)).ok();
    }
}
