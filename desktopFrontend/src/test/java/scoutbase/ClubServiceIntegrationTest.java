package scoutbase;

import org.junit.jupiter.api.Test;
import scoutbase.app.SessionManager;
import scoutbase.auth.AuthService;
import scoutbase.club.ClubDTO;
import scoutbase.club.ClubService;
import scoutbase.common.ApiResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de integración para {@link ClubService}.
 *
 * <p>Realiza una llamada real al backend para obtener
 * la lista de clubes.</p>
 *
 * <p>Este test DEPENDE de que la API esté activa.</p>
 */
public class ClubServiceIntegrationTest {

    @Test
    void shouldGetAllClubsSuccessfully() throws Exception {

        AuthService authService = new AuthService();
        ApiResponse loginResponse = authService.login("john_doe", "password123");
        String token = authService.extractToken(loginResponse);

        SessionManager.saveSession(
                token,
                loginResponse.getSessionId(),
                "john_doe",
                null
        );

        ClubService clubService = new ClubService();
        List<ClubDTO> clubs = clubService.getAllClubs();

        assertNotNull(clubs, "La lista de clubes no debería ser null");
        assertFalse(clubs.isEmpty(), "La lista de clubes no debería estar vacía");
    }
}