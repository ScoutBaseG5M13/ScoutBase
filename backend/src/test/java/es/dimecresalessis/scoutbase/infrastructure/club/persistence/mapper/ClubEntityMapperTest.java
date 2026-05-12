package es.dimecresalessis.scoutbase.infrastructure.club.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.infrastructure.club.persistence.ClubEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClubEntityMapperTest {

    private ClubEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ClubEntityMapper.class);
    }

    @Test
    void toEntity_ShouldMapDomainToEntity() {
        UUID id = UUID.randomUUID();
        Club domain = Club.builder()
                .id(id)
                .name("Scout Club")
                .teams(List.of(UUID.randomUUID()))
                .build();

        ClubEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getName(), entity.getName());
        assertEquals(domain.getTeams(), entity.getTeams());
    }

    @Test
    void toDomain_ShouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        ClubEntity entity = new ClubEntity();
        entity.setId(id);
        entity.setName("Scout Club Entity");
        entity.setTeams(List.of(UUID.randomUUID()));

        Club domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getName(), domain.getName());
        assertEquals(entity.getTeams(), domain.getTeams());
    }

    @Test
    void updateEntityFromDomain_ShouldUpdateExistingEntity() {
        UUID id = UUID.randomUUID();
        Club domain = Club.builder()
                .id(id)
                .name("Updated Name")
                .teams(List.of(UUID.randomUUID()))
                .build();

        ClubEntity entity = new ClubEntity();
        entity.setId(id);
        entity.setName("Old Name");

        mapper.updateEntityFromDomain(domain, entity);

        assertEquals("Updated Name", entity.getName());
        assertEquals(domain.getTeams(), entity.getTeams());
        assertEquals(id, entity.getId());
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDomainIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toDomain_ShouldReturnNull_WhenEntityIsNull() {
        assertNull(mapper.toDomain(null));
    }
}