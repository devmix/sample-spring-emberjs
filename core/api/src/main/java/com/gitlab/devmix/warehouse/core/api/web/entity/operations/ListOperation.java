package com.gitlab.devmix.warehouse.core.api.web.entity.operations;

import com.gitlab.devmix.warehouse.core.api.web.entity.Operation;
import com.gitlab.devmix.warehouse.core.api.web.entity.Projection;
import com.gitlab.devmix.warehouse.core.api.web.entity.Request;
import com.gitlab.devmix.warehouse.core.api.web.entity.handlers.ReadListHandler;
import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Sergey Grachev
 */
@Builder
@Value
public final class ListOperation<E, R extends Request> implements Operation<E, R> {

    private final Class<E> entityClass;
    private final Class<R> parametersClass;
    private final Projection projection;
    private final ReadListHandler<E, R> handler;
    private final Set<Class<?>> includes;

    @Override
    public Type getType() {
        return Type.LIST;
    }

    public static final class ListOperationBuilder<E, R extends Request> {
        public ListOperationBuilder<E, R> projection(final String... properties) {
            projection = properties.length == 0 ? Projection.full() : Projection.of(properties);
            return this;
        }

        public ListOperationBuilder<E, R> include(final Class<?>... classes) {
            if (includes == null) {
                includes = new HashSet<>();
            }
            Collections.addAll(includes, classes);
            return this;
        }
    }
}
