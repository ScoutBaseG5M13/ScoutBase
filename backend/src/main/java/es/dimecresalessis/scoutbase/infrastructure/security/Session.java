package es.dimecresalessis.scoutbase.infrastructure.security;

import es.dimecresalessis.scoutbase.domain.user.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Infrastructure utility class for managing and accessing the current security session.
 */
public class Session {

    /**
     * Retrieves the currently authenticated user from the Security Context.
     *
     * @return The {@link User} entity associated with the current session.
     * @throws ClassCastException if the authenticated principal is not of type {@link User}.
     * @throws NullPointerException if there is no active authentication in the context.
     */
    public static User getSessionUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
