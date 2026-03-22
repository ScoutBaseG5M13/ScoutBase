package es.dimecresalessis.scoutbase.infrastructure.shared.utils;

/**
 * Infrastructure-level utility class for text manipulation and normalization.
 */
public class TextUtils {
    /**
     * Normalizes a string by converting it to uppercase and removing
     * leading/trailing whitespace.
     *
     * @param text The raw input string to be normalized.
     * @return The normalized, trimmed, uppercase version of the input string.
     */
    public static String normalizeToUpperCase(String text) {
        if (text == null) {
            return null;
        }
        return text.toUpperCase().trim();
    }
}
