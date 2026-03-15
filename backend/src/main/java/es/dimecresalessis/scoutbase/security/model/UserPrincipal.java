package es.dimecresalessis.scoutbase.security.model;

import es.dimecresalessis.scoutbase.user.domain.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

    private final User domainUser;

    public UserPrincipal(User domainUser) {
        this.domainUser = domainUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return domainUser.getRole().getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toList());
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