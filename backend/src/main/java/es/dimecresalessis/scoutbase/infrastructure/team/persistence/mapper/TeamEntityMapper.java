package es.dimecresalessis.scoutbase.infrastructure.team.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.infrastructure.team.persistence.TeamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Infrastructure mapper for converting between {@link Team} domain models
 * and {@link TeamEntity} persistence objects.
 */
@Mapper(componentModel = "spring")
public interface TeamEntityMapper {

    @Mapping(target = "id", source = "id")
    TeamEntity toEntity(Team domain);

    @Mapping(target = "id", source = "id")
    Team toDomain(TeamEntity entity);

    @Mapping(target = "id", source = "id")
    void updateEntityFromDomain(Team domain, @MappingTarget TeamEntity entity);
}
