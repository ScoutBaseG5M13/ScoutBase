package es.dimecresalessis.scoutbase.infrastructure.userclub.web.mapper;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.infrastructure.userclub.web.dto.UserClubCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.userclub.web.dto.UserClubDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserUserClubMapperTest {

    private UserClubMapper userClubMapper;

    @BeforeEach
    void setUp() {
        userClubMapper = new UserClubMapperImpl();
    }

    @Test
    void dtoToDomain_ShouldMapDtoToDomain() {
        UUID id = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        UserClubDTO dto = new UserClubDTO(id, List.of(adminId), "Web Club", List.of());

        UserClub domain = userClubMapper.dtoToDomain(dto);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("Web Club", domain.getName());
        assertEquals(adminId, domain.getAdminUserIds().get(0));
    }

    @Test
    void createToDomain_ShouldMapCreateRequestToDomain() {
        UUID adminId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();
        UserClubCreateRequest request = new UserClubCreateRequest(
                List.of(adminId),
                "New Scout Club",
                List.of(teamId)
        );

        UserClub domain = userClubMapper.createToDomain(request);

        assertNotNull(domain);
        assertEquals("New Scout Club", domain.getName());
        assertEquals(adminId, domain.getAdminUserIds().get(0));
        // Si tu modelo de dominio Club tiene el campo teams, se mapeará automáticamente
    }

    @Test
    void domainToDTO_ShouldMapDomainToDto() {
        UUID id = UUID.randomUUID();
        UserClub domain = UserClub.builder()
                .id(id)
                .name("Domain Club")
                .adminUserIds(List.of(UUID.randomUUID()))
                .build();

        UserClubDTO dto = userClubMapper.domainToDTO(domain);

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals("Domain Club", dto.getName());
    }
}