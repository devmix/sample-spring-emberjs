package com.gitlab.devmix.warehouse.core.api.web.entity;

import java.util.Set;

/**
 * @author Sergey Grachev
 */
public interface Operation<E, R extends Request> {

    Type getType();

    Class<E> getEntityClass();

    Class<R> getParametersClass();

    Set<Class<?>> getIncludes();

    Projection getProjection();

    enum Type {
        LIST,
        CREATE,
        READ,
        UPDATE,
        DELETE;
    }
}
