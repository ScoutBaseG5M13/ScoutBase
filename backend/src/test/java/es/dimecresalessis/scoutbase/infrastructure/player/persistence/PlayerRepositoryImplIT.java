package es.dimecresalessis.scoutbase.infrastructure.player.persistence;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.PositionEnum;
import es.dimecresalessis.scoutbase.infrastructure.player.persistence.mapper.PlayerEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({PlayerRepositoryImpl.class, PlayerEntityMapper.class})
class PlayerRepositoryImplIT {

    @Autowired
    private PlayerRepositoryImpl playerRepository;

    @Autowired
    private JpaPlayerRepository jpaPlayerRepository;

    private UUID playerId;
    private UUID teamId;
    private Player player;

    @BeforeEach
    void setUp() {
        playerId = UUID.randomUUID();
        teamId = UUID.randomUUID();
        player = Player.builder()
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
    }

    @Test
    @DisplayName("save - Should persist new player in database")
    void save_ShouldPersistNewPlayer() {
        Player savedPlayer = playerRepository.save(player);

        Optional<PlayerEntity> entityInDb = jpaPlayerRepository.findById(playerId);
        assertTrue(entityInDb.isPresent());
        assertEquals("Lionel", entityInDb.get().getName());
        assertEquals(savedPlayer.getId(), entityInDb.get().getId());
    }

    @Test
    @DisplayName("save - Should update existing player instead of creating new one")
    void save_ShouldUpdateExistingPlayer() {
        playerRepository.save(player);

        Player updatedPlayer = Player.builder()
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

        playerRepository.save(updatedPlayer);

        assertEquals(1, jpaPlayerRepository.count());
        assertEquals("Lionel Updated", jpaPlayerRepository.findById(playerId).get().getName());
    }

    @Test
    @DisplayName("findById - Should return empty optional if player not exists")
    void findById_ShouldReturnEmpty() {
        Optional<Player> result = playerRepository.findById(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }
}