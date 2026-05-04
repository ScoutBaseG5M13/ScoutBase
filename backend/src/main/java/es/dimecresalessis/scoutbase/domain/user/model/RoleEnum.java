package es.dimecresalessis.scoutbase.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum representing predefined user roles within the system.
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {
    SCOUTER("SCOUTER", 1),
    SECOND_TRAINER("SECOND_TRAINER", 2),
    TRAINER("TRAINER", 3),
    ADMIN("ADMIN", 4),
    SUPERADMIN("SUPERADMIN", 5);

    private final String roleName;
    private final int roleAuthLevel;


    /**
     * Retrieves a {@link RoleEnum} from its name as a string.
     *
     * @param roleName The name of the role to resolve.
     * @return The matching {@link RoleEnum} instance, or {@code null} if no match is found.
     */
    public static RoleEnum fromName(String roleName) {
        for (RoleEnum roleEnum : values()) {
            if (roleEnum.getRoleName().equalsIgnoreCase(roleName)) {
                return roleEnum;
            }
        }
        return null;
    }

    public static boolean isEqualsOrHigher(RoleEnum minRole, RoleEnum userRole) {
        return userRole.getRoleAuthLevel() >= minRole.getRoleAuthLevel();
    }
}