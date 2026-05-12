package es.dimecresalessis.scoutbase.infrastructure.userclub.web.mapper;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.infrastructure.userclub.web.dto.UserClubCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.userclub.web.dto.UserClubDTO;
import es.dimecresalessis.scoutbase.infrastructure.userclub.web.dto.UserClubUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserClubMapperTest {

    private UserClubMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(UserClubMapper.class);
    }

    @Test
    void dtoToDomain_ShouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        UserClubDTO dto = new UserClubDTO();
        dto.setId(id);
        dto.setName("UserClub DTO");
        dto.setAdminUserIds(List.of(UUID.randomUUID()));

        UserClub domain = mapper.dtoToDomain(dto);

        assertNotNull(domain);
        assertEquals(dto.getId(), domain.getId());
        assertEquals(dto.getName(), domain.getName());
        assertEquals(dto.getAdminUserIds(), domain.getAdminUserIds());
    }

    @Test
    void createToDomain_ShouldMapRequestAndInitializeAdminList() {
        UUID creatorId = UUID.randomUUID();
        UserClubCreateRequest request = new UserClubCreateRequest();
        request.setName("New UserClub");

        UserClub domain = mapper.createToDomain(request, creatorId);

        assertNotNull(domain);
        assertEquals("New UserClub", domain.getName());
        assertNotNull(domain.getAdminUserIds());
        assertEquals(1, domain.getAdminUserIds().size());
        assertEquals(creatorId, domain.getAdminUserIds().get(0));
    }

    @Test
    void updateToDomain_ShouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        UserClubUpdateRequest request = new UserClubUpdateRequest();
        request.setId(id);
        request.setName("Updated UserClub");

        UserClub domain = mapper.updateToDomain(request);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("Updated UserClub", domain.getName());
    }

    @Test
    void domainToDTO_ShouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        UserClub domain = UserClub.builder()
                .id(id)
                .name("Domain UserClub")
                .adminUserIds(List.of(UUID.randomUUID()))
                .userTeams(List.of(UUID.randomUUID()))
                .managedClubs(List.of(UUID.randomUUID()))
                .build();

        UserClubDTO dto = mapper.domainToDTO(domain);

        assertNotNull(dto);
        assertEquals(domain.getId(), dto.getId());
        assertEquals(domain.getName(), dto.getName());
        assertEquals(domain.getAdminUserIds(), dto.getAdminUserIds());
        assertEquals(domain.getUserTeams(), dto.getUserTeams());
        assertEquals(domain.getManagedClubs(), dto.getManagedClubs());
    }

    @Test
    void mappers_ShouldReturnNull_WhenInputsAreNull() {
        assertNull(mapper.dtoToDomain(null));
        assertNull(mapper.createToDomain(null, null));
        assertNull(mapper.updateToDomain(null));
        assertNull(mapper.domainToDTO(null));
    }
}