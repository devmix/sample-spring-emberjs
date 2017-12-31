package com.gitlab.devmix.warehouse.core.api.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Sergey Grachev
 */
public class SysUserDetails extends User {

    private static final long serialVersionUID = -4422684423026922063L;

    @Getter
    private final UUID id;

    public SysUserDetails(final String username, final String password, final boolean enabled,
                          final boolean accountNonExpired, final boolean credentialsNonExpired,
                          final boolean accountNonLocked, final Collection<? extends GrantedAuthority> authorities,
                          final UUID id) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
    }
}
