package es.dimecresalessis.scoutbase.infrastructure.security;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import es.dimecresalessis.scoutbase.infrastructure.security.config.JwtProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    private JwtService jwtService;

    @Mock
    private JwtProperties jwtProperties;

    private final String SECRET = "v9y$B&E)H@McQfTjWnZr4u7x!A%C*F-JaNdRgUkXp2s5v8y/B?E(G+KbPeShVmYq";
    private final long EXPIRATION = 3600000;

    @BeforeEach
    void setUp() {
        when(jwtProperties.secret()).thenReturn(SECRET);
        when(jwtProperties.expirationMs()).thenReturn(EXPIRATION);
        jwtService = new JwtService(jwtProperties);
    }

    @Test
    void shouldCreateValidToken() {
        User user = mock(User.class);
        when(user.getUsername()).thenReturn("scout_master");

        String token = jwtService.createToken(user);

        assertNotNull(token);
        assertEquals("scout_master", jwtService.extractUsername(token));
    }

    @Test
    void shouldValidateTokenCorrectly() {
        User user = mock(User.class);
        when(user.getUsername()).thenReturn("scout_master");
        String token = jwtService.createToken(user);

        boolean isValid = jwtService.isTokenValid(token, user);

        assertTrue(isValid);
    }

    @Test
    void shouldBeInvalidForDifferentUser() {
        User user = mock(User.class);
        User differentUser = mock(User.class);
        when(user.getUsername()).thenReturn("user1");
        when(differentUser.getUsername()).thenReturn("user2");

        String token = jwtService.createToken(user);
        boolean isValid = jwtService.isTokenValid(token, differentUser);

        assertFalse(isValid);
    }

    @Test
    void shouldDetectNonExpiredToken() {
        User user = mock(User.class);
        when(user.getUsername()).thenReturn("tester");
        String token = jwtService.createToken(user);

        boolean isExpired = jwtService.isTokenExpired(token);

        assertFalse(isExpired);
    }
}