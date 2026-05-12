package es.dimecresalessis.scoutbase.infrastructure.user.persistence;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.persistence.mapper.UserEntityMapperImpl;
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
@Import({UserRepositoryImpl.class, UserEntityMapperImpl.class})
class UserRepositoryImplIT {

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @BeforeEach
    void setUp() {
        jpaUserRepository.deleteAll();
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        userRepositoryImpl.save(createUserDomain(null, "user1", "user1@test.com"));
        userRepositoryImpl.save(createUserDomain(null, "user2", "user2@test.com"));

        List<User> result = userRepositoryImpl.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void findById_ShouldReturnUser_WhenExists() {
        UUID userId = UUID.randomUUID();
        userRepositoryImpl.save(createUserDomain(userId, "findme", "find@test.com"));

        Optional<User> result = userRepositoryImpl.findById(userId);

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("findme");
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenExists() {
        userRepositoryImpl.save(createUserDomain(null, "unique_user", "unique@test.com"));

        Optional<User> result = userRepositoryImpl.findByUsername("unique_user");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("unique_user");
    }

    @Test
    void findFirstByUsername_ShouldReturnUser_WhenExists() {
        userRepositoryImpl.save(createUserDomain(null, "first_user", "first@test.com"));

        Optional<User> result = userRepositoryImpl.findFirstByUsername("first_user");

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("first@test.com");
    }

    @Test
    void save_ShouldPersistNewUser() {
        UUID userId = UUID.randomUUID();
        User user = createUserDomain(userId, "newuser", "new@test.com");

        userRepositoryImpl.save(user);

        Optional<UserEntity> saved = jpaUserRepository.findById(userId);
        assertThat(saved).isPresent();
        assertThat(saved.get().getUsername()).isEqualTo("newuser");
        assertThat(saved.get().isSuperAdmin()).isFalse();
    }

    @Test
    void save_ShouldUpdateExistingUser() {
        UUID userId = UUID.randomUUID();
        userRepositoryImpl.save(createUserDomain(userId, "oldname", "old@test.com"));

        User updatedUser = User.builder()
                .id(userId)
                .username("newname")
                .name("New")
                .surname("Name")
                .email("new@test.com")
                .password("newpass")
                .superAdmin(true)
                .build();

        userRepositoryImpl.save(updatedUser);

        Optional<UserEntity> saved = jpaUserRepository.findById(userId);
        assertThat(saved).isPresent();
        assertThat(saved.get().getUsername()).isEqualTo("newname");
        assertThat(saved.get().isSuperAdmin()).isTrue();
    }

    @Test
    void deleteById_ShouldRemoveUser() {
        UUID userId = UUID.randomUUID();
        userRepositoryImpl.save(createUserDomain(userId, "delete_me", "del@test.com"));

        userRepositoryImpl.deleteById(userId);

        assertThat(jpaUserRepository.findById(userId)).isEmpty();
    }

    private User createUserDomain(UUID id, String username, String email) {
        return User.builder()
                .id(id)
                .username(username)
                .password("encoded_password")
                .name("TestName")
                .surname("TestSurname")
                .email(email)
                .superAdmin(false)
                .build();
    }
}