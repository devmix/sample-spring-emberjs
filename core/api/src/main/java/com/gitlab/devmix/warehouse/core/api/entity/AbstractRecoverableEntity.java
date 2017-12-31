package com.gitlab.devmix.warehouse.core.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author Sergey Grachev
 */
@Data
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
public abstract class AbstractRecoverableEntity extends AbstractIdEntity implements RecoverableEntity {

    @Column(name = "DELETED", nullable = false)
    private boolean deleted;
}
