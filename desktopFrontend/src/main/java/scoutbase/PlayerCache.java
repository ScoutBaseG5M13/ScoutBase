package scoutbase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Caché temporal en memoria para mantener visibles los jugadores creados
 * mientras el backend no devuelve correctamente los jugadores por equipo.
 */
public class PlayerCache {

    private static final List<PlayerDTO> createdPlayers = new ArrayList<>();

    private PlayerCache() {
    }

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

    public static List<PlayerDTO> getPlayersByTeamId(String teamId) {
        return createdPlayers.stream()
                .filter(player -> player.getTeamId() != null && player.getTeamId().equals(teamId))
                .collect(Collectors.toList());
    }
}