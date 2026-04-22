package es.dimecresalessis.scoutbase.infrastructure.team.persistence;

import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.SubcategoryEnum;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.infrastructure.team.persistence.mapper.TeamEntityMapperImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Ignoring until H2 uuid[] array type compatibility is resolved")
@DataJpaTest
@Import({TeamRepositoryImpl.class, TeamEntityMapperImpl.class})
@ActiveProfiles("test")
class TeamRepositoryImplIT {

    @Autowired
    private TeamRepositoryImpl teamRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveAndFindTeamById() {
        UUID teamId = UUID.randomUUID();
        Team team = Team.builder()
                .id(teamId)
                .name("Infantil A")
                .category(CategoryEnum.INFANTIL)
                .subcategory(SubcategoryEnum.SUB14)
                .build();

        teamRepository.save(team);
        entityManager.flush();
        entityManager.clear();

        Optional<Team> found = teamRepository.findById(teamId);

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Infantil A");
        assertThat(found.get().getCategory()).isEqualTo(CategoryEnum.INFANTIL);
    }

    @Test
    void shouldFindAllTeamsByPlayerId() {
        UUID userId = UUID.randomUUID();

        Team teamAsTrainer = Team.builder()
                .id(UUID.randomUUID())
                .name("Team 1")
                .category(CategoryEnum.JUVENIL)
                .subcategory(SubcategoryEnum.SUB_SUPERIOR)
                .trainer(userId)
                .build();

        Team teamAsScouter = Team.builder()
                .id(UUID.randomUUID())
                .name("Team 2")
                .category(CategoryEnum.CADETE)
                .subcategory(SubcategoryEnum.SUB16)
                .scouters(List.of(userId))
                .build();

        teamRepository.save(teamAsTrainer);
        teamRepository.save(teamAsScouter);

        entityManager.flush();
        entityManager.clear();

        List<Team> teams = teamRepository.findAllByUserId(userId);

        assertThat(teams).hasSize(2);
        assertThat(teams).extracting(Team::getName).containsExactlyInAnyOrder("Team 1", "Team 2");
    }

    @Test
    void shouldDeleteTeam() {
        UUID teamId = UUID.randomUUID();
        Team team = Team.builder()
                .id(teamId)
                .name("To Delete")
                .build();

        teamRepository.save(team);
        entityManager.flush();

        teamRepository.deleteById(teamId);
        entityManager.flush();
        entityManager.clear();

        Optional<Team> found = teamRepository.findById(teamId);
        assertThat(found).isEmpty();
    }
}