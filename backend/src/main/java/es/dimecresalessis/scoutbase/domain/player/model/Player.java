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
    private int age;
    private String email;
    private int number; // "Dorsal"
    private PositionEnum position;
    private int priority;

    @Builder
    public Player(UUID id, String name, String surname, int age, String email,
                  int number, PositionEnum position, int priority) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.number = number;
        this.position = position;
        this.priority = priority;
    }

//    public void setPosition(String position) {
//        this.position = PositionEnum.fromValue(position);
//    }
}