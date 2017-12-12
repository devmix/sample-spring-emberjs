package com.gitlab.devmix.warehouse.core.api.web.entity.operations;

import com.gitlab.devmix.warehouse.core.api.web.entity.Operation;
import com.gitlab.devmix.warehouse.core.api.web.entity.RestQuery;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.Set;

/**
 * @author Sergey Grachev
 */
@Builder
@Value
public final class ListOperation<E, R extends RestQuery> implements Operation {

    private final Class<E> entityClass;
    private final Class<R> queryClass;
    @Singular
    private final Set<Class<?>> relationships;
    private final Run run;

    @Override
    public Type getType() {
        return Type.LIST;
    }

    @FunctionalInterface
    public interface Run<E, R extends RestQuery> {
        Page<E> handle(R query);
    }
}
