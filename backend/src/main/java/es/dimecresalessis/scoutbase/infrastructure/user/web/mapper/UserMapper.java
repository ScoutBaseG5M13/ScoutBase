package es.dimecresalessis.scoutbase.infrastructure.user.web.mapper;

import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDTO;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Infrastructure mapper for converting between {@link UserDTO} and {@link User} domain models.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    User toDomain(UserDTO dto);

    User createToDomain(UserCreateRequest dto);

    UserDTO domainToDto(User domain);

    @Mapping(target = "role", source = "role.roleName")
    UserInfoDTO domainToInfoDto(User domain, RoleEnum role);

}