package es.dimecresalessis.scoutbase.infrastructure.security.model;

import es.dimecresalessis.scoutbase.domain.user.model.RoleEnum;
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

    private final User domainUser;
    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return domainUser.getPassword();
    }

    @Override
    public String getUsername() {
        return domainUser.getUsername();
    }
}