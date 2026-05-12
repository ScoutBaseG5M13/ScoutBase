package es.dimecresalessis.scoutbase.infrastructure.userteam.web.mapper;

import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.SubcategoryEnum;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.infrastructure.userteam.web.dto.UserTeamCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.userteam.web.dto.UserTeamDTO;
import es.dimecresalessis.scoutbase.infrastructure.userteam.web.dto.UserTeamUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTeamMapperTest {

    private UserTeamMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(UserTeamMapper.class);
    }

    @Test
    void dtoToDomain_ShouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        UserTeamDTO dto = new UserTeamDTO();
        dto.setId(id);
        dto.setName("DTO Team");
        dto.setCategory("JUVENIL");

        UserTeam domain = mapper.dtoToDomain(dto);

        assertNotNull(domain);
        assertEquals(dto.getId(), domain.getId());
        assertEquals(dto.getName(), domain.getName());
    }

    @Test
    void createToDomain_ShouldMapRequestAndClubId() {
        UUID clubId = UUID.randomUUID();
        UserTeamCreateRequest request = new UserTeamCreateRequest();
        request.setName("New Team");
        request.setCategory(CategoryEnum.CADETE.getCategoryName());
        request.setSubcategory(SubcategoryEnum.SUB16.getSubcategoryName());

        UserTeam domain = mapper.createToDomain(request, clubId);

        assertNotNull(domain);
        assertEquals("New Team", domain.getName());
        assertEquals(clubId, domain.getUserClub());
        assertEquals(CategoryEnum.CADETE, domain.getCategory());
        assertEquals(SubcategoryEnum.SUB16, domain.getSubcategory());
    }

    @Test
    void updateToDomain_ShouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        UserTeamUpdateRequest request = new UserTeamUpdateRequest();
        request.setId(id);
        request.setName("Updated Team");

        UserTeam domain = mapper.updateToDomain(request);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("Updated Team", domain.getName());
    }

    @Test
    void toDto_ShouldMapDomainToDtoResolvingEnumNames() {
        UUID id = UUID.randomUUID();
        UserTeam domain = UserTeam.builder()
                .id(id)
                .name("Domain Team")
                .category(CategoryEnum.JUVENIL)
                .subcategory(SubcategoryEnum.SUB_SUPERIOR)
                .userClub(UUID.randomUUID())
                .scouters(List.of(UUID.randomUUID()))
                .build();

        UserTeamDTO dto = mapper.toDto(domain);

        assertNotNull(dto);
        assertEquals(domain.getId(), dto.getId());
        assertEquals(domain.getName(), dto.getName());
        assertEquals(CategoryEnum.JUVENIL.getCategoryName(), dto.getCategory());
        assertEquals(SubcategoryEnum.SUB_SUPERIOR.getSubcategoryName(), dto.getSubcategory());
    }

    @Test
    void mappers_ShouldReturnNull_WhenInputsAreNull() {
        assertNull(mapper.dtoToDomain(null));
        assertNull(mapper.createToDomain(null, null));
        assertNull(mapper.updateToDomain(null));
        assertNull(mapper.toDto(null));
    }
}