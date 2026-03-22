package es.dimecresalessis.scoutbase.infrastructure.player.persistence.mapper;

import es.dimecresalessis.scoutbase.domain.player.model.Player;
import es.dimecresalessis.scoutbase.infrastructure.player.persistence.PlayerEntity;
import org.springframework.stereotype.Component;

/**
 * Infrastructure mapper for converting between {@link Player} domain models
 * and {@link PlayerEntity} persistence objects.
 */
@Component
public class PlayerEntityMapper {

    /**
     * Transforms a {@link Player} domain object into a {@link PlayerEntity}
     * suitable for database operations.
     *
     * @param domain The domain-level player entity to be converted.
     * @return A {@link PlayerEntity} ready for persistence via JPA.
     */
    public PlayerEntity toEntity(Player domain) {
        PlayerEntity entity = new PlayerEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setTeam(domain.getTeam());
        entity.setEmail(domain.getEmail());
        return entity;
    }

    /**
     * Transforms a {@link PlayerEntity} retrieved from the database back
     * into a {@link Player} domain object.
     * <p>
     *
     * @param entity The {@link PlayerEntity} entity retrieved by the repository.
     * @return A {@link Player} instance.
     */
    public Player toDomain(PlayerEntity entity) {
        return new Player(
                entity.getId(),
                entity.getName(),
                entity.getTeam(),
                entity.getEmail()
        );
    }
}