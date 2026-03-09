package es.dimecresalessis.scoutbase.player.domain.exception;

import es.dimecresalessis.scoutbase.shared.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.shared.exception.ScoutbaseException;

public class PlayerException extends ScoutbaseException {
    public PlayerException(ErrorEnum errorEnum, String[] message) {
        super(errorEnum, message);
    }

    public PlayerException(ErrorEnum errorEnum, String message) {
        super(errorEnum, message);
    }
}
