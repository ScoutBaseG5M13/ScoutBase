package scoutbase;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de integración para {@link AuthService}.
 *
 * <p>Realiza una llamada real al backend para comprobar que el login
 * funciona correctamente.</p>
 *
 * <p>Este test DEPENDE de que la API esté activa.</p>
 * <p>Si el servidor está apagado o no hay conexión,
 * el test fallará.</p>
 */
public class AuthServiceIntegrationTest {

    /**
     * Comprueba que el login contra el backend devuelve
     * una respuesta válida con token.
     *
     * <p>Requiere que la API esté en funcionamiento.</p>
     *
     * @throws Exception si ocurre un error de comunicación
     */
    @Test
    void shouldLoginSuccessfullyWithValidCredentials() throws Exception {

        AuthService authService = new AuthService();

        String username = "john_doe";
        String password = "password123";

        ApiResponse response = authService.login(username, password);

        assertNotNull(response);
        assertTrue(response.isSuccess(), "El login debería ser exitoso");

        String token = authService.extractToken(response);

        assertNotNull(token, "El token no debería ser null");
        assertFalse(token.isBlank(), "El token no debería estar vacío");
    }
}