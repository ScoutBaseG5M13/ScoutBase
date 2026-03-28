package scoutbase;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para la clase {@link AuthService}.
 *
 * <p>Comprueban la correcta extracción del token de autenticación
 * a partir de distintas estructuras posibles en la respuesta del backend.</p>
 */
public class AuthServiceTest {

    private final AuthService authService = new AuthService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Comprueba que el token se extrae correctamente cuando está
     * dentro del campo data.token.
     *
     * @throws Exception si ocurre un error al procesar el JSON
     */
    @Test
    void shouldExtractTokenFromTokenField() throws Exception {
        ApiResponse response = new ApiResponse();
        JsonNode data = objectMapper.readTree("""
                {
                  "token": "abc123"
                }
                """);

        response.setData(data);

        String token = authService.extractToken(response);

        assertEquals("abc123", token);
    }

    /**
     * Comprueba que el token se extrae correctamente cuando está
     * dentro del campo data.accessToken.
     *
     * @throws Exception si ocurre un error al procesar el JSON
     */
    @Test
    void shouldExtractTokenFromAccessTokenField() throws Exception {
        ApiResponse response = new ApiResponse();
        JsonNode data = objectMapper.readTree("""
                {
                  "accessToken": "access-456"
                }
                """);

        response.setData(data);

        String token = authService.extractToken(response);

        assertEquals("access-456", token);
    }

    /**
     * Comprueba que el token se extrae correctamente cuando está
     * dentro del campo data.jwt.
     *
     * @throws Exception si ocurre un error al procesar el JSON
     */
    @Test
    void shouldExtractTokenFromJwtField() throws Exception {
        ApiResponse response = new ApiResponse();
        JsonNode data = objectMapper.readTree("""
                {
                  "jwt": "jwt-789"
                }
                """);

        response.setData(data);

        String token = authService.extractToken(response);

        assertEquals("jwt-789", token);
    }

    /**
     * Comprueba que el token se extrae correctamente cuando el campo
     * data es directamente un texto plano.
     *
     * @throws Exception si ocurre un error al procesar el JSON
     */
    @Test
    void shouldExtractTokenFromTextualData() throws Exception {
        ApiResponse response = new ApiResponse();
        JsonNode data = objectMapper.readTree("\"plain-token\"");

        response.setData(data);

        String token = authService.extractToken(response);

        assertEquals("plain-token", token);
    }

    /**
     * Comprueba que se devuelve null cuando la respuesta no contiene datos.
     */
    @Test
    void shouldReturnNullWhenDataIsNull() {
        ApiResponse response = new ApiResponse();
        response.setData(null);

        String token = authService.extractToken(response);

        assertNull(token);
    }

    /**
     * Comprueba que se devuelve null cuando el campo data no contiene
     * ningún nombre de token reconocido.
     *
     * @throws Exception si ocurre un error al procesar el JSON
     */
    @Test
    void shouldReturnNullWhenTokenFieldIsMissing() throws Exception {
        ApiResponse response = new ApiResponse();
        JsonNode data = objectMapper.readTree("""
                {
                  "unexpectedField": "value"
                }
                """);

        response.setData(data);

        String token = authService.extractToken(response);

        assertNull(token);
    }
    /**
     * Comprueba que el método {@link AuthService#extractToken(ApiResponse)}
     * devuelve null cuando el campo data existe pero no contiene
     * ningún atributo válido de token.
     *
     * <p>Este caso representa respuestas inesperadas o incompletas
     * del backend.</p>
     *
     * @throws Exception si ocurre un error al procesar el JSON
     */
    @Test
    void shouldReturnNullWhenDataIsEmptyObject() throws Exception {

        ApiResponse response = new ApiResponse();
        JsonNode data = objectMapper.readTree("{}");

        response.setData(data);

        String token = authService.extractToken(response);

        assertNull(
                token,
                "No debería extraerse ningún token de un objeto vacío"
        );
    }
}