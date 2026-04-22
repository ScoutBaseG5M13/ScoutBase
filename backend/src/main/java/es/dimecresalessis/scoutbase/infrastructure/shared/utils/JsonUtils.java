package es.dimecresalessis.scoutbase.infrastructure.shared.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class for JSON processing.
 */
public class JsonUtils {

    /**
     * Serializes a Java object into a JSON formatted string.
     *
     * @param obj The object to be converted to JSON.
     * @return A JSON string representation of the object.
     * @throws RuntimeException if the object cannot be serialized (e.g., due to circular references or lack of getters).
     */
    public static String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error converting to JSON", e);
        }
    }
}
