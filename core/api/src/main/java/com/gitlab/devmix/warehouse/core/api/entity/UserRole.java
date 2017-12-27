package com.gitlab.devmix.warehouse.core.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Sergey Grachev
 */
@Entity(name = UserRole.ENTITY)
@Table(name = "SYS_USER_ROLE")
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"user"})
@ToString(callSuper = true)
public class UserRole extends AbstractEntity {

    public static final String ENTITY = "sysUserRole";

    @Column(name = "PASSWORD", length = Byte.MAX_VALUE)
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false, updatable = false)
    private User user;
}
