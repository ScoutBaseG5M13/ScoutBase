package es.dimecresalessis.scoutbase.player.domain;

import es.dimecresalessis.scoutbase.player.application.dto.PlayerDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PlayerMapper {
    public Player toPlayer(PlayerDto dto) {
        if (dto == null) return null;

        return new Player(
                dto.getId(),
                dto.getName(),
                dto.getTeam(),
                dto.getEmail()
        );
    }

    // Convert Domain Object to DTO for the Frontend
    public PlayerDto toPlayerDto(Player player) {
        if (player == null) return null;

        return new PlayerDto(
                player.getId(),
                player.getName(),
                player.getTeam(),
                player.getEmail()
        );
    }
}