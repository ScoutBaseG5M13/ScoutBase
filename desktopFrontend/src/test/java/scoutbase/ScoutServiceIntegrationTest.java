package scoutbase;

import org.junit.jupiter.api.Test;
import scoutbase.app.SessionManager;
import scoutbase.auth.AuthService;
import scoutbase.common.ApiResponse;
import scoutbase.scout.ScoutDTO;
import scoutbase.scout.ScoutService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de integración para comprobar la carga de scouts.
 *
 * <p>Valida que el servicio de scouts puede consultar correctamente
 * los usuarios con rol o perfil de scout desde backend.</p>
 */
public class ScoutServiceIntegrationTest {

    /**
     * Comprueba que el listado de scouts se puede solicitar sin devolver null.
     *
     * @throws Exception si falla el login o la petición a backend
     */
    @Test
    void shouldGetScoutsSuccessfully() throws Exception {
        AuthService authService = new AuthService();
        ApiResponse loginResponse = authService.login("john_doe", "password123");
        String token = authService.extractToken(loginResponse);

        SessionManager.saveSession(token, loginResponse.getSessionId(), "john_doe", null);

        ScoutService scoutService = new ScoutService();
        List<ScoutDTO> scouts = scoutService.getAllScouts();

        assertNotNull(scouts, "La lista de scouts no debería ser null");
    }
}