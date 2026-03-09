package es.dimecresalessis.scoutbase.player.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.Random;
import java.util.UUID;

@AllArgsConstructor
@Getter
@ToString
public class PlayerDto {
    private UUID id;
    private String name;
    private String team;
    private String email;

    public static PlayerDto getInstance() {
        String name = "Player " + new Random().nextInt(1, 33);
        String team = "Team " + new Random().nextInt(1, 33);
        return new PlayerDto(UUID.randomUUID(),
                name,
                team,
                name.toLowerCase().replace(" ","") + "@" + team.toLowerCase().replace(" ","") + ".es");
    }
}
