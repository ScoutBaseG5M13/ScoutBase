package es.dimecresalessis.scoutbase.infrastructure.player.web.mapper;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMapperTest {

    private PlayerMapper playerMapper;
    private UUID playerId;
    private String name;
    private String team;
    private String email;

    @BeforeEach
    void setUp() {
        playerMapper = new PlayerMapper();
        playerId = UUID.randomUUID();
        name = "Palermo McMommy";
        team = "Screvilla FC";
        email = "ronald@scoutbase.es";
    }

    @Test
    @DisplayName("Should map PlayerDto to Domain Player with existing ID")
    void shouldMapToDomain_WithId() {
        PlayerDto dto = new PlayerDto(playerId, name, team, email);

        Player result = playerMapper.toDomain(dto);

        assertNotNull(result);
        assertEquals(playerId, result.getId());
        assertEquals(name, result.getName());
        assertEquals(team, result.getTeam());
        assertEquals(email, result.getEmail());
    }

    @Test
    @DisplayName("Should map PlayerDto to Domain Player and generate new ID when ID is null")
    void shouldMapToDomain_WithoutId() {
        PlayerDto dto = new PlayerDto(null, name, team, email);

        Player result = playerMapper.toDomain(dto);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(name, result.getName());
        assertEquals(team, result.getTeam());
        assertEquals(email, result.getEmail());
    }

    @Test
    @DisplayName("Should map Domain Player to PlayerDto")
    void shouldMapToDto() {
        Player domain = new Player(playerId, name, team, email);

        PlayerDto result = playerMapper.toDto(domain);

        assertNotNull(result);
        assertEquals(playerId, result.getId());
        assertEquals(name, result.getName());
        assertEquals(team, result.getTeam());
        assertEquals(email, result.getEmail());
    }

    @Test
    @DisplayName("Should return null when mapping null DTO")
    void shouldReturnNull_WhenDtoIsNull() {
        assertNull(playerMapper.toDomain(null));
    }
}