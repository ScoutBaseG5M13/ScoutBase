package es.dimecresalessis.scoutbase.infrastructure.player.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.shared.domain.PositionEnum;
import es.dimecresalessis.scoutbase.infrastructure.player.persistence.PlayerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Infrastructure mapper for converting between {@link Player} domain models
 * and {@link PlayerEntity} persistence objects.
 */
@Mapper(componentModel = "spring", imports = { PositionEnum.class} )
public interface PlayerEntityMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "position", source = "position")
    PlayerEntity toEntity(Player domain);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "position", expression = "java(PositionEnum.fromValue(entity.getPosition()))")
    Player toDomain(PlayerEntity entity);

    void updateEntityFromDomain(Player domain, @MappingTarget PlayerEntity entity);
}