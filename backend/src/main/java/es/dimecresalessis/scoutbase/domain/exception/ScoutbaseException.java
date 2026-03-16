package es.dimecresalessis.scoutbase.domain.exception;

public class ScoutbaseException extends RuntimeException {

    public ScoutbaseException(ErrorEnum errorEnum, String... variables) {
        super(formatMessage(errorEnum, variables));
    }

    private static String formatMessage(ErrorEnum errorEnum, String[] variables) {
        String message = errorEnum.getMessage();
        String replacementRegex = "\\{}";
        for (String variable : variables) {
            message = message.replaceFirst(replacementRegex, variable);
        }
        return message;
    }
}