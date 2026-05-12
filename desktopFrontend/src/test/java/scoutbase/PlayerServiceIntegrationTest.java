package scoutbase;

import org.junit.jupiter.api.Test;
import scoutbase.app.SessionManager;
import scoutbase.auth.AuthService;
import scoutbase.club.ClubDTO;
import scoutbase.club.ClubService;
import scoutbase.common.ApiResponse;
import scoutbase.team.TeamDTO;
import scoutbase.team.TeamService;
import scoutbase.userClub.UserClubDTO;
import scoutbase.userClub.UserClubService;

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

    /**
     * Verifica que al menos un equipo contiene jugadores asociados.
     *
     * @throws Exception si falla el login o alguna petición HTTP
     */
    @Test
    void shouldHavePlayersInsideTeams() throws Exception {

        AuthService authService = new AuthService();

        ApiResponse loginResponse =
                authService.login("john_doe", "password123");

        String token = authService.extractToken(loginResponse);

        SessionManager.saveSession(
                token,
                loginResponse.getSessionId(),
                "john_doe",
                null
        );

        UserClubService userClubService = new UserClubService();
        UserClubDTO userClub = userClubService.getDefaultUserClub();

        assertNotNull(userClub, "El UserClub no debería ser null");

        ClubService clubService = new ClubService();
        List<ClubDTO> clubs =
                clubService.getClubsByUserClub(userClub.getId());

        assertNotNull(clubs, "La lista de clubes no debería ser null");
        assertFalse(clubs.isEmpty(), "Debe existir al menos un club");

        TeamService teamService = new TeamService();

        List<TeamDTO> teams =
                teamService.getTeamsByClubId(clubs.get(0).getId());

        assertNotNull(teams, "La lista de equipos no debería ser null");
        assertFalse(teams.isEmpty(), "Debe existir al menos un equipo");

        boolean hasPlayers = false;

        for (TeamDTO team : teams) {
            if (team.getPlayers() != null
                    && !team.getPlayers().isEmpty()) {

                hasPlayers = true;
                break;
            }
        }

        assertTrue(
                hasPlayers,
                "Al menos un equipo debería tener jugadores"
        );
    }
}