package es.dimecresalessis.scoutbase.domain.userclub.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class UserClub {

    private UUID id;
    private List<UUID> adminUserIds;
    private String name;
    private List<UUID> userTeams;
    private List<UUID> managedClubs;

    @Builder
    public UserClub(UUID id, List<UUID> adminUserIds, String name, List<UUID> userTeams, List<UUID> clubIds) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.adminUserIds = adminUserIds;
        this.name = name;
        this.userTeams = userTeams;
        this.managedClubs = clubIds;
    }
}
