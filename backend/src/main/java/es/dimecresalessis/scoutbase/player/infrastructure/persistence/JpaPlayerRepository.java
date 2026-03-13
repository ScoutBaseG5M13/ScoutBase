package es.dimecresalessis.scoutbase.player.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaPlayerRepository extends JpaRepository<PlayerEntity, UUID> {
}
