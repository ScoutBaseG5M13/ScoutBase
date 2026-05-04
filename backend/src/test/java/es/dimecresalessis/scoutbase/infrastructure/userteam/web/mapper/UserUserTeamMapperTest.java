package es.dimecresalessis.scoutbase.infrastructure.userteam.web.mapper;

import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.SubcategoryEnum;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.infrastructure.userteam.web.dto.UserTeamCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.userteam.web.dto.UserTeamDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = UserTeamMapperImpl.class)
class UserUserTeamMapperTest {

    @Autowired
    private UserTeamMapper userTeamMapper;

    @Test
    void shouldMapDomainToDto() {
        UserTeam domain = UserTeam.builder()
                .id(UUID.randomUUID())
                .name("Juvenil A")
                .category(CategoryEnum.JUVENIL)
                .subcategory(SubcategoryEnum.SUB_SUPERIOR)
                .trainer(UUID.randomUUID())
                .secondTrainer(UUID.randomUUID())
                .players(List.of(UUID.randomUUID()))
                .scouters(List.of(UUID.randomUUID()))
                .build();

        UserTeamDTO dto = userTeamMapper.toDto(domain);

        assertThat(dto.getId()).isEqualTo(domain.getId());
        assertThat(dto.getName()).isEqualTo(domain.getName());
        assertThat(dto.getCategory()).isEqualTo(CategoryEnum.JUVENIL.getCategoryName());
        assertThat(dto.getSubcategory()).isEqualTo(SubcategoryEnum.SUB_SUPERIOR.getSubcategoryName());
        assertThat(dto.getTrainer()).isEqualTo(domain.getTrainer());
        assertThat(dto.getSecondTrainer()).isEqualTo(domain.getSecondTrainer());
    }

    @Test
    void shouldMapCreateRequestToDomain() {
        UUID trainerId = UUID.randomUUID();
        UserTeamCreateRequest request = new UserTeamCreateRequest(
                "Nuevos Talentos",
                CategoryEnum.CADETE.name(),
                SubcategoryEnum.SUB16.name(),
                List.of(UUID.randomUUID()),
                List.of(trainerId),
                List.of(UUID.randomUUID()),
                UUID.randomUUID()
        );

        UserTeam domain = userTeamMapper.createToDomain(request);

        assertThat(domain.getName()).isEqualTo("Nuevos Talentos");
        assertThat(domain.getCategory()).isEqualTo(CategoryEnum.CADETE);
        assertThat(domain.getSubcategory()).isEqualTo(SubcategoryEnum.SUB16);
        assertThat(domain.getPlayers()).hasSize(1);
    }

    @Test
    void shouldMapDtoToDomain() {
        UUID id = UUID.randomUUID();
        UserTeamDTO dto = new UserTeamDTO(
                id,
                "Senior B",
                CategoryEnum.JUVENIL.name(),
                SubcategoryEnum.SUB_SUPERIOR.name(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                List.of(UUID.randomUUID()),
                List.of(UUID.randomUUID())
        );

        UserTeam domain = userTeamMapper.dtoToDomain(dto);

        assertThat(domain.getId()).isEqualTo(id);
        assertThat(domain.getName()).isEqualTo("Senior B");
        assertThat(domain.getCategory()).isEqualTo(CategoryEnum.JUVENIL);
        assertThat(domain.getSubcategory()).isEqualTo(SubcategoryEnum.SUB_SUPERIOR);
    }
}