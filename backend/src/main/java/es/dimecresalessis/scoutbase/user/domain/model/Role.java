package es.dimecresalessis.scoutbase.user.domain.model;

import es.dimecresalessis.scoutbase.shared.utils.TextUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN("ADMIN", List.of(
            Permission.PLAYER_WRITE,
            Permission.PLAYER_READ
    )),

    USER("USER", List.of(
            Permission.PLAYER_READ
    ));

    private final String name;
    private final List<Permission> permissions;

    public static Role fromName(String name) {
        for (Role role : values()) {
            if (TextUtils.normalizeToUpperCase(role.getName()).equals(name)) {
                return role;
            }
        }
        return null;
    }
}