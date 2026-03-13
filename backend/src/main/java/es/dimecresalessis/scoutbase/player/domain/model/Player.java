package es.dimecresalessis.scoutbase.player.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
@Table(name = "player")
public class Player {
    private UUID id;
    private String name;
    private String team;
    private String email;

    public static Player getNewInstance(String name, String team, String email) {
        return new Player(UUID.randomUUID(), name, team, email);
    }
}
