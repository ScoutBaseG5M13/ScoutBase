package es.dimecresalessis.scoutbase.shared.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorEnum {
    PLAYER_NOT_FOUND("PLAYER_NOT_FOUND", "The player {} was not found.");

    private String code;
    private String message;
}
