package es.dimecresalessis.scoutbase.domain.user.model;

import es.dimecresalessis.scoutbase.infrastructure.shared.utils.TextUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enum representing predefined user roles within the system.
 * <p>
 * Each role has a {@code name}, {@code apiPath}, and a list of allowed {@code permissions} for users with that role.
 * </p>
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {
    ADMIN("ROLE_ADMIN", "/admin", List.of(
            Permission.PLAYER_WRITE,
            Permission.PLAYER_READ,
            Permission.USER_WRITE,
            Permission.USER_READ
    )),

    USER("ROLE_USER", "/user", List.of(
            Permission.PLAYER_READ,
            Permission.USER_READ
    ));

    private final String name;
    private final String apiPath;
    private final List<Permission> permissions;

    /**
     * Retrieves a {@link RoleEnum} from its name as a string.
     *
     * @param name The name of the role to resolve.
     * @return The matching {@link RoleEnum} instance, or {@code null} if no match is found.
     */
    public static RoleEnum fromName(String name) {
        for (RoleEnum roleEnum : values()) {
            if (TextUtils.normalizeToUpperCase(roleEnum.getName()).equals(TextUtils.normalizeToUpperCase(name))) {
                return roleEnum;
            }
        }
        return null;
    }

    public static String getEnumsBySeparator(String separatorSymbol) {
        return Arrays.stream(values())
                .map(RoleEnum::getName)
                .collect(Collectors.joining(separatorSymbol));
    }
}