package com.gitlab.devmix.warehouse.core.api.web.entity.operations;

import com.gitlab.devmix.warehouse.core.api.web.entity.Operation;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Set;

/**
 * @author Sergey Grachev
 */
@Builder
@Value
public final class ReadOperation<E> implements Operation {

    private final Class<E> entityClass;
    private final Run<E> run;
    @Singular
    private final Set<Class<?>> relationships;

    @Override
    public Type getType() {
        return Type.READ;
    }

    @FunctionalInterface
    public interface Run<E> {
        E handle(String id);
    }
}
