package es.dimecresalessis.scoutbase.shared.exception;


public class ScoutbaseException extends Exception {

    private final ErrorEnum errorEnum;

    public ScoutbaseException(ErrorEnum errorEnum, String[] variables) {
        super(formatMessage(errorEnum, variables));
        this.errorEnum = errorEnum;
    }

    public ScoutbaseException(ErrorEnum errorEnum, String variable) {
        super(formatMessage(errorEnum, new String[]{variable}));
        this.errorEnum = errorEnum;
    }

    private static String formatMessage(ErrorEnum errorEnum, String[] variables) {
        String message = errorEnum.getMessage();
        if (variables.length < 2) return message;
        for (String variable : variables) {
            message = message.replaceFirst("\\{}", variable);
        }
        return message;
    }
}