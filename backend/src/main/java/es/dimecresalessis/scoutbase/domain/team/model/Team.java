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
    private List<UUID> trainers;
    private List<UUID> scouters;

    @Builder
    public Team(UUID id, String name, CategoryEnum category, SubcategoryEnum subcategory, List<UUID> players, List<UUID> trainers, List<UUID> scouters) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.name = name;
        this.category = category;
        this.subcategory = subcategory;
//        this.setCategory(category);
//        this.setSubcategory(subcategory);
        this.players = players;
        this.trainers = trainers;
        this.scouters = scouters;
    }

//    public void setCategory(String category) {
//        this.category = CategoryEnum.fromValue(category);
//    }
//
//    public void setSubcategory(String subcategory) {
//        this.subcategory = SubcategoryEnum.fromValue(subcategory);
//    }
}
