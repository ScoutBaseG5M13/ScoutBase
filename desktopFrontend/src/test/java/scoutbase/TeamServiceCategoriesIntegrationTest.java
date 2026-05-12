package scoutbase;

import org.junit.jupiter.api.Test;
import scoutbase.app.SessionManager;
import scoutbase.auth.AuthService;
import scoutbase.common.ApiResponse;
import scoutbase.team.TeamService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test de integración para comprobar la obtención de categorías de equipos.
 *
 * <p>Valida que el backend devuelve correctamente la lista de categorías
 * y subcategorías utilizadas para la creación de equipos.</p>
 */
public class TeamServiceCategoriesIntegrationTest {

    /**
     * Comprueba que el servicio de equipos puede obtener categorías desde backend.
     *
     * @throws Exception si falla el login o la petición a la API
     */
    @Test
    void shouldGetTeamCategoriesSuccessfully() throws Exception {
        AuthService authService = new AuthService();
        ApiResponse loginResponse = authService.login("john_doe", "password123");
        String token = authService.extractToken(loginResponse);

        SessionManager.saveSession(token, loginResponse.getSessionId(), "john_doe", null);

        TeamService teamService = new TeamService();
        List<TeamService.TeamCategoryDTO> categories = teamService.getCategories();

        assertNotNull(categories, "La lista de categorías no debería ser null");
        assertFalse(categories.isEmpty(), "La lista de categorías no debería estar vacía");
        assertNotNull(categories.get(0).getName(), "La categoría debería tener nombre");
    }
}