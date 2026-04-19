package es.dimecresalessis.scoutbase.application.user.find;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for finding a user entity by their ID.
 */
@Service
@AllArgsConstructor
public class FindUserByIdUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindUserByIdUseCase.class);
    private final UserRepository userRepository;

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user.
     * @return The {@link User} entity corresponding to the provided ID.
     * @throws IllegalArgumentException If the user cannot be found.
     */
    public User execute(UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        logger.info("[FIND] Found User with id '{}'", id);
        return user;
    }
}
