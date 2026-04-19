package es.dimecresalessis.scoutbase.infrastructure.user.persistence;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.persistence.mapper.UserEntityMapperImpl;
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
@Import({UserRepositoryImpl.class, UserEntityMapperImpl.class})
@ActiveProfiles("test")
class UserRepositoryImplIT {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveAndFindUserById() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .username("test_user")
                .password("pass123")
                .role("USER")
                .name("Test")
                .email("test@scoutbase.com")
                .build();

        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        Optional<User> found = userRepository.findById(userId);

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("test_user");
        assertThat(found.get().getEmail()).isEqualTo("test@scoutbase.com");
    }

    @Test
    void shouldFindByUsername() {
        String username = "unique_scout";
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .password("secure")
                .role("ADMIN")
                .name("Admin")
                .email("admin@scoutbase.com")
                .build();

        userRepository.save(user);
        entityManager.flush();
        entityManager.clear();

        Optional<User> found = userRepository.findByUsername(username);

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo(username);
    }

    @Test
    void shouldUpdateExistingUser() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .username("original_user")
                .password("pass")
                .role("USER")
                .name("Original")
                .email("original@test.com")
                .build();
        userRepository.save(user);
        entityManager.flush();

        User updatedUser = user.toBuilder()
                .name("Updated Name")
                .email("updated@test.com")
                .build();

        userRepository.save(updatedUser);
        entityManager.flush();
        entityManager.clear();

        Optional<User> found = userRepository.findById(userId);
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Updated Name");
        assertThat(found.get().getEmail()).isEqualTo("updated@test.com");
    }

    @Test
    void shouldDeleteUser() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .username("delete_me")
                .password("pass")
                .role("USER")
                .name("Delete")
                .email("delete@test.com")
                .build();
        userRepository.save(user);
        entityManager.flush();

        userRepository.deleteById(userId);
        entityManager.flush();
        entityManager.clear();

        Optional<User> found = userRepository.findById(userId);
        assertThat(found).isEmpty();
    }
}