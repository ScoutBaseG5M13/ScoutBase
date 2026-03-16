package es.dimecresalessis.scoutbase.domain.user.repository;

import es.dimecresalessis.scoutbase.domain.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    List<User> findAll();
    Optional<User> findById(UUID id);
    Optional<User> findByUsername(String username);
    User save(User player);
    void deleteById(UUID id);
}
