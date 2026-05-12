package es.dimecresalessis.scoutbase.infrastructure.team.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.SubcategoryEnum;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.infrastructure.team.persistence.TeamEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TeamEntityMapperTest {

    private TeamEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TeamEntityMapper.class);
    }

    @Test
    void toEntity_ShouldMapDomainToEntity() {
        UUID id = UUID.randomUUID();
        Team domain = Team.builder()
                .id(id)
                .name("Juvenil A")
                .category(CategoryEnum.JUVENIL)
                .subcategory(SubcategoryEnum.SUB_SUPERIOR)
                .players(List.of(UUID.randomUUID()))
                .build();

        TeamEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getName(), entity.getName());
        assertEquals(CategoryEnum.JUVENIL.name(), entity.getCategory());
        assertEquals(SubcategoryEnum.SUB_SUPERIOR.name(), entity.getSubcategory());
        assertEquals(domain.getPlayers(), entity.getPlayers());
    }

    @Test
    void toDomain_ShouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        TeamEntity entity = new TeamEntity();
        entity.setId(id);
        entity.setName("Cadet B");
        entity.setCategory(CategoryEnum.CADETE.name());
        entity.setSubcategory(SubcategoryEnum.SUB16.name());
        entity.setPlayers(List.of(UUID.randomUUID()));

        Team domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getName(), domain.getName());
        assertEquals(CategoryEnum.CADETE, domain.getCategory());
        assertEquals(SubcategoryEnum.SUB16, domain.getSubcategory());
        assertEquals(entity.getPlayers(), domain.getPlayers());
    }

    @Test
    void updateEntityFromDomain_ShouldUpdateExistingEntity() {
        UUID id = UUID.randomUUID();
        Team domain = Team.builder()
                .id(id)
                .name("Updated Team")
                .category(CategoryEnum.INFANTIL)
                .subcategory(SubcategoryEnum.SUB14)
                .build();

        TeamEntity entity = new TeamEntity();
        entity.setId(id);
        entity.setName("Old Team");
        entity.setCategory(CategoryEnum.ALEVIN.name());
        entity.setSubcategory(SubcategoryEnum.SUB12.name());

        mapper.updateEntityFromDomain(domain, entity);

        assertEquals("Updated Team", entity.getName());
        assertEquals(CategoryEnum.INFANTIL.name(), entity.getCategory());
        assertEquals(SubcategoryEnum.SUB14.name(), entity.getSubcategory());
        assertEquals(id, entity.getId());
    }

    @Test
    void toDomain_ShouldThrowException_WhenSubcategoryValueIsInvalid() {
        TeamEntity entity = new TeamEntity();
        entity.setCategory(CategoryEnum.JUVENIL.name());
        entity.setSubcategory("INVALID_SUB");

        assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(entity));
    }

    @Test
    void toDomain_ShouldThrowException_WhenCategoryValueIsInvalid() {
        TeamEntity entity = new TeamEntity();
        entity.setCategory("INVALID_CAT");
        entity.setSubcategory(SubcategoryEnum.SUB_SUPERIOR.name());

        assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(entity));
    }

    @Test
    void mappers_ShouldReturnNull_WhenInputsAreNull() {
        assertNull(mapper.toEntity(null));
        assertNull(mapper.toDomain(null));
    }
}