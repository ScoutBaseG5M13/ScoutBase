package es.dimecresalessis.scoutbase.infrastructure.team.web.mapper;

import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.infrastructure.team.web.dto.TeamCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.team.web.dto.TeamDTO;
import es.dimecresalessis.scoutbase.infrastructure.team.web.dto.TeamUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Infrastructure mapper for converting between {@link TeamDTO} and {@link Team} domain models.
 */
@Mapper(componentModel = "spring")
public interface TeamMapper {

    Team dtoToDomain(TeamDTO dto);

    Team createToDomain(TeamCreateRequest request);

    Team updateToDomain(TeamUpdateRequest dto);

    @Mapping(target = "category", source = "category.categoryName")
    @Mapping(target = "subcategory", source = "subcategory.subcategoryName")
    TeamDTO domainToDTO(Team domain);
}
