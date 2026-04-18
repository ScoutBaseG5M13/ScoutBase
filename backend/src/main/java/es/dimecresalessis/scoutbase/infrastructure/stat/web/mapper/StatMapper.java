package es.dimecresalessis.scoutbase.infrastructure.stat.web.mapper;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Infrastructure mapper for converting between {@link StatDTO} and {@link Stat} domain models.
 */
@Mapper(componentModel = "spring")
public interface StatMapper {

    /**
     * Transforms an incoming {@link StatDTO} into a {@link Stat} domain model.
     *
     * @param dto The DTO received from the web request.
     * @return A {@link Stat} domain object, or {@code null} if the input was null.
     */
    @Mapping(target = "id", source = "id")
    Stat toDomain(StatDTO dto);

    /**
     * Transforms a {@link Stat} domain model into a {@link StatDTO} for API responses.
     *
     * @param domain The {@link Stat} domain object.
     * @return A {@link StatDTO} formatted for JSON serialization.
     */
    @Mapping(target = "id", source = "id")
    StatDTO toDto(Stat domain);
}
