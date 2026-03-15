package es.dimecresalessis.scoutbase.player.infrastructure.web;

import es.dimecresalessis.scoutbase.infrastructure.web.annotation.ApiCommonResponses;
import es.dimecresalessis.scoutbase.infrastructure.web.dto.ApiResponse;
import es.dimecresalessis.scoutbase.player.domain.model.Player;
import es.dimecresalessis.scoutbase.player.infrastructure.web.dto.PlayerDto;
import es.dimecresalessis.scoutbase.player.application.usecases.*;
import es.dimecresalessis.scoutbase.player.domain.exception.PlayerException;
import es.dimecresalessis.scoutbase.player.infrastructure.web.mapper.PlayerMapper;
import es.dimecresalessis.scoutbase.shared.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.shared.routes.Routes;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static es.dimecresalessis.scoutbase.infrastructure.web.dto.ResponseFactory.handleResponse;

@RestController
@AllArgsConstructor
@ApiCommonResponses
@RequestMapping(Routes.API_ROOT + Routes.PLAYERS)
public class PlayerController {

    private final PlayerMapper playerMapper;
    private final FindAllPlayersUseCase findAllPlayersUseCase;
    private final FindPlayerByIdUseCase findPlayerByIdUseCase;
    private final UpdatePlayerUseCase updatePlayerUseCase;
    private final CreatePlayerUseCase createPlayerUseCase;
    private final DeletePlayerUseCase deletePlayerUseCase;

    @GetMapping
    public ApiResponse<List<PlayerDto>> findAll() {
        return handleResponse(
                findAllPlayersUseCase.execute()
                .stream()
                .map(playerMapper::toDto)
                .toList()
        ).ok();
    }

    @GetMapping("/{id}")
    public ApiResponse<PlayerDto> findById(@PathVariable UUID id) throws PlayerException {
        try {
            return handleResponse(
                    playerMapper.toDto(
                            findPlayerByIdUseCase.execute(id)
                    )
            ).ok();
        } catch (NoSuchElementException ex) {
            throw new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, id.toString());
        }
    }

    @PostMapping("/create")
    public ApiResponse<PlayerDto> create(@Valid @RequestBody PlayerDto playerDto) throws PlayerException {
        Player player = playerMapper.toDomain(playerDto);
        return handleResponse(
                playerMapper.toDto(
                        createPlayerUseCase.execute(player)
                )
        ).ok();
    }

    @PutMapping("/update")
    public ApiResponse<PlayerDto> update(@Valid @RequestBody PlayerDto playerDto) {
        try {
            return handleResponse(
                    playerMapper.toDto(
                            updatePlayerUseCase.execute(
                                    playerMapper.toDomain(playerDto)
                            )
                    )
            ).ok();
        } catch (NoSuchElementException ex) {
            throw new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, playerDto.getId().toString());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable UUID id) {
        try {
            return handleResponse(
                    deletePlayerUseCase.execute(id)
            ).ok();
        } catch (NoSuchElementException ex) {
            throw new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, id.toString());
        }
    }
}
