package es.dimecresalessis.scoutbase.infrastructure.team.web.mapper;

import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.SubcategoryEnum;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.infrastructure.team.web.dto.TeamCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.team.web.dto.TeamDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TeamMapperImpl.class)
class TeamMapperTest {

    @Autowired
    private TeamMapper teamMapper;

    @Test
    void shouldMapDomainToDto() {
        Team domain = Team.builder()
                .id(UUID.randomUUID())
                .name("Juvenil A")
                .category(CategoryEnum.JUVENIL)
                .subcategory(SubcategoryEnum.SUB_SUPERIOR)
                .players(List.of(UUID.randomUUID()))
                .build();

        TeamDTO dto = teamMapper.domainToDTO(domain);

        assertThat(dto.getId()).isEqualTo(domain.getId());
        assertThat(dto.getName()).isEqualTo(domain.getName());
        assertThat(dto.getCategory()).isEqualTo(CategoryEnum.JUVENIL.getCategoryName());
        assertThat(dto.getSubcategory()).isEqualTo(SubcategoryEnum.SUB_SUPERIOR.getSubcategoryName());
    }

    @Test
    void shouldMapCreateRequestToDomain() {
        TeamCreateRequest request = new TeamCreateRequest(
                "Nuevos Talentos",
                CategoryEnum.CADETE.name(),
                SubcategoryEnum.SUB16.name()
        );

        Team domain = teamMapper.createToDomain(request);

        assertThat(domain.getName()).isEqualTo("Nuevos Talentos");
        assertThat(domain.getCategory()).isEqualTo(CategoryEnum.CADETE);
        assertThat(domain.getSubcategory()).isEqualTo(SubcategoryEnum.SUB16);
    }

    @Test
    void shouldMapDtoToDomain() {
        UUID id = UUID.randomUUID();
        TeamDTO dto = new TeamDTO(
                id,
                UUID.randomUUID(),
                "Senior B",
                CategoryEnum.JUVENIL.name(),
                SubcategoryEnum.SUB_SUPERIOR.name(),
                List.of(UUID.randomUUID())
        );

        Team domain = teamMapper.dtoToDomain(dto);

        assertThat(domain.getId()).isEqualTo(id);
        assertThat(domain.getName()).isEqualTo("Senior B");
        assertThat(domain.getCategory()).isEqualTo(CategoryEnum.JUVENIL);
        assertThat(domain.getSubcategory()).isEqualTo(SubcategoryEnum.SUB_SUPERIOR);
    }
}