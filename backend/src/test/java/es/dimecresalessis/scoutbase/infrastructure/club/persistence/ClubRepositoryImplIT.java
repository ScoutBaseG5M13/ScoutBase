package es.dimecresalessis.scoutbase.infrastructure.club.persistence;

import es.dimecresalessis.scoutbase.domain.club.model.Club;
import es.dimecresalessis.scoutbase.infrastructure.club.persistence.mapper.ClubEntityMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("Ignoring until H2 uuid[] array type compatibility is resolved")
@DataJpaTest
@ActiveProfiles("test")
@Import({ClubRepositoryImpl.class, ClubEntityMapperImpl.class})
class ClubRepositoryImplIT {

    @Autowired
    private ClubRepositoryImpl clubRepository;

    @Autowired
    private JpaClubRepository jpaClubRepository;

    private UUID clubId;
    private Club club;

    @BeforeEach
    void setUp() {
        jpaClubRepository.deleteAll();
        clubId = UUID.randomUUID();
        club = Club.builder()
                .id(clubId)
                .name("Scout Base Club")
                .adminUserIds(List.of(UUID.randomUUID()))
                .build();
    }

    @Test
    void save_ShouldPersistClub() {
        Club savedClub = clubRepository.save(club);

        assertNotNull(savedClub);
        assertTrue(jpaClubRepository.findById(clubId).isPresent());
        assertEquals("Scout Base Club", jpaClubRepository.findById(clubId).get().getName());
    }

    @Test
    void findById_ShouldReturnClub_WhenExists() {
        clubRepository.save(club);

        Optional<Club> foundClub = clubRepository.findById(clubId);

        assertTrue(foundClub.isPresent());
        assertEquals(clubId, foundClub.get().getId());
    }

    @Test
    void findAll_ShouldReturnAllClubs() {
        clubRepository.save(club);

        List<Club> clubs = clubRepository.findAll();

        assertFalse(clubs.isEmpty());
        assertEquals(1, clubs.size());
    }

    @Test
    void findAllByUserId_ShouldReturnFilteredClubs() {
        UUID adminId = UUID.randomUUID();
        Club clubWithAdmin = Club.builder()
                .id(UUID.randomUUID())
                .name("Admin Club")
                .adminUserIds(List.of(adminId))
                .build();

        clubRepository.save(clubWithAdmin);
        clubRepository.save(club); // Club sin ese admin

        List<Club> result = clubRepository.findAllByUserId(adminId);

        assertEquals(1, result.size());
        assertEquals("Admin Club", result.get(0).getName());
    }

    @Test
    void deleteById_ShouldRemoveClub() {
        clubRepository.save(club);
        assertTrue(jpaClubRepository.existsById(clubId));

        clubRepository.deleteById(clubId);

        assertFalse(jpaClubRepository.existsById(clubId));
    }
}