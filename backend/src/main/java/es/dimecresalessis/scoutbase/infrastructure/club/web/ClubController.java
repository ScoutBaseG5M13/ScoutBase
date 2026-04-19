package es.dimecresalessis.scoutbase.infrastructure.club.web;

import es.dimecresalessis.scoutbase.application.club.*;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.club.exception.ClubException;
import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.infrastructure.club.web.dto.ClubDTO;
import es.dimecresalessis.scoutbase.infrastructure.club.web.dto.ClubCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.club.web.mapper.ClubMapper;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.infrastructure.web.annotation.ApiCommonResponses;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    /**
     * Finds all clubs.
     *
     * @return {@link ApiResponse} containing a list of all {@link Club}.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Find all clubs", description = "Retrieves all registered clubs in the DB")
    public ResponseEntity<ApiResponse<List<ClubDTO>>> findAll() {
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Find club by ID", description = "Finds and returns a club by their ID")
    public ResponseEntity<ApiResponse<ClubDTO>> findById(@PathVariable UUID id) throws ClubException {
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
     * @param clubDto The club details submitted by the client.
     * @return {@link ApiResponse} containing the created club's details.
     * @throws ClubException If an error occurs during club creation.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Create a new club", description = "Adds a new club to the DB")
    public ResponseEntity<ApiResponse<ClubDTO>> create(@Valid @RequestBody ClubCreateRequest clubRequest) throws ClubException {
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
    @PutMapping(value = Routes.ID_PATHVAR, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Update club by ID", description = "Updates the data for a specific club")
    public ResponseEntity<ApiResponse<ClubDTO>> update(@Valid @RequestBody ClubDTO clubDto, @PathVariable UUID id) {
        try {
            Club updatedClub = updateClubUseCase.execute(clubMapper.dtoToDomain(clubDto), id);
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
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Delete club by ID", description = "Deletes a specific club by their ID")
    public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable UUID id) {
        try {
            boolean isDeleted = deleteClubUseCase.execute(id);
            return handleResponse(isDeleted).ok();
        } catch (NoSuchElementException ex) {
            throw new ClubException(ErrorEnum.CLUB_NOT_FOUND, id.toString());
        }
    }
}
