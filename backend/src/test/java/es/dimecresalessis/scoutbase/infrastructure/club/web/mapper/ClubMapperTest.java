package es.dimecresalessis.scoutbase.infrastructure.club.web.mapper;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.infrastructure.club.web.dto.ClubCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.club.web.dto.ClubDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClubMapperTest {

    private ClubMapper clubMapper;

    @BeforeEach
    void setUp() {
        clubMapper = new ClubMapperImpl();
    }

    @Test
    void dtoToDomain_ShouldMapDtoToDomain() {
        UUID id = UUID.randomUUID();
        UUID userClubId = UUID.randomUUID();
        ClubDTO dto = new ClubDTO(id, "Web Club", List.of(UUID.randomUUID(), UUID.randomUUID()), userClubId);

        Club domain = clubMapper.dtoToDomain(dto);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("Web Club", domain.getName());
    }

    @Test
    void createToDomain_ShouldMapCreateRequestToDomain() {
        ClubCreateRequest request = new ClubCreateRequest("New Scout Club");

        Club domain = clubMapper.createToDomain(request);

        assertNotNull(domain);
        assertEquals("New Scout Club", domain.getName());
    }

    @Test
    void domainToDTO_ShouldMapDomainToDto() {
        UUID id = UUID.randomUUID();
        Club domain = Club.builder()
                .id(id)
                .name("Domain Club")
                .teams(List.of(UUID.randomUUID(), UUID.randomUUID()))
                .build();

        ClubDTO dto = clubMapper.domainToDTO(domain);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals("Domain Club", dto.getName());
    }
}