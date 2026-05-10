package es.dimecresalessis.scoutbase.domain.shared.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

import static es.dimecresalessis.scoutbase.domain.shared.domain.SubcategoryEnum.*;

@Getter
@AllArgsConstructor
public enum CategoryEnum {
    PREBENJAMIN("PREBENJAMÍN", List.of(SUB7, SUB8)),
    BENJAMIN("BENJAMÍN", List.of(SUB9, SUB10)),
    ALEVIN("ALEVÍN", List.of(SUB11, SUB12)),
    INFANTIL("INFANTIL", List.of(SUB13, SUB14)),
    CADETE("CADETE", List.of(SUB15, SUB16)),
    JUVENIL("JUVENIL", List.of(SUB_SUPERIOR));

    private String categoryName;
    private List<SubcategoryEnum> subcategories;

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
