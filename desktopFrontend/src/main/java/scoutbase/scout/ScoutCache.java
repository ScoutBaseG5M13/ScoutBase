package scoutbase.scout;

import java.util.ArrayList;
import java.util.List;

/**
 * Caché temporal en memoria para mantener visibles los scouts creados
 * mientras no exista una integración real con backend.
 */
public class ScoutCache {

    private static final List<ScoutDTO> scouts = new ArrayList<>();

    private ScoutCache() {
    }

    public static List<ScoutDTO> getScouts() {
        return new ArrayList<>(scouts);
    }

    public static void addScout(ScoutDTO scout) {
        if (scout == null) {
            return;
        }

        boolean exists = scouts.stream()
                .anyMatch(existing -> existing.getId() != null
                        && existing.getId().equals(scout.getId()));

        if (!exists) {
            scouts.add(scout);
        }
    }

    public static boolean isEmpty() {
        return scouts.isEmpty();
    }
}