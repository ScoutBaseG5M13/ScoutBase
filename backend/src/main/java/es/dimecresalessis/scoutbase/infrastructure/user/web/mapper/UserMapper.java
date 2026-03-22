package es.dimecresalessis.scoutbase.infrastructure.user.web.mapper;

import es.dimecresalessis.scoutbase.domain.user.model.Role;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDto;
import org.springframework.stereotype.Component;

/**
 * Infrastructure mapper for converting between {@link UserDto} and {@link User} domain models.
 */
@Component
public class UserMapper {

    /**
     * Transforms an incoming {@link UserDto} into a {@link User} domain model.
     * <p>
     * This method uses the {@link Role#fromName} factory to ensure that the
     * role string provided by the client corresponds to a valid system role
     * before instantiating the domain object.
     * </p>
     *
     * @param dto The data transfer object received from the web request.
     * @return A {@link User} domain object, or {@code null} if the input was null.
     */
    public User toDomain(UserDto dto) {
        if (dto == null) {
            return null;
        }

        return new User(
                dto.getId(),
                dto.getUsername(),
                dto.getPassword(),
                Role.fromName(dto.getRole()).getName()
        );
    }

    /**
     * Transforms a {@link User} domain model into a {@link UserDto} for API responses.
     *
     * @param domain The business-level user entity.
     * @return A {@link UserDto} formatted for JSON serialization in the web layer.
     */
    public UserDto toDto(User domain) {
        if (domain == null) {
            return null;
        }

        return new UserDto(
                domain.getId(),
                domain.getUsername(),
                domain.getPassword(),
                domain.getRole()
        );
    }
}