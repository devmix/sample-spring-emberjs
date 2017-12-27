package com.gitlab.devmix.warehouse.core.impl.services;

import com.gitlab.devmix.warehouse.core.api.entity.User;
import com.gitlab.devmix.warehouse.core.api.entity.UserRole;
import com.gitlab.devmix.warehouse.core.api.repositories.UserRepository;
import com.gitlab.devmix.warehouse.core.api.services.DbUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * @author Sergey Grachev
 */
@Service
public class DbUserDetailsServiceImpl implements DbUserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbUserDetailsServiceImpl.class);

    @Inject
    private UserRepository repository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        LOGGER.info("Try to find user {}", username);

        final User user = repository.findByIdentity(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getIdentity(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                grantedAuthoritiesOf(user));
    }

    private List<GrantedAuthority> grantedAuthoritiesOf(final User user) {
        final Set<UserRole> roles = user.getRoles();
        return isEmpty(roles) ? emptyList() : roles.stream()
                .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.getType()))
                .collect(toList());
    }
}