package es.dimecresalessis.scoutbase.domain.club.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class Club {

    private UUID id;
    private String name;
    private List<UUID> teams = Collections.emptyList();
    private UUID userClub;

    @Builder
    public Club(UUID id, String name, List<UUID> teams, UUID userClub) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.name = name;
        this.teams = teams != null ? teams : Collections.emptyList();
        this.userClub = userClub;
    }

    public void matchWithObject(Club incoming) {
        this.id = this.id != null ? this.id : incoming.id;
        this.name = this.name != null ? this.name : incoming.name;
        this.teams = this.teams != null ? this.teams : incoming.teams;
        this.userClub = this.userClub != null ? this.userClub : incoming.userClub;
    }
}
