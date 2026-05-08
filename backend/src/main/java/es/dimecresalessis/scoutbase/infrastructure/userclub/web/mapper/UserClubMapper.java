package es.dimecresalessis.scoutbase.infrastructure.userclub.web.mapper;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.infrastructure.userclub.web.dto.UserClubDTO;
import es.dimecresalessis.scoutbase.infrastructure.userclub.web.dto.UserClubCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.userclub.web.dto.UserClubUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * Infrastructure mapper for converting between {@link UserClubDTO} and {@link UserClub} domain models.
 */
@Mapper(componentModel = "spring")
public interface UserClubMapper {

    UserClub dtoToDomain(UserClubDTO dto);

    @Mapping(target = "adminUserIds", expression = "java(java.util.List.of(creatorUserId))")
    UserClub createToDomain(UserClubCreateRequest dto, UUID creatorUserId);

    UserClub updateToDomain(UserClubUpdateRequest dto);

    UserClubDTO domainToDTO(UserClub domain);
}
