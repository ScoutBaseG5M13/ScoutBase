package scoutbase;

import org.junit.jupiter.api.Test;
import scoutbase.auth.AuthService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de integración para comprobar el manejo de credenciales incorrectas
 * en el login contra el backend.
 *
 * <p>Este test requiere que la API esté activa.</p>
 */
public class AuthServiceInvalidLoginTest {

    /**
     * Comprueba que el servicio lanza una excepción cuando
     * las credenciales son inválidas.
     */
    @Test
    void shouldFailLoginWithInvalidCredentials() {

        AuthService authService = new AuthService();

        String username = "usuario_inexistente";
        String password = "password_incorrecta";

        Exception exception = assertThrows(
                RuntimeException.class,
                () -> authService.login(username, password)
        );

        assertNotNull(exception.getMessage());
        assertFalse(exception.getMessage().isBlank());
    }
}