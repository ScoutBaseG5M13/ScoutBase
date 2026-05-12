package scoutbase;

import org.junit.jupiter.api.Test;
import scoutbase.app.SessionManager;
import scoutbase.auth.AuthService;
import scoutbase.common.ApiResponse;
import scoutbase.stat.StatDTO;
import scoutbase.stat.StatsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de integración para comprobar la carga de estadísticas.
 *
 * <p>Valida que el servicio de estadísticas puede recuperar correctamente
 * la lista de stats disponibles desde backend.</p>
 */
public class StatsServiceIntegrationTest {

    /**
     * Comprueba que el listado de estadísticas se puede obtener correctamente.
     *
     * @throws Exception si falla el login o la petición a backend
     */
    @Test
    void shouldGetStatsSuccessfully() throws Exception {
        AuthService authService = new AuthService();
        ApiResponse loginResponse = authService.login("john_doe", "password123");
        String token = authService.extractToken(loginResponse);

        SessionManager.saveSession(token, loginResponse.getSessionId(), "john_doe", null);

        StatsService statsService = new StatsService();
        List<StatDTO> stats = statsService.getAllStats();

        assertNotNull(stats, "La lista de estadísticas no debería ser null");
    }
}