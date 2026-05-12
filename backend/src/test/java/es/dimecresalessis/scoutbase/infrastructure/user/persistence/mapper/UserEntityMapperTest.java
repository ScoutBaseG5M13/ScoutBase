package es.dimecresalessis.scoutbase.infrastructure.user.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.persistence.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityMapperTest {

    private UserEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(UserEntityMapper.class);
    }

    @Test
    void toEntity_ShouldMapDomainToEntity() {
        UUID id = UUID.randomUUID();
        User domain = User.builder()
                .id(id)
                .email("test@example.com")
                .username("testuser")
                .build();

        UserEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getEmail(), entity.getEmail());
        assertEquals(domain.getUsername(), entity.getUsername());
    }

    @Test
    void toDomain_ShouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        UserEntity entity = new UserEntity();
        entity.setId(id);
        entity.setEmail("entity@example.com");
        entity.setUsername("entityuser");

        User domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getEmail(), domain.getEmail());
        assertEquals(entity.getUsername(), domain.getUsername());
    }

    @Test
    void updateEntity_ShouldUpdateTargetWithSourceData() {
        UserEntity source = new UserEntity();
        source.setEmail("new@example.com");
        source.setUsername("newuser");

        UserEntity target = new UserEntity();
        target.setEmail("old@example.com");
        target.setUsername("olduser");

        mapper.updateEntity(target, source);

        assertEquals("new@example.com", target.getEmail());
        assertEquals("newuser", target.getUsername());
    }

    @Test
    void updateEntityFromDomain_ShouldUpdateEntityWithDomainState() {
        UUID id = UUID.randomUUID();
        User domain = User.builder()
                .id(id)
                .email("updated@example.com")
                .username("updateduser")
                .build();

        UserEntity target = new UserEntity();
        target.setId(id);
        target.setEmail("old@example.com");

        mapper.updateEntityFromDomain(domain, target);

        assertEquals("updated@example.com", target.getEmail());
        assertEquals("updateduser", target.getUsername());
        assertEquals(id, target.getId());
    }

    @Test
    void mappers_ShouldReturnNull_WhenInputsAreNull() {
        assertNull(mapper.toEntity(null));
        assertNull(mapper.toDomain(null));
    }
}