package com.gitlab.devmix.warehouse.core.api.web.entity.operations;

import com.gitlab.devmix.warehouse.core.api.web.entity.Operation;
import com.gitlab.devmix.warehouse.core.api.web.entity.Projection;
import com.gitlab.devmix.warehouse.core.api.web.entity.RequestParameters;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.springframework.data.domain.Page;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * @author Sergey Grachev
 */
@Builder
@Value
public final class ListOperation<E, R extends RequestParameters> implements Operation {

    private final Class<E> entityClass;

    private final Class<R> parametersClass;

    @Singular
    private final Set<Class<?>> relationships;

    private final Run<E, R> run;

    @Nullable
    private final Projection projection;

    @Override
    public Type getType() {
        return Type.LIST;
    }

    @FunctionalInterface
    public interface Run<E, R extends RequestParameters> {
        Page<E> handle(ListOperation<E, R> operation, R parameters);
    }

    public static final class ListOperationBuilder<E, R extends RequestParameters> {

        public ListOperationBuilder<E, R> projection(final String... properties) {
            projection = Projection.of(properties);
            return this;
        }
    }
}
