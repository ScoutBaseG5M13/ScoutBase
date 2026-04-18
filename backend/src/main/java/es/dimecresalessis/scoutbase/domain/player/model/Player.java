package es.dimecresalessis.scoutbase.domain.player.model;

import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.PositionEnum;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class Player {
    private UUID id = UUID.randomUUID();
    private UUID teamId;
    private String name;
    private String surname;
    private int age;
    private String email;
    private int number; // "Dorsal"
    private PositionEnum position;
    private CategoryEnum category;
    private int priority;

    @Builder
    public Player(UUID id, UUID teamId, String name, String surname, int age, String email,
                  int number, String position, String category, int priority) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.teamId = teamId;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.number = number;
        this.setPosition(position);
        this.setCategory(category);
        this.priority = priority;
    }

    public void setPosition(String position) {
        this.position = PositionEnum.fromName(position);
    }

    public void setCategory(String category) {
        this.category = CategoryEnum.fromName(category);
    }
}