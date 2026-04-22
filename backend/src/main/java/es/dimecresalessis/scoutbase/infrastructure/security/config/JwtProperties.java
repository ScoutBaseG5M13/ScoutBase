package es.dimecresalessis.scoutbase.infrastructure.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Immutable configuration properties for JSON Web Token (JWT) management.
 * <p>
 * It maps properties prefixed with {@code security.jwt} from the application configuration files
 * (e.g., {@code application.yml} or {@code application.properties}) into a strongly-typed object.
 * </p>
 * <p>
 * Using a {@code record} ensures that the security settings are immutable
 * once the application context has been initialized.
 * </p>
 * @param secret The cryptographic key used to sign and verify JWT tokens.
 * This should be kept secure and not hardcoded in source control.
 * @param expirationMs The duration (in milliseconds) for which a generated
 * token remains valid before expiring.
 */
@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(String secret, long expirationMs) {

    /**
     * Compact constructor for the JwtProperties record.
     *
     * @param secret The JWT signing secret.
     * @param expirationMs The token lifespan in milliseconds.
     */
    public JwtProperties {
        // Validation here for the future
    }
}