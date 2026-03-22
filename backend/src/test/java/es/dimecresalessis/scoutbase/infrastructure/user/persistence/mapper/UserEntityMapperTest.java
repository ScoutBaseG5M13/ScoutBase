package es.dimecresalessis.scoutbase.infrastructure.user.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.persistence.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityMapperTest {

    private UserEntityMapper userEntityMapper;
    private UUID userId;
    private String username;
    private String password;
    private String role;

    @BeforeEach
    void setUp() {
        userEntityMapper = new UserEntityMapper();
        userId = UUID.randomUUID();
        username = "scout_master";
        password = "password123";
        role = "ADMIN";
    }

    @Test
    @DisplayName("Should map Domain User to UserEntity")
    void shouldMapToEntity() {
        User user = new User(userId, username, password, role);

        UserEntity result = userEntityMapper.toEntity(user);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        assertEquals(role, result.getRole());
    }

    @Test
    @DisplayName("Should map UserEntity to Domain User")
    void shouldMapToDomain() {
        UserEntity entity = new UserEntity();
        entity.setId(userId);
        entity.setUsername(username);
        entity.setPassword(password);
        entity.setRole(role);

        User result = userEntityMapper.toDomain(entity);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        assertEquals(role, result.getRole());
    }

    @Test
    @DisplayName("Should return null when mapping null objects")
    void shouldReturnNull_WhenInputsAreNull() {
        assertNull(userEntityMapper.toEntity(null));
        assertNull(userEntityMapper.toDomain(null));
    }
}