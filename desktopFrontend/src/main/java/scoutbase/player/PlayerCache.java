package scoutbase.player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Caché temporal en memoria para mantener visibles los jugadores creados
 * localmente mientras el backend no devuelve correctamente los jugadores
 * asociados a cada equipo.
 *
 * <p>Esta clase actúa como un almacenamiento auxiliar durante la ejecución
 * de la aplicación, permitiendo recuperar jugadores recientemente creados
 * sin depender exclusivamente de la respuesta del backend.</p>
 */
public class PlayerCache {

    /**
     * Lista en memoria que almacena los jugadores creados temporalmente.
     */
    private static final List<PlayerDTO> createdPlayers = new ArrayList<>();

    /**
     * Constructor privado para evitar la instanciación de la clase.
     */
    private PlayerCache() {
    }

    /**
     * Añade un jugador a la caché temporal y lo asocia a un equipo.
     *
     * <p>Si el jugador es nulo no se realiza ninguna acción. Antes de añadirlo,
     * se comprueba si ya existe en la caché un jugador con el mismo identificador
     * para evitar duplicados.</p>
     *
     * @param player jugador a almacenar en caché
     * @param teamId identificador del equipo al que pertenece
     */
    public static void addPlayer(PlayerDTO player, String teamId) {
        if (player == null) {
            return;
        }

        player.setTeamId(teamId);

        boolean exists = createdPlayers.stream()
                .anyMatch(p -> p.getId() != null && p.getId().equals(player.getId()));

        if (!exists) {
            createdPlayers.add(player);
        }
    }

    /**
     * Devuelve los jugadores almacenados en caché que pertenecen a un equipo concreto.
     *
     * @param teamId identificador del equipo a filtrar
     * @return lista de jugadores asociados al equipo indicado
     */
    public static List<PlayerDTO> getPlayersByTeamId(String teamId) {
        return createdPlayers.stream()
                .filter(player -> player.getTeamId() != null && player.getTeamId().equals(teamId))
                .collect(Collectors.toList());
    }
}