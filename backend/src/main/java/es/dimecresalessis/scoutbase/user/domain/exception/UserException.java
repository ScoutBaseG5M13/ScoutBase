package es.dimecresalessis.scoutbase.user.domain.exception;

import es.dimecresalessis.scoutbase.shared.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.shared.exception.ScoutbaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserException extends ScoutbaseException {
    public UserException(ErrorEnum errorEnum, String... variables) {
        super(errorEnum, variables);
    }
}
