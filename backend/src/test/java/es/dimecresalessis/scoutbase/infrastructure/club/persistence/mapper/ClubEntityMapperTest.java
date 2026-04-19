package es.dimecresalessis.scoutbase.infrastructure.club.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.infrastructure.club.persistence.ClubEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClubEntityMapperTest {

    private ClubEntityMapper clubEntityMapper;

    @BeforeEach
    void setUp() {
        clubEntityMapper = new ClubEntityMapperImpl();
    }

    @Test
    void toEntity_ShouldMapDomainToEntity() {
        UUID id = UUID.randomUUID();
        Club club = Club.builder()
                .id(id)
                .name("Scout Club")
                .build();

        ClubEntity entity = clubEntityMapper.toEntity(club);

        assertNotNull(entity);
        assertEquals(id, entity.getId());
        assertEquals("Scout Club", entity.getName());
    }

    @Test
    void toDomain_ShouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        ClubEntity entity = new ClubEntity();
        entity.setId(id);
        entity.setName("Scout Club");

        Club domain = clubEntityMapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("Scout Club", domain.getName());
    }

    @Test
    void updateEntityFromDomain_ShouldUpdateExistingEntity() {
        UUID id = UUID.randomUUID();
        Club domain = Club.builder()
                .id(id)
                .name("New Name")
                .build();

        ClubEntity entity = new ClubEntity();
        entity.setId(id);
        entity.setName("Old Name");

        clubEntityMapper.updateEntityFromDomain(domain, entity);

        assertEquals("New Name", entity.getName());
        assertEquals(id, entity.getId());
    }
}