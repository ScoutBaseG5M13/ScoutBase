package es.dimecresalessis.scoutbase.application.security;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public RegistrationService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        User encodedUser = User.getNewInstance(user.getUsername(), passwordEncoder.encode(user.getPassword()), user.getRole());
        userRepository.save(new User(encodedUser.getId(), encodedUser.getUsername(), encodedUser.getPassword(), encodedUser.getRole()));
        return new User(encodedUser.getId(), encodedUser.getUsername(), user.getPassword(), encodedUser.getRole());
    }
}