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
        UUID adminId = UUID.randomUUID();
        ClubDTO dto = new ClubDTO(id, List.of(adminId), "Web Club", List.of());

        Club domain = clubMapper.dtoToDomain(dto);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("Web Club", domain.getName());
        assertEquals(adminId, domain.getAdminUserIds().get(0));
    }

    @Test
    void createToDomain_ShouldMapCreateRequestToDomain() {
        UUID adminId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        ClubCreateRequest request = new ClubCreateRequest(
                List.of(adminId),
                "New Scout Club",
                List.of(teamId)
        );

        Club domain = clubMapper.createToDomain(request);

        assertNotNull(domain);
        assertEquals("New Scout Club", domain.getName());
        assertEquals(adminId, domain.getAdminUserIds().get(0));
        // Si tu modelo de dominio Club tiene el campo teams, se mapeará automáticamente
    }

    @Test
    void domainToDTO_ShouldMapDomainToDto() {
        UUID id = UUID.randomUUID();
        Club domain = Club.builder()
                .id(id)
                .name("Domain Club")
                .adminUserIds(List.of(UUID.randomUUID()))
                .build();

        ClubDTO dto = clubMapper.domainToDTO(domain);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals("Domain Club", dto.getName());
    }
}