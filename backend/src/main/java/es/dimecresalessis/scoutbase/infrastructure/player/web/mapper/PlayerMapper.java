package es.dimecresalessis.scoutbase.infrastructure.player.web.mapper;

import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDto;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {
    public Player toDomain(PlayerDto dto) {
        if (dto == null) {
            return null;
        }

        if (dto.getId() == null) {
            return Player.getNewInstance(dto.getName(), dto.getTeam(), dto.getEmail());
        }

        return new Player(
                dto.getId(),
                dto.getName(),
                dto.getTeam(),
                dto.getEmail()
        );
    }

    public PlayerDto toDto(Player domain) {
        return new PlayerDto(
                domain.getId(),
                domain.getName(),
                domain.getTeam(),
                domain.getEmail()
        );
    }
}