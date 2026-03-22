package es.dimecresalessis.scoutbase.infrastructure.player.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.infrastructure.player.persistence.PlayerEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerEntityMapperTest {

    private PlayerEntityMapper playerEntityMapper;
    private UUID playerId;
    private String name;
    private String team;
    private String email;

    @BeforeEach
    void setUp() {
        playerEntityMapper = new PlayerEntityMapper();
        playerId = UUID.randomUUID();
        name = "Ronald McGallahan";
        team = "Scoutbase FC";
        email = "ronald@scoutbase.es";
    }

    @Test
    @DisplayName("Should map Domain Player to PlayerEntity")
    void shouldMapToEntity() {
        Player domain = new Player(playerId, name, team, email);

        PlayerEntity result = playerEntityMapper.toEntity(domain);

        assertNotNull(result);
        assertEquals(playerId, result.getId());
        assertEquals(name, result.getName());
        assertEquals(team, result.getTeam());
        assertEquals(email, result.getEmail());
    }

    @Test
    @DisplayName("Should map PlayerEntity to Domain Player")
    void shouldMapToDomain() {
        PlayerEntity entity = new PlayerEntity();
        entity.setId(playerId);
        entity.setName(name);
        entity.setTeam(team);
        entity.setEmail(email);

        Player result = playerEntityMapper.toDomain(entity);

        assertNotNull(result);
        assertEquals(playerId, result.getId());
        assertEquals(name, result.getName());
        assertEquals(team, result.getTeam());
        assertEquals(email, result.getEmail());
    }
}