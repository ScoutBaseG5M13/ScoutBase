package scoutbase.player;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import scoutbase.common.ApiClient;
import scoutbase.common.ApiResponse;

import java.util.List;
import java.util.Map;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con jugadores.
 *
 * <p>Esta clase actúa como capa de comunicación entre los controladores JavaFX
 * y la API REST del backend para la entidad {@link PlayerDTO}.</p>
 *
 * <p>En la nueva estructura del backend, los jugadores se crean asociados
 * directamente a un equipo mediante el endpoint {@code /teams/{id}/players}.
 * Además, el backend espera el campo {@code birthYear} en lugar de {@code age}
 * para la creación de jugadores.</p>
 */
public class PlayerService {

    /**
     * Endpoint relativo base para operaciones directas sobre jugadores.
     */
    private static final String PLAYERS_ENDPOINT = "/players";

    /**
     * Endpoint relativo base para operaciones sobre equipos.
     */
    private static final String TEAMS_ENDPOINT = "/teams";

    /**
     * Cliente HTTP utilizado para comunicarse con la API.
     */
    private final ApiClient apiClient = new ApiClient();

    /**
     * Objeto utilizado para la serialización y deserialización de JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Obtiene los jugadores asociados a un equipo concreto.
     *
     * <p>Utiliza el endpoint {@code /players/teams/{id}}, donde {@code id}
     * corresponde al identificador UUID del equipo seleccionado.</p>
     *
     * @param teamId identificador UUID del equipo cuyos jugadores se desean obtener
     * @return lista de jugadores asociados al equipo indicado
     * @throws RuntimeException si ocurre un error durante la petición
     *                          o si la respuesta recibida no es válida
     */
    public List<PlayerDTO> getPlayersByTeamId(String teamId) {
        try {
            String responseJson = apiClient.get(PLAYERS_ENDPOINT + "/teams/" + teamId);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo jugadores del equipo");

            return response.dataAs(new TypeReference<List<PlayerDTO>>() {});

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo jugadores del equipo", e);
        }
    }

    /**
     * Obtiene un jugador concreto a partir de su identificador único.
     *
     * @param playerId identificador UUID del jugador
     * @return jugador encontrado
     * @throws RuntimeException si el jugador no existe o la API devuelve error
     */
    public PlayerDTO getPlayerById(String playerId) {
        try {
            String responseJson = apiClient.get(PLAYERS_ENDPOINT + "/" + playerId);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error obteniendo jugador por ID");

            return response.dataAs(PlayerDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo jugador por ID", e);
        }
    }

    /**
     * Crea un nuevo jugador asociado a un equipo concreto.
     *
     * <p>Utiliza el endpoint {@code /teams/{id}/players}. El cuerpo de la
     * petición corresponde a {@code PlayerCreateRequest}, que requiere como
     * mínimo los campos {@code name} y {@code surname}.</p>
     *
     * @param teamId identificador UUID del equipo al que pertenece el jugador
     * @param name nombre del jugador
     * @param surname apellidos del jugador
     * @param birthYear año de nacimiento del jugador
     * @param email correo electrónico del jugador
     * @param number dorsal del jugador
     * @param position posición del jugador en el campo
     * @param priority prioridad asignada al jugador
     * @return jugador creado, si el backend lo devuelve en {@code data}
     * @throws RuntimeException si ocurre un error durante la creación
     */
    public PlayerDTO createPlayer(String teamId,
                                  String name,
                                  String surname,
                                  int birthYear,
                                  String email,
                                  int number,
                                  String position,
                                  int priority) {
        try {
            Map<String, Object> body = Map.of(
                    "name", name,
                    "surname", surname,
                    "birthYear", birthYear,
                    "email", email,
                    "number", number,
                    "position", position,
                    "priority", priority
            );

            String jsonBody = objectMapper.writeValueAsString(body);

            String responseJson = apiClient.post(
                    TEAMS_ENDPOINT + "/" + teamId + "/players",
                    jsonBody
            );

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error creando jugador");

            return response.dataAs(PlayerDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error creando jugador", e);
        }
    }

    /**
     * Actualiza un jugador existente.
     *
     * <p>Utiliza el endpoint {@code /players/{id}}. El cuerpo enviado incluye
     * los campos principales del jugador según el DTO esperado por el backend.</p>
     *
     * @param player jugador con los datos actualizados
     * @return jugador actualizado, si el backend lo devuelve en {@code data}
     * @throws RuntimeException si ocurre un error durante la actualización
     */
    public PlayerDTO updatePlayer(PlayerDTO player) {
        try {
            String jsonBody = objectMapper.writeValueAsString(player);

            String responseJson = apiClient.put(
                    PLAYERS_ENDPOINT + "/" + player.getId(),
                    jsonBody
            );

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponse(response, "Error actualizando jugador");

            return response.dataAs(PlayerDTO.class);

        } catch (Exception e) {
            throw new RuntimeException("Error actualizando jugador", e);
        }
    }

    /**
     * Elimina un jugador existente a partir de su identificador.
     *
     * @param playerId identificador UUID del jugador a eliminar
     * @throws RuntimeException si ocurre un error durante la eliminación
     */
    public void deletePlayer(String playerId) {
        try {
            String responseJson = apiClient.delete(PLAYERS_ENDPOINT + "/" + playerId);
            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            validateResponseWithoutData(response, "Error eliminando jugador");

        } catch (Exception e) {
            throw new RuntimeException("Error eliminando jugador", e);
        }
    }

    /**
     * Válida una respuesta estándar que debe contener datos en {@code data}.
     *
     * @param response respuesta recibida desde la API
     * @param defaultMessage mensaje por defecto en caso de error
     * @throws RuntimeException si la respuesta no es válida
     */
    private void validateResponse(ApiResponse response, String defaultMessage) {
        validateResponseWithoutData(response, defaultMessage);

        if (response.getData() == null || response.getData().isNull()) {
            throw new RuntimeException(defaultMessage + ": respuesta sin datos");
        }
    }

    /**
     * Válida una respuesta estándar sin exigir que contenga datos.
     *
     * @param response respuesta recibida desde la API
     * @param defaultMessage mensaje por defecto en caso de error
     * @throws RuntimeException si la respuesta no es válida o indica fallo
     */
    private void validateResponseWithoutData(ApiResponse response, String defaultMessage) {
        if (response == null) {
            throw new RuntimeException(defaultMessage);
        }

        if (!response.isSuccess()) {
            throw new RuntimeException(response.getMessage() != null
                    ? response.getMessage()
                    : defaultMessage);
        }
    }
}