package es.dimecresalessis.scoutbase.infrastructure.userclub.persistence;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.infrastructure.userclub.persistence.mapper.UserClubEntityMapperImpl;
import es.dimecresalessis.scoutbase.infrastructure.userteam.persistence.JpaUserTeamRepository;
import es.dimecresalessis.scoutbase.infrastructure.userteam.persistence.UserTeamEntity;
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
@Import({UserClubRepositoryImpl.class, UserClubEntityMapperImpl.class})
class UserClubRepositoryImplIT {

    @Autowired
    private UserClubRepositoryImpl userClubRepositoryImpl;

    @Autowired
    private JpaUserClubRepository jpaUserClubRepository;

    @Autowired
    private JpaUserTeamRepository jpaUserTeamRepository;

    @BeforeEach
    void setUp() {
        jpaUserTeamRepository.deleteAll();
        jpaUserClubRepository.deleteAll();
    }

    @Test
    void findUserClubByTeam_ShouldReturnClub_WhenTeamIsAssociated() {
        UUID teamId = UUID.randomUUID();
        UserClub club = UserClub.builder()
                .name("Club with Teams")
                .userTeams(List.of(teamId))
                .adminUserIds(List.of(UUID.randomUUID()))
                .build();
        userClubRepositoryImpl.save(club);

        Optional<UserClub> result = userClubRepositoryImpl.findUserClubByTeam(teamId);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Club with Teams");
    }

    @Test
    void findAllUserClubsByUserId_ShouldReturnClubsWhereUserIsAdmin() {
        UUID userId = UUID.randomUUID();
        UserClub adminClub = UserClub.builder()
                .name("Admin Club")
                .adminUserIds(List.of(userId))
                .userTeams(List.of())
                .build();
        userClubRepositoryImpl.save(adminClub);

        List<UserClub> result = userClubRepositoryImpl.findAllUserClubsByUserId(userId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Admin Club");
    }

    @Test
    void findAllUserClubsByUserId_ShouldReturnClubsWhereUserIsTrainer() {
        UUID userId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();

        // Equipo donde el usuario es entrenador
        UserTeamEntity team = UserTeamEntity.builder()
                .id(teamId)
                .name("Team A")
                .category("CAT")
                .subcategory("SUB")
                .userClub(UUID.randomUUID())
                .trainer(userId)
                .build();
        jpaUserTeamRepository.saveAndFlush(team);

        // Club que contiene ese equipo
        UserClub trainerClub = UserClub.builder()
                .name("Trainer Club")
                .adminUserIds(List.of(UUID.randomUUID()))
                .userTeams(List.of(teamId))
                .build();
        userClubRepositoryImpl.save(trainerClub);

        List<UserClub> result = userClubRepositoryImpl.findAllUserClubsByUserId(userId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Trainer Club");
    }

    @Test
    void findAllUserClubsByUserId_ShouldReturnClubsWhereUserIsScouter() {
        UUID userId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();

        UserTeamEntity team = UserTeamEntity.builder()
                .id(teamId)
                .name("Team B")
                .category("CAT")
                .subcategory("SUB")
                .userClub(UUID.randomUUID())
                .scouters(List.of(userId))
                .build();
        jpaUserTeamRepository.saveAndFlush(team);

        UserClub scoutClub = UserClub.builder()
                .name("Scouter Club")
                .adminUserIds(List.of(UUID.randomUUID()))
                .userTeams(List.of(teamId))
                .build();
        userClubRepositoryImpl.save(scoutClub);

        List<UserClub> result = userClubRepositoryImpl.findAllUserClubsByUserId(userId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Scouter Club");
    }

    @Test
    void save_ShouldPersistUserClubWithAllFields() {
        UUID clubId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();
        UUID teamId = UUID.randomUUID();

        UserClub club = UserClub.builder()
                .id(clubId)
                .name("Full Club")
                .adminUserIds(List.of(adminId))
                .userTeams(List.of(teamId))
                .managedClubs(List.of(UUID.randomUUID()))
                .build();

        userClubRepositoryImpl.save(club);

        Optional<UserClubEntity> saved = jpaUserClubRepository.findById(clubId);
        assertThat(saved).isPresent();
        assertThat(saved.get().getAdminUserIds()).containsExactly(adminId);
        assertThat(saved.get().getUserTeams()).containsExactly(teamId);
    }

    @Test
    void deleteById_ShouldRemoveClub() {
        UUID clubId = UUID.randomUUID();
        userClubRepositoryImpl.save(UserClub.builder().id(clubId).name("To Delete").adminUserIds(List.of()).build());

        userClubRepositoryImpl.deleteById(clubId);

        assertThat(jpaUserClubRepository.findById(clubId)).isEmpty();
    }
}