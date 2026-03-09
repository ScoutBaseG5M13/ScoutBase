package es.dimecresalessis.scoutbase.player.infrastructure.web;

import es.dimecresalessis.scoutbase.player.application.dto.PlayerDto;
import es.dimecresalessis.scoutbase.player.domain.Player;
import es.dimecresalessis.scoutbase.player.domain.PlayerMapper;
import es.dimecresalessis.scoutbase.player.domain.exception.PlayerException;
import es.dimecresalessis.scoutbase.player.infrastructure.port.PlayerRepository;
import es.dimecresalessis.scoutbase.shared.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.shared.routes.Routes;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(Routes.API_BASE + Routes.PLAYERS)
public class PlayerController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    @GetMapping
    public PlayerDto[] findAll() {
        PlayerDto[] players = playerRepository.findAll().stream().map(playerMapper::toPlayerDto).toArray(PlayerDto[]::new);
        return players;
    }

    @GetMapping("/{id}")
    public PlayerDto findById(@PathVariable UUID id) {
        return playerMapper.toPlayerDto(playerRepository.findById(id).orElse(null));
    }

    @PostMapping
    public UUID save(@RequestBody PlayerDto playerDto) {
        Player player = playerRepository.save(playerMapper.toPlayer(playerDto));
        return player.getId();
    }

    @PutMapping("/{id}")
    public PlayerDto update(@PathVariable UUID id, @RequestBody PlayerDto playerDto) throws PlayerException {
        UUID playerId = playerRepository.findById(id).orElseThrow(() -> new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, id.toString())).getId();
        Player player = new Player(playerId, playerDto.getName(), playerDto.getTeam(), playerDto.getEmail());
        playerRepository.save(player);
        return playerMapper.toPlayerDto(player);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable UUID id) {
        try {
            playerRepository.findById(id).orElseThrow(() -> new PlayerException(ErrorEnum.PLAYER_NOT_FOUND, id.toString()));
            playerRepository.deleteById(id);
            return true;
        } catch (PlayerException e) {
            return false;
        }
    }
}
