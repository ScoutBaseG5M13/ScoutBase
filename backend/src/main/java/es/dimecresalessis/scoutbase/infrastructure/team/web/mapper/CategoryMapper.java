package es.dimecresalessis.scoutbase.infrastructure.team.web.mapper;

import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.SubcategoryEnum;
import es.dimecresalessis.scoutbase.infrastructure.team.web.dto.CategoryEnumDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * Infrastructure mapper for converting between {@link CategoryEnumDTO} and {@link CategoryEnum} domain models.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "name", source = "categoryName")
    @Mapping(target = "subcategories", source = "subcategories", qualifiedByName = "toSubcategoryDTOList")
    CategoryEnumDTO domainToDTO(CategoryEnum domain);

    @Named("toSubcategoryDTOList")
    default List<String> toSubcategoryDTOList(List<SubcategoryEnum> subcategories) {
        return subcategories.stream()
                .map(SubcategoryEnum::name)
                .toList();
    }
}
