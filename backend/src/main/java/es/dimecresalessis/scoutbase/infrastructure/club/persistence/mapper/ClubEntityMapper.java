package es.dimecresalessis.scoutbase.infrastructure.club.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.infrastructure.club.persistence.ClubEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Infrastructure mapper for converting between {@link Club} domain models
 * and {@link ClubEntity} persistence objects.
 */
@Mapper(componentModel = "spring")
public interface ClubEntityMapper {

    ClubEntity toEntity(Club domain);

    Club toDomain(ClubEntity entity);

    void updateEntityFromDomain(Club domain, @MappingTarget ClubEntity entity);
}
