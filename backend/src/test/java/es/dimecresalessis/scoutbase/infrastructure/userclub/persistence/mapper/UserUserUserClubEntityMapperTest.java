package es.dimecresalessis.scoutbase.infrastructure.userclub.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.infrastructure.userclub.persistence.UserClubEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserUserUserClubEntityMapperTest {

    private UserClubEntityMapper userClubEntityMapper;

    @BeforeEach
    void setUp() {
        userClubEntityMapper = new UserClubEntityMapperImpl();
    }

    @Test
    void toEntity_ShouldMapDomainToEntity() {
        UUID id = UUID.randomUUID();
        UserClub userClub = UserClub.builder()
                .id(id)
                .name("Scout Club")
                .build();

        UserClubEntity entity = userClubEntityMapper.toEntity(userClub);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("Scout Club", entity.getName());
    }

    @Test
    void toDomain_ShouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        UserClubEntity entity = new UserClubEntity();
        entity.setId(id);
        entity.setName("Scout Club");

        UserClub domain = userClubEntityMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("Scout Club", domain.getName());
    }

    @Test
    void updateEntityFromDomain_ShouldUpdateExistingEntity() {
        UUID id = UUID.randomUUID();
        UserClub domain = UserClub.builder()
                .id(id)
                .name("New Name")
                .build();

        UserClubEntity entity = new UserClubEntity();
        entity.setId(id);
        entity.setName("Old Name");

        userClubEntityMapper.updateEntityFromDomain(domain, entity);

        assertEquals("New Name", entity.getName());
        assertEquals(id, entity.getId());
    }
}