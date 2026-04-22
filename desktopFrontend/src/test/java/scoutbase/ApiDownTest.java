package scoutbase;

import org.junit.jupiter.api.Test;
import scoutbase.club.ClubService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test que verifica el comportamiento cuando la API está caída.
 */
public class ApiDownTest {

    @Test
    void shouldFailWhenApiIsDown() {

        ClubService clubService = new ClubService();

        assertThrows(Exception.class, clubService::getAllClubs,
                "Debería lanzar excepción si la API no está disponible");
    }
}