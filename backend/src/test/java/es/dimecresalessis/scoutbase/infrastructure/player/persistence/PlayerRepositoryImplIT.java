package es.dimecresalessis.scoutbase.infrastructure.player.persistence;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.domain.shared.domain.PositionEnum;
import es.dimecresalessis.scoutbase.infrastructure.player.persistence.mapper.PlayerEntityMapperImpl;
import es.dimecresalessis.scoutbase.infrastructure.team.persistence.JpaTeamRepository;
import es.dimecresalessis.scoutbase.infrastructure.team.persistence.TeamEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({PlayerRepositoryImpl.class, PlayerEntityMapperImpl.class})
class PlayerRepositoryImplIT {

    @Autowired
    private PlayerRepositoryImpl playerRepositoryImpl;

    @Autowired
    private JpaPlayerRepository jpaPlayerRepository;

    @Autowired
    private JpaTeamRepository jpaTeamRepository;

    private UUID savedTeamUuid;

    @BeforeEach
    void setUp() {
        jpaPlayerRepository.deleteAll();
        jpaTeamRepository.deleteAll();

        TeamEntity team = TeamEntity.builder()
                .id(UUID.randomUUID())
                .clubId(UUID.randomUUID())
                .name("Test Team")
                .build();

        TeamEntity savedTeam = jpaTeamRepository.saveAndFlush(team);
        savedTeamUuid = savedTeam.getId();
    }

    @Test
    void save_ShouldPersistNewPlayer() {
        UUID playerId = UUID.randomUUID();
        Player playerDomain = createPlayerDomain(playerId, "John", "Doe");

        playerRepositoryImpl.save(playerDomain);

        Optional<PlayerEntity> savedEntity = jpaPlayerRepository.findById(playerId);
        assertThat(savedEntity).isPresent();
        assertThat(savedEntity.get().getName()).isEqualTo("John");
        assertThat(savedEntity.get().getTeamId()).isEqualTo(savedTeamUuid.toString());
    }

    @Test
    void save_ShouldUpdateExistingPlayer() {
        UUID playerId = UUID.randomUUID();
        Player initialPlayer = createPlayerDomain(playerId, "Old Name", "Surname");
        playerRepositoryImpl.save(initialPlayer);

        Player updatedPlayer = createPlayerDomain(playerId, "New Name", "Surname");

        playerRepositoryImpl.save(updatedPlayer);

        Optional<PlayerEntity> savedEntity = jpaPlayerRepository.findById(playerId);
        assertThat(savedEntity).isPresent();
        assertThat(savedEntity.get().getName()).isEqualTo("New Name");
    }

    @Test
    void findAll_ShouldReturnAllPlayers() {
        playerRepositoryImpl.save(createPlayerDomain(UUID.randomUUID(), "Player 1", "S1"));
        playerRepositoryImpl.save(createPlayerDomain(UUID.randomUUID(), "Player 2", "S2"));

        List<Player> result = playerRepositoryImpl.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void findById_ShouldReturnPlayer_WhenExists() {
        UUID playerId = UUID.randomUUID();
        playerRepositoryImpl.save(createPlayerDomain(playerId, "Specific", "Player"));

        Optional<Player> result = playerRepositoryImpl.findById(playerId);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Specific");
    }

    @Test
    void deleteById_ShouldRemovePlayer() {
        UUID playerId = UUID.randomUUID();
        playerRepositoryImpl.save(createPlayerDomain(playerId, "To Delete", "Player"));

        playerRepositoryImpl.deleteById(playerId);

        assertThat(jpaPlayerRepository.findById(playerId)).isEmpty();
    }

    private Player createPlayerDomain(UUID id, String name, String surname) {
        return Player.builder()
                .id(id)
                .teamId(savedTeamUuid)
                .name(name)
                .surname(surname)
                .email(name.replace(" ", "").toLowerCase() + "@test.com")
                .birthYear(2000)
                .number(10)
                .position(PositionEnum.DELANTERO_CENTRO)
                .priority(1)
                .build();
    }
}