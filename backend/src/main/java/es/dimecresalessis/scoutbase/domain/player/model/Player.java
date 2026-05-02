package es.dimecresalessis.scoutbase.domain.player.model;

import es.dimecresalessis.scoutbase.domain.shared.domain.PositionEnum;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class Player {
    private UUID id = UUID.randomUUID();
    private String name;
    private String surname;
    private int birthYear;
    private String email;
    private int number; // "Dorsal"
    private PositionEnum position;
    private int priority;

    @Builder
    public Player(UUID id, String name, String surname, int birthYear, String email,
                  int number, PositionEnum position, int priority) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.name = name;
        this.surname = surname;
        this.birthYear = birthYear;
        this.email = email;
        this.number = number;
        this.position = position;
        this.priority = priority;
    }
}