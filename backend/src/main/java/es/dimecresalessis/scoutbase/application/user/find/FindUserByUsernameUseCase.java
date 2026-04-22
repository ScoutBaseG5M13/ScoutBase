package es.dimecresalessis.scoutbase.application.user.find;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Use case for finding a {@link User} entity by their username.
 */
@Service
@AllArgsConstructor
public class FindUserByUsernameUseCase {

    private static final Logger logger = LoggerFactory.getLogger(FindUserByUsernameUseCase.class);
    private final UserRepository userRepository;

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user.
     * @return The {@link User} entity corresponding to the provided ID.
     * @throws IllegalArgumentException If the user cannot be found.
     */
    public User execute(String username) {
        User user = userRepository.findFirstByUsername(username).orElseThrow();
        logger.info("[FIND] Found User with username '{}'", username);
        return user;
    }
}
