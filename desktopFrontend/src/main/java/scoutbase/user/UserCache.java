package scoutbase.user;

import java.util.ArrayList;
import java.util.List;

/**
 * Caché temporal en memoria para gestionar usuarios dentro de la aplicación.
 *
 * <p>Permite almacenar usuarios de forma local durante la ejecución
 * de la aplicación, evitando duplicados y facilitando su recuperación
 * sin necesidad de consultar continuamente el backend.</p>
 *
 * <p>Los datos almacenados se pierden al cerrar la aplicación.</p>
 */
public class UserCache {

    /**
     * Lista en memoria que contiene los usuarios almacenados.
     */
    private static final List<UserDto> users = new ArrayList<>();

    /**
     * Añade un usuario a la caché si no existe previamente.
     *
     * <p>Se comprueba si ya existe un usuario con el mismo identificador
     * para evitar duplicados.</p>
     *
     * @param user usuario a añadir
     */
    public static void addUser(UserDto user) {
        if (user == null) {
            return;
        }

        boolean exists = users.stream().anyMatch(existing ->
                existing.getId() != null
                        && user.getId() != null
                        && existing.getId().equals(user.getId())
        );

        if (!exists) {
            users.add(user);
        }
    }

    /**
     * Devuelve la lista de usuarios almacenados en caché.
     *
     * <p>Se devuelve una copia para evitar modificaciones externas
     * sobre la colección interna.</p>
     *
     * @return lista de usuarios almacenados
     */
    public static List<UserDto> getUsers() {
        return new ArrayList<>(users);
    }

    /**
     * Elimina todos los usuarios almacenados en la caché.
     *
     * <p>Se utiliza para limpiar los datos en memoria, por ejemplo,
     * al cerrar sesión o reiniciar el estado de la aplicación.</p>
     */
    public static void clear() {
        users.clear();
    }
}