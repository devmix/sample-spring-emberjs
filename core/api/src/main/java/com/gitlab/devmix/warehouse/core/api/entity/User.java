package com.gitlab.devmix.warehouse.core.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * @author Sergey Grachev
 */
@Entity(name = User.ENTITY)
@Table(name = "SYS_USER")
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"roles"})
@ToString(callSuper = true)
public class User extends AbstractEntity {

    public static final String ENTITY = "coreUser";

    @Column(name = "IDENTITY")
    private String identity;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ENABLED")
    private boolean enabled = true;

    @Column(name = "ACCOUNT_NON_EXPIRED")
    private boolean accountNonExpired = true;

    @Column(name = "CREDENTIALS_NON_EXPIRED")
    private boolean credentialsNonExpired = true;

    @Column(name = "ACCOUNT_NON_LOCKED")
    private boolean accountNonLocked = true;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> roles;
}
