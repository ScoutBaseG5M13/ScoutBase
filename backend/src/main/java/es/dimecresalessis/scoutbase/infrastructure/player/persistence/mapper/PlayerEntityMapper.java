package es.dimecresalessis.scoutbase.infrastructure.player.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.infrastructure.player.persistence.PlayerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Infrastructure mapper for converting between {@link Player} domain models
 * and {@link PlayerEntity} persistence objects.
 */
@Mapper(componentModel = "spring")
public interface PlayerEntityMapper {

    @Mapping(target = "id", source = "id")
    PlayerEntity toEntity(Player domain);

    @Mapping(target = "id", source = "id")
    Player toDomain(PlayerEntity entity);

    @Mapping(target = "id", source = "id")
    void updateEntityFromDomain(Player domain, @MappingTarget PlayerEntity entity);
}