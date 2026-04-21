package es.dimecresalessis.scoutbase.infrastructure.stat.web.mapper;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.model.StatEnum;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatDTO;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatModifyRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Infrastructure mapper for converting between {@link StatDTO} and {@link Stat} domain models.
 */
@Mapper(componentModel = "spring", imports = { StatEnum.class })
public interface StatMapper {

    Stat dtoToDomain(StatDTO dto);

    Stat createToDomain(StatCreateRequest request);

    Stat modifyToDomain(StatModifyRequest request);

    @Mapping(target = "name", expression = "java(StatEnum.fromStatCode(domain.getCode()).statName)")
    StatDTO toDto(Stat domain);
}
