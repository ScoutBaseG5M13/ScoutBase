package es.dimecresalessis.scoutbase.domain.stat.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@Builder
public class Stat {

    private UUID id;

    private UUID playerId;

    private String code;

    private int value;

    public Stat(UUID id, UUID playerId, String code, int value) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.setPlayerId(playerId);
        this.setCode(code);
        this.value = value;
    }

    public void matchWithObject(Stat incoming) {
        this.id = this.id != null ? this.id : incoming.id;
        this.playerId = this.playerId != null ? this.playerId : incoming.playerId;
        this.code = this.code != null ? this.code : incoming.code;
        this.value = this.value >= 0 || this.value < 10 ? this.value : incoming.value;
    }

    public void setCode(final String code) {
        this.code = StatEnum.fromStatCode(code).statCode;
    }
}
