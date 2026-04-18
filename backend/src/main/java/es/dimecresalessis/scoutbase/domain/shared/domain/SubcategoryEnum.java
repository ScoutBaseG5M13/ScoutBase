package es.dimecresalessis.scoutbase.domain.shared.domain;

public enum SubcategoryEnum {
    SUB6,
    SUB7,
    SUB8,
    SUB9,
    SUB10,
    SUB11,
    SUB12,
    SUB13,
    SUB14,
    SUB15,
    SUB16,
    SUB17;

    public static SubcategoryEnum fromName(String name) {
        if (isValid(name)) {
            return SubcategoryEnum.valueOf(name.toUpperCase());
        }
        throw new IllegalArgumentException("Invalid 'Subcategory' name: '" + name + "'");
    }

    public static boolean isValid(String value) {
        for (SubcategoryEnum subcategory : SubcategoryEnum.values()) {
            if (subcategory.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
