package es.dimecresalessis.scoutbase.application.user.create;

import es.dimecresalessis.scoutbase.application.user.GetEncodedUserPasswordUseCase;
import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.user.exception.UserException;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

/**
 * Use case for creating {@link User}.
 */
@Service
@AllArgsConstructor
public class CreateUserUseCase {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CreateUserUseCase.class);

    private final SaveUserUseCase saveUserUseCase;
    private final GetEncodedUserPasswordUseCase getEncodedUserPasswordUseCase;
    private final UserRepository userRepository;

    /**
     * Creates a new {@link User} in the DB.
     *
     * @param user The {@link User} object containing the user's details.
     * @return The persisted {@link User} object after creating.
     * @throws UserException If the provided user object is {@code null}.
     */
    public User execute(User user) {
        validate(user);

        user = getEncodedUserPasswordUseCase.execute(user);
        User savedUser = saveUserUseCase.execute(user);

        logger.info("[CREATE] Created User with id '{}'", savedUser.getId());
        return savedUser;
    }

    private void validate(User user) {
        if (user == null) {
            throw new UserException(ErrorEnum.USER_IS_NULL);
        }

        if (user.getId() != null) {
            userRepository.findById(user.getId())
                    .ifPresent(userDb -> {
                        throw new UserException(ErrorEnum.USER_ID_ALREADY_EXISTS, userDb.getId().toString());
                    });
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserException(ErrorEnum.USERNAME_ALREADY_EXISTS, user.getUsername());
        }
    }
}
