package es.dimecresalessis.scoutbase.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ScoutbaseExceptionTest {

    @Test
    void shouldFormatMessageWithOneVariable() {
        String id = "123-ABC";
        ScoutbaseException exception = new ScoutbaseException(ErrorEnum.PLAYER_NOT_FOUND, id);

        assertEquals("The player with id '123-ABC' could not be found", exception.getMessage());
        assertEquals(ErrorEnum.PLAYER_NOT_FOUND, exception.getErrorEnum());
    }

    @Test
    void shouldFormatMessageWithMultipleVariables() {
        String username = "admin";
        String id = "uuid-999";
        ScoutbaseException exception = new ScoutbaseException(
                ErrorEnum.USER_ID_DOES_NOT_MATCH,
                username,
                id
        );

        assertEquals("The user id 'admin' does not match the one provided 'uuid-999'.", exception.getMessage());
        assertArrayEquals(new String[]{username, id}, exception.getVariables());
    }

    @Test
    void shouldFormatStatIntegrityError() {
        ScoutbaseException exception = new ScoutbaseException(
                ErrorEnum.STAT_INTEGRITY_ERROR,
                "Pase",
                "VEL"
        );

        assertEquals("The stat has name 'Pase' but code 'VEL'. They have no correlation", exception.getMessage());
    }

    @Test
    void shouldIgnoreExtraVariables() {
        ScoutbaseException exception = new ScoutbaseException(ErrorEnum.PLAYER_IS_NULL, "Extra", "Unused");

        assertEquals("The player can't be null", exception.getMessage());
    }

    @Test
    void shouldKeepPlaceholderIfVariableMissing() {
        ScoutbaseException exception = new ScoutbaseException(
                ErrorEnum.STAT_CODE_ALREADY_EXISTS,
                "PAS"
        );

        assertEquals("The stat with code 'PAS' already exists on player with id '{}'", exception.getMessage());
    }
}