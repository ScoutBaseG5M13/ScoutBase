package es.dimecresalessis.scoutbase.infrastructure.player.web.mapper;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.PositionEnum;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMapperTest {

    private PlayerMapper playerMapper;
    private UUID playerId;
    private UUID teamId;
    private Player playerDomain;
    private PlayerDTO playerDTO;

    @BeforeEach
    void setUp() {
        playerMapper = Mappers.getMapper(PlayerMapper.class);
        playerId = UUID.randomUUID();
        teamId = UUID.randomUUID();
        playerDomain = Player.builder()
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

        playerDTO = new PlayerDTO(
                playerId,
                teamId,
                "Ronald",
                "McGallahan",
                14,
                "ronald@mcgall.es",
                16,
                PositionEnum.PORTERO.name(),
                CategoryEnum.BENJAMIN.name(),
                6
        );
    }

    @Test
    @DisplayName("Should map PlayerDto to Domain Player with existing ID")
    void shouldMapToDomain_WithId() {
        Player result = playerMapper.toDomain(playerDTO);

        assertNotNull(result);
        assertEquals(playerDTO.getId(), result.getId());
        assertEquals(playerDTO.getTeamId(), result.getTeamId());
        assertEquals(playerDTO.getName(), result.getName());
        assertEquals(playerDTO.getSurname(), result.getSurname());
        assertEquals(playerDTO.getAge(), result.getAge());
        assertEquals(playerDTO.getEmail(), result.getEmail());
        assertEquals(playerDTO.getNumber(), result.getNumber());
        assertEquals(playerDTO.getPosition(), result.getPosition().name());
        assertEquals(playerDTO.getCategory(), result.getCategory().name());
    }

    @Test
    @DisplayName("Should map PlayerDto to Domain Player and generate new ID when ID is null")
    void shouldMapToDomain_WithoutId() {
        playerDTO.setId(null);
        Player result = playerMapper.toDomain(playerDTO);

        assertNotNull(result);
        assertEquals(playerDTO.getId(), result.getId());
        assertEquals(playerDTO.getTeamId(), result.getTeamId());
        assertEquals(playerDTO.getName(), result.getName());
        assertEquals(playerDTO.getSurname(), result.getSurname());
        assertEquals(playerDTO.getAge(), result.getAge());
        assertEquals(playerDTO.getEmail(), result.getEmail());
        assertEquals(playerDTO.getNumber(), result.getNumber());
        assertEquals(playerDTO.getPosition(), result.getPosition().name());
        assertEquals(playerDTO.getCategory(), result.getCategory().name());
    }

    @Test
    @DisplayName("Should map Domain Player to PlayerDto")
    void shouldMapToDto() {
        PlayerDTO result = playerMapper.toDto(playerDomain);

        assertNotNull(result);
        assertEquals(playerDomain.getId(), result.getId());
        assertEquals(playerDomain.getTeamId(), result.getTeamId());
        assertEquals(playerDomain.getName(), result.getName());
        assertEquals(playerDomain.getSurname(), result.getSurname());
        assertEquals(playerDomain.getAge(), result.getAge());
        assertEquals(playerDomain.getEmail(), result.getEmail());
        assertEquals(playerDomain.getNumber(), result.getNumber());
        assertEquals(playerDomain.getPosition().name(), result.getPosition());
        assertEquals(playerDomain.getCategory().name(), result.getCategory());
    }

    @Test
    @DisplayName("Should return null when mapping null DTO")
    void shouldReturnNull_WhenDtoIsNull() {
        assertNull(playerMapper.toDomain(null));
    }
}