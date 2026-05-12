package es.dimecresalessis.scoutbase.infrastructure.club.persistence;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.domain.team.repository.TeamRepository;
import es.dimecresalessis.scoutbase.infrastructure.club.persistence.mapper.ClubEntityMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import({
        ClubRepositoryImpl.class,
        ClubEntityMapperImpl.class
})
class ClubRepositoryImplIT {

    @Autowired
    private ClubRepositoryImpl clubRepository;

    @Autowired
    private JpaClubRepository jpaClubRepository;

    @MockitoBean
    private TeamRepository teamRepository;

    private UUID clubId;
    private UUID userId;
    private Club club;

    @BeforeEach
    void setUp() {
        jpaClubRepository.deleteAll();
        clubId = UUID.randomUUID();
        userId = UUID.randomUUID();

        // Usamos el builder corregido según tu clase de dominio
        club = Club.builder()
                .id(clubId)
                .name("Scout Base Club")
                .userClub(userId)
                .teams(new ArrayList<>(List.of(UUID.randomUUID())))
                .build();
    }

    @Test
    void save_ShouldPersistClub() {
        Club savedClub = clubRepository.save(club);

        assertNotNull(savedClub);
        Optional<ClubEntity> entity = jpaClubRepository.findById(clubId);
        assertTrue(entity.isPresent());
        assertEquals("Scout Base Club", entity.get().getName());
        assertEquals(userId, entity.get().getUserClub());
    }

    @Test
    void findById_ShouldReturnClub_WhenExists() {
        clubRepository.save(club);

        Optional<Club> foundClub = clubRepository.findById(clubId);

        assertTrue(foundClub.isPresent());
        assertEquals(clubId, foundClub.get().getId());
        assertEquals(userId, foundClub.get().getUserClub());
    }

    @Test
    void findClubByTeam_ShouldReturnClub_WhenTeamMatches() {
        UUID teamId = UUID.randomUUID();
        club.setTeams(new ArrayList<>(List.of(teamId)));
        clubRepository.save(club);

        Optional<Club> result = clubRepository.findClubByTeam(teamId);

        assertTrue(result.isPresent());
        assertEquals(clubId, result.get().getId());
    }

    @Test
    void deleteById_ShouldRemoveClub() {
        clubRepository.save(club);
        assertTrue(jpaClubRepository.existsById(clubId));

        clubRepository.deleteById(clubId);

        assertFalse(jpaClubRepository.existsById(clubId));
    }
}