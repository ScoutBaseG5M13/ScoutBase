package es.dimecresalessis.scoutbase.infrastructure.userclub.persistence;

import es.dimecresalessis.scoutbase.domain.userclub.model.UserClub;
import es.dimecresalessis.scoutbase.infrastructure.userclub.persistence.mapper.UserClubEntityMapperImpl;
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
@Import({UserClubRepositoryImpl.class, UserClubEntityMapperImpl.class})
class UserUserClubRepositoryImplIT {

    @Autowired
    private UserClubRepositoryImpl clubRepository;

    @Autowired
    private JpaUserClubRepository jpaUserClubRepository;

    private UUID clubId;
    private UserClub userClub;

    @BeforeEach
    void setUp() {
        jpaUserClubRepository.deleteAll();
        clubId = UUID.randomUUID();
        userClub = UserClub.builder()
                .id(clubId)
                .name("Scout Base Club")
                .adminUserIds(List.of(UUID.randomUUID()))
                .build();
    }

    @Test
    void save_ShouldPersistClub() {
        UserClub savedUserClub = clubRepository.save(userClub);

        assertNotNull(savedUserClub);
        assertTrue(jpaUserClubRepository.findById(clubId).isPresent());
        assertEquals("Scout Base Club", jpaUserClubRepository.findById(clubId).get().getName());
    }

    @Test
    void findUserClubById_ShouldReturnClub_WhenExists() {
        clubRepository.save(userClub);

        Optional<UserClub> foundClub = clubRepository.findUserClubById(clubId);

        assertTrue(foundClub.isPresent());
        assertEquals(clubId, foundClub.get().getId());
    }

    @Test
    void findAll_ShouldReturnAllClubs() {
        clubRepository.save(userClub);

        List<UserClub> userClubs = clubRepository.findAll();

        assertFalse(userClubs.isEmpty());
        assertEquals(1, userClubs.size());
    }

    @Test
    void findAllByUserId_ShouldReturnFilteredClubs() {
        UUID adminId = UUID.randomUUID();
        UserClub userClubWithAdmin = UserClub.builder()
                .id(UUID.randomUUID())
                .name("Admin Club")
                .adminUserIds(List.of(adminId))
                .build();

        clubRepository.save(userClubWithAdmin);
        clubRepository.save(userClub); // Club sin ese admin

        List<UserClub> result = clubRepository.findAllByUserId(adminId);

        assertEquals(1, result.size());
        assertEquals("Admin Club", result.get(0).getName());
    }

    @Test
    void deleteById_ShouldRemoveClub() {
        clubRepository.save(userClub);
        assertTrue(jpaUserClubRepository.existsById(clubId));

        clubRepository.deleteById(clubId);

        assertFalse(jpaUserClubRepository.existsById(clubId));
    }
}