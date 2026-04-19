package es.dimecresalessis.scoutbase.infrastructure.security;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class Session {

    public static User getSessionUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
