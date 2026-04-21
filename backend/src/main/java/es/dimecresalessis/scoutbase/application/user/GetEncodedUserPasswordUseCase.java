package es.dimecresalessis.scoutbase.application.user;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetEncodedUserPasswordUseCase {
    private final PasswordEncoder passwordEncoder;

    public User execute(User user) {
        User encodedUser = user.toBuilder().build();
        encodedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return encodedUser;
    }
}
