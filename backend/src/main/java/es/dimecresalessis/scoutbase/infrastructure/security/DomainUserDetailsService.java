package es.dimecresalessis.scoutbase.infrastructure.security;

import es.dimecresalessis.scoutbase.application.user.find.FindUserByUsernameUseCase;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Infrastructure implementation of Spring Security's {@link UserDetailsService}.
 */
@Service
@AllArgsConstructor
public class DomainUserDetailsService implements UserDetailsService {

    private final FindUserByUsernameUseCase findUserByUsernameUseCase;

    /**
     * Locates the user based on the username and converts the domain {@link User}
     * into a Spring Security {@link UserDetails} object.
     *
     * @param username The username identifying the user whose data is required.
     * @return A {@link UserDetails}.
     * @throws UsernameNotFoundException if the user could not be found or the
     * use case throws an exception.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = findUserByUsernameUseCase.execute(username);

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .build();
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}