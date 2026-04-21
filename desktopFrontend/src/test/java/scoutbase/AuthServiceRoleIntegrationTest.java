package scoutbase;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Test de integración para comprobar la obtención del rol
 * del usuario autenticado.
 *
 * <p>Actualmente deshabilitado porque el backend todavía no expone
 * el rol de usuario de forma estable para el frontend.</p>
 */
@Disabled("Deshabilitado temporalmente: el backend no expone el rol de usuario de forma estable")
public class AuthServiceRoleIntegrationTest {

    @Test
    void shouldGetCurrentUserRoleSuccessfully() {
        // Test pendiente de reactivar cuando el backend soporte
        // correctamente la obtención del rol del usuario autenticado.
    }
}