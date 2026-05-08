package es.dimecresalessis.scoutbase.domain.player.model;

import es.dimecresalessis.scoutbase.domain.shared.domain.PositionEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class Player {
    private UUID id = UUID.randomUUID();
    private UUID teamId;
    private String name;
    private String surname;
    private int birthYear;
    private String email;
    private int number;
    private PositionEnum position;
    private int priority;
    private List<UUID> stats;

    @Builder
    public Player(UUID id, UUID teamId, String name, String surname, int birthYear, String email,
                  int number, PositionEnum position, int priority, List<UUID> stats) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.teamId = teamId;
        this.name = name;
        this.surname = surname;
        this.birthYear = birthYear;
        this.email = email;
        this.number = number;
        this.position = position;
        this.priority = priority;
        this.stats = stats;
    }

    public void matchWithObject(Player incoming) {
        this.id = this.id != null ? this.id : incoming.id;
        this.teamId = this.teamId != null ? this.teamId : incoming.teamId;
        this.name = this.name != null ? this.name : incoming.name;
        this.surname = this.surname != null ? this.surname : incoming.surname;
        this.birthYear = this.birthYear != 0 ? this.birthYear : incoming.birthYear;
        this.email = this.email != null ? this.email : incoming.email;
        this.number = this.number != 0 ? this.number : incoming.number;
        this.position = this.position != null ? this.position : incoming.position;
        this.priority = this.priority != 0 ? this.priority : incoming.priority;
        this.stats = this.stats != null ? this.stats : incoming.stats;
    }
}