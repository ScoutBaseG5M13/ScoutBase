package es.dimecresalessis.scoutbase.domain.userclub.exception;

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
public class UserClubException extends ScoutbaseException {
    /**
     * Constructs a new {@link UserClubException}.
     *
     * @param errorEnum The {@link ErrorEnum} representing a userclub specific error.
     * @param variables The variables to customize error message placeholders.
     */
    public UserClubException(ErrorEnum errorEnum, String... variables) {
        super(errorEnum, variables);
    }
}
