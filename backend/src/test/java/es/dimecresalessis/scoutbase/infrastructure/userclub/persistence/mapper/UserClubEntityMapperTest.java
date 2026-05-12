package es.dimecresalessis.scoutbase.infrastructure.userclub.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.infrastructure.userclub.persistence.UserClubEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserClubEntityMapperTest {

    private UserClubEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(UserClubEntityMapper.class);
    }

    @Test
    void toEntity_ShouldMapDomainToEntity() {
        UUID id = UUID.randomUUID();
        List<UUID> admins = List.of(UUID.randomUUID());
        List<UUID> teams = List.of(UUID.randomUUID());

        UserClub domain = UserClub.builder()
                .id(id)
                .name("Test UserClub")
                .adminUserIds(admins)
                .userTeams(teams)
                .build();

        UserClubEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.getId(), entity.getId());
        assertEquals("Test UserClub", entity.getName());
        assertEquals(admins, entity.getAdminUserIds());
        assertEquals(teams, entity.getUserTeams());
    }

    @Test
    void toDomain_ShouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        UserClubEntity entity = UserClubEntity.builder()
                .id(id)
                .name("Persistence UserClub")
                .adminUserIds(List.of(UUID.randomUUID()))
                .managedClubs(List.of(UUID.randomUUID()))
                .build();

        UserClub domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getName(), domain.getName());
        assertEquals(entity.getAdminUserIds(), domain.getAdminUserIds());
        assertEquals(entity.getManagedClubs(), domain.getManagedClubs());
    }

    @Test
    void updateEntityFromDomain_ShouldUpdateExistingEntityFields() {
        UUID id = UUID.randomUUID();
        List<UUID> newAdmins = List.of(UUID.randomUUID());

        UserClub domain = UserClub.builder()
                .id(id)
                .name("Updated Name")
                .adminUserIds(newAdmins)
                .build();

        UserClubEntity entity = UserClubEntity.builder()
                .id(id)
                .name("Old Name")
                .adminUserIds(new ArrayList<>(List.of(UUID.randomUUID())))
                .build();

        mapper.updateEntityFromDomain(domain, entity);

        assertEquals("Updated Name", entity.getName());
        assertEquals(newAdmins, entity.getAdminUserIds());
        assertEquals(id, entity.getId());
    }

    @Test
    void mappers_ShouldReturnNull_WhenInputsAreNull() {
        assertNull(mapper.toEntity(null));
        assertNull(mapper.toDomain(null));
    }
}