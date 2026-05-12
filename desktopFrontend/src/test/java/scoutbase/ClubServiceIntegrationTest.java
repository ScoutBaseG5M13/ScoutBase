package scoutbase;

import org.junit.jupiter.api.Test;
import scoutbase.app.SessionManager;
import scoutbase.auth.AuthService;
import scoutbase.club.ClubDTO;
import scoutbase.club.ClubService;
import scoutbase.common.ApiResponse;
import scoutbase.userClub.UserClubDTO;
import scoutbase.userClub.UserClubService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de integración para {@link ClubService}.
 *
 * <p>Comprueba que el backend devuelve correctamente
 * los clubes asociados al UserClub autenticado.</p>
 *
 * <p>Este test depende de que la API esté activa.</p>
 */
public class ClubServiceIntegrationTest {

    /**
     * Comprueba que se pueden recuperar correctamente
     * los clubes asociados al usuario autenticado.
     *
     * @throws Exception si falla el login o la petición HTTP
     */
    @Test
    void shouldGetClubsSuccessfully() throws Exception {

        AuthService authService = new AuthService();

        ApiResponse loginResponse =
                authService.login("john_doe", "password123");

        String token =
                authService.extractToken(loginResponse);

        SessionManager.saveSession(
                token,
                loginResponse.getSessionId(),
                "john_doe",
                null
        );

        UserClubService userClubService =
                new UserClubService();

        UserClubDTO userClub =
                userClubService.getDefaultUserClub();

        assertNotNull(userClub,
                "El UserClub no debería ser null");

        ClubService clubService =
                new ClubService();

        List<ClubDTO> clubs =
                clubService.getClubsByUserClub(userClub.getId());

        assertNotNull(clubs,
                "La lista de clubes no debería ser null");

        assertFalse(clubs.isEmpty(),
                "La lista de clubes no debería estar vacía");
    }
}