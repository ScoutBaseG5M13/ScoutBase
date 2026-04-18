package es.dimecresalessis.scoutbase.infrastructure.user.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.persistence.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityMapperTest {

    private UserEntityMapper userEntityMapper;
    private UUID userId;
    private User user;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userEntityMapper = Mappers.getMapper(UserEntityMapper.class);
        userId = UUID.randomUUID();
        user = User.builder()
                .id(userId)
                .username("scout_master")
                .password("encoded_password")
                .role("ADMIN")
                .name("Alex")
                .surname("Scout")
                .email("alex@scoutbase.com")
                .build();

        userEntity = UserEntity.builder()
                .id(userId)
                .username("scout_entity")
                .password("encoded_password_entity")
                .role("ADMIN")
                .name("Alexine")
                .surname("Manager")
                .email("alex@scoutbase.com")
                .build();
    }

    @Test
    @DisplayName("Should map Domain User to UserEntity")
    void shouldMapToEntity() {
        UserEntity result = userEntityMapper.toEntity(user);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getRole(), result.getRole());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getSurname(), result.getSurname());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    @DisplayName("Should map UserEntity to Domain User")
    void shouldMapToDomain() {
        User result = userEntityMapper.toDomain(userEntity);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getRole(), result.getRole());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getSurname(), result.getSurname());
        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    @DisplayName("Should return null when mapping null objects")
    void shouldReturnNull_WhenInputsAreNull() {
        assertNull(userEntityMapper.toEntity(null));
        assertNull(userEntityMapper.toDomain(null));
    }
}