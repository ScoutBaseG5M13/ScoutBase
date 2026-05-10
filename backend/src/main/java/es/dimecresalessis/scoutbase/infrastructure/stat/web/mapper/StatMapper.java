package es.dimecresalessis.scoutbase.infrastructure.stat.web.mapper;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.model.StatEnum;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatDTO;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatEnumDTO;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

/**
 * Infrastructure mapper for converting between {@link StatDTO} and {@link Stat} domain models.
 */
@Mapper(componentModel = "spring", imports = { StatEnum.class })
public interface StatMapper {

    Stat dtoToDomain(StatDTO dto);

    Stat createToDomain(StatCreateRequest request, UUID playerId);

    Stat updateToDomain(StatUpdateRequest request);

    @Mapping(target = "name", expression = "java(StatEnum.fromStatCode(domain.getCode()).statName)")
    StatDTO domainToDto(Stat domain);

    @Mapping(target = "name", source = "statName")
    @Mapping(target = "code", source = "statCode")
    StatEnumDTO statEnumToStatEnumDto(StatEnum statEnum);
}
