package es.dimecresalessis.scoutbase.infrastructure.userteam.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.SubcategoryEnum;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.infrastructure.userteam.persistence.UserTeamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Infrastructure mapper for converting between {@link UserTeam} domain models
 * and {@link UserTeamEntity} persistence objects.
 */
@Mapper(componentModel = "spring", imports = { CategoryEnum.class, SubcategoryEnum.class })
public interface UserTeamEntityMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "subcategory", source = "subcategory")
    UserTeamEntity toEntity(UserTeam domain);

    @Mapping(target = "category", expression = "java(CategoryEnum.fromValue(entity.getCategory()))")
    @Mapping(target = "subcategory", expression = "java(SubcategoryEnum.fromValue(entity.getSubcategory()))")
    UserTeam toDomain(UserTeamEntity entity);

    void updateEntityFromDomain(UserTeam domain, @MappingTarget UserTeamEntity entity);
}
