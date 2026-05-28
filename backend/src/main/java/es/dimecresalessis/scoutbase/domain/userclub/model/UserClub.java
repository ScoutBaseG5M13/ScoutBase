package es.dimecresalessis.scoutbase.domain.userclub.model;

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
public class UserClub {

    private UUID id;
    private List<UUID> adminUserIds;
    private String name;
    private List<UUID> userTeams = Collections.emptyList();
    private List<UUID> managedClubs = Collections.emptyList();

    @Builder
    public UserClub(UUID id, List<UUID> adminUserIds, String name, List<UUID> userTeams, List<UUID> managedClubs) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.adminUserIds = adminUserIds;
        this.name = name;
        this.userTeams = userTeams != null ? userTeams : Collections.emptyList();
        this.managedClubs = managedClubs != null ? managedClubs : Collections.emptyList();
    }

    public void matchWithObject(UserClub incoming) {
        this.id = this.id != null ? this.id : incoming.id;
        this.adminUserIds = this.adminUserIds != null ? this.adminUserIds : incoming.adminUserIds;
        this.name = this.name != null ? this.name : incoming.name;
        this.userTeams = this.userTeams != null ? this.userTeams : incoming.userTeams;
        this.managedClubs = this.managedClubs != null ? this.managedClubs : incoming.managedClubs;
    }
}
