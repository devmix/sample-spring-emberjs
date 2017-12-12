package com.gitlab.devmix.warehouse.core.api.web.entity;

/**
 * @author Sergey Grachev
 */
public interface Operation {

    Type getType();

    enum Type {
        LIST,
        CREATE,
        READ,
        UPDATE,
        DELETE;
    }
}
