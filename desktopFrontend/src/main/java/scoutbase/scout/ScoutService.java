package scoutbase.scout;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import scoutbase.common.ApiClient;
import scoutbase.common.ApiResponse;
import scoutbase.user.UserDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con scouts.
 *
 * <p>Dado que un scout es un tipo de usuario dentro del sistema,
 * este servicio utiliza el endpoint específico de scouters del backend
 * para obtener su listado y el endpoint general de usuarios para crearlos.</p>
 */
public class ScoutService {

    /**
     * URL base del endpoint de usuarios en el backend.
     */
    private static final String BASE_URL =
            "https://scoutbase-dev-6r6d.onrender.com/api/v1/users";

    /**
     * Cliente HTTP utilizado para comunicarse con la API.
     */
    private final ApiClient apiClient = new ApiClient();

    /**
     * Objeto encargado de la serialización y deserialización de JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Obtiene todos los scouters disponibles desde el backend.
     *
     * <p>Utiliza el endpoint específico {@code /users/scouters},
     * por lo que no es necesario filtrar usuarios en frontend.</p>
     *
     * @return lista de scouts adaptados al DTO de la vista
     * @throws RuntimeException si ocurre un error al obtener o mapear los datos
     */
    public List<ScoutDTO> getAllScouts() {
        try {
            String responseJson = apiClient.get(BASE_URL + "/scouters");
            System.out.println("GET SCOUTERS RAW: " + responseJson);

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            if (!response.isSuccess()) {
                throw new RuntimeException(response.getMessage());
            }

            JsonNode data = response.getData();

            List<UserDto> users = objectMapper
                    .readerForListOf(UserDto.class)
                    .readValue(data);

            return users.stream()
                    .map(this::toScoutDTO)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error obteniendo scouts", e);
        }
    }

    /**
     * Crea un nuevo scout utilizando el endpoint de creación de usuarios.
     *
     * @param username nombre de usuario
     * @param password contraseña
     * @param name nombre real
     * @param surname apellidos
     * @param email correo electrónico
     * @return scout creado adaptado al DTO de la vista
     * @throws RuntimeException si ocurre un error durante la creación
     */
    public ScoutDTO createScout(String username,
                                String password,
                                String name,
                                String surname,
                                String email) {
        try {
            String body = """
                    {
                      "username": "%s",
                      "password": "%s",
                      "role": "SCOUTER",
                      "name": "%s",
                      "surname": "%s",
                      "email": "%s"
                    }
                    """.formatted(username, password, name, surname, email);

            String responseJson = apiClient.post(BASE_URL, body);
            System.out.println("CREATE SCOUT RESPONSE: " + responseJson);

            ApiResponse response = objectMapper.readValue(responseJson, ApiResponse.class);

            if (!response.isSuccess()) {
                throw new RuntimeException(response.getMessage());
            }

            ScoutDTO scout = new ScoutDTO();
            scout.setUsername(username);
            scout.setName(name);
            scout.setSurname(surname);
            scout.setEmail(email);
            scout.setRole("SCOUTER");

            return scout;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creando scout", e);
        }
    }

    /**
     * Convierte un {@link UserDto} en un {@link ScoutDTO}.
     *
     * <p>Como el endpoint de scouters ya devuelve únicamente usuarios
     * con ese rol, se asigna {@code SCOUTER} directamente.</p>
     *
     * @param user usuario de origen
     * @return scout adaptado para la vista
     */
    private ScoutDTO toScoutDTO(UserDto user) {
        ScoutDTO scout = new ScoutDTO();
        scout.setId(user.getId());
        scout.setUsername(user.getUsername());
        scout.setName(user.getName());
        scout.setSurname(user.getSurname());
        scout.setEmail(user.getEmail());
        scout.setRole("SCOUTER");
        return scout;
    }
}