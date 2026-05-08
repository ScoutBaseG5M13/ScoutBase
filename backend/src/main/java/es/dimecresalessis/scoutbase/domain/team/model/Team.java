package es.dimecresalessis.scoutbase.domain.team.model;

import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.SubcategoryEnum;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class Team {
    private UUID id;
    private UUID clubId;
    private String name;
    private CategoryEnum category;
    private SubcategoryEnum subcategory;
    private List<UUID> players;

    @Builder
    public Team(UUID id, String name, CategoryEnum category, SubcategoryEnum subcategory, List<UUID> players, UUID clubId) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.clubId = clubId;
        this.name = name;
        this.category = category;
        this.subcategory = subcategory;
        this.players = players;
    }

    public void matchWithObject(Team incoming) {
        this.id = this.id != null ? this.id : incoming.id;
        this.clubId = this.clubId != null ? this.clubId : incoming.clubId;
        this.name = this.name != null ? this.name : incoming.name;
        this.category = this.category != null ? this.category : incoming.category;
        this.subcategory = this.subcategory != null ? this.subcategory : incoming.subcategory;
        this.players = this.players != null ? this.players : incoming.players;
    }
}
