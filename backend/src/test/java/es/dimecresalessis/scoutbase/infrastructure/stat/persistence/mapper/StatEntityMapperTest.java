package es.dimecresalessis.scoutbase.infrastructure.stat.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.infrastructure.stat.persistence.StatEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class StatEntityMapperTest {

    private final StatEntityMapper mapper = Mappers.getMapper(StatEntityMapper.class);

    @Test
    void shouldMapDomainToEntity() {
        // Usamos "PAS" que es un código válido según tu StatEnum
        Stat domain = new Stat(UUID.randomUUID(), UUID.randomUUID(), "PAS", 8);

        StatEntity entity = mapper.toEntity(domain);

        assertThat(entity.getId()).isEqualTo(domain.getId());
        assertThat(entity.getPlayerId()).isEqualTo(domain.getPlayerId());
        assertThat(entity.getCode()).isEqualTo("PAS");
        assertThat(entity.getValue()).isEqualTo(8);
        assertThat(entity.getName()).isEqualTo("Pase");
    }

    @Test
    void shouldMapEntityToDomain() {
        StatEntity entity = new StatEntity();
        entity.setId(UUID.randomUUID());
        entity.setPlayerId(UUID.randomUUID());
        entity.setCode("VEL");
        entity.setValue(9);
        entity.setName("Velocidad");

        Stat domain = mapper.toDomain(entity);

        assertThat(domain.getId()).isEqualTo(entity.getId());
        assertThat(domain.getPlayerId()).isEqualTo(entity.getPlayerId());
        assertThat(domain.getCode()).isEqualTo("VEL");
        assertThat(domain.getValue()).isEqualTo(9);
    }

    @Test
    void shouldUpdateEntityFromDomain() {
        StatEntity entity = new StatEntity();
        entity.setId(UUID.randomUUID());
        entity.setCode("CON");
        entity.setName("Conducción");

        // Actualizamos a "POT" (Potencia)
        Stat domain = new Stat(entity.getId(), UUID.randomUUID(), "POT", 7);

        mapper.updateEntityFromDomain(domain, entity);

        assertThat(entity.getCode()).isEqualTo("POT");
        assertThat(entity.getValue()).isEqualTo(7);
        assertThat(entity.getName()).isEqualTo("Potencia");
    }
}