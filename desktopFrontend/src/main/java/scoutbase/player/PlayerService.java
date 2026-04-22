package scoutbase.player;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import scoutbase.common.ApiClient;
import scoutbase.common.ApiResponse;

import java.util.List;
import java.util.UUID;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con jugadores.
 *
 * <p>Permite recuperar los jugadores asociados a un equipo concreto,
 * crear nuevos jugadores en el backend y generar jugadores temporales
 * en memoria cuando el backend no responde como se espera.</p>
 */
public class PlayerService {

    /**
     * URL base del endpoint de jugadores en el backend.
     */
    private static final String BASE_URL =
            "https://scoutbase-dev-6r6d.onrender.com/api/v1/players";

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
     * <p>Realiza una petición al backend utilizando el identificador del equipo
     * y convierte la respuesta en una lista de objetos {@link PlayerDTO}.</p>
     *
     * <p>Como solución temporal, si el backend devuelve el mensaje
     * {@code "No value present"} para equipos sin jugadores, se interpreta
     * como una lista vacía en lugar de lanzar una excepción.</p>
     *
     * @param teamId identificador del equipo cuyos jugadores se desean obtener
     * @return lista de jugadores del equipo indicado
     * @throws RuntimeException si ocurre un error durante la petición
     *                          o el backend devuelve una respuesta no válida
     */
    public List<PlayerDTO> getPlayersByTeamId(String teamId) {
        try {
            String responseJson = apiClient.get(BASE_URL + "/teams/" + teamId);

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            if (!response.isSuccess()) {
                throw new RuntimeException(response.getMessage());
            }

            JsonNode data = response.getData();

            return objectMapper
                    .readerForListOf(PlayerDTO.class)
                    .readValue(data);

        } catch (Exception e) {
            String message = e.getMessage() != null ? e.getMessage() : "";

            // Apaño temporal: si backend devuelve "No value present" para teams vacíos,
            // tratamos la respuesta como una lista vacía.
            if (message.contains("No value present")) {
                return List.of();
            }

            e.printStackTrace();
            throw new RuntimeException("Error obteniendo jugadores", e);
        }
    }

    /**
     * Crea un nuevo jugador asociado a un equipo concreto.
     *
     * <p>Construye el cuerpo JSON con los datos del jugador, realiza
     * una petición POST al backend y devuelve el jugador creado
     * como objeto {@link PlayerDTO}.</p>
     *
     * @param name nombre del jugador
     * @param surname apellidos del jugador
     * @param age edad del jugador
     * @param email correo electrónico del jugador
     * @param number dorsal del jugador
     * @param position posición del jugador en el campo
     * @param priority prioridad asignada al jugador
     * @param teamId identificador del equipo al que pertenece
     * @return jugador creado devuelto por el backend
     * @throws RuntimeException si ocurre un error durante la creación
     */
    public PlayerDTO createPlayer(String name,
                                  String surname,
                                  int age,
                                  String email,
                                  int number,
                                  String position,
                                  int priority,
                                  String teamId) {
        try {
            String jsonBody = """
                {
                  "name": "%s",
                  "surname": "%s",
                  "age": %d,
                  "email": "%s",
                  "number": %d,
                  "position": "%s",
                  "priority": %d,
                  "teamId": "%s"
                }
                """.formatted(name, surname, age, email, number, position, priority, teamId);

            System.out.println("CREATE PLAYER URL: " + BASE_URL + "/teams/" + teamId);
            System.out.println("CREATE PLAYER BODY: " + jsonBody);

            String responseJson = apiClient.post(BASE_URL + "/teams/" + teamId, jsonBody);

            System.out.println("CREATE PLAYER RESPONSE: " + responseJson);

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            if (!response.isSuccess()) {
                throw new RuntimeException(response.getMessage());
            }

            return objectMapper.treeToValue(response.getData(), PlayerDTO.class);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creando jugador", e);
        }
    }

    /**
     * Crea un jugador únicamente en memoria como solución temporal
     * cuando el backend no puede devolver correctamente el jugador creado.
     *
     * <p>Este método se utiliza como apoyo para la demo y genera
     * un identificador aleatorio local mediante {@link UUID}.</p>
     *
     * @param name nombre del jugador
     * @param surname apellidos del jugador
     * @param age edad del jugador
     * @param email correo electrónico del jugador
     * @param number dorsal del jugador
     * @param position posición del jugador en el campo
     * @param priority prioridad asignada al jugador
     * @param teamId identificador del equipo al que pertenece
     * @return jugador creado localmente en memoria
     */
    public PlayerDTO createLocalPlayer(String name,
                                       String surname,
                                       int age,
                                       String email,
                                       int number,
                                       String position,
                                       int priority,
                                       String teamId) {
        PlayerDTO player = new PlayerDTO();
        player.setId(UUID.randomUUID().toString());
        player.setName(name);
        player.setSurname(surname);
        player.setAge(age);
        player.setEmail(email);
        player.setNumber(number);
        player.setPosition(position);
        player.setPriority(priority);
        player.setTeamId(teamId);
        return player;
    }
}