package es.dimecresalessis.scoutbase.infrastructure.user.web.mapper;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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
                .name("Alex")
                .surname("Scout")
                .email("alex@scoutbase.com")
                .build();

        UserDTO dto = userMapper.toDto(domain);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(domain.getId());
        assertThat(dto.getUsername()).isEqualTo(domain.getUsername());
        assertThat(dto.getEmail()).isEqualTo(domain.getEmail());
    }

    @Test
    void shouldReturnNullWhenMappingNulls() {
        assertThat(userMapper.toDomain(null)).isNull();
        assertThat(userMapper.toDto(null)).isNull();
    }
}