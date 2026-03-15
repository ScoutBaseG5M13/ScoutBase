package es.dimecresalessis.scoutbase.shared.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorEnum {
    PLAYER_NOT_FOUND("PLAYER_NOT_FOUND", "The player with id '{}' could not be found"),
    PLAYER_IS_NULL("PLAYER_IS_NULL", "The player can't be null"),
    PLAYER_BAD_FORMAT("PLAYER_BAD_FORMAT", "The player has bad format in fields: {}"),

    USER_NOT_FOUND("USER_NOT_FOUND", "The user with id '{}' could not be found"),
    USER_IS_NULL("USER_IS_NULL", "The user can't be null");

    private String code;
    private String message;
}
