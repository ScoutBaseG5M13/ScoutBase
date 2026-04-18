package es.dimecresalessis.scoutbase.infrastructure.user.web.mapper;

import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static es.dimecresalessis.scoutbase.domain.exception.ErrorEnum.ROLE_NOT_FOUND;

/**
 * Infrastructure mapper for converting between {@link UserDTO} and {@link User} domain models.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Transforms an incoming {@link UserDTO} into a {@link User} domain model.
     * <p>
     * This method uses the {@link RoleEnum#fromName} factory to ensure that the
     * role string provided by the client corresponds to a valid system role
     * before instantiating the domain object.
     * </p>
     *
     * @param dto The data transfer object received from the web request.
     * @return A {@link User} domain object, or {@code null} if the input was null.
     */
    @Mapping(target = "role", source = "role", qualifiedByName = "validateAndNormalizeRole")
    User toDomain(UserDTO dto);

    /**
     * Transforms a {@link User} domain model into a {@link UserDTO} for API responses.
     *
     * @param domain The business-level user entity.
     * @return A {@link UserDTO} formatted for JSON serialization in the web layer.
     */
    UserDTO toDto(User domain);

    @Named("validateAndNormalizeRole")
    default String validateAndNormalizeRole(String role) {
        RoleEnum roleEnum = RoleEnum.fromName(role);
        if (roleEnum == null) {
            throw new UserException(ROLE_NOT_FOUND, role);
        }
        return roleEnum.getName();
    }
}