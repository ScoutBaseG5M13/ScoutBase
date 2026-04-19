package es.dimecresalessis.scoutbase.domain.shared.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum CategoryEnum {
    QUERUBIN("QUERUBÍN"),
    PREBENJAMIN("PREBENJAMÍN"),
    BENJAMIN("BENJAMÍN"),
    ALEVIN("ALEVÍN"),
    INFANTIL("INFANTIL"),
    CADETE("CADETE"),
    JUVENIL("JUVENIL");

    private String categoryName;

    public static CategoryEnum fromValue(String value) {
        return Arrays.stream(CategoryEnum.values())
                .filter(category -> category.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid 'Category.name': '" + value + "'"));
    }

    public static CategoryEnum fromCategoryName(String name) {
        return Arrays.stream(CategoryEnum.values())
            .filter(category -> category.categoryName.equalsIgnoreCase(name))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Invalid 'Category.categoryName': '" + name + "'"));
    }
}
