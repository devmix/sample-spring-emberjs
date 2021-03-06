package com.gitlab.devmix.warehouse.core.api.web.entity.operations;

import com.gitlab.devmix.warehouse.core.api.web.entity.Operation;
import com.gitlab.devmix.warehouse.core.api.web.entity.Projection;
import com.gitlab.devmix.warehouse.core.api.web.entity.Request;
import com.gitlab.devmix.warehouse.core.api.web.entity.handlers.UpdateHandler;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

/**
 * @author Sergey Grachev
 */
@Builder
@Value
public final class UpdateOperation<E, R extends Request> implements Operation<E, R> {

    private final Class<E> entityClass;
    private final Class<R> parametersClass;
    private final UpdateHandler<E, R> handler;
    private final Projection projection;
    private final Set<Class<?>> includes;

    @Override
    public Type getType() {
        return Type.UPDATE;
    }
}
