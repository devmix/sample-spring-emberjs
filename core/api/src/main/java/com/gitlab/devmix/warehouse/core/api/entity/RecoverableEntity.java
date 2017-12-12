package com.gitlab.devmix.warehouse.core.api.entity;

/**
 * @author Sergey Grachev
 */
public interface RecoverableEntity {

    boolean isDeleted();

    void setDeleted(boolean deleted);
}
