package es.dimecresalessis.scoutbase.infrastructure.player.web.mapper;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.shared.domain.PositionEnum;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerCreateRequest;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerDTO;
import es.dimecresalessis.scoutbase.infrastructure.player.web.dto.PlayerUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMapperTest {

    private PlayerMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(PlayerMapper.class);
    }

    @Test
    void dtoToDomain_ShouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        PlayerDTO dto = new PlayerDTO();
        dto.setId(id);
        dto.setName("Player Name");
        dto.setPosition(PositionEnum.PORTERO.getPositionName());

        Player domain = mapper.dtoToDomain(dto);

        assertNotNull(domain);
        assertEquals(dto.getId(), domain.getId());
        assertEquals(dto.getName(), domain.getName());
        assertEquals(dto.getPosition(), domain.getPosition().getPositionName());
    }

    @Test
    void createToDomain_ShouldMapCorrectly_WithProvidedTeamId() {
        UUID teamId = UUID.randomUUID();
        PlayerCreateRequest request = new PlayerCreateRequest();
        request.setName("New Player");
        request.setPosition(PositionEnum.DELANTERO_CENTRO.getPositionName());

        Player domain = mapper.createToDomain(request, teamId);

        assertNotNull(domain);
        assertEquals("New Player", domain.getName());
        assertEquals(PositionEnum.DELANTERO_CENTRO, domain.getPosition());
        assertEquals(teamId, domain.getTeamId());
    }

    @Test
    void updateToDomain_ShouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        PlayerUpdateRequest request = new PlayerUpdateRequest();
        request.setId(id);
        request.setName("Updated Player");
        request.setPosition(PositionEnum.MEDIOCENTRO.getPositionName());

        Player domain = mapper.updateToDomain(request);

        assertNotNull(domain);
        assertEquals(id, domain.getId());
        assertEquals("Updated Player", domain.getName());
        assertEquals(PositionEnum.MEDIOCENTRO, domain.getPosition());
    }

    @Test
    void toDto_ShouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        Player domain = Player.builder()
                .id(id)
                .name("Domain Player")
                .position(PositionEnum.DEFENSA_CENTRAL)
                .build();

        PlayerDTO dto = mapper.toDto(domain);

        assertNotNull(dto);
        assertEquals(domain.getId(), dto.getId());
        assertEquals(domain.getName(), dto.getName());
        assertEquals(PositionEnum.DEFENSA_CENTRAL.name(), dto.getPosition());
    }

    @Test
    void mappers_ShouldReturnNull_WhenInputsAreNull() {
        assertNull(mapper.dtoToDomain(null));
        assertNull(mapper.createToDomain(null, null));
        assertNull(mapper.updateToDomain(null));
        assertNull(mapper.toDto(null));
    }
}