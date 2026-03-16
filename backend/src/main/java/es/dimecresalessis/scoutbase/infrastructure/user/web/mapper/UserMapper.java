package es.dimecresalessis.scoutbase.infrastructure.user.web.mapper;

import es.dimecresalessis.scoutbase.domain.user.model.Role;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
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

    public UserDto toDto(User domain) {
        return new UserDto(
                domain.getId(),
                domain.getUsername(),
                domain.getPassword(),
                domain.getRole()
        );
    }
}
