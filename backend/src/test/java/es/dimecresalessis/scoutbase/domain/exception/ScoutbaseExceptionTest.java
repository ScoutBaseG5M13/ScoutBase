package es.dimecresalessis.scoutbase.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoutbaseExceptionTest {

    @Test
    @DisplayName("Constructor - Should format message with single variable")
    void shouldFormatMessageWithOneVariable() {
        String id = "123-ABC";
        ScoutbaseException exception = new ScoutbaseException(ErrorEnum.PLAYER_NOT_FOUND, id);

        assertEquals("The player with id '123-ABC' could not be found", exception.getMessage());
        assertEquals(ErrorEnum.PLAYER_NOT_FOUND, exception.getErrorEnum());
    }

//    Me lo guardo para cuando tenga un error con dos {}
//    @Test
//    @DisplayName("Constructor - Should format message with multiple variables")
//    void shouldFormatMessageWithMultipleVariables() {
//        ScoutbaseException exception = new ScoutbaseException(
//                ErrorEnum.PLAYER_BAD_FORMAT,
//                "email, name"
//        );
//
//        assertEquals("The player has bad format in fields: email, name", exception.getMessage());
//    }

    @Test
    @DisplayName("Constructor - Should ignore extra variables if placeholders are missing")
    void shouldIgnoreExtraVariables() {
        ScoutbaseException exception = new ScoutbaseException(ErrorEnum.PLAYER_IS_NULL, "Extra");

        assertEquals("The player can't be null", exception.getMessage());
    }
}