package scoutbase;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase {@link SessionManager}.
 *
 * <p>Comprueban el correcto almacenamiento, recuperación y limpieza
 * de los datos de sesión del usuario autenticado.</p>
 */
public class SessionManagerTest {

    /**
     * Limpia la sesión después de cada test para evitar interferencias
     * entre pruebas.
     */
    @AfterEach
    void tearDown() {
        SessionManager.clear();
    }

    /**
     * Comprueba que los datos de sesión se guardan correctamente.
     */
    @Test
    void shouldSaveSessionCorrectly() {
        SessionManager.saveSession(
                "test-token",
                "test-session-id",
                "john_doe",
                "ROLE_ADMIN"
        );

        assertEquals("test-token", SessionManager.getAuthToken());
        assertEquals("test-session-id", SessionManager.getSessionId());
        assertEquals("john_doe", SessionManager.getUsername());
        assertEquals("ROLE_ADMIN", SessionManager.getRole());
    }

    /**
     * Comprueba que isLoggedIn devuelve true cuando existe un token válido.
     */
    @Test
    void shouldReturnTrueWhenUserIsLoggedIn() {
        SessionManager.saveSession(
                "valid-token",
                "session-123",
                "jane_dove",
                "ROLE_USER"
        );

        assertTrue(SessionManager.isLoggedIn());
    }

    /**
     * Comprueba que isLoggedIn devuelve false cuando no existe sesión activa.
     */
    @Test
    void shouldReturnFalseWhenNoSessionExists() {
        assertFalse(SessionManager.isLoggedIn());
    }

    /**
     * Comprueba que clear elimina correctamente todos los datos de sesión.
     */
    @Test
    void shouldClearSessionCorrectly() {
        SessionManager.saveSession(
                "token-to-clear",
                "session-to-clear",
                "user_to_clear",
                "ROLE_USER"
        );

        SessionManager.clear();

        assertNull(SessionManager.getAuthToken());
        assertNull(SessionManager.getSessionId());
        assertNull(SessionManager.getUsername());
        assertNull(SessionManager.getRole());
        assertFalse(SessionManager.isLoggedIn());
    }

    /**
     * Comprueba que el método {@link SessionManager#isLoggedIn()}
     * devuelve false cuando el token almacenado está vacío o contiene
     * únicamente espacios en blanco.
     *
     * <p>Esto asegura que no se considera válida una sesión con un token
     * incorrecto o mal formado.</p>
     */
    @Test
    void shouldReturnFalseWhenTokenIsBlank() {

        SessionManager.saveSession(
                "   ",
                "session-blank",
                "user_blank",
                "ROLE_USER"
        );

        assertFalse(
                SessionManager.isLoggedIn(),
                "Una sesión con token vacío no debería considerarse válida"
        );
    }
}