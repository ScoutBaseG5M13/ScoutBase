package es.dimecresalessis.scoutbase.infrastructure.security.model;

import es.dimecresalessis.scoutbase.domain.user.model.Role;
import es.dimecresalessis.scoutbase.domain.user.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {

    private final User domainUser;

    public UserPrincipal(User domainUser) {
        this.domainUser = domainUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // save Roles
        authorities.add(new SimpleGrantedAuthority(domainUser.getRole()));

        // save Permissions
        Role.fromName(domainUser.getRole()).getPermissions()
                .forEach(p -> authorities.add(new SimpleGrantedAuthority(p.name())));

        return authorities;
    }

    @Override
    public String getPassword() {
        return domainUser.getPassword(); // Assuming your Domain object has this
    }

    @Override
    public String getUsername() {
        return domainUser.getUsername();
    }
}