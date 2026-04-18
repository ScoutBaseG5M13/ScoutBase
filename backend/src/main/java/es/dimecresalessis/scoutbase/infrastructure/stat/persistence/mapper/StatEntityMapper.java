package es.dimecresalessis.scoutbase.infrastructure.stat.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.infrastructure.stat.persistence.StatEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Infrastructure mapper for converting between {@link Stat} domain models
 * and {@link StatEntity} persistence objects.
 */
@Mapper(componentModel = "spring")
public interface StatEntityMapper {

    /**
     * Transforms a {@link Stat} domain object into a {@link StatEntity}
     * suitable for database operations.
     *
     * @param domain The domain-level Stat entity to be converted.
     * @return A {@link StatEntity} ready for persistence via JPA.
     */
    @Mapping(target = "id", source = "id")
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

    @Mapping(target = "id", source = "id")
    void updateEntityFromDomain(Stat domain, @MappingTarget StatEntity entity);
}
