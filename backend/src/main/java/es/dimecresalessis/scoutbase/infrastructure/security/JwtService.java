package es.dimecresalessis.scoutbase.infrastructure.security;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.security.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Service for generating and validating JWT tokens.
 * <p>
 * This service provides utilities to create JWT tokens, validate them,
 * and extract user-specific data.
 * </p>
 */
@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final SecretKey key;

    /**
     * Constructs the service with required properties for token creation.
     *
     * @param jwtProperties Configuration properties for JWT.
     */
    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT token for a user.
     *
     * @param user The user to associate with the token.
     * @return A signed JWT token as a string.
     */
    public String createToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.expirationMs()))
                .signWith(key)
                .compact();
    }

    /**
     * Extracts a username from the provided token.
     *
     * @param token The token to parse.
     * @return The username placed in the token subject field.
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Extracts the user role from the provided token.
     * @param token The token to parse.
     * @return The role stored in the custom "role" claim.
     */
    public String extractRole(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    /**
     * Checks if the provided JWT token is valid for the given user details.
     *
     * @param token The JWT token to validate.
     * @param user The user details to verify.
     * @return {@code true} if the token matches the provided user details and is not expired.
     */

    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return username.equals(user.getUsername());
    }

    /**
     * Determines if the token has surpassed its defined expiration date.
     * <p>
     * This is a helper method used during the validation process to ensure
     * a token is still within its allowed lifespan.
     * </p>
     *
     * @param token The JWT string to check.
     * @return {@code true} if the current system time is after the token's expiration date,
     * {@code false} otherwise.
     */
    public boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    /**
     * Retrieves the expiration date directly from the JWT claims.
     * <p>
     * This method parses the token using the configured secret key and
     * extracts the "exp" claim from the payload.
     * </p>
     *
     * @param token The JWT string to parse.
     * @return The {@link Date} at which this token becomes invalid.
     */
    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}