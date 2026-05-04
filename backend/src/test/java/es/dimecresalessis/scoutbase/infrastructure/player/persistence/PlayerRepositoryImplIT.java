package es.dimecresalessis.scoutbase.infrastructure.player.persistence;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.shared.domain.PositionEnum;
import es.dimecresalessis.scoutbase.infrastructure.player.persistence.mapper.PlayerEntityMapperImpl;
import es.dimecresalessis.scoutbase.infrastructure.userteam.persistence.JpaUserTeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({PlayerRepositoryImpl.class, PlayerEntityMapperImpl.class})
class PlayerRepositoryImplIT {

    @Autowired
    private PlayerRepositoryImpl playerRepository;

    @Autowired
    private JpaPlayerRepository jpaPlayerRepository;

    @MockitoBean
    private JpaUserTeamRepository jpaUserTeamRepository;

    private UUID playerId;
    private Player player;

    @BeforeEach
    void setUp() {
        playerId = UUID.randomUUID();
        player = Player.builder()
                .id(playerId)
                .name("Ronald")
                .surname("Araujo")
                .age(25)
                .email("ronald@scoutbase.es")
                .number(4)
                .position(PositionEnum.DEFENSA_CENTRAL)
                .priority(1)
                .build();
    }

    @Test
    void save_ShouldPersistNewPlayer() {
        playerRepository.save(player);

        Optional<PlayerEntity> entityInDb = jpaPlayerRepository.findById(playerId);
        assertTrue(entityInDb.isPresent());
        assertEquals("Ronald", entityInDb.get().getName());
        assertEquals("DEFENSA_CENTRAL", entityInDb.get().getPosition());
    }

    @Test
    void save_ShouldUpdateExistingPlayer() {
        playerRepository.save(player);

        Player updatedPlayer = Player.builder()
                .id(playerId)
                .name("Ronald Updated")
                .surname("Araujo")
                .age(25)
                .email("ronald@scoutbase.es")
                .number(4)
                .position(PositionEnum.DEFENSA_CENTRAL)
                .priority(2)
                .build();

        playerRepository.save(updatedPlayer);

        Optional<PlayerEntity> entityInDb = jpaPlayerRepository.findById(playerId);
        assertEquals(1, jpaPlayerRepository.count());
        assertTrue(entityInDb.isPresent());
        assertEquals("Ronald Updated", entityInDb.get().getName());
    }

    @Test
    void findById_ShouldReturnPlayerWhenExists() {
        playerRepository.save(player);

        Optional<Player> result = playerRepository.findById(playerId);

        assertTrue(result.isPresent());
        assertEquals(playerId, result.get().getId());
    }

    @Test
    void findById_ShouldReturnEmptyWhenNotExists() {
        Optional<Player> result = playerRepository.findById(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteById_ShouldRemovePlayer() {
        playerRepository.save(player);
        assertTrue(jpaPlayerRepository.existsById(playerId));

        playerRepository.deleteById(playerId);

        assertFalse(jpaPlayerRepository.existsById(playerId));
    }
}