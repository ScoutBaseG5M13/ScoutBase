package es.dimecresalessis.scoutbase.application.user;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Use case for updating an existing {@link User} entity in the system.
 */
@Service
@AllArgsConstructor
public class UpdateUserUseCase {

    private static final Logger logger = LoggerFactory.getLogger(UpdateUserUseCase.class);
    private final UserRepository userRepository;

    /**
     * Updates the details of a {@link User} identified by their unique ID.
     *
     * @param user The updated {@link User} object with the new details.
     * @param id The ID of the user to be updated.
     * @return The updated {@link User} object after being persisted.
     */
    public User execute(User user, UUID id) {
        User idUser = userRepository.findById(id).orElseThrow();
        User bodyUser = userRepository.findById(user.getId()).orElseThrow();
        if (!idUser.getId().toString().equals(bodyUser.getId().toString())) {
            throw new IllegalArgumentException("User id does not match");
        }
        userRepository.save(user);
        logger.info("[UPDATE] Updated User with id '{}'", user.getId());
        return user;
    }
}
