package es.dimecresalessis.scoutbase.infrastructure.security.service;

import es.dimecresalessis.scoutbase.infrastructure.security.model.UserPrincipal;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of the {@link UserDetailsService} interface.
 * <p>
 * This service loads user-specific data from the {@link UserRepository}.
 * </p>
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs a new instance with a {@link UserRepository} dependency.
     *
     * @param userRepository Repository for managing user data.
     */
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Locates the user based on the provided username.
     *
     * @param username The username identifying the user whose data is required.
     * @return A fully populated {@link UserDetails} object.
     * @throws UsernameNotFoundException If no user is found with the given username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found!!"));

        return new UserPrincipal(user);
    }
}