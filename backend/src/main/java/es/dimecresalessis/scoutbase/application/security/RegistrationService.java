package es.dimecresalessis.scoutbase.application.security;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import es.dimecresalessis.scoutbase.infrastructure.user.persistence.UserRepositoryImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service responsible for handling user registration tasks
 * <p>
 * This includes validating and encoding user credentials.
 * </p>
 */
@Service
@AllArgsConstructor
public class RegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRepositoryImpl userRepositoryImpl;

//    /**
//     * Constructor of the service with required dependencies.
//     *
//     * @param passwordEncoder The password encoder implementation to secure passwords.
//     * @param userRepository The user repository for persistence.
//     */

    /**
     * Creates a new {@link User} by encoding its password and saving it in the repository.
     *
     * @param user The {@link User} object to be persisted.
     * @return A saved {@link User} object with its decoded password.
     */
    public User createUser(User user) {
        User encodedUser;
        if (user.getId() == null) {
            encodedUser = User.getNewInstance(user.getUsername(), passwordEncoder.encode(user.getPassword()), user.getRole());
        } else {
            encodedUser = new User(user.getId(), user.getUsername(), passwordEncoder.encode(user.getPassword()), user.getRole());
        }
        userRepository.save(userRepositoryImpl.save(encodedUser));
        return new User(encodedUser.getId(), encodedUser.getUsername(), user.getPassword(), encodedUser.getRole());
    }
}