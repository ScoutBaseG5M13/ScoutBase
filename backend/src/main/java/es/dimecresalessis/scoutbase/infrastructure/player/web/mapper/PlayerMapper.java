package es.dimecresalessis.scoutbase.infrastructure.player.web.mapper;

import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDTO;
import es.dimecresalessis.scoutbase.domain.player.model.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Infrastructure mapper for converting between {@link PlayerDTO} and {@link Player} domain models.
 */
@Mapper(componentModel = "spring")
public interface PlayerMapper {

    Player dtoToDomain(PlayerDTO dto);

    Player createToDomain(PlayerCreateRequest dto);

    @Mapping(target = "position", source = "position")
    PlayerDTO toDto(Player domain);
}