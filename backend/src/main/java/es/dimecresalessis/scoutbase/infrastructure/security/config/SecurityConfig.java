package es.dimecresalessis.scoutbase.infrastructure.security.config;

import es.dimecresalessis.scoutbase.infrastructure.security.JwtFilter;
import es.dimecresalessis.scoutbase.infrastructure.security.handler.SecurityHandlers;
import es.dimecresalessis.scoutbase.infrastructure.security.filter.SessionFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main security configuration for the application.
 * <p>
 * It couples the application to the Spring Security framework and defines the entry points for
 * external web traffic.
 * </p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Configures the primary security filter chain for all HTTP requests.
     *
     * @param http The http security object to modify.
     * @param securityHandlers Custom handlers for authentication and access denial.
     * @param sessionFilter Custom filter for session-specific logic.
     * @param jwtFilter Custom filter for JWT token validation.
     * @return A fully built security filter chain.
     * @throws Exception If an error occurs during the security configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityHandlers securityHandlers, SessionFilter sessionFilter, JwtFilter jwtFilter) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(sessionFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtFilter, SessionFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(securityHandlers)
                        .accessDeniedHandler(securityHandlers)
                )
                .build();
    }

    /**
     * Provides the standard {@link AuthenticationManager} from the current configuration.
     * <p>
     * This manager is used to verify user credentials.
     * </p>
     *
     * @param config The {@link AuthenticationConfiguration} provided by Spring.
     * @return The configured authentication manager.
     * @throws Exception If the manager cannot be retrieved.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Defines the hashing algorithm for password storage.
     * <p>
     * Uses the <b>BCrypt</b> strong hashing function, which includes a random
     * salt to protect against rainbow table attacks.
     * </p>
     *
     * @return A {@link BCryptPasswordEncoder} instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}