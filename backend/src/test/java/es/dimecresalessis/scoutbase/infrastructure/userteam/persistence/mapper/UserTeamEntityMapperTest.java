package es.dimecresalessis.scoutbase.infrastructure.userteam.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.SubcategoryEnum;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.infrastructure.userteam.persistence.UserTeamEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTeamEntityMapperTest {

    private UserTeamEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(UserTeamEntityMapper.class);
    }

    @Test
    void toEntity_ShouldMapDomainToEntity() {
        UUID id = UUID.randomUUID();
        UUID clubId = UUID.randomUUID();
        UUID trainerId = UUID.randomUUID();
        List<UUID> scouterIds = List.of(UUID.randomUUID(), UUID.randomUUID());

        UserTeam domain = UserTeam.builder()
                .id(id)
                .name("Juvenil A")
                .category(CategoryEnum.JUVENIL)
                .subcategory(SubcategoryEnum.SUB_SUPERIOR)
                .userClub(clubId)
                .trainer(trainerId)
                .scouters(scouterIds)
                .build();

        UserTeamEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.getId(), entity.getId());
        assertEquals("Juvenil A", entity.getName());
        assertEquals(CategoryEnum.JUVENIL.name(), entity.getCategory());
        assertEquals(SubcategoryEnum.SUB_SUPERIOR.name(), entity.getSubcategory());
        assertEquals(clubId, entity.getUserClub());
        assertEquals(trainerId, entity.getTrainer());
        assertEquals(scouterIds, entity.getScouters());
    }

    @Test
    void toDomain_ShouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        UUID clubId = UUID.randomUUID();
        UserTeamEntity entity = UserTeamEntity.builder()
                .id(id)
                .name("Cadete B")
                .category(CategoryEnum.CADETE.name())
                .subcategory(SubcategoryEnum.SUB16.name())
                .userClub(clubId)
                .build();

        UserTeam domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getName(), domain.getName());
        assertEquals(CategoryEnum.CADETE, domain.getCategory());
        assertEquals(SubcategoryEnum.SUB16, domain.getSubcategory());
        assertEquals(clubId, domain.getUserClub());
    }

    @Test
    void updateEntityFromDomain_ShouldUpdateExistingEntityFields() {
        UUID id = UUID.randomUUID();
        UUID newTrainer = UUID.randomUUID();
        UserTeam domain = UserTeam.builder()
                .id(id)
                .name("Updated Name")
                .category(CategoryEnum.INFANTIL)
                .subcategory(SubcategoryEnum.SUB14)
                .trainer(newTrainer)
                .build();

        UserTeamEntity entity = UserTeamEntity.builder()
                .id(id)
                .name("Old Name")
                .category(CategoryEnum.ALEVIN.name())
                .subcategory(SubcategoryEnum.SUB12.name())
                .build();

        mapper.updateEntityFromDomain(domain, entity);

        assertEquals("Updated Name", entity.getName());
        assertEquals(CategoryEnum.INFANTIL.name(), entity.getCategory());
        assertEquals(SubcategoryEnum.SUB14.name(), entity.getSubcategory());
        assertEquals(newTrainer, entity.getTrainer());
    }

    @Test
    void toDomain_ShouldThrowException_WhenCategoryValueIsInvalid() {
        UserTeamEntity entity = UserTeamEntity.builder()
                .category("UNKNOWN_CATEGORY")
                .subcategory(SubcategoryEnum.SUB16.name())
                .build();

        assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(entity));
    }

    @Test
    void toDomain_ShouldThrowException_WhenSubcategoryValueIsInvalid() {
        UserTeamEntity entity = UserTeamEntity.builder()
                .category(CategoryEnum.JUVENIL.name())
                .subcategory("UNKNOWN_SUB")
                .build();

        assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(entity));
    }

    @Test
    void mappers_ShouldReturnNull_WhenInputsAreNull() {
        assertNull(mapper.toEntity(null));
        assertNull(mapper.toDomain(null));
    }
}