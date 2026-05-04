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
    private UUID trainer;
    private UUID secondTrainer;
    private List<UUID> players;
    private List<UUID> scouters;

    @Builder
    public UserTeam(UUID id, String name, CategoryEnum category, SubcategoryEnum subcategory, UUID trainer, UUID secondTrainer, List<UUID> players, List<UUID> scouters) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.name = name;
        this.category = category;
        this.subcategory = subcategory;
        this.trainer = trainer;
        this.secondTrainer = secondTrainer;
        this.players = players;
        this.scouters = scouters;
    }
}
