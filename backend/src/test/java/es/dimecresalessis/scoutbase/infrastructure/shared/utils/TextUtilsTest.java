package es.dimecresalessis.scoutbase.infrastructure.shared.utils;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class TextUtilsTest {

    @Test
    void shouldNormalizeToUpperCaseAndTrim() {
        String input = "  helloo mundoo  ";

        String result = TextUtils.normalizeToUpperCase(input);

        assertThat(result).isEqualTo("HELLOO MUNDOO");
    }

    @Test
    void shouldReturnNullWhenInputIsNull() {
        String result = TextUtils.normalizeToUpperCase(null);

        assertThat(result).isNull();
    }

    @Test
    void shouldHandleEmptyString() {
        String result = TextUtils.normalizeToUpperCase("");

        assertThat(result).isEmpty();
    }

    @Test
    void shouldHandleStringWithOnlySpaces() {
        String result = TextUtils.normalizeToUpperCase("   ");

        assertThat(result).isEmpty();
    }
}