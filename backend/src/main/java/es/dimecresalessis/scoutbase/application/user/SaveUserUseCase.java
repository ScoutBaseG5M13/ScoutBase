package es.dimecresalessis.scoutbase.application.user;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service responsible for handling user registration tasks
 * <p>
 * This includes validating and encoding user credentials.
 * </p>
 */
@Service
@AllArgsConstructor
public class SaveUserUseCase {

    private final UserRepository userRepository;

    /**
     * Creates a new {@link User} by encoding its password and saving it in the repository.
     *
     * @param user The {@link User} object to be persisted.
     * @return A saved {@link User} object with its decoded password.
     */
    public User execute(User user) {
        userRepository.save(user);
        return user;
    }
}