package es.dimecresalessis.scoutbase.infrastructure.stat.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.model.StatEnum;
import es.dimecresalessis.scoutbase.infrastructure.stat.persistence.StatEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StatEntityMapperTest {

    private StatEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(StatEntityMapper.class);
    }

    @Test
    void toEntity_ShouldMapDomainToEntityWithStatNameFromEnum() {
        UUID id = UUID.randomUUID();
        Stat domain = Stat.builder()
                .id(id)
                .code(StatEnum.REM.statCode)
                .value(5)
                .build();

        StatEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.getId(), entity.getId());
        assertEquals(StatEnum.REM.statCode, entity.getCode());
        assertEquals(StatEnum.REM.statName, entity.getName());
        assertEquals(5, entity.getValue());
    }

    @Test
    void toDomain_ShouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        StatEntity entity = new StatEntity();
        entity.setId(id);
        entity.setCode(StatEnum.VJU.statCode);
        entity.setName(StatEnum.VJU.statName);
        entity.setValue(4);

        Stat domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(StatEnum.VJU.statCode, domain.getCode());
        assertEquals(4, domain.getValue());
    }

    @Test
    void updateEntityFromDomain_ShouldUpdateExistingEntityAndResolveName() {
        UUID id = UUID.randomUUID();
        Stat domain = Stat.builder()
                .id(id)
                .code(StatEnum.FUE.statCode)
                .value(2)
                .build();

        StatEntity entity = new StatEntity();
        entity.setId(id);
        entity.setCode(StatEnum.REM.statCode);
        entity.setName(StatEnum.REM.statName);

        mapper.updateEntityFromDomain(domain, entity);

        assertEquals(StatEnum.FUE.statCode, entity.getCode());
        assertEquals(StatEnum.FUE.statName, entity.getName());
        assertEquals(2, entity.getValue());
    }

    @Test
    void toEntity_ShouldThrowException_WhenStatCodeIsInvalid() {
        assertThrows(IllegalArgumentException.class, () ->  Stat.builder()
                .code("INVALID_CODE")
                .build());
    }

    @Test
    void mappers_ShouldReturnNull_WhenInputsAreNull() {
        assertNull(mapper.toEntity(null));
        assertNull(mapper.toDomain(null));
    }
}