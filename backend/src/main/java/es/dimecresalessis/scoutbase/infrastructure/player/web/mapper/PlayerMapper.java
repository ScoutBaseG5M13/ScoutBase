package es.dimecresalessis.scoutbase.infrastructure.player.web.mapper;

import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDTO;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Infrastructure mapper for converting between {@link PlayerDTO} and {@link Player} domain models.
 */
@Mapper(componentModel = "spring")
public interface PlayerMapper {

    /**
     * Transforms an incoming {@link PlayerDTO} into a {@link Player} domain model.
     *
     * @param dto The DTO received from the web request.
     * @return A {@link Player} domain object, or {@code null} if the input was null.
     */
    @Mapping(target = "id", source = "id")
    Player toDomain(PlayerDTO dto);

    /**
     * Transforms a {@link Player} domain model into a {@link PlayerDTO} for API responses.
     *
     * @param domain The {@link Player} domain object.
     * @return A {@link PlayerDTO} formatted for JSON serialization.
     */
    PlayerDTO toDto(Player domain);
}