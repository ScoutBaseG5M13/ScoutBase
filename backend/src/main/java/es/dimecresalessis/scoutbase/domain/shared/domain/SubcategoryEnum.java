package es.dimecresalessis.scoutbase.domain.shared.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SubcategoryEnum {
    SUB6("SUB-6"),
    SUB7("SUB-7"),
    SUB8("SUB-8"),
    SUB9("SUB-9"),
    SUB10("SUB-10"),
    SUB11("SUB-11"),
    SUB12("SUB-12"),
    SUB13("SUB-13"),
    SUB14("SUB-14"),
    SUB15("SUB-15"),
    SUB16("SUB-16"),
    SUB_SUPERIOR("SUB17-19");

    String subcategoryName;

    public static SubcategoryEnum fromValue(String value) {
        return Arrays.stream(SubcategoryEnum.values())
                .filter(category -> category.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid 'Subcategory.name': '" + value + "'"));
    }

    public static SubcategoryEnum fromSubcategoryName(String name) {
        return Arrays.stream(SubcategoryEnum.values())
                .filter(category -> category.subcategoryName.equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid 'Subcategory.categoryName': '" + name + "'"));
    }
}
