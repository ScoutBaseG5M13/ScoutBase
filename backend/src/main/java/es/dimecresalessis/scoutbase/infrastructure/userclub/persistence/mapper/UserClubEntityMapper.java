package es.dimecresalessis.scoutbase.infrastructure.userclub.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.infrastructure.userclub.persistence.UserClubEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Infrastructure mapper for converting between {@link UserClub} domain models
 * and {@link UserClubEntity} persistence objects.
 */
@Mapper(componentModel = "spring")
public interface UserClubEntityMapper {

    UserClubEntity toEntity(UserClub domain);

    UserClub toDomain(UserClubEntity entity);

    void updateEntityFromDomain(UserClub domain, @MappingTarget UserClubEntity entity);
}
