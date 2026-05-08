package es.dimecresalessis.scoutbase.domain.userteam.model;

import es.dimecresalessis.scoutbase.domain.shared.domain.CategoryEnum;
import es.dimecresalessis.scoutbase.domain.shared.domain.SubcategoryEnum;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class UserTeam {
    private UUID id;
    private String name;
    private CategoryEnum category;
    private SubcategoryEnum subcategory;
    private UUID userClub;
    private UUID trainer;
    private UUID secondTrainer;
    private List<UUID> scouters;

    @Builder
    public UserTeam(UUID id, String name, CategoryEnum category, SubcategoryEnum subcategory, UUID userClub, UUID trainer, UUID secondTrainer, List<UUID> scouters) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.name = name;
        this.category = category;
        this.subcategory = subcategory;
        this.userClub = userClub;
        this.trainer = trainer;
        this.secondTrainer = secondTrainer;
        this.scouters = scouters;
    }

    public void matchWithObject(UserTeam incoming) {
        this.id = this.id != null ? this.id : incoming.id;
        this.name = this.name != null ? this.name : incoming.name;
        this.category = this.category != null ? this.category : incoming.category;
        this.subcategory = this.subcategory != null ? this.subcategory : incoming.subcategory;
        this.userClub = this.userClub != null ? this.userClub : incoming.userClub;
        this.trainer = this.trainer != null ? this.trainer : incoming.trainer;
        this.secondTrainer = this.secondTrainer != null ? this.secondTrainer : incoming.secondTrainer;
        this.scouters = this.scouters != null ? this.scouters : incoming.scouters;
    }
}
