package es.dimecresalessis.scoutbase.application.user;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Use case responsible for encoding a user's password.
 */
@Service
@AllArgsConstructor
public class GetEncodedUserPasswordUseCase {
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a copy of the user with an encoded version of their current password.
     *
     * @param user The {@link User} domain entity containing the plain-text password.
     * @return A new {@link User} instance with the password encoded.
     */
    public User execute(User user) {
        User encodedUser = user.toBuilder().build();
        encodedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return encodedUser;
    }
}
