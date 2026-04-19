package es.dimecresalessis.scoutbase.infrastructure.stat.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.model.StatEnum;
import es.dimecresalessis.scoutbase.infrastructure.stat.persistence.StatEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Infrastructure mapper for converting between {@link Stat} domain models
 * and {@link StatEntity} persistence objects.
 */
@Mapper(componentModel = "spring", imports = { StatEnum.class } )
public interface StatEntityMapper {

    /**
     * Transforms a {@link Stat} domain object into a {@link StatEntity}
     * suitable for database operations.
     *
     * @param domain The domain-level Stat entity to be converted.
     * @return A {@link StatEntity} ready for persistence via JPA.
     */
    @Mapping(target = "name", expression = "java(StatEnum.fromStatCode(domain.getCode()).statName)")
    StatEntity toEntity(Stat domain);

    /**
     * Transforms a {@link StatEntity} retrieved from the database back
     * into a {@link Stat} domain object.
     * <p>
     *
     * @param entity The {@link StatEntity} entity retrieved by the repository.
     * @return A {@link Stat} instance.
     */
    Stat toDomain(StatEntity entity);

    @Mapping(target = "name", expression = "java(StatEnum.fromStatCode(domain.getCode()).statName)")
    void updateEntityFromDomain(Stat domain, @MappingTarget StatEntity entity);
}
