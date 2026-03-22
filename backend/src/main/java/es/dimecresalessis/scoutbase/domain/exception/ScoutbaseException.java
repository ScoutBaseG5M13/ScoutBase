package es.dimecresalessis.scoutbase.domain.exception;

import lombok.Getter;

/**
 * Application-Specific Errors.
 * <p>
 * This exception wraps instances of {@link ErrorEnum} with support for
 * dynamic message formatting.
 * </p>
 */
@Getter
public class ScoutbaseException extends RuntimeException {

    private final ErrorEnum errorEnum;
    private final String[] variables;

    /**
     * Constructs a new exception with the formatted message from {@link ErrorEnum}.
     *
     * @param errorEnum The {@link ErrorEnum} representing the error type and message.
     * @param variables The variables to replace placeholders in the error message.
     */
    public ScoutbaseException(ErrorEnum errorEnum, String... variables) {
        super(formatMessage(errorEnum, variables));
        this.errorEnum = errorEnum;
        this.variables = variables;
    }

    /**
     * Formats the error message by replacing placeholders with variables.
     *
     * @param errorEnum The {@link ErrorEnum} containing the message template.
     * @param variables The variables to inject into the message template.
     * @return A formatted error message.
     */
    private static String formatMessage(ErrorEnum errorEnum, String[] variables) {
        String message = errorEnum.getMessage();
        String replacementRegex = "\\{}";
        for (String variable : variables) {
            message = message.replaceFirst(replacementRegex, variable);
        }
        return message;
    }
}