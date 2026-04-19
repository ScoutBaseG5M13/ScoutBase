package es.dimecresalessis.scoutbase.infrastructure.user.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.user.persistence.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = UserEntityMapperImpl.class)
class UserEntityMapperTest {

    @Autowired
    private UserEntityMapper userEntityMapper;

    @Test
    void shouldMapToEntity() {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .username("scout_master")
                .password("encoded_password")
                .name("Alex")
                .surname("Scout")
                .email("alex@scoutbase.com")
                .build();

        UserEntity result = userEntityMapper.toEntity(user);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getUsername()).isEqualTo(user.getUsername());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
        assertThat(result.getName()).isEqualTo(user.getName());
        assertThat(result.getSurname()).isEqualTo(user.getSurname());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void shouldMapToDomain() {
        UUID userId = UUID.randomUUID();
        UserEntity userEntity = UserEntity.builder()
                .id(userId)
                .username("scout_entity")
                .password("encoded_password_entity")
                .name("Alexine")
                .surname("Manager")
                .email("alexine@scoutbase.com")
                .build();

        User result = userEntityMapper.toDomain(userEntity);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userEntity.getId());
        assertThat(result.getUsername()).isEqualTo(userEntity.getUsername());
        assertThat(result.getPassword()).isEqualTo(userEntity.getPassword());
        assertThat(result.getName()).isEqualTo(userEntity.getName());
        assertThat(result.getSurname()).isEqualTo(userEntity.getSurname());
        assertThat(result.getEmail()).isEqualTo(userEntity.getEmail());
    }

    @Test
    void shouldUpdateEntityFromDomain() {
        UserEntity entity = UserEntity.builder()
                .id(UUID.randomUUID())
                .username("old_user")
                .name("Old Name")
                .build();

        User domain = User.builder()
                .username("new_user")
                .name("New Name")
                .email("new@test.com")
                .build();

        userEntityMapper.updateEntityFromDomain(domain, entity);

        assertThat(entity.getUsername()).isEqualTo("new_user");
        assertThat(entity.getName()).isEqualTo("New Name");
        assertThat(entity.getEmail()).isEqualTo("new@test.com");
    }

    @Test
    void shouldReturnNullWhenInputsAreNull() {
        assertThat(userEntityMapper.toEntity(null)).isNull();
        assertThat(userEntityMapper.toDomain(null)).isNull();
    }
}