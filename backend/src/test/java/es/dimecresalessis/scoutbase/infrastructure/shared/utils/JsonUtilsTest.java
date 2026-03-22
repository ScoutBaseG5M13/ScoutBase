package es.dimecresalessis.scoutbase.infrastructure.shared.utils;

import org.junit.jupiter.api.Test;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonUtilsTest {

    @Test
    void shouldConvertMapToJson() {
        Map<String, Object> data = Map.of("id", 1, "name", "Test");

        String result = JsonUtils.toJson(data);

        assertThat(result).contains("\"id\":1");
        assertThat(result).contains("\"name\":\"Test\"");
    }

    @Test
    void shouldHandleSimplePojo() {
        var user = new Object() {
            public String getEmail() { return "test@mail.com"; }
        };

        String result = JsonUtils.toJson(user);

        assertThat(result).isEqualTo("{\"email\":\"test@mail.com\"}");
    }

    @Test
    void shouldThrowExceptionOnInvalidObject() {
        Object emptyObject = new Object(); // Jackson falla si no hay getters o propiedades

        assertThatThrownBy(() -> JsonUtils.toJson(emptyObject))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Error converting to JSON");
    }
}