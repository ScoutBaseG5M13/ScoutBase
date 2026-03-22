package es.dimecresalessis.scoutbase.domain.player.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.UUID;

/**
 * Domain entity representing a football base player.
 */
@Getter
@AllArgsConstructor
public class Player {

    /**
     * ID of the player.
     */
    private UUID id;

    /**
     *  name of the player.
     */
    private String name;

    /**
     * team of the player.
     */
    private String team;

    /**
     * email of the player.
     */
    private String email;

    /**
     * Factory method to create a new {@link Player} instance with a generated unique ID.
     * <p>
     * This method is for testing.
     * </p>
     *
     * @param name The name of the player.
     * @param team The current team.
     * @param email The player's email.
     * @return A new {@link Player} instance with a randomized unique identifier.
     */
    public static Player getNewInstance(String name, String team, String email) {
        return new Player(UUID.randomUUID(), name, team, email);
    }
}