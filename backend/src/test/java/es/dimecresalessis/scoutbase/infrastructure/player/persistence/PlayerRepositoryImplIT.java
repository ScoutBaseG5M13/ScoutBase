package es.dimecresalessis.scoutbase.infrastructure.player.persistence;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
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
    private Player player;

    @BeforeEach
    void setUp() {
        playerId = UUID.randomUUID();
        player = Player.builder()
                .id(playerId)
                .name("Lionel")
                .team("Miami")
                .email("leo@mesa.com")
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
                .name("Lionel Updated")
                .team("Miami")
                .email("leo@scout.com")
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