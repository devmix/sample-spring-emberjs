package com.gitlab.devmix.warehouse.core.api.web.entity.handlers;

import com.gitlab.devmix.warehouse.core.api.web.entity.Request;
import com.gitlab.devmix.warehouse.core.api.web.entity.jpa.JpaReadHandler;
import com.gitlab.devmix.warehouse.core.api.web.entity.jpa.JpaReadListHandler;

/**
 * @author Sergey Grachev
 */

public final class OperationHandlers {

    private OperationHandlers() {
    }

    public static <E, R extends Request> JpaReadListHandler.JpaReadListHandlerBuilder<E, R> jpaReadList(
            final Class<E> entityClass) {
        return JpaReadListHandler.builder();
    }

    public static <E, R extends Request> JpaReadHandler.JpaReadHandlerBuilder<E, R> jpaRead(
            final Class<E> entityClass) {
        return JpaReadHandler.builder();
    }
}
