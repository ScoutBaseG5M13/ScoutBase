package es.dimecresalessis.scoutbase.infrastructure.user.web.mapper;

import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = UserMapperImpl.class)
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void shouldMapDomainToDto() {
        User domain = User.builder()
                .id(UUID.randomUUID())
                .username("scout_master")
                .password("pass123")
                .role(RoleEnum.ADMIN.getName())
                .name("Alex")
                .surname("Scout")
                .email("alex@scoutbase.com")
                .build();

        UserDTO dto = userMapper.toDto(domain);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(domain.getId());
        assertThat(dto.getUsername()).isEqualTo(domain.getUsername());
        assertThat(dto.getRole()).isEqualTo(RoleEnum.ADMIN.getName());
        assertThat(dto.getEmail()).isEqualTo(domain.getEmail());
    }

    @Test
    void shouldMapDtoToDomainWithValidRole() {
        UserDTO dto = UserDTO.getRandomInstance(RoleEnum.USER.getName());

        User domain = userMapper.toDomain(dto);

        assertThat(domain).isNotNull();
        assertThat(domain.getUsername()).isEqualTo(dto.getUsername());
        assertThat(domain.getRole()).isEqualTo(RoleEnum.USER.getName());
    }

    @Test
    void shouldNormalizeRoleToUpperCaseWhenMappingToDomain() {
        UserDTO dto = UserDTO.getRandomInstance("role_admin");

        User domain = userMapper.toDomain(dto);

        assertThat(domain.getRole()).isEqualTo(RoleEnum.ADMIN.getName());
    }

    @Test
    void shouldThrowExceptionWhenRoleIsInvalid() {
        UserDTO dto = UserDTO.getRandomInstance("INVALID_ROLE");

        assertThatThrownBy(() -> userMapper.toDomain(dto))
                .isInstanceOf(UserException.class)
                .hasMessageContaining("INVALID_ROLE");
    }

    @Test
    void shouldReturnNullWhenMappingNulls() {
        assertThat(userMapper.toDomain(null)).isNull();
        assertThat(userMapper.toDto(null)).isNull();
    }
}