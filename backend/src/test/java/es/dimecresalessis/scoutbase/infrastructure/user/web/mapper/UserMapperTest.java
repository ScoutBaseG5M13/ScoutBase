package es.dimecresalessis.scoutbase.infrastructure.user.web.mapper;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;
    private UUID userId;
    private String username;
    private String password;
    private String role;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
        userId = UUID.randomUUID();
        username = "scout_admin";
        password = "secretPassword";
        role = "ROLE_ADMIN";
    }

    @Test
    @DisplayName("Should map UserDto to Domain User")
    void shouldMapToDomain() {
        UserDto dto = new UserDto(userId, username, password, role);

        User result = userMapper.toDomain(dto);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        assertEquals(role, result.getRole());
    }

    @Test
    @DisplayName("Should map Domain User to UserDto")
    void shouldMapToDto() {
        User domain = new User(userId, username, password, role);

        UserDto result = userMapper.toDto(domain);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        assertEquals(role, result.getRole());
    }

    @Test
    @DisplayName("Should return null when mapping null objects")
    void shouldReturnNull_WhenInputsAreNull() {
        assertNull(userMapper.toDomain(null));
        assertNull(userMapper.toDto(null));
    }
}