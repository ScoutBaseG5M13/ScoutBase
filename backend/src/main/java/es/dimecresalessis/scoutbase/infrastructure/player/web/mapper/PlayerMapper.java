package es.dimecresalessis.scoutbase.infrastructure.player.web.mapper;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.shared.domain.PositionEnum;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDTO;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * Infrastructure mapper for converting between {@link PlayerDTO} and {@link Player} domain models.
 */
@Mapper(componentModel = "spring", imports = { PositionEnum.class })
public interface PlayerMapper {

    @Mapping(target = "position", expression = "java(PositionEnum.fromPositionName(dto.getPosition()))")
    Player dtoToDomain(PlayerDTO dto);

    @Mapping(target = "teamId", source = "team")
    @Mapping(target = "position", expression = "java(PositionEnum.fromPositionName(dto.getPosition()))")
    Player createToDomain(PlayerCreateRequest dto, UUID team);

    @Mapping(target = "position", expression = "java(PositionEnum.fromPositionName(dto.getPosition()))")
    Player updateToDomain(PlayerUpdateRequest dto);

    @Mapping(target = "position", source = "position")
    PlayerDTO toDto(Player domain);
}