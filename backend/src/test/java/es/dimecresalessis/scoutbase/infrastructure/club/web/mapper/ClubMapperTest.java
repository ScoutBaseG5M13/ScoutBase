package es.dimecresalessis.scoutbase.infrastructure.club.web.mapper;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.infrastructure.club.web.dto.ClubCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.club.web.dto.ClubDTO;
import es.dimecresalessis.scoutbase.infrastructure.club.web.dto.ClubUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClubMapperTest {

    private ClubMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ClubMapper.class);
    }

    @Test
    void dtoToDomain_ShouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        ClubDTO dto = new ClubDTO();
        dto.setId(id);
        dto.setName("Club DTO");
        dto.setTeams(List.of(UUID.randomUUID()));

        Club domain = mapper.dtoToDomain(dto);

        assertNotNull(domain);
        assertEquals(dto.getId(), domain.getId());
        assertEquals(dto.getName(), domain.getName());
        assertEquals(dto.getTeams(), domain.getTeams());
    }

    @Test
    void createToDomain_ShouldMapCorrectly() {
        ClubCreateRequest request = new ClubCreateRequest("New Club");

        Club domain = mapper.createToDomain(request);

        assertNotNull(domain);
        assertEquals("New Club", domain.getName());
    }

    @Test
    void updateToDomain_ShouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        ClubUpdateRequest request = new ClubUpdateRequest();
        request.setId(id);
        request.setName("Updated Club");

        Club domain = mapper.updateToDomain(request);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("Updated Club", domain.getName());
    }

    @Test
    void domainToDTO_ShouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        Club domain = Club.builder()
                .id(id)
                .name("Domain Club")
                .teams(List.of(UUID.randomUUID()))
                .build();

        ClubDTO dto = mapper.domainToDTO(domain);

        assertNotNull(dto);
        assertEquals(domain.getId(), dto.getId());
        assertEquals(domain.getName(), dto.getName());
        assertEquals(domain.getTeams(), dto.getTeams());
    }

    @Test
    void mappers_ShouldReturnNull_WhenInputsAreNull() {
        assertNull(mapper.dtoToDomain(null));
        assertNull(mapper.createToDomain(null));
        assertNull(mapper.updateToDomain(null));
        assertNull(mapper.domainToDTO(null));
    }
}