package com.gitlab.devmix.warehouse.core.api.web.entity.handlers;

import com.gitlab.devmix.warehouse.core.api.web.entity.Operation;
import com.gitlab.devmix.warehouse.core.api.web.entity.Request;
import org.springframework.data.domain.Page;

/**
 * @author Sergey Grachev
 */
@FunctionalInterface
public interface ReadListHandler<E, R extends Request> {

    Page<E> handle(Operation<E, R> operation, R parameters);
}
