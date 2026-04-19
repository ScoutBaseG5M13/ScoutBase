package es.dimecresalessis.scoutbase.infrastructure.stat.persistence;

import es.dimecresalessis.scoutbase.domain.stat.model.Stat;
import es.dimecresalessis.scoutbase.domain.stat.model.StatEnum;
import es.dimecresalessis.scoutbase.infrastructure.stat.persistence.mapper.StatEntityMapperImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({StatRepositoryImpl.class, StatEntityMapperImpl.class})
@ActiveProfiles("test")
class StatRepositoryImplIT {

    @Autowired
    private StatRepositoryImpl statRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldUpdateExistingStat() {
        UUID statId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        Stat stat = new Stat(statId, playerId, StatEnum.PASE.statCode, 5);
        statRepository.save(stat);

        entityManager.flush();
        entityManager.clear();

        Stat updatedStat = new Stat(statId, playerId, StatEnum.PASE.statCode, 9);
        statRepository.update(updatedStat);

        entityManager.flush();
        entityManager.clear();

        Optional<Stat> found = statRepository.findById(statId);
        assertThat(found).isPresent();
        assertThat(found.get().getValue()).isEqualTo(9);
    }

    @Test
    void shouldDeleteStat() {
        UUID statId = UUID.randomUUID();
        Stat stat = new Stat(statId, UUID.randomUUID(), StatEnum.CONDUCCION.statCode, 6);
        statRepository.save(stat);

        entityManager.flush();
        entityManager.clear();

        statRepository.deleteById(statId);

        entityManager.flush();
        entityManager.clear();

        Optional<Stat> found = statRepository.findById(statId);
        assertThat(found).isEmpty();
    }
}