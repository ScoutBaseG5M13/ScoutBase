package es.dimecresalessis.scoutbase.user.infrastructure.persistence;

import es.dimecresalessis.scoutbase.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<User> findByUsername(String username);
}
