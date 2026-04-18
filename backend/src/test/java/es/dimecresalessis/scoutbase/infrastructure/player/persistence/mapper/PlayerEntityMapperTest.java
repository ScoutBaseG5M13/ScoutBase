package es.dimecresalessis.scoutbase.infrastructure.player.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.PositionEnum;
import es.dimecresalessis.scoutbase.infrastructure.player.persistence.PlayerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerEntityMapperTest {

    private PlayerEntityMapper playerEntityMapper;
    private UUID playerId;
    private UUID teamId;
    private Player playerDomain;
    private PlayerEntity playerEntity;

    @BeforeEach
    void setUp() {
        playerEntityMapper = Mappers.getMapper(PlayerEntityMapper.class);
        playerId = UUID.randomUUID();
        teamId = UUID.randomUUID();
        playerDomain = Player.builder()
                .id(playerId)
                .teamId(teamId)
                .name("Ronald")
                .surname("McGallahan")
                .age(14)
                .email("ronald@mcgall.es")
                .number(16)
                .position(PositionEnum.PORTERO.name())
                .category(CategoryEnum.BENJAMIN.name())
                .priority(8)
                .build();
        playerEntity = PlayerEntity.builder()
                .id(playerId)
                .teamId(teamId)
                .name("Lionel")
                .surname("Updated")
                .age(16)
                .email("lionel@updated.es")
                .number(7)
                .position(PositionEnum.DELANTERO_CENTRO.name())
                .category(CategoryEnum.CADETE.name())
                .priority(6)
                .build();
    }

    @Test
    @DisplayName("Should map Domain Player to PlayerEntity")
    void shouldMapToEntity() {
        PlayerEntity result = playerEntityMapper.toEntity(playerDomain);

        assertNotNull(result);
        assertEquals(playerEntity.getId(), result.getId());
        assertEquals(playerEntity.getTeamId(), result.getTeamId());
        assertEquals(playerEntity.getName(), result.getName());
        assertEquals(playerEntity.getSurname(), result.getSurname());
        assertEquals(playerEntity.getAge(), result.getAge());
        assertEquals(playerEntity.getEmail(), result.getEmail());
        assertEquals(playerEntity.getNumber(), result.getNumber());
        assertEquals(playerEntity.getPosition(), result.getPosition());
        assertEquals(playerEntity.getCategory(), result.getCategory());
        assertEquals(playerEntity.getPriority(), result.getPriority());
    }

    @Test
    @DisplayName("Should map PlayerEntity to Domain Player")
    void shouldMapToDomain() {
        Player result = playerEntityMapper.toDomain(playerEntity);
        assertNotNull(result);
        assertEquals(playerDomain.getId(), result.getId());
        assertEquals(playerDomain.getTeamId(), result.getTeamId());
        assertEquals(playerDomain.getName(), result.getName());
        assertEquals(playerDomain.getSurname(), result.getSurname());
        assertEquals(playerDomain.getAge(), result.getAge());
        assertEquals(playerDomain.getEmail(), result.getEmail());
        assertEquals(playerDomain.getNumber(), result.getNumber());
        assertEquals(playerDomain.getPosition(), result.getPosition());
        assertEquals(playerDomain.getCategory(), result.getCategory());
        assertEquals(playerDomain.getPriority(), result.getPriority());
    }
}