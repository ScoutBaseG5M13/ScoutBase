package es.dimecresalessis.scoutbase.infrastructure.userteam.persistence;

import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.SubcategoryEnum;
import es.dimecresalessis.scoutbase.domain.userteam.model.UserTeam;
import es.dimecresalessis.scoutbase.infrastructure.userclub.persistence.JpaUserClubRepository;
import es.dimecresalessis.scoutbase.infrastructure.userclub.persistence.UserClubEntity;
import es.dimecresalessis.scoutbase.infrastructure.userteam.persistence.mapper.UserTeamEntityMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({UserTeamRepositoryImpl.class, UserTeamEntityMapperImpl.class})
class UserTeamRepositoryImplIT {

    @Autowired
    private UserTeamRepositoryImpl userTeamRepositoryImpl;

    @Autowired
    private JpaUserTeamRepository jpaUserTeamRepository;

    @Autowired
    private JpaUserClubRepository jpaUserClubRepository;

    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        jpaUserTeamRepository.deleteAll();
        jpaUserClubRepository.deleteAll();
    }

    @Test
    void findAllByUserId_ShouldReturnTeamsWhereUserIsTrainer() {
        UserTeam team = createTeamDomain(null, "Team Trainer");
        team.setTrainer(userId);
        userTeamRepositoryImpl.save(team);

        List<UserTeam> result = userTeamRepositoryImpl.findAllByUserId(userId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Team Trainer");
    }

    @Test
    void findAllByUserId_ShouldReturnTeamsWhereUserIsScouter() {
        UserTeam team = createTeamDomain(null, "Team Scouter");
        team.setScouters(new ArrayList<>(List.of(userId)));
        userTeamRepositoryImpl.save(team);

        List<UserTeam> result = userTeamRepositoryImpl.findAllByUserId(userId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getScouters()).contains(userId);
    }

    @Test
    void findAllByUserId_ShouldReturnAllClubTeams_WhenUserIsClubAdmin() {
        UUID teamId1 = UUID.randomUUID();
        UUID teamId2 = UUID.randomUUID();

        userTeamRepositoryImpl.save(createTeamDomain(teamId1, "Club Team 1"));
        userTeamRepositoryImpl.save(createTeamDomain(teamId2, "Club Team 2"));

        UserClubEntity club = UserClubEntity.builder()
                .id(UUID.randomUUID())
                .name("Admin Club")
                .adminUserIds(List.of(userId))
                .userTeams(List.of(teamId1, teamId2))
                .build();
        jpaUserClubRepository.saveAndFlush(club);

        List<UserTeam> result = userTeamRepositoryImpl.findAllByUserId(userId);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(UserTeam::getName)
                .containsExactlyInAnyOrder("Club Team 1", "Club Team 2");
    }

    @Test
    void save_ShouldCreateNewTeam() {
        UUID newTeamId = UUID.randomUUID();
        UserTeam team = createTeamDomain(newTeamId, "New Team");

        userTeamRepositoryImpl.save(team);

        Optional<UserTeamEntity> saved = jpaUserTeamRepository.findById(newTeamId);
        assertThat(saved).isPresent();
        assertThat(saved.get().getName()).isEqualTo("New Team");
        assertThat(saved.get().getCategory()).isEqualTo(CategoryEnum.JUVENIL.name());
    }

    /**
     * Helper method corregido para usar los Enums de dominio.
     */
    private UserTeam createTeamDomain(UUID id, String name) {
        return UserTeam.builder()
                .id(id)
                .name(name)
                .category(CategoryEnum.JUVENIL)
                .subcategory(SubcategoryEnum.SUB_SUPERIOR)
                .userClub(UUID.randomUUID())
                .scouters(new ArrayList<>())
                .build();
    }
}