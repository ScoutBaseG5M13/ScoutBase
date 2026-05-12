package es.dimecresalessis.scoutbase.infrastructure.userteam.web.mapper;

import es.dimecresalessis.scoutbase.domain.shared.domain.SubcategoryEnum;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.infrastructure.userteam.web.dto.UserTeamCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.userteam.web.dto.UserTeamDTO;
import es.dimecresalessis.scoutbase.infrastructure.userteam.web.dto.UserTeamUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * Infrastructure mapper for converting between {@link UserTeamDTO} and {@link UserTeam} domain models.
 */
@Mapper(componentModel = "spring", imports = { SubcategoryEnum.class })
public interface UserTeamMapper {

    UserTeam dtoToDomain(UserTeamDTO dto);

    @Mapping(target = "subcategory", expression = "java(SubcategoryEnum.fromSubcategoryName(request.getSubcategory()))")
    @Mapping(target = "userClub", source = "clubId")
    UserTeam createToDomain(UserTeamCreateRequest request, UUID clubId);

    UserTeam updateToDomain(UserTeamUpdateRequest request);

    @Mapping(target = "category", source = "category.categoryName")
    @Mapping(target = "subcategory", source = "subcategory.subcategoryName")
    UserTeamDTO toDto(UserTeam domain);
}
