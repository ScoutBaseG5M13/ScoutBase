package es.dimecresalessis.scoutbase.infrastructure.user.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.persistence.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

/**
 * Infrastructure mapper for converting between {@link User} domain models
 * and {@link UserEntity} persistence objects.
 */
@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    /**
     * Maps a {@link User} domain object to a {@link UserEntity} for system storage.
     *
     * @param user The domain-level user to be converted.
     * @return A {@link UserEntity} ready for persistence operations.
     */

    UserEntity toEntity(User user);

    void updateEntity(@MappingTarget UserEntity target, UserEntity source);

    void updateEntityFromDomain(User domain, @MappingTarget UserEntity target);

    /**
     * Maps a {@link UserEntity} retrieved from the database to a {@link User} domain model.
     *
     * @param entity The persistence-level entity to be converted.
     * @return A {@link User} domain object populated with database values.
     */
    User toDomain(UserEntity entity);
}