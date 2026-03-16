package es.dimecresalessis.scoutbase.domain.user.model;

import es.dimecresalessis.scoutbase.infrastructure.shared.utils.TextUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum Role {
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

    public static Role fromName(String name) {
        for (Role role : values()) {
            if (TextUtils.normalizeToUpperCase(role.getName()).equals(TextUtils.normalizeToUpperCase(name))) {
                return role;
            }
        }
        return null;
    }
}