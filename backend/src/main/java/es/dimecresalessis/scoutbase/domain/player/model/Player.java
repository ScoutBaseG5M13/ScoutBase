package es.dimecresalessis.scoutbase.domain.player.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class Player {
    private UUID id;
    private String name;
    private String team;
    private String email;

    public static Player getNewInstance(String name, String team, String email) {
        return new Player(UUID.randomUUID(), name, team, email);
    }
}
