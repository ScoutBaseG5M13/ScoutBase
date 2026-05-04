package es.dimecresalessis.scoutbase.infrastructure.userclub.web;

import es.dimecresalessis.scoutbase.application.club.create.CreateClubUseCase;
import es.dimecresalessis.scoutbase.application.userclub.create.CreateUserClubUseCase;
import es.dimecresalessis.scoutbase.application.userclub.delete.DeleteUserClubUseCase;
import es.dimecresalessis.scoutbase.application.userclub.find.FindAllUserClubsByUserUseCase;
import es.dimecresalessis.scoutbase.application.userclub.find.FindAllUserClubsUseCase;
import es.dimecresalessis.scoutbase.application.userclub.find.FindUserClubByIdUseCase;
import es.dimecresalessis.scoutbase.application.userclub.update.UpdateUserClubUseCase;
import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.infrastructure.club.web.dto.ClubCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.club.web.dto.ClubDTO;
import es.dimecresalessis.scoutbase.infrastructure.club.web.mapper.ClubMapper;
import es.dimecresalessis.scoutbase.infrastructure.security.Session;
import es.dimecresalessis.scoutbase.infrastructure.security.UserAuthService;
import es.dimecresalessis.scoutbase.domain.userclub.exception.UserClubException;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.infrastructure.userclub.web.dto.UserClubCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.userclub.web.dto.UserClubDTO;
import es.dimecresalessis.scoutbase.infrastructure.userclub.web.dto.UserClubUpdateRequest;
import es.dimecresalessis.scoutbase.infrastructure.userclub.web.mapper.UserClubMapper;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
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
 * REST Controller for managing UserClub-related operations.
 */
@RestController
@AllArgsConstructor
@ApiCommonResponses
@Tag(name = "Club", description = "Club management endpoints")
@RequestMapping(Routes.API_ROOT + Routes.USER_CLUBS)
public class UserClubController {

    private final UserClubMapper userClubMapper;
    private final FindAllUserClubsUseCase findAllUserClubsUseCase;
    private final FindUserClubByIdUseCase findUserClubByIdUseCase;
    private final CreateUserClubUseCase createUserClubUseCase;
    private final UpdateUserClubUseCase updateUserClubUseCase;
    private final DeleteUserClubUseCase deleteUserClubUseCase;
    private final UserAuthService userAuthService;
    private final FindAllUserClubsByUserUseCase findAllUserClubsByUserUseCase;
    private final CreateClubUseCase createClubUseCase;
    private final ClubMapper clubMapper;

    /**
     * Finds all clubs.
     *
     * @return {@link ApiResponse} containing a list of all {@link UserClub}.
     */
    @GetMapping
    @Operation(summary = "Finds all user clubs", description = "Finds all UserClubs where the logged User takes part in")
    public ResponseEntity<ApiResponse<List<UserClubDTO>>> findAll() {
        List<UserClub> userClubs;
        if (userAuthService.isSuperadmin()) {
            userClubs = findAllUserClubsUseCase.execute();
        } else {
            userClubs = findAllUserClubsByUserUseCase.execute(Session.getSessionUser().getId());
        }
        List<UserClubDTO> clubsDto = userClubs.stream().map(userClubMapper::domainToDTO).toList();
        return handleResponse(clubsDto).ok();
    }

    /**
     * Fetches a single userclub record by ID.
     *
     * @param clubId The ID of the userclub.
     * @return {@link ApiResponse} containing the userclub details.
     * @throws UserClubException If the requested userclub does not exist.
     */
    @GetMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Find user clubs by ID [Auth SCOUTER]", description = "Finds and returns a UserClub by ID")
    public ResponseEntity<ApiResponse<UserClubDTO>> findById(@PathVariable("id") UUID clubId) {
        userAuthService.isAuthorizedByClub(clubId, RoleEnum.SCOUTER);
        try {
            UserClub userClub = findUserClubByIdUseCase.execute(clubId);
            UserClubDTO userClubDto = userClubMapper.domainToDTO(userClub);
            return handleResponse(userClubDto).ok();
        } catch (NoSuchElementException ex) {
            throw new UserClubException(ErrorEnum.CLUB_NOT_FOUND, clubId.toString());
        }
    }

    /**
     * Creates a new userclub record.
     *
     * @param clubRequest The userclub details submitted by the client.
     * @return {@link ApiResponse} containing the created userclub's details.
     * @throws UserClubException If an error occurs during userclub creation.
     */
    @PostMapping
    @Operation(summary = "Create a user clubs", description = "Creates a new UserClub")
    public ResponseEntity<ApiResponse<UserClubDTO>> create(@Valid @RequestBody UserClubCreateRequest clubRequest) {
        UserClub userClub = userClubMapper.createToDomain(clubRequest, Session.getSessionUser().getId());
        UserClub createdUserClub = createUserClubUseCase.execute(userClub);
        UserClubDTO createdUserClubDTO = userClubMapper.domainToDTO(createdUserClub);
        return handleResponse(createdUserClubDTO).created();
    }

    /**
     * Updates an existing userclub record.
     *
     * @param userClubDto The updated userclub details.
     * @param id The ID of the userclub to be updated.
     * @return {@link ApiResponse} containing the updated userclub's details.
     * @throws UserClubException If the userclub is not found.
     */
    @PutMapping(value = Routes.ID_PATHVAR)
    @Operation(summary = "Update a user club by ID [Auth ADMIN]", description = "Updates a UserClub")
    public ResponseEntity<ApiResponse<UserClubDTO>> update(@Valid @RequestBody UserClubUpdateRequest userClubDto, @PathVariable UUID id) {
        userAuthService.isAuthorizedByClub(id, RoleEnum.ADMIN);
        try {
            UserClub userClub = userClubMapper.updateToDomain(userClubDto);
            UserClub updatedUserClub = updateUserClubUseCase.execute(userClub, id);
            UserClubDTO updatedUserClubDto = userClubMapper.domainToDTO(updatedUserClub);
            return handleResponse(updatedUserClubDto).ok();
        } catch (NoSuchElementException ex) {
            throw new UserClubException(ErrorEnum.CLUB_NOT_FOUND, ex.getMessage());
        }
    }

    /**
     * Deletes a userclub record by ID.
     *
     * @param id The ID of the userclub to be deleted.
     * @return {@link ApiResponse} containing {@code true} if the userclub was deleted successfully.
     * @throws UserClubException If the userclub is not found.
     */
    @DeleteMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Deletes a user club by ID [Auth ADMIN]", description = "Deletes a UserClub")
    public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable UUID id) {
        userAuthService.isAuthorizedByClub(id, RoleEnum.ADMIN);
        try {
            boolean isDeleted = deleteUserClubUseCase.execute(id);
            return handleResponse(isDeleted).ok();
        } catch (NoSuchElementException ex) {
            throw new UserClubException(ErrorEnum.CLUB_NOT_FOUND, id.toString());
        }
    }

    /**
     * Adds a new Admin to a UserClub
     *
     * @param clubId The ID of the UserClub to be modified.
     * @param userId The ID of the User to be added as an Admin of the UserClub.
     * @return {@link ApiResponse} containing {@code true} if the UserClub was updated successfully.
     * @throws UserClubException If the userclub is not found.
     */
    @PostMapping(Routes.ID_PATHVAR + Routes.USERS + Routes.ID_TWO_PATHVAR + Routes.ADMIN_PATH)
    @Operation(summary = "Adds a user as admin in a user club [Auth ADMIN]", description = "Adds a new User admin to a UserClub")
    public ResponseEntity<ApiResponse<UserClubDTO>> addAdmin(@PathVariable(value = "id") UUID clubId, @PathVariable(value = "id2") UUID userId) {
        userAuthService.isAuthorizedByClub(clubId, RoleEnum.ADMIN);
        try {
            UserClub userClub = findUserClubByIdUseCase.execute(clubId);
            userClub.getAdminUserIds().add(userId);
            UserClub updatedUserClub = updateUserClubUseCase.execute(userClub, clubId);
            return handleResponse(userClubMapper.domainToDTO(updatedUserClub)).ok();
        } catch (NoSuchElementException ex) {
            throw new UserClubException(ErrorEnum.CLUB_NOT_FOUND, clubId.toString());
        }
    }

    /**
     * Removes a Admin from a UserClub
     *
     * @param clubId The ID of the UserClub to be modified.
     * @param userId The ID of the User to be removed as an Admin of the UserClub.
     * @return {@link ApiResponse} containing {@code true} if the UserClub was updated successfully.
     * @throws UserClubException If the userclub is not found.
     */
    @DeleteMapping(Routes.ID_PATHVAR + Routes.USERS + Routes.ID_TWO_PATHVAR + Routes.ADMIN_PATH)
    @Operation(summary = "Removes a user as admin from a user club [Auth ADMIN]", description = "Removes a User as admin from a UserClub")
    public ResponseEntity<ApiResponse<UserClubDTO>> removeAdmin(@PathVariable(value = "id") UUID clubId, @PathVariable(value = "id2") UUID userId) {
        userAuthService.isAuthorizedByClub(clubId, RoleEnum.ADMIN);
        try {
            UserClub userClub = findUserClubByIdUseCase.execute(clubId);
            userClub.getAdminUserIds().remove(userId);
            UserClub updatedUserClub = updateUserClubUseCase.execute(userClub, clubId);
            return handleResponse(userClubMapper.domainToDTO(updatedUserClub)).ok();
        } catch (NoSuchElementException ex) {
            throw new UserClubException(ErrorEnum.CLUB_NOT_FOUND, clubId.toString());
        }
    }

    /**
     * Creates a new managed Club
     *
     * @param userClubId The ID of the UserClub to be modified.
     * @param request ClubCreateRequest.
     * @return {@link ApiResponse} containing {@code true} if the UserClub was updated successfully.
     * @throws UserClubException If the userclub is not found.
     */
    @PostMapping(Routes.ID_PATHVAR + Routes.CLUBS)
    @Operation(summary = "Created a new managed [Auth ADMIN]", description = "Creates a new managed Club")
    public ResponseEntity<ApiResponse<ClubDTO>> createManagedClub(@PathVariable(value = "id") UUID userClubId, @RequestBody ClubCreateRequest request) {
        userAuthService.isAuthorizedByClub(userClubId, RoleEnum.SCOUTER);
        try {
            Club club = clubMapper.createToDomain(request);
            Club createdClub = createClubUseCase.execute(club, userClubId);
            return handleResponse(clubMapper.domainToDTO(createdClub)).ok();
        } catch (NoSuchElementException ex) {
            throw new UserClubException(ErrorEnum.CLUB_NOT_FOUND, userClubId.toString());
        }
    }
}
