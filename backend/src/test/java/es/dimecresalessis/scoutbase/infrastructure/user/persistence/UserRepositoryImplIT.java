package es.dimecresalessis.scoutbase.infrastructure.user.persistence;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.persistence.mapper.UserEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({UserRepositoryImpl.class, UserEntityMapper.class})
class UserRepositoryImplIT {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder()
                .id(UUID.randomUUID())
                .username("scout_master")
                .password("encoded_password")
                .role("ADMIN")
                .name("Alex")
                .surname("Scout")
                .email("alex@scoutbase.com")
                .build();
    }

    @Test
    @DisplayName("save - Should persist a new user and retrieve it by ID")
    void saveAndFindById_ShouldWork() {
        userRepository.save(user);

        Optional<User> found = userRepository.findById(userId);

        assertTrue(found.isPresent());
        assertEquals("admin_test", found.get().getUsername());
        assertEquals("ROLE_ADMIN", found.get().getRole());
    }

    @Test
    @DisplayName("findByUsername - Should return user when username exists")
    void findByUsername_ShouldReturnUser() {
        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("admin_test");

        assertTrue(found.isPresent());
        assertEquals(userId, found.get().getId());
    }

    @Test
    @DisplayName("save - Should update existing user instead of creating a duplicate")
    void save_ShouldUpdateExistingUser() {
        userRepository.save(user);

        User updatedUser = User.builder()
                .id(userId)
                .username("scout_master")
                .password("encoded_password")
                .role("ADMIN")
                .name("Alex")
                .surname("Scout")
                .email("alex@scoutbase.com")
                .build();
        userRepository.save(updatedUser);

        assertEquals(1, jpaUserRepository.count());
        Optional<UserEntity> entityInDb = jpaUserRepository.findById(userId);
        assertTrue(entityInDb.isPresent());
        assertEquals("admin_updated", entityInDb.get().getUsername());
        assertEquals("ROLE_USER", entityInDb.get().getRole());
    }

    @Test
    @DisplayName("deleteById - Should remove user from database")
    void deleteById_ShouldRemoveUser() {
        userRepository.save(user);
        assertTrue(jpaUserRepository.existsById(userId));

        userRepository.deleteById(userId);

        assertFalse(jpaUserRepository.existsById(userId));
    }

    @Test
    @DisplayName("findFirstByUsername - Should return first user matching username")
    void findFirstByUsername_ShouldReturnUser() {
        userRepository.save(user);

        Optional<User> found = userRepository.findFirstByUsername("admin_test");

        assertTrue(found.isPresent());
        assertEquals("admin_test", found.get().getUsername());
    }
}