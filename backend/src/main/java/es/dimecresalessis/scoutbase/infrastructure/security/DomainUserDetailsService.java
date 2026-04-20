package es.dimecresalessis.scoutbase.infrastructure.security;

import es.dimecresalessis.scoutbase.application.user.find.FindUserByUsernameUseCase;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DomainUserDetailsService implements UserDetailsService {

    private final FindUserByUsernameUseCase findUserByUsernameUseCase;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // El caso de uso devuelve tu objeto de DOMINIO 'User'
            User user = findUserByUsernameUseCase.execute(username);

            // Debes convertir tu User de dominio a un UserDetails de Spring
            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getUsername())
                    .password(user.getPassword())
                    .build();
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}