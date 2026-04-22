package es.dimecresalessis.scoutbase.domain.club.exception;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.exception.ScoutbaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for Club specific errors.
 * <p>
 * Extends {@link ScoutbaseException} to add custom error handling logic.
 * </p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClubException extends ScoutbaseException {
    /**
     * Constructs a new {@link ClubException}.
     *
     * @param errorEnum The {@link ErrorEnum} representing a club specific error.
     * @param variables The variables to customize error message placeholders.
     */
    public ClubException(ErrorEnum errorEnum, String... variables) {
        super(errorEnum, variables);
    }
}
