package es.dimecresalessis.scoutbase.infrastructure.stat.persistence;

import es.dimecresalessis.scoutbase.domain.stat.exception.StatException;
import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.infrastructure.player.persistence.JpaPlayerRepository;
import es.dimecresalessis.scoutbase.infrastructure.player.persistence.PlayerEntity;
import es.dimecresalessis.scoutbase.infrastructure.stat.persistence.mapper.StatEntityMapperImpl;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
@Import({StatRepositoryImpl.class, StatEntityMapperImpl.class})
class StatRepositoryImplIT {

    @Autowired
    private StatRepositoryImpl statRepositoryImpl;

    @Autowired
    private JpaStatRepository jpaStatRepository;

    @Autowired
    private JpaPlayerRepository jpaPlayerRepository;

    @Autowired
    private JpaTeamRepository jpaTeamRepository;

    private UUID savedPlayerId;

    @BeforeEach
    void setUp() {
        jpaStatRepository.deleteAll();
        jpaPlayerRepository.deleteAll();
        jpaTeamRepository.deleteAll();

        TeamEntity team = TeamEntity.builder()
                .id(UUID.randomUUID())
                .clubId(UUID.randomUUID())
                .name("Scout Team")
                .build();
        jpaTeamRepository.saveAndFlush(team);

        PlayerEntity player = PlayerEntity.builder()
                .id(UUID.randomUUID())
                .teamId(team.getId().toString())
                .name("Lionel")
                .surname("Messi")
                .email("leo@test.com")
                .build();
        PlayerEntity savedPlayer = jpaPlayerRepository.saveAndFlush(player);
        savedPlayerId = savedPlayer.getId();
    }

    @Test
    void findAll_ShouldReturnAllStats() {
        statRepositoryImpl.save(createStatDomain(UUID.randomUUID(), "VEL", 5));
        statRepositoryImpl.save(createStatDomain(UUID.randomUUID(), "TIR", 4));

        List<Stat> result = statRepositoryImpl.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void findById_ShouldReturnStat_WhenExists() {
        UUID statId = UUID.randomUUID();
        statRepositoryImpl.save(createStatDomain(statId, "DR1", 5));

        Optional<Stat> result = statRepositoryImpl.findById(statId);

        assertThat(result).isPresent();
        assertThat(result.get().getCode()).isEqualTo("DR1");
    }

    @Test
    void findAllByPlayerId_ShouldReturnPlayerSpecificStats() {
        statRepositoryImpl.save(createStatDomain(UUID.randomUUID(), "FUE", 4));
        statRepositoryImpl.save(createStatDomain(UUID.randomUUID(), "OMA", 3));

        List<Stat> result = statRepositoryImpl.findAllByPlayerId(savedPlayerId);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(s -> s.getPlayerId().equals(savedPlayerId));
    }

    @Test
    void save_ShouldPersistNewStat() {
        UUID statId = UUID.randomUUID();
        Stat statDomain = createStatDomain(statId, "VJU", 5);

        statRepositoryImpl.save(statDomain);

        Optional<StatEntity> saved = jpaStatRepository.findById(statId);
        assertThat(saved).isPresent();
        assertThat(saved.get().getCode()).isEqualTo("VJU");
    }

    @Test
    void save_ShouldThrowException_WhenStatCodeAlreadyExistsForPlayer() {
        statRepositoryImpl.save(createStatDomain(UUID.randomUUID(), "PCA", 4));
        Stat duplicateStat = createStatDomain(UUID.randomUUID(), "PCA", 3);

        assertThatThrownBy(() -> statRepositoryImpl.save(duplicateStat))
                .isInstanceOf(StatException.class);
    }

    @Test
    void update_ShouldModifyExistingStat() {
        UUID statId = UUID.randomUUID();
        statRepositoryImpl.save(createStatDomain(statId, "REM", 2));
        Stat updateData = createStatDomain(statId, "REM", 5);

        statRepositoryImpl.update(updateData);

        Optional<StatEntity> updated = jpaStatRepository.findById(statId);
        assertThat(updated).isPresent();
        assertThat(updated.get().getValue()).isEqualTo(5);
    }

    @Test
    void deleteById_ShouldRemoveStat() {
        UUID statId = UUID.randomUUID();
        statRepositoryImpl.save(createStatDomain(statId, "CAR", 4));

        statRepositoryImpl.deleteById(statId);

        assertThat(jpaStatRepository.findById(statId)).isEmpty();
    }

    private Stat createStatDomain(UUID id, String code, int value) {
        return new Stat(id, savedPlayerId, code, value);
    }
}