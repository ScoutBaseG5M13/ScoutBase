package es.dimecresalessis.scoutbase.player.infrastructure.persistence;

import es.dimecresalessis.scoutbase.infrastructure.web.persistence.CommonEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "player")
public class PlayerEntity extends CommonEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String team;
    @Column(nullable = false)
    private String email;
}