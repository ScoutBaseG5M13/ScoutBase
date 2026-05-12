package scoutbase;

import org.junit.jupiter.api.Test;
import scoutbase.club.ClubService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test que verifica el comportamiento cuando la API está caída.
 *
 * <p>Comprueba que el servicio lanza una excepción cuando
 * no puede conectar con backend.</p>
 */
public class ApiDownTest {

    /**
     * Comprueba que se lanza una excepción al intentar
     * obtener clubes sin backend disponible.
     */
    @Test
    void shouldFailWhenApiIsDown() {

        ClubService clubService = new ClubService();

        assertThrows(
                Exception.class,
                () -> clubService.getClubsByUserClub("invalid-id"),
                "Debería lanzar excepción si la API no está disponible"
        );
    }
}