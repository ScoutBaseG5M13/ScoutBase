package es.dimecresalessis.scoutbase.domain.club.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class Club {

    private UUID id;
    private List<UUID> adminUserIds;
    private String name;
    private List<UUID> teams;

    @Builder
    public Club(UUID id, List<UUID> adminUserIds, String name, List<UUID> teams) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.adminUserIds = adminUserIds;
        this.name = name;
        this.teams = teams;
    }
}
