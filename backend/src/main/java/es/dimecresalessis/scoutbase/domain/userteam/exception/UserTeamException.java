package es.dimecresalessis.scoutbase.domain.userteam.exception;

import es.dimecresalessis.scoutbase.domain.exception.ErrorEnum;
import es.dimecresalessis.scoutbase.domain.exception.ScoutbaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception for Team specific errors.
 * <p>
 * Extends {@link ScoutbaseException} to add custom error handling logic.
 * </p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserTeamException extends ScoutbaseException {



  /**
   * Constructs a new {@link UserTeamException}.
   *
   * @param errorEnum The {@link ErrorEnum} representing a userteam specific error.
   * @param variables The variables to customize error message placeholders.
   */
  public UserTeamException(ErrorEnum errorEnum, String... variables) {
    super(errorEnum, variables);
  }
}