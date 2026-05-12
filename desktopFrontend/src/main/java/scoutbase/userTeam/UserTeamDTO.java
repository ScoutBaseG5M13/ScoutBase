package scoutbase.userTeam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import scoutbase.team.TeamDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTeamDTO {

    private String id;
    private String name;
    private String category;
    private String subcategory;
    private String teamId;
    private TeamDTO team;

    public UserTeamDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if (name != null && !name.isBlank()) {
            return name;
        }

        if (team != null) {
            return team.getName();
        }

        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        if (category != null && !category.isBlank()) {
            return category;
        }

        if (team != null) {
            return team.getCategory();
        }

        return null;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        if (subcategory != null && !subcategory.isBlank()) {
            return subcategory;
        }

        if (team != null) {
            return team.getSubcategory();
        }

        return null;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public TeamDTO getTeam() {
        return team;
    }

    public void setTeam(TeamDTO team) {
        this.team = team;
    }

    public String getResolvedTeamId() {
        if (teamId != null && !teamId.isBlank()) {
            return teamId;
        }

        if (team != null) {
            return team.getId();
        }

        return null;
    }
}