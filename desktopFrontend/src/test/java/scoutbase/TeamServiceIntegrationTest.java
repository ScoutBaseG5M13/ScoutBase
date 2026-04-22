package scoutbase;

import org.junit.jupiter.api.Test;
import scoutbase.app.SessionManager;
import scoutbase.auth.AuthService;
import scoutbase.common.ApiResponse;
import scoutbase.team.TeamDTO;
import scoutbase.team.TeamService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de integración para {@link TeamService}.
 *
 * <p>Comprueba que se pueden obtener equipos desde el backend.</p>
 *
 * <p>Este test DEPENDE de que la API esté activa.</p>
 */
public class TeamServiceIntegrationTest {

    @Test
    void shouldGetAllTeamsSuccessfully() throws Exception {

        AuthService authService = new AuthService();
        ApiResponse loginResponse = authService.login("john_doe", "password123");
        String token = authService.extractToken(loginResponse);

        SessionManager.saveSession(
                token,
                loginResponse.getSessionId(),
                "john_doe",
                null
        );

        TeamService teamService = new TeamService();
        List<TeamDTO> teams = teamService.getAllTeams();

        assertNotNull(teams, "La lista de equipos no debería ser null");
        assertFalse(teams.isEmpty(), "La lista de equipos no debería estar vacía");
    }
}