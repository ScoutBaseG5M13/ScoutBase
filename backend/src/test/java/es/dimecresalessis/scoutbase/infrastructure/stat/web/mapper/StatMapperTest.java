package es.dimecresalessis.scoutbase.infrastructure.stat.web.mapper;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.model.StatEnum;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatDTO;
import es.dimecresalessis.scoutbase.infrastructure.stat.web.dto.StatModifyRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = StatMapperImpl.class)
class StatMapperTest {

    @Autowired
    private StatMapper statMapper;

    @Test
    void shouldMapDtoToDomain() {
        UUID id = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        StatDTO dto = new StatDTO(id, playerId, StatEnum.PASE.statName, StatEnum.PASE.statCode, 8);

        Stat domain = statMapper.dtoToDomain(dto);

        assertThat(domain.getId()).isEqualTo(id);
        assertThat(domain.getPlayerId()).isEqualTo(playerId);
        assertThat(domain.getCode()).isEqualTo(StatEnum.PASE.statCode);
        assertThat(domain.getValue()).isEqualTo(8);
    }

    @Test
    void shouldMapCreateRequestToDomain() {
        UUID playerId = UUID.randomUUID();
        StatCreateRequest request = new StatCreateRequest(playerId, StatEnum.VELOCIDAD.statCode, 9);

        Stat domain = statMapper.createToDomain(request);

        assertThat(domain.getPlayerId()).isEqualTo(playerId);
        assertThat(domain.getCode()).isEqualTo(StatEnum.VELOCIDAD.statCode);
        assertThat(domain.getValue()).isEqualTo(9);
    }

    @Test
    void shouldMapModifyRequestToDomain() {
        UUID playerId = UUID.randomUUID();
        StatModifyRequest request = new StatModifyRequest(playerId, StatEnum.POTENCIA.statCode, 7);

        Stat domain = statMapper.modifyToDomain(request);

        assertThat(domain.getPlayerId()).isEqualTo(playerId);
        assertThat(domain.getCode()).isEqualTo(StatEnum.POTENCIA.statCode);
        assertThat(domain.getValue()).isEqualTo(7);
    }

    @Test
    void shouldMapDomainToDto() {
        UUID id = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        Stat domain = new Stat(id, playerId, StatEnum.RESISTENCIA.statCode, 6);

        StatDTO dto = statMapper.toDto(domain);

        assertThat(dto.getId()).isEqualTo(id);
        assertThat(dto.getPlayerId()).isEqualTo(playerId);
        assertThat(dto.getCode()).isEqualTo(StatEnum.RESISTENCIA.statCode);
        assertThat(dto.getName()).isEqualTo(StatEnum.RESISTENCIA.statName);
        assertThat(dto.getValue()).isEqualTo(6);
    }
}