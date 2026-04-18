package es.dimecresalessis.scoutbase.infrastructure.stat.web;

import es.dimecresalessis.scoutbase.application.stat.*;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.stat.exception.StatException;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.infrastructure.routes.Routes;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatDTO;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.mapper.StatMapper;
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
 * REST Controller for managing stat-related operations.
 */
@RestController
@AllArgsConstructor
@ApiCommonResponses
@Tag(name = "Stats", description = "Stat management endpoints")
@RequestMapping(Routes.API_ROOT + Routes.STAT)
public class StatController {

    private final StatMapper statMapper;
    private final FindAllStatsUseCase findAllStatsUseCase;
    private final CreateStatUseCase createStatUseCase;
    private final FindAllStatsByPlayerIdUseCase findAllStatsByPlayerIdUseCase;
    private final FindStatByIdUseCase findStatByIdUseCase;
    private final UpdateStatUseCase updateStatUseCase;
    private final DeleteStatUseCase deleteStatUseCase;

    /**
     * Finds all stats.
     *
     * @return {@link ApiResponse} containing a list of all {@link Stat}.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Find all stats", description = "Retrieves all registered stats in the DB")
    public ResponseEntity<ApiResponse<List<StatDTO>>> findAll() {
        List<Stat> stats = findAllStatsUseCase.execute();
        List<StatDTO> statsDto = stats.stream().map(statMapper::toDto).toList();
        return handleResponse(statsDto).ok();
    }

    @GetMapping(Routes.ID_PATHVAR)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Find stat by ID", description = "Retrieves a stat registered in the DB through its ID")
    public ResponseEntity<ApiResponse<StatDTO>> findById(@PathVariable(name = "id") UUID id) {
        Stat stat = findStatByIdUseCase.execute(id);
        StatDTO statDto = statMapper.toDto(stat);
        return handleResponse(statDto).ok();
    }

    /**
     * Finds all stats by player.
     *
     * @return {@link ApiResponse} containing a list of all {@link Stat}.
     */
    @GetMapping(Routes.PLAYERS)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Find stats by player", description = "Retrieves all registered stats in the DB assigned to certain player")
    public ResponseEntity<ApiResponse<List<StatDTO>>> findAllByPlayerId(@RequestParam(name = "playerId") UUID playerId) {
        List<Stat> stats = findAllStatsByPlayerIdUseCase.execute(playerId);
        List<StatDTO> statsDto = stats.stream().map(statMapper::toDto).toList();
        return handleResponse(statsDto).ok();
    }

    /**
     * Creates a new stat record.
     *
     * @param statDto The stat details submitted by the client.
     * @return {@link ApiResponse} containing the created stat's details.
     * @throws StatException If an error occurs during stat creation.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Create a new stat", description = "Adds a new stat to the DB")
    public ResponseEntity<ApiResponse<StatDTO>> create(@Valid @RequestBody StatDTO statDto) throws StatException {
        Stat stat = statMapper.toDomain(statDto);
        Stat createdStat = createStatUseCase.execute(stat);
        StatDTO createdStatDTO = statMapper.toDto(createdStat);
        return handleResponse(createdStatDTO).created();
    }

    /**
     * Updates an existing stat record.
     *
     * @param statDto The updated stat details.
     * @param id The ID of the stat to be updated.
     * @return {@link ApiResponse} containing the updated stat's details.
     * @throws StatException If the stat is not found.
     */
    @PutMapping(value = Routes.ID_PATHVAR, consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Update stat by ID", description = "Updates the data for a specific stat")
    public ResponseEntity<ApiResponse<StatDTO>> update(@Valid @RequestBody StatDTO statDto, @PathVariable UUID id) {
        try {
            Stat stat = statMapper.toDomain(statDto);
            Stat updatedStat = updateStatUseCase.execute(stat, id);
            StatDTO updatedStatDTO = statMapper.toDto(updatedStat);
            return handleResponse(updatedStatDTO).ok();
        } catch (NoSuchElementException ex) {
            throw new StatException(ErrorEnum.STAT_NOT_FOUND, ex.getMessage());
        }
    }

    /**
     * Deletes a stat record by ID.
     *
     * @param id The ID of the stat to be deleted.
     * @return {@link ApiResponse} containing {@code true} if the stat was deleted successfully.
     * @throws StatException If the stat is not found.
     */
    @DeleteMapping(Routes.ID_PATHVAR)
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Delete stat by ID", description = "Deletes a specific stat by their ID")
    public ResponseEntity<ApiResponse<Boolean>> delete(@PathVariable UUID id) {
        try {
            boolean isDeleted = deleteStatUseCase.execute(id);
            return handleResponse(isDeleted).ok();
        } catch (NoSuchElementException ex) {
            throw new StatException(ErrorEnum.STAT_NOT_FOUND, id.toString());
        }
    }
}
