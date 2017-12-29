package com.gitlab.devmix.warehouse.core.api.web.entity.handlers;

import com.gitlab.devmix.warehouse.core.api.web.entity.Operation;
import com.gitlab.devmix.warehouse.core.api.web.entity.Request;

/**
 * @author Sergey Grachev
 */
@FunctionalInterface
public interface ReadHandler<E, R extends Request> {
    E handle(Operation<E, R> operation, String id);
}
