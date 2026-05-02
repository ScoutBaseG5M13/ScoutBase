package es.dimecresalessis.scoutbase.infrastructure.player.web.mapper;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.shared.domain.PositionEnum;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMapperTest {

    private PlayerMapper playerMapper;
    private UUID playerId;
    private Player playerDomain;
    private PlayerDTO playerDTO;

    @BeforeEach
    void setUp() {
        playerMapper = new PlayerMapperImpl();
        playerId = UUID.randomUUID();

        playerDomain = Player.builder()
                .id(playerId)
                .name("Lionel")
                .surname("Messi")
                .birthYear(1986)
                .email("leo@scoutbase.es")
                .number(10)
                .position(PositionEnum.MEDIAPUNTA)
                .priority(1)
                .build();

        playerDTO = PlayerDTO.builder()
                .id(playerId)
                .name("Ronald")
                .surname("Araujo")
                .birthYear(1995)
                .email("ronald@scoutbase.es")
                .number(4)
                .position("DEFENSA_CENTRAL")
                .priority(1)
                .build();
    }

    @Test
    void dtoToDomain_ShouldMapCorrectly() {
        Player result = playerMapper.dtoToDomain(playerDTO);

        assertNotNull(result);
        assertEquals(playerDTO.getId(), result.getId());
        assertEquals(playerDTO.getName(), result.getName());
        assertEquals(PositionEnum.DEFENSA_CENTRAL, result.getPosition());
    }

    @Test
    void createToDomain_ShouldMapRequestAndHandleEnums() {
        PlayerCreateRequest request = new PlayerCreateRequest(
                "Lamine",
                "Yamal",
                16,
                "lamine@scoutbase.es",
                19,
                "EXTREMO_DERECHO",
                1
        );

        Player result = playerMapper.createToDomain(request);

        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        assertEquals(PositionEnum.EXTREMO_DERECHO, result.getPosition());
    }

    @Test
    void dtoToDomain_ShouldReturnNull_WhenInputIsNull() {
        assertNull(playerMapper.dtoToDomain(null));
        assertNull(playerMapper.createToDomain(null));
    }

    @Test
    void toDto_ShouldMapDomainToDto() {
        PlayerDTO result = playerMapper.toDto(playerDomain);

        assertNotNull(result);
        assertEquals(playerDomain.getId(), result.getId());
        assertEquals(playerDomain.getName(), result.getName());
        assertEquals(playerDomain.getPosition().name(), result.getPosition());
    }

    @Test
    void toDto_ShouldReturnNull_WhenDomainIsNull() {
        assertNull(playerMapper.toDto(null));
    }
}