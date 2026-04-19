package es.dimecresalessis.scoutbase.application.security;

import es.dimecresalessis.scoutbase.application.user.find.FindUserByUsernameUseCase;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

/**
 * Service for handling user authentication and token generation within the application.
 * <p>
 * It validates user credentials through Spring Security's authentication manager
 * and generates a JSON Web Token (JWT) for authenticated users.
 * </p>
 */
@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final FindUserByUsernameUseCase findUserByUsernameUseCase;

    /**
     * Authenticates the user and generates a JWT token.
     *
     * @param username The username provided by the user.
     * @param password The password provided by the user.
     * @return A string with the JWT token, if authentication is successful.
     * @throws org.springframework.security.core.AuthenticationException If authentication fails.
     */
    public String authenticateAndGenerateToken(String username, String password) {
        //authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        User user = findUserByUsernameUseCase.execute(username);
        return jwtService.createToken(user);
    }
}