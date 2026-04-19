package es.dimecresalessis.scoutbase.infrastructure.club.web.mapper;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.infrastructure.club.web.dto.ClubDTO;
import es.dimecresalessis.scoutbase.infrastructure.club.web.dto.ClubCreateRequest;
import org.mapstruct.Mapper;

/**
 * Infrastructure mapper for converting between {@link ClubDTO} and {@link Club} domain models.
 */
@Mapper(componentModel = "spring")
public interface ClubMapper {

    Club dtoToDomain(ClubDTO dto);

    Club createToDomain(ClubCreateRequest dto);

    ClubDTO domainToDTO(Club domain);
}
