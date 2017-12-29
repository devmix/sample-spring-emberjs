package com.gitlab.devmix.warehouse.core.api.web.entity.operations;

import com.gitlab.devmix.warehouse.core.api.web.entity.Operation;
import com.gitlab.devmix.warehouse.core.api.web.entity.Projection;
import com.gitlab.devmix.warehouse.core.api.web.entity.Request;
import com.gitlab.devmix.warehouse.core.api.web.entity.handlers.ReadHandler;
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
public final class ReadOperation<E, R extends Request> implements Operation<E, R> {

    private final Class<E> entityClass;
    private final Class<R> parametersClass;
    private final Set<Class<?>> includes;
    private final Projection projection;
    private final ReadHandler<E, R> handler;

    @Override
    public Type getType() {
        return Type.READ;
    }

    public static final class ReadOperationBuilder<E, R extends Request> {
        public ReadOperation.ReadOperationBuilder<E, R> include(final Class<?>... classes) {
            if (includes == null) {
                includes = new HashSet<>();
            }
            Collections.addAll(includes, classes);
            return this;
        }
    }
}
