package es.dimecresalessis.scoutbase.user.domain.repository;

import es.dimecresalessis.scoutbase.user.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    List<User> findAll();
    Optional<User> findById(UUID id);
    User save(User player);
    void deleteById(UUID id);
}
