package es.dimecresalessis.scoutbase.infrastructure.team.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.SubcategoryEnum;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.infrastructure.team.persistence.TeamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Infrastructure mapper for converting between {@link Team} domain models
 * and {@link TeamEntity} persistence objects.
 */
@Mapper(componentModel = "spring", imports = { CategoryEnum.class, SubcategoryEnum.class })
public interface TeamEntityMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "subcategory", source = "subcategory")
    TeamEntity toEntity(Team domain);

    @Mapping(target = "category", expression = "java(CategoryEnum.fromValue(entity.getCategory()))")
    @Mapping(target = "subcategory", expression = "java(SubcategoryEnum.fromValue(entity.getSubcategory()))")
    Team toDomain(TeamEntity entity);

    void updateEntityFromDomain(Team domain, @MappingTarget TeamEntity entity);
}
