package es.dimecresalessis.scoutbase.infrastructure.team.web.mapper;

import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.infrastructure.team.web.dto.TeamDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Infrastructure mapper for converting between {@link TeamDTO} and {@link Team} domain models.
 */
@Mapper(componentModel = "spring")
public interface TeamMapper {
    /**
     * Transforms an incoming {@link TeamDTO} into a {@link Team} domain model.
     *
     * @param dto The DTO received from the web request.
     * @return A {@link Team} domain object, or {@code null} if the input was null.
     */
    Team toDomain(TeamDTO dto);

    /**
     * Transforms a {@link Team} domain model into a {@link TeamDTO} for API responses.
     *
     * @param domain The {@link Team} domain object.
     * @return A {@link TeamDTO} formatted for JSON serialization.
     */
    @Mapping(target = "category", source = "category.categoryName")
    @Mapping(target = "subcategory", source = "subcategory.subcategoryName")
    TeamDTO toDto(Team domain);
}
