package es.dimecresalessis.scoutbase.domain.shared.domain;

public enum CategoryEnum {
    QUERUBIN,
    PREBENJAMIN,
    BENJAMIN,
    ALEVIN,
    INFANTIL,
    CADETE,
    JUVENIL;

    public static CategoryEnum fromName(String name) {
        if (isValid(name)) {
            return CategoryEnum.valueOf(name.toUpperCase());
        }
        throw new IllegalArgumentException("Invalid 'Category' name: '" + name + "'");
    }

    public static boolean isValid(String value) {
        for (CategoryEnum category : CategoryEnum.values()) {
            if (category.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
