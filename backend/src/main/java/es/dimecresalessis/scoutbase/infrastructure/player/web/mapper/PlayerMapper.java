package es.dimecresalessis.scoutbase.infrastructure.player.web.mapper;

import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDto;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import org.springframework.stereotype.Component;

/**
 * Infrastructure mapper for converting between {@link PlayerDto} and {@link Player} domain models.
 */
@Component
public class PlayerMapper {

    /**
     * Transforms an incoming {@link PlayerDto} into a {@link Player} domain model.
     *
     * @param dto The DTO received from the web request.
     * @return A {@link Player} domain object, or {@code null} if the input was null.
     */
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

    /**
     * Transforms a {@link Player} domain model into a {@link PlayerDto} for API responses.
     *
     * @param domain The {@link Player} domain object.
     * @return A {@link PlayerDto} formatted for JSON serialization.
     */
    public PlayerDto toDto(Player domain) {
        return new PlayerDto(
                domain.getId(),
                domain.getName(),
                domain.getTeam(),
                domain.getEmail()
        );
    }
}