package es.dimecresalessis.scoutbase.infrastructure.player.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.shared.domain.PositionEnum;
import es.dimecresalessis.scoutbase.infrastructure.player.persistence.PlayerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerEntityMapperTest {

    private PlayerEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(PlayerEntityMapper.class);
    }

    @Test
    void toEntity_ShouldMapDomainToEntity() {
        UUID id = UUID.randomUUID();
        Player domain = Player.builder()
                .id(id)
                .name("John Doe")
                .position(PositionEnum.PORTERO)
                .build();

        PlayerEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(domain.getId(), entity.getId());
        assertEquals(domain.getName(), entity.getName());
        assertEquals(PositionEnum.PORTERO, PositionEnum.valueOf(entity.getPosition()));
    }

    @Test
    void toDomain_ShouldMapEntityToDomain() {
        UUID id = UUID.randomUUID();
        PlayerEntity entity = new PlayerEntity();
        entity.setId(id);
        entity.setName("Jane Doe");
        entity.setPosition(PositionEnum.PORTERO.name());

        Player domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getName(), domain.getName());
        assertEquals(PositionEnum.PORTERO, domain.getPosition());
    }

    @Test
    void updateEntityFromDomain_ShouldUpdateExistingEntity() {
        UUID id = UUID.randomUUID();
        Player domain = Player.builder()
                .id(id)
                .name("Updated Name")
                .position(PositionEnum.DELANTERO_CENTRO)
                .build();

        PlayerEntity entity = new PlayerEntity();
        entity.setId(id);
        entity.setName("Old Name");
        entity.setPosition(PositionEnum.PORTERO.name());

        mapper.updateEntityFromDomain(domain, entity);

        assertEquals("Updated Name", entity.getName());
        assertEquals(PositionEnum.DELANTERO_CENTRO, PositionEnum.valueOf(entity.getPosition()));
        assertEquals(id, entity.getId());
    }

    @Test
    void toDomain_ShouldThrowException_WhenPositionValueIsInvalid() {
        PlayerEntity entity = new PlayerEntity();
        entity.setPosition("INVALID_POSITION");

        assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(entity));
    }

    @Test
    void mappers_ShouldReturnNull_WhenInputsAreNull() {
        assertNull(mapper.toEntity(null));
        assertNull(mapper.toDomain(null));
    }
}