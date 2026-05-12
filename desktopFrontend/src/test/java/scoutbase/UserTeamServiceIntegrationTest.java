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
import scoutbase.userTeam.UserTeamDTO;
import scoutbase.userTeam.UserTeamService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de integración para comprobar la resolución de UserTeams.
 *
 * <p>Valida que, a partir de un UserClub y un Team existente, el sistema
 * puede resolver un UserTeam asociado.</p>
 */
public class UserTeamServiceIntegrationTest {

    /**
     * Comprueba que se puede obtener o resolver un UserTeam asociado a un equipo.
     *
     * @throws Exception si falla el login o alguna petición a backend
     */
    @Test
    void shouldResolveUserTeamSuccessfully() throws Exception {
        AuthService authService = new AuthService();
        ApiResponse loginResponse = authService.login("john_doe", "password123");
        String token = authService.extractToken(loginResponse);

        SessionManager.saveSession(token, loginResponse.getSessionId(), "john_doe", null);

        UserClubService userClubService = new UserClubService();
        UserClubDTO userClub = userClubService.getDefaultUserClub();

        assertNotNull(userClub, "El UserClub no debería ser null");

        ClubService clubService = new ClubService();
        List<ClubDTO> clubs = clubService.getClubsByUserClub(userClub.getId());

        assertNotNull(clubs, "La lista de clubes no debería ser null");
        assertFalse(clubs.isEmpty(), "Debe existir al menos un club");

        TeamService teamService = new TeamService();
        List<TeamDTO> teams = teamService.getTeamsByClubId(clubs.get(0).getId());

        assertNotNull(teams, "La lista de equipos no debería ser null");
        assertFalse(teams.isEmpty(), "Debe existir al menos un equipo");

        UserTeamService userTeamService = new UserTeamService();
        UserTeamDTO userTeam = userTeamService.getOrCreateUserTeamForTeam(userClub.getId(), teams.get(0));

        assertNotNull(userTeam, "El UserTeam resuelto no debería ser null");
        assertNotNull(userTeam.getId(), "El UserTeam debería tener ID");
    }
}