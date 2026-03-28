package scoutbase;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de integración para comprobar la obtención del rol
 * del usuario autenticado mediante el endpoint /users/role
 *
 * <p>Este test requiere que la API esté activa.</p>
 */
public class AuthServiceRoleIntegrationTest {

    /**
     * Comprueba que, tras un login correcto, se puede recuperar
     * el rol del usuario autenticado.
     *
     * @throws Exception si ocurre un error de comunicación con el backend
     */
    @Test
    void shouldGetCurrentUserRoleSuccessfully() throws Exception {

        AuthService authService = new AuthService();

        String username = "john_doe";
        String password = "password123";

        ApiResponse response = authService.login(username, password);
        String token = authService.extractToken(response);
        String role = authService.getCurrentUserRole(token);

        assertNotNull(response);
        assertTrue(response.isSuccess(), "El login debería ser exitoso");

        assertNotNull(token, "El token no debería ser null");
        assertFalse(token.isBlank(), "El token no debería estar vacío");

        assertNotNull(role, "El rol no debería ser null");
        assertFalse(role.isBlank(), "El rol no debería estar vacío");
        assertTrue(
                role.equals("ROLE_ADMIN") || role.equals("ROLE_USER"),
                "El rol debería ser ROLE_ADMIN o ROLE_USER"
        );
    }
}