package com.gitlab.devmix.warehouse.core.api.web.entity.handlers;

import com.gitlab.devmix.warehouse.core.api.web.entity.Request;

/**
 * @author Sergey Grachev
 */
@FunctionalInterface
public interface UpdateHandler<E, R extends Request> {
    E handle(String id, E entity);
}
