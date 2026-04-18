package es.dimecresalessis.scoutbase.domain.stat.exception;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.exception.ScoutbaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for Stat specific errors.
 * <p>
 * Extends {@link ScoutbaseException} to add custom error handling logic.
 * </p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class StatException extends ScoutbaseException {



    /**
     * Constructs a new {@link StatException}.
     *
     * @param errorEnum The {@link ErrorEnum} representing a stat specific error.
     * @param variables The variables to customize error message placeholders.
     */
    public StatException(ErrorEnum errorEnum, String... variables) {
        super(errorEnum, variables);
    }
}