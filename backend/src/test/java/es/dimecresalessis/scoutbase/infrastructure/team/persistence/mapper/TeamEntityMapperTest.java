package es.dimecresalessis.scoutbase.infrastructure.team.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.SubcategoryEnum;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.infrastructure.team.persistence.TeamEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TeamEntityMapperImpl.class)
class TeamEntityMapperTest {

    @Autowired
    private TeamEntityMapper teamEntityMapper;

    @Test
    void shouldMapDomainToEntity() {
        UUID id = UUID.randomUUID();
        Team domain = Team.builder()
                .id(id)
                .name("Dream Team")
                .category(CategoryEnum.CADETE)
                .subcategory(SubcategoryEnum.SUB16)
                .trainer(UUID.randomUUID())
                .secondTrainer(UUID.randomUUID())
                .players(List.of(UUID.randomUUID()))
                .scouters(List.of(UUID.randomUUID()))
                .build();

        TeamEntity entity = teamEntityMapper.toEntity(domain);

        assertThat(entity.getId()).isEqualTo(id);
        assertThat(entity.getName()).isEqualTo("Dream Team");
        assertThat(entity.getCategory()).isEqualTo(CategoryEnum.CADETE.name());
        assertThat(entity.getSubcategory()).isEqualTo(SubcategoryEnum.SUB16.name());
        assertThat(entity.getTrainer()).isEqualTo(domain.getTrainer());
        assertThat(entity.getSecondTrainer()).isEqualTo(domain.getSecondTrainer());
        assertThat(entity.getPlayers()).hasSize(1);
        assertThat(entity.getScouters()).hasSize(1);
    }

    @Test
    void shouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        TeamEntity entity = TeamEntity.builder()
                .id(id)
                .name("Scout Base FC")
                .category(CategoryEnum.BENJAMIN.name())
                .subcategory(SubcategoryEnum.SUB9.name())
                .trainer(UUID.randomUUID())
                .players(List.of(UUID.randomUUID()))
                .build();

        Team domain = teamEntityMapper.toDomain(entity);

        assertThat(domain.getId()).isEqualTo(id);
        assertThat(domain.getName()).isEqualTo("Scout Base FC");
        assertThat(domain.getCategory()).isEqualTo(CategoryEnum.BENJAMIN);
        assertThat(domain.getSubcategory()).isEqualTo(SubcategoryEnum.SUB9);
        assertThat(domain.getTrainer()).isEqualTo(entity.getTrainer());
    }

    @Test
    void shouldUpdateEntityFromDomain() {
        TeamEntity entity = TeamEntity.builder()
                .id(UUID.randomUUID())
                .name("Old Name")
                .category(CategoryEnum.ALEVIN.name())
                .build();

        Team domain = Team.builder()
                .name("New Name")
                .category(CategoryEnum.INFANTIL)
                .subcategory(SubcategoryEnum.SUB14)
                .build();

        teamEntityMapper.updateEntityFromDomain(domain, entity);

        assertThat(entity.getName()).isEqualTo("New Name");
        assertThat(entity.getCategory()).isEqualTo(CategoryEnum.INFANTIL.name());
        assertThat(entity.getSubcategory()).isEqualTo(SubcategoryEnum.SUB14.name());
    }
}