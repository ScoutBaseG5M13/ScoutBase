package scoutbase;

import org.junit.jupiter.api.Test;
import scoutbase.auth.AuthService;
import scoutbase.common.ApiResponse;
import scoutbase.user.UserDto;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de integración para endpoints de usuario.
 *
 * <p>Comprueba la obtención del usuario autenticado (/me).</p>
 *
 * <p>Este test DEPENDE de que la API esté activa.</p>
 */
public class UserEndpointsIntegrationTest {

    @Test
    void shouldGetCurrentUserSuccessfully() throws Exception {

        AuthService authService = new AuthService();

        ApiResponse loginResponse = authService.login("john_doe", "password123");

        assertTrue(loginResponse.isSuccess());

        String token = authService.extractToken(loginResponse);

        assertNotNull(token, "El token no debería ser null");
        assertFalse(token.isBlank(), "El token no debería estar vacío");

        UserDto user = authService.getCurrentUser(token);

        assertNotNull(user, "El usuario no debería ser null");
        assertEquals("john_doe", user.getUsername(), "El username debería coincidir");
    }
}