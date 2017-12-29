package com.gitlab.devmix.warehouse.core.api.web.entity.handlers;

import com.gitlab.devmix.warehouse.core.api.web.entity.Request;

/**
 * @author Sergey Grachev
 */
@FunctionalInterface
public interface DeleteHandler<E, R extends Request> {
    void handle(String id);
}
