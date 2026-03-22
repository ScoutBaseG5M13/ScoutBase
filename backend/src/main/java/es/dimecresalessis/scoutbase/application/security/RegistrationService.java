package es.dimecresalessis.scoutbase.application.security;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for handling user registration tasks
 * <p>
 * This includes validating and encoding user credentials.
 * </p>
 */
@Service
public class RegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /**
     * Constructor of the service with required dependencies.
     *
     * @param passwordEncoder The password encoder implementation to secure passwords.
     * @param userRepository  The user repository for persistence.
     */
    public RegistrationService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new {@link User} by encoding its password and saving it in the repository.
     *
     * @param user The {@link User} object to be persisted.
     * @return A saved {@link User} object with its decoded password.
     */
    public User createUser(User user) {
        User encodedUser = User.getNewInstance(user.getUsername(), passwordEncoder.encode(user.getPassword()), user.getRole());
        userRepository.save(new User(encodedUser.getId(), encodedUser.getUsername(), encodedUser.getPassword(), encodedUser.getRole()));
        return new User(encodedUser.getId(), encodedUser.getUsername(), user.getPassword(), encodedUser.getRole());
    }
}