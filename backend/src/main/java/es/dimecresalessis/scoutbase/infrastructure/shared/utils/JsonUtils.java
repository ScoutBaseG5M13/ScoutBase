package es.dimecresalessis.scoutbase.infrastructure.shared.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    public static String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error converting to JSON", e);
        }
    }
}
