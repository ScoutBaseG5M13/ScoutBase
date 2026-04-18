package es.dimecresalessis.scoutbase.infrastructure.user.web.mapper;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.web.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;
    private UUID userId;
    private User userDomain;
    private UserDTO userDto;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
        userId = UUID.randomUUID();

        userDomain = User.builder()
                .id(userId)
                .username("scout_master")
                .password("encoded_password")
                .role("ADMIN")
                .name("Alex")
                .surname("Scout")
                .email("alex@scoutbase.com")
                .build();

        userDto = new UserDTO(
                userId,
                "scout_user",
                "raw_password",
                "USER",
                "John",
                "Doe",
                "john@doe.com"
        );
    }

    @Test
    void shouldMapToDomain_WithId() {
        User result = userMapper.toDomain(userDto);

        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getUsername(), result.getUsername());
        assertEquals(userDto.getPassword(), result.getPassword());
        assertEquals(userDto.getRole(), result.getRole());
        assertEquals(userDto.getName(), result.getName());
        assertEquals(userDto.getSurname(), result.getSurname());
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    void shouldMapToDomain_WithoutId() {
        userDto.setId(null);

        User result = userMapper.toDomain(userDto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotEquals(userId, result.getId());
        assertEquals(userDto.getUsername(), result.getUsername());
    }

    @Test
    void shouldMapToDto() {
        UserDTO result = userMapper.toDto(userDomain);

        assertNotNull(result);
        assertEquals(userDomain.getId(), result.getId());
        assertEquals(userDomain.getUsername(), result.getUsername());
        assertEquals(userDomain.getPassword(), result.getPassword());
        assertEquals(userDomain.getRole(), result.getRole());
        assertEquals(userDomain.getName(), result.getName());
        assertEquals(userDomain.getSurname(), result.getSurname());
        assertEquals(userDomain.getEmail(), result.getEmail());
    }

    @Test
    void shouldReturnNull_WhenInputsAreNull() {
        assertNull(userMapper.toDomain(null));
        assertNull(userMapper.toDto(null));
    }
}