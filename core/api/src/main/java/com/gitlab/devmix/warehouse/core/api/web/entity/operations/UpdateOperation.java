package com.gitlab.devmix.warehouse.core.api.web.entity.operations;

import com.gitlab.devmix.warehouse.core.api.web.entity.Operation;
import lombok.Builder;
import lombok.Value;

/**
 * @author Sergey Grachev
 */
@Builder
@Value
public final class UpdateOperation<E> implements Operation {

    private final Class<E> entityClass;
    private final Run<E> run;

    @Override
    public Type getType() {
        return Type.UPDATE;
    }

    @FunctionalInterface
    public interface Run<E> {
        E handle(String id, E entity);
    }
}
