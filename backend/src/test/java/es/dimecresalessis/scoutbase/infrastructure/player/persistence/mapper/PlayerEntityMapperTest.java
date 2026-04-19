package es.dimecresalessis.scoutbase.infrastructure.player.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.shared.domain.PositionEnum;
import es.dimecresalessis.scoutbase.infrastructure.player.persistence.PlayerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerEntityMapperTest {

    private PlayerEntityMapper playerEntityMapper;
    private UUID playerId;
    private Player playerDomain;
    private PlayerEntity playerEntity;

    @BeforeEach
    void setUp() {
        playerEntityMapper = new PlayerEntityMapperImpl();
        playerId = UUID.randomUUID();

        playerDomain = Player.builder()
                .id(playerId)
                .name("Ronald")
                .surname("Araujo")
                .age(25)
                .email("ronald@scoutbase.es")
                .number(4)
                .position(PositionEnum.DEFENSA_CENTRAL)
                .priority(1)
                .build();

        playerEntity = PlayerEntity.builder()
                .id(playerId)
                .name("Ronald")
                .surname("Araujo")
                .age(25)
                .email("ronald@scoutbase.es")
                .number(4)
                .position("DEFENSA_CENTRAL")
                .priority(1)
                .build();
    }

    @Test
    void toEntity_ShouldMapDomainToEntity() {
        PlayerEntity result = playerEntityMapper.toEntity(playerDomain);

        assertNotNull(result);
        assertEquals(playerDomain.getId(), result.getId());
        assertEquals(playerDomain.getName(), result.getName());
        assertEquals(playerDomain.getSurname(), result.getSurname());
        assertEquals(playerDomain.getPosition().name(), result.getPosition());
        assertEquals(playerDomain.getPriority(), result.getPriority());
    }

    @Test
    void toDomain_ShouldMapEntityToDomain() {
        Player result = playerEntityMapper.toDomain(playerEntity);

        assertNotNull(result);
        assertEquals(playerEntity.getId(), result.getId());
        assertEquals(playerEntity.getName(), result.getName());
        assertEquals(playerEntity.getSurname(), result.getSurname());
        assertEquals(PositionEnum.DEFENSA_CENTRAL, result.getPosition());
        assertEquals(playerEntity.getPriority(), result.getPriority());
    }

    @Test
    void updateEntityFromDomain_ShouldUpdateFieldsWithNewData() {
        PlayerEntity entityToUpdate = PlayerEntity.builder()
                .id(playerId)
                .name("Old Name")
                .surname("Old Surname")
                .position("PORTERO")
                .priority(5)
                .build();

        playerEntityMapper.updateEntityFromDomain(playerDomain, entityToUpdate);

        assertEquals("Ronald", entityToUpdate.getName());
        assertEquals("Araujo", entityToUpdate.getSurname());
        assertEquals("DEFENSA_CENTRAL", entityToUpdate.getPosition());
        assertEquals(1, entityToUpdate.getPriority());
        assertEquals(playerId, entityToUpdate.getId());
    }
}