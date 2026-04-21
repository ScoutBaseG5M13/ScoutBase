package scoutbase.team;

import java.util.ArrayList;
import java.util.List;

/**
 * Caché temporal en memoria para mantener visibles los equipos creados
 * localmente mientras el backend no soporta correctamente su creación o listado.
 */
public class TeamCache {

    private static final List<TeamDTO> createdTeams = new ArrayList<>();

    private TeamCache() {
    }

    public static void addTeam(TeamDTO team) {
        if (team == null) {
            return;
        }

        boolean exists = createdTeams.stream()
                .anyMatch(existing -> existing.getId() != null
                        && team.getId() != null
                        && existing.getId().equals(team.getId()));

        if (!exists) {
            createdTeams.add(team);
        }
    }

    public static List<TeamDTO> getTeams() {
        return new ArrayList<>(createdTeams);
    }
}