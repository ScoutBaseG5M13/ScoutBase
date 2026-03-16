package es.dimecresalessis.scoutbase.infrastructure.player.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.infrastructure.player.persistence.PlayerEntity;
import org.springframework.stereotype.Component;

@Component
public class PlayerEntityMapper {
    public PlayerEntity toEntity(Player domain) {
        PlayerEntity entity = new PlayerEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setTeam(domain.getTeam());
        entity.setEmail(domain.getEmail());
        return entity;
    }

    public Player toDomain(PlayerEntity entity) {
        return new Player(
                entity.getId(),
                entity.getName(),
                entity.getTeam(),
                entity.getEmail()
        );
    }
}
