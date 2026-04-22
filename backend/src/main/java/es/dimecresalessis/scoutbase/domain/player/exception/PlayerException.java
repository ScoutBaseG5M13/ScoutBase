package es.dimecresalessis.scoutbase.domain.player.exception;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.exception.ScoutbaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for Player specific errors.
 * <p>
 * Extends {@link ScoutbaseException} to add custom error handling logic.
 * </p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlayerException extends ScoutbaseException {



    /**
     * Constructs a new {@link PlayerException}.
     *
     * @param errorEnum The {@link ErrorEnum} representing a player specific error.
     * @param variables The variables to customize error message placeholders.
     */
    public PlayerException(ErrorEnum errorEnum, String... variables) {
        super(errorEnum, variables);
    }
}
