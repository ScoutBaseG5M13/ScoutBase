package es.dimecresalessis.scoutbase.infrastructure.stat.web.mapper;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.model.StatEnum;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatDTO;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatEnumDTO;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class StatMapperTest {

    private StatMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(StatMapper.class);
    }

    @Test
    void dtoToDomain_ShouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        StatDTO dto = new StatDTO();
        dto.setId(id);
        dto.setCode(StatEnum.REM.statCode);
        dto.setValue(1);

        Stat domain = mapper.dtoToDomain(dto);

        assertNotNull(domain);
        assertEquals(dto.getId(), domain.getId());
        assertEquals(dto.getCode(), domain.getCode());
        assertEquals(dto.getValue(), domain.getValue());
    }

    @Test
    void createToDomain_ShouldMapCorrectly_WithPlayerId() {
        UUID playerId = UUID.randomUUID();
        StatCreateRequest request = new StatCreateRequest();
        request.setCode(StatEnum.VJU.statCode);
        request.setValue(3);

        Stat domain = mapper.createToDomain(request, playerId);

        assertNotNull(domain);
        assertEquals(StatEnum.VJU.statCode, domain.getCode());
        assertEquals(3, domain.getValue());
        assertEquals(playerId, domain.getPlayerId());
    }

    @Test
    void updateToDomain_ShouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        StatUpdateRequest request = new StatUpdateRequest();
        request.setId(id);
        request.setCode(StatEnum.FUE.statCode);
        request.setValue(3);

        Stat domain = mapper.updateToDomain(request);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals(StatEnum.FUE.statCode, domain.getCode());
        assertEquals(3, domain.getValue());
    }

    @Test
    void domainToDto_ShouldMapCorrectly_AndResolveNameFromEnum() {
        UUID id = UUID.randomUUID();
        Stat domain = Stat.builder()
                .id(id)
                .code(StatEnum.REM.statCode)
                .value(0)
                .build();

        StatDTO dto = mapper.domainToDto(domain);

        assertNotNull(dto);
        assertEquals(domain.getId(), dto.getId());
        assertEquals(domain.getCode(), dto.getCode());
        assertEquals(StatEnum.REM.statName, dto.getName());
        assertEquals(domain.getValue(), dto.getValue());
    }

    @Test
    void statEnumToStatEnumDto_ShouldMapFieldsCorrectly() {
        StatEnum statEnum = StatEnum.REM;

        StatEnumDTO dto = mapper.statEnumToStatEnumDto(statEnum);

        assertNotNull(dto);
        assertEquals(statEnum.statName, dto.getName());
        assertEquals(statEnum.statCode, dto.getCode());
        assertEquals(statEnum.type.name(), dto.getType());
    }

    @Test
    void domainToDto_ShouldThrowException_WhenCodeIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> Stat.builder()
                .code("INVALID_CODE")
                .build());
    }

    @Test
    void mappers_ShouldReturnNull_WhenInputsAreNull() {
        assertNull(mapper.dtoToDomain(null));
        assertNull(mapper.createToDomain(null, null));
        assertNull(mapper.updateToDomain(null));
        assertNull(mapper.domainToDto(null));
        assertNull(mapper.statEnumToStatEnumDto(null));
    }
}