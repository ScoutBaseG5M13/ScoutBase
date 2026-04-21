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
 * Test de integración para la obtención de jugadores.
 *
 * <p>Comprueba que los equipos contienen jugadores asociados.</p>
 *
 * <p>Este test DEPENDE de que la API esté activa.</p>
 */
public class PlayerServiceIntegrationTest {

    @Test
    void shouldHavePlayersInsideTeams() throws Exception {

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

        assertNotNull(teams);
        assertFalse(teams.isEmpty());

        boolean hasPlayers = false;

        for (TeamDTO team : teams) {
            if (team.getPlayers() != null && !team.getPlayers().isEmpty()) {
                hasPlayers = true;
                break;
            }
        }

        assertTrue(hasPlayers, "Al menos un equipo debería tener jugadores");
    }
}