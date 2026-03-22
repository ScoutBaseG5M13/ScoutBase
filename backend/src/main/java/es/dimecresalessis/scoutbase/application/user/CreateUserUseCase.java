package es.dimecresalessis.scoutbase.application.user;

import es.dimecresalessis.scoutbase.application.security.RegistrationService;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Use case for creating {@link User} entities in the system.
 */
@Service
@AllArgsConstructor
public class CreateUserUseCase {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CreateUserUseCase.class);
    private final RegistrationService registrationService;

    /**
     * Creates a new {@link User} in the DB.
     *
     * @param user The {@link User} object containing the user's details.
     * @return The persisted {@link User} object after creating.
     * @throws UserException If the provided user object is {@code null}.
     */
    public User execute(User user) {
        if (user == null) {
            throw new UserException(ErrorEnum.USER_IS_NULL);
        }
        User savedUser = registrationService.createUser(user);
        logger.info("[CREATE] Created User with id '{}'", savedUser.getId());
        return savedUser;
    }
}
