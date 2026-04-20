package scoutbase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamDTO {

    private String id;
    private String name;
    private String category;
    private String subcategory;

    private String clubId;

    private ClubRef club;

    private List<String> players;
    private List<String> trainers;
    private List<String> scouters;

    public TeamDTO() {}

    public String getResolvedClubId() {
        if (clubId != null && !clubId.isBlank()) {
            return clubId;
        }
        if (club != null) {
            return club.getId();
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getClubId() {
        return clubId;
    }

    public void setClubId(String clubId) {
        this.clubId = clubId;
    }

    public ClubRef getClub() {
        return club;
    }

    public void setClub(ClubRef club) {
        this.club = club;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public List<String> getTrainers() {
        return trainers;
    }

    public void setTrainers(List<String> trainers) {
        this.trainers = trainers;
    }

    public List<String> getScouters() {
        return scouters;
    }

    public void setScouters(List<String> scouters) {
        this.scouters = scouters;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ClubRef {
        private String id;

        public ClubRef() {}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}