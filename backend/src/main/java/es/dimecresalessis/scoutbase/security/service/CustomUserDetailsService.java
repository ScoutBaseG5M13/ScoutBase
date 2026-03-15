package es.dimecresalessis.scoutbase.security.service;

import es.dimecresalessis.scoutbase.security.model.UserPrincipal;
import es.dimecresalessis.scoutbase.user.domain.model.User;
import es.dimecresalessis.scoutbase.user.domain.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User entity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!!"));

        return new UserPrincipal(entity);
    }
}