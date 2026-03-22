package es.dimecresalessis.scoutbase.infrastructure.user.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.persistence.UserEntity;
import org.springframework.stereotype.Component;

/**
 * Infrastructure mapper for converting between {@link User} domain models
 * and {@link UserEntity} persistence objects.
 */
@Component
public class UserEntityMapper {

    /**
     * Maps a {@link User} domain object to a {@link UserEntity} for system storage.
     *
     * @param user The domain-level user to be converted.
     * @return A {@link UserEntity} ready for persistence operations.
     */
    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole());
        return entity;
    }

    /**
     * Maps a {@link UserEntity} retrieved from the database to a {@link User} domain model.
     *
     * @param entity The persistence-level entity to be converted.
     * @return A {@link User} domain object populated with database values.
     */
    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getRole()
        );
    }
}