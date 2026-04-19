package es.dimecresalessis.scoutbase.domain.stat.model;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.stat.exception.StatException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    private String code;

    @NotNull
    private int value;

    public Stat(UUID id, UUID playerId, String code, int value) {
        this.id = (id == null) ? UUID.randomUUID() : id;
        this.setPlayerId(playerId);
        this.setCode(code);
        this.value = value;
    }

    public void setPlayerId(final UUID playerId) {
        try {
            if (UUID.fromString(playerId.toString()).toString().isEmpty()) {
                throw new StatException(ErrorEnum.STAT_MUST_HAVE_PLAYER_ID);
            }
        } catch (IllegalArgumentException ex) {
            throw new StatException(ErrorEnum.INVALID_UUID, playerId.toString());
        }
        this.playerId = playerId;
    }

    public void setCode(final String code) {
        this.code = StatEnum.fromStatCode(code).statCode;
    }
}
