package com.gitlab.devmix.warehouse.core.api.web.entity.handlers;

import com.gitlab.devmix.warehouse.core.api.web.entity.Request;

/**
 * @author Sergey Grachev
 */
@FunctionalInterface
public interface CreateHandler<E, R extends Request> {
    E handle(E entity);
}
