package es.dimecresalessis.scoutbase.infrastructure.security.model;

import es.dimecresalessis.scoutbase.domain.user.model.Role;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Security principal adapter that bridges the Domain User and Spring Security.
 * <p>
 * It implements {@link UserDetails} to allow the Spring Security framework to perform authentication and authorization
 * using the data stored in the {@link User} domain entity.
 * </p>
 */
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    /**
     * The underlying domain user containing business-level identity and roles.
     */
    private final User domainUser;

    /**
     * Extracts and flattens the user's roles and permissions into Spring Security authorities.
     * <p>
     * This method converts the domain-defined {@link Role} and its associated permissions
     * into a collection of {@link SimpleGrantedAuthority}. This enables the use of
     * {@code @PreAuthorize} and other security expressions at the controller level.
     * </p>
     *
     * @return A collection of {@link GrantedAuthority} representing both roles and permissions.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // Add the primary Role as an authority
        authorities.add(new SimpleGrantedAuthority(domainUser.getRole()));

        // Add granular Permissions derived from the Role
        Role.fromName(domainUser.getRole()).getPermissions()
                .forEach(p -> authorities.add(new SimpleGrantedAuthority(p.name())));

        return authorities;
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return The hashed password from the domain user.
     */
    @Override
    public String getPassword() {
        return domainUser.getPassword();
    }

    /**
     * Returns the username used to authenticate the user.
     *
     * @return The unique username or identifier from the domain user.
     */
    @Override
    public String getUsername() {
        return domainUser.getUsername();
    }
}