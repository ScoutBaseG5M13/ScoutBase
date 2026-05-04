package es.dimecresalessis.scoutbase.infrastructure.userteam.persistence;

import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.SubcategoryEnum;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.infrastructure.userteam.persistence.mapper.UserTeamEntityMapperImpl;
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
@Import({UserTeamRepositoryImpl.class, UserTeamEntityMapperImpl.class})
@ActiveProfiles("test")
class UserUserTeamRepositoryImplIT {

    @Autowired
    private UserTeamRepositoryImpl teamRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveAndFindTeamById() {
        UUID teamId = UUID.randomUUID();
        UserTeam userTeam = UserTeam.builder()
                .id(teamId)
                .name("Infantil A")
                .category(CategoryEnum.INFANTIL)
                .subcategory(SubcategoryEnum.SUB14)
                .build();

        teamRepository.save(userTeam);
        entityManager.flush();
        entityManager.clear();

        Optional<UserTeam> found = teamRepository.findById(teamId);

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Infantil A");
        assertThat(found.get().getCategory()).isEqualTo(CategoryEnum.INFANTIL);
    }

    @Test
    void shouldFindAllTeamsByPlayerId() {
        UUID userId = UUID.randomUUID();

        UserTeam userTeamAsTrainer = UserTeam.builder()
                .id(UUID.randomUUID())
                .name("Team 1")
                .category(CategoryEnum.JUVENIL)
                .subcategory(SubcategoryEnum.SUB_SUPERIOR)
                .trainer(userId)
                .build();

        UserTeam userTeamAsScouter = UserTeam.builder()
                .id(UUID.randomUUID())
                .name("Team 2")
                .category(CategoryEnum.CADETE)
                .subcategory(SubcategoryEnum.SUB16)
                .scouters(List.of(userId))
                .build();

        teamRepository.save(userTeamAsTrainer);
        teamRepository.save(userTeamAsScouter);

        entityManager.flush();
        entityManager.clear();

        List<UserTeam> userTeams = teamRepository.findAllByUserId(userId);

        assertThat(userTeams).hasSize(2);
        assertThat(userTeams).extracting(UserTeam::getName).containsExactlyInAnyOrder("Team 1", "Team 2");
    }

    @Test
    void shouldDeleteTeam() {
        UUID teamId = UUID.randomUUID();
        UserTeam userTeam = UserTeam.builder()
                .id(teamId)
                .name("To Delete")
                .build();

        teamRepository.save(userTeam);
        entityManager.flush();

        teamRepository.deleteById(teamId);
        entityManager.flush();
        entityManager.clear();

        Optional<UserTeam> found = teamRepository.findById(teamId);
        assertThat(found).isEmpty();
    }
}