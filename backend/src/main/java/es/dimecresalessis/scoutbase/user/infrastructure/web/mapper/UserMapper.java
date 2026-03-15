package es.dimecresalessis.scoutbase.user.infrastructure.web.mapper;

import es.dimecresalessis.scoutbase.user.domain.model.Role;
import es.dimecresalessis.scoutbase.user.domain.model.User;
import es.dimecresalessis.scoutbase.user.infrastructure.web.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toDomain(UserDto dto) {
        if (dto == null) {
            return null;
        }

        if (dto.getId() == null) {
            return User.getNewInstance(dto.getUsername(), dto.getPassword(), Role.fromName(dto.getRole()));
        }

        return new User(
                dto.getId(),
                dto.getUsername(),
                dto.getPassword(),
                Role.fromName(dto.getRole())
        );
    }

    public UserDto toDto(User domain) {
        return new UserDto(
                domain.getId(),
                domain.getUsername(),
                domain.getPassword(),
                domain.getRole().getName()
        );
    }
}
