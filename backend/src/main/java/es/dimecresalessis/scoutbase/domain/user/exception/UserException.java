package es.dimecresalessis.scoutbase.domain.user.exception;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.exception.ScoutbaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for User specific errors.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserException extends ScoutbaseException {

    /**
     * Constructs a new UserException with a specific error code and dynamic variables.
     * <p>
     * The {@link ErrorEnum} provides the localized message template, while the
     * {@code variables} are injected into the message placeholders.
     * </p>
     *
     * @param errorEnum The specific error code from {@link ErrorEnum}.
     * @param variables Strings to be injected into the error message template.
     */
    public UserException(ErrorEnum errorEnum, String... variables) {
        super(errorEnum, variables);
    }
}