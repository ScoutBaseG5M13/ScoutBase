package es.dimecresalessis.scoutbase.application.security;

import es.dimecresalessis.scoutbase.infrastructure.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    public String authenticateAndGenerateToken(String username, String password) {
        // 1. Verify credentials (Spring Security handles the DB check)
        authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        // 2. If no exception was thrown, generate the token using your JwtService
        return jwtService.createToken(username);
    }
}