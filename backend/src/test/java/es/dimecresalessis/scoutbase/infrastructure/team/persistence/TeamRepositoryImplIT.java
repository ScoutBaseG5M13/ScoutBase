package es.dimecresalessis.scoutbase.infrastructure.team.persistence;

import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.SubcategoryEnum;
import es.dimecresalessis.scoutbase.domain.team.model.Team;
import es.dimecresalessis.scoutbase.infrastructure.team.persistence.mapper.TeamEntityMapperImpl;
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
@Import({TeamRepositoryImpl.class, TeamEntityMapperImpl.class})
class TeamRepositoryImplIT {

    @Autowired
    private TeamRepositoryImpl teamRepositoryImpl;

    @Autowired
    private JpaTeamRepository jpaTeamRepository;

    private UUID clubId;

    @BeforeEach
    void setUp() {
        jpaTeamRepository.deleteAll();
        clubId = UUID.randomUUID();
    }

    @Test
    void findAll_ShouldReturnAllTeams() {
        teamRepositoryImpl.save(createTeamDomain(UUID.randomUUID(), "Team A"));
        teamRepositoryImpl.save(createTeamDomain(UUID.randomUUID(), "Team B"));

        List<Team> result = teamRepositoryImpl.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void findById_ShouldReturnTeam_WhenExists() {
        UUID teamId = UUID.randomUUID();
        teamRepositoryImpl.save(createTeamDomain(teamId, "Unique Team"));

        Optional<Team> result = teamRepositoryImpl.findById(teamId);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Unique Team");
    }

    @Test
    void findByPlayerId_ShouldReturnTeam_WhenPlayerIsMember() {
        UUID playerId = UUID.randomUUID();

        Team team = Team.builder()
                .id(UUID.randomUUID())
                .clubId(clubId)
                .name("Scout Team")
                .players(List.of(playerId))
                .category(CategoryEnum.JUVENIL)
                .subcategory(SubcategoryEnum.SUB_SUPERIOR)
                .build();

        teamRepositoryImpl.save(team);

        Optional<Team> result = teamRepositoryImpl.findByPlayerId(playerId);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Scout Team");
        assertThat(result.get().getPlayers()).contains(playerId);
    }

    @Test
    void save_ShouldPersistNewTeam() {
        UUID teamId = UUID.randomUUID();
        Team team = createTeamDomain(teamId, "Persisted Team");

        teamRepositoryImpl.save(team);

        Optional<TeamEntity> saved = jpaTeamRepository.findById(teamId);
        assertThat(saved).isPresent();
        assertThat(saved.get().getName()).isEqualTo("Persisted Team");
        assertThat(saved.get().getCategory()).isEqualTo(CategoryEnum.INFANTIL.name());
    }

    @Test
    void save_ShouldUpdateExistingTeam() {
        UUID teamId = UUID.randomUUID();
        teamRepositoryImpl.save(createTeamDomain(teamId, "Old Name"));

        Team updatedTeam = createTeamDomain(teamId, "New Name");
        teamRepositoryImpl.save(updatedTeam);

        Optional<TeamEntity> saved = jpaTeamRepository.findById(teamId);
        assertThat(saved).isPresent();
        assertThat(saved.get().getName()).isEqualTo("New Name");
    }

    @Test
    void deleteById_ShouldRemoveTeam() {
        UUID teamId = UUID.randomUUID();
        teamRepositoryImpl.save(createTeamDomain(teamId, "To Delete"));

        teamRepositoryImpl.deleteById(teamId);

        assertThat(jpaTeamRepository.findById(teamId)).isEmpty();
    }

    private Team createTeamDomain(UUID id, String name) {
        return Team.builder()
                .id(id)
                .clubId(clubId)
                .name(name)
                .category(CategoryEnum.INFANTIL)
                .subcategory(SubcategoryEnum.SUB13)
                .players(List.of())
                .build();
    }
}