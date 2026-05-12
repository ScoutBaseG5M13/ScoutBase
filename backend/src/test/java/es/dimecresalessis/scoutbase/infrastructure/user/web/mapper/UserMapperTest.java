package es.dimecresalessis.scoutbase.infrastructure.user.web.mapper;

import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDTO;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserInfoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void toDomain_ShouldMapDtoToDomain() {
        UUID id = UUID.randomUUID();
        UserDTO dto = new UserDTO();
        dto.setId(id);
        dto.setUsername("scout_master");
        dto.setEmail("scout@base.com");

        User domain = mapper.toDomain(dto);

        assertNotNull(domain);
        assertEquals(dto.getId(), domain.getId());
        assertEquals(dto.getUsername(), domain.getUsername());
        assertEquals(dto.getEmail(), domain.getEmail());
    }

    @Test
    void createToDomain_ShouldMapRequestToDomain() {
        UserCreateRequest request = new UserCreateRequest();
        request.setUsername("new_user");
        request.setPassword("securePass123");
        request.setEmail("new@base.com");

        User domain = mapper.createToDomain(request);

        assertNotNull(domain);
        assertEquals(request.getUsername(), domain.getUsername());
        assertEquals(request.getEmail(), domain.getEmail());
    }

    @Test
    void domainToDto_ShouldMapDomainToDto() {
        UUID id = UUID.randomUUID();
        User domain = User.builder()
                .id(id)
                .username("admin")
                .email("admin@base.com")
                .build();

        UserDTO dto = mapper.domainToDto(domain);

        assertNotNull(dto);
        assertEquals(domain.getId(), dto.getId());
        assertEquals(domain.getUsername(), dto.getUsername());
        assertEquals(domain.getEmail(), dto.getEmail());
    }

    @Test
    void domainToInfoDto_ShouldMapDomainAndRoleToInfoDto() {
        UUID id = UUID.randomUUID();
        User domain = User.builder()
                .id(id)
                .username("worker")
                .email("worker@base.com")
                .build();

        RoleEnum role = RoleEnum.ADMIN;

        UserInfoDTO infoDto = mapper.domainToInfoDto(domain, role);

        assertNotNull(infoDto);
        assertEquals(domain.getUsername(), infoDto.getUsername());
        assertEquals(domain.getEmail(), infoDto.getEmail());
        assertEquals(role.getRoleName(), infoDto.getRole());
    }

    @Test
    void mappers_ShouldReturnNull_WhenInputsAreNull() {
        assertNull(mapper.toDomain(null));
        assertNull(mapper.createToDomain(null));
        assertNull(mapper.domainToDto(null));
        assertNull(mapper.domainToInfoDto(null, null));
    }
}