package es.dimecresalessis.scoutbase.infrastructure.club.web;

import es.dimecresalessis.scoutbase.application.club.create.CreateClubUseCase;
import es.dimecresalessis.scoutbase.application.club.delete.DeleteClubUseCase;
import es.dimecresalessis.scoutbase.application.club.find.FindAllClubsUseCase;
import es.dimecresalessis.scoutbase.application.club.find.FindClubByIdUseCase;
import es.dimecresalessis.scoutbase.application.club.update.UpdateClubUseCase;
import es.dimecresalessis.scoutbase.application.security.UserAuthService;
import es.dimecresalessis.scoutbase.domain.club.exception.ClubException;
import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.infrastructure.club.web.dto.ClubCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.club.web.dto.ClubDTO;
import es.dimecresalessis.scoutbase.infrastructure.club.web.mapper.ClubMapper;
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
 * REST Controller for managing club-related operations.
 */
@RestController
@AllArgsConstructor
@ApiCommonResponses
@Tag(name = "Club", description = "Club management endpoints")
@RequestMapping(Routes.API_ROOT + Routes.CLUBS)
public class ClubController {

    private final ClubMapper clubMapper;
    private final FindAllClubsUseCase findAllClubsUseCase;
    private final FindClubByIdUseCase findClubByIdUseCase;
    private final CreateClubUseCase createClubUseCase;
    private final UpdateClubUseCase updateClubUseCase;
    private final DeleteClubUseCase deleteClubUseCase;
    private final UserAuthService userAuthService;

    /**
     * Finds all clubs.
     *
     * @return {@link ApiResponse} containing a list of all {@link Club}.
     */
    @GetMapping
    @Operation(summary = "Find all Clubs", description = "Finds all Clubs where the logged User takes part in")
    public ResponseEntity<ApiResponse<List<ClubDTO>>> findAllClubs() {
        List<Club> clubs = findAllClubsUseCase.execute();
        List<ClubDTO> clubsDto = clubs.stream().map(clubMapper::domainToDTO).toList();
        return handleResponse(clubsDto).ok();
    }

    /**
     * Fetches a single club record by ID.
     *
     * @param id The ID of the club.
     * @return {@link ApiResponse} containing the club details.
     * @throws ClubException If the requested club does not exist.
     */
    @GetMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Find Club by ID", description = "Finds and returns a Club by ID")
    public ResponseEntity<ApiResponse<ClubDTO>> findClubById(@PathVariable UUID id) throws ClubException {
        try {
            Club club = findClubByIdUseCase.execute(id);
            ClubDTO clubDto = clubMapper.domainToDTO(club);
            return handleResponse(clubDto).ok();
        } catch (NoSuchElementException ex) {
            throw new ClubException(ErrorEnum.CLUB_NOT_FOUND, id.toString());
        }
    }

    /**
     * Creates a new club record.
     *
     * @param clubRequest The club details submitted by the client.
     * @return {@link ApiResponse} containing the created club's details.
     * @throws ClubException If an error occurs during club creation.
     */
    @PostMapping
    @Operation(summary = "Create a Club", description = "Creates a new Club")
    public ResponseEntity<ApiResponse<ClubDTO>> createClub(@Valid @RequestBody ClubCreateRequest clubRequest) throws ClubException {
        Club club = clubMapper.createToDomain(clubRequest);
        Club createdClub = createClubUseCase.execute(club);
        ClubDTO createdClubDTO = clubMapper.domainToDTO(createdClub);
        return handleResponse(createdClubDTO).created();
    }

    /**
     * Updates an existing club record.
     *
     * @param clubDto The updated club details.
     * @param id The ID of the club to be updated.
     * @return {@link ApiResponse} containing the updated club's details.
     * @throws ClubException If the club is not found.
     */
    @PutMapping(value = Routes.ID_PATHVAR)
    @Operation(summary = "Update Club by ID", description = "Updates a Club")
    public ResponseEntity<ApiResponse<ClubDTO>> updateClub(@Valid @RequestBody ClubDTO clubDto, @PathVariable UUID id) {
        try {
            Club updatedClub = updateClubUseCase.execute(clubMapper.dtoToDomain(clubDto), id);
            ClubDTO updatedClubDto = clubMapper.domainToDTO(updatedClub);
            return handleResponse(updatedClubDto).ok();
        } catch (NoSuchElementException ex) {
            throw new ClubException(ErrorEnum.CLUB_NOT_FOUND, ex.getMessage());
        }
    }

//    /**
//     * Adds a User UUID as admin of a Club
//     *
//     * @param id The ID of the club to be updated.
//     * @return {@link ApiResponse} containing the updated club's details.
//     * @throws ClubException If the club is not found.
//     */
//    @PutMapping(value = Routes.ID_PATHVAR + Routes.USERS + Routes.ADMIN_PATH + Routes.USER_ID_PATHVAR)
//    @Operation(summary = "Update Club by ID", description = "Updates a Club")
//    public ResponseEntity<ApiResponse<ClubDTO>> updateClub(@PathVariable UUID userId) {
//        try {
//            Club updatedClub = updateClubUseCase.execute(clubMapper.dtoToDomain(clubDto), id);
//            ClubDTO updatedClubDto = clubMapper.domainToDTO(updatedClub);
//            return handleResponse(updatedClubDto).ok();
//        } catch (NoSuchElementException ex) {
//            throw new ClubException(ErrorEnum.CLUB_NOT_FOUND, ex.getMessage());
//        }
//    }

    /**
     * Deletes a club record by ID.
     *
     * @param id The ID of the club to be deleted.
     * @return {@link ApiResponse} containing {@code true} if the club was deleted successfully.
     * @throws ClubException If the club is not found.
     */
    @DeleteMapping(Routes.ID_PATHVAR)
    @Operation(summary = "Delete Club by ID", description = "Deletes a Club")
    public ResponseEntity<ApiResponse<Boolean>> deleteClub(@PathVariable UUID id) {
        try {
            boolean isDeleted = deleteClubUseCase.execute(id);
            return handleResponse(isDeleted).ok();
        } catch (NoSuchElementException ex) {
            throw new ClubException(ErrorEnum.CLUB_NOT_FOUND, id.toString());
        }
    }
}
