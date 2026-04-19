package es.dimecresalessis.scoutbase.application.user.update;

import es.dimecresalessis.scoutbase.application.user.GetEncodedUserPasswordUseCase;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
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
    private final GetEncodedUserPasswordUseCase getEncodedUserPasswordUseCase;

    /**
     * Updates the details of a {@link User} identified by their unique ID.
     *
     * @param user The updated {@link User} object with the new details.
     * @param id The ID of the user to be updated.
     * @return The updated {@link User} object after being persisted.
     */
    public User execute(User user, UUID id) {
        validate(user, id);
        user = getEncodedUserPasswordUseCase.execute(user);

        userRepository.save(user);
        logger.info("[UPDATE] Updated User with id '{}'", user.getId());
        return user;
    }

    private void validate (User user, UUID id) {
        if (user == null) {
            throw new UserException(ErrorEnum.USER_IS_NULL);
        }

        if (user.getId() == null) {
            throw new UserException(ErrorEnum.USER_ID_IS_NULL, user.getUsername());
        }

        if (!user.getId().toString().equals(id.toString())) {
            throw new UserException(ErrorEnum.USER_ID_DOES_NOT_MATCH, user.getUsername(), id.toString());
        }

        if (userRepository.findById(user.getId()).isEmpty()) {
            throw new UserException(ErrorEnum.USER_NOT_FOUND, user.getUsername());
        }
    }
}
