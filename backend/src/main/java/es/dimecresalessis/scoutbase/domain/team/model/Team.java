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
    private String name;
    private CategoryEnum category;
    private SubcategoryEnum subcategory;
    private List<UUID> players;

    @Builder
    public Team(UUID id, String name, CategoryEnum category, SubcategoryEnum subcategory, List<UUID> players) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.name = name;
        this.category = category;
        this.subcategory = subcategory;
        this.players = players;
    }
}
