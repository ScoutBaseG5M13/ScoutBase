package scoutbase.scout;

import java.util.ArrayList;
import java.util.List;

/**
 * Caché temporal en memoria para gestionar scouts creados en la aplicación.
 *
 * <p>Se utiliza como solución provisional para almacenar y recuperar scouts
 * mientras no exista una integración completa con el backend.</p>
 *
 * <p>Los datos se mantienen únicamente durante la ejecución de la aplicación
 * y se pierden al cerrar la misma.</p>
 */
public class ScoutCache {

    /**
     * Lista en memoria que almacena los scouts creados temporalmente.
     */
    private static final List<ScoutDTO> scouts = new ArrayList<>();

    /**
     * Constructor privado para evitar la instanciación de la clase.
     */
    private ScoutCache() {
    }

    /**
     * Devuelve la lista de scouts almacenados en caché.
     *
     * <p>Se devuelve una copia de la lista para evitar modificaciones
     * externas sobre la colección interna.</p>
     *
     * @return lista de scouts almacenados
     */
    public static List<ScoutDTO> getScouts() {
        return new ArrayList<>(scouts);
    }

    /**
     * Añade un scout a la caché si no existe previamente.
     *
     * <p>Se comprueba si ya existe un scout con el mismo identificador
     * para evitar duplicados en la colección.</p>
     *
     * @param scout scout a añadir
     */
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

    /**
     * Indica si la caché de scouts está vacía.
     *
     * @return {@code true} si no hay scouts almacenados;
     * {@code false} en caso contrario
     */
    public static boolean isEmpty() {
        return scouts.isEmpty();
    }
}