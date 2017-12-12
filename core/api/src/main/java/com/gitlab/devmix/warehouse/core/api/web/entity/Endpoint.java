package com.gitlab.devmix.warehouse.core.api.web.entity;

import com.gitlab.devmix.warehouse.core.api.web.entity.operations.CreateOperation;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.DeleteOperation;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.ListOperation;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.ReadOperation;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.UpdateOperation;
import lombok.Value;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Sergey Grachev
 */
@Value
public class Endpoint {

    private final String rootUri;
    private final Map<Operation.Type, Operation> operations;
    private final UnsupportedRequestHandler unsupportedRequestHandler;

    public static <E> Builder<E> builder(final String rootUri) {
        return new Builder<>(rootUri);
    }

    public static <E, R extends RestQuery> ListOperation.ListOperationBuilder<E, R> list(final Class<E> entityClass) {
        return ListOperation.<E, R>builder().entityClass(entityClass);
    }

    public static <E> CreateOperation.CreateOperationBuilder<E> create(final Class<E> entityClass) {
        return CreateOperation.<E>builder().entityClass(entityClass);
    }

    public static <E> ReadOperation.ReadOperationBuilder<E> read(final Class<E> entityClass) {
        return ReadOperation.<E>builder().entityClass(entityClass);
    }

    public static <E> UpdateOperation.UpdateOperationBuilder<E> update(final Class<E> entityClass) {
        return UpdateOperation.<E>builder().entityClass(entityClass);
    }

    public static <E> DeleteOperation.DeleteOperationBuilder<E> delete(final Class<E> entityClass) {
        return DeleteOperation.<E>builder().entityClass(entityClass);
    }

    @Nullable
    public Operation findOperation(final Operation.Type type) {
        return operations.get(type);
    }

    public static class Builder<E> {

        private final String rootUri;
        private final Map<Operation.Type, Operation> operations = new HashMap<>();
        private UnsupportedRequestHandler unsupportedRequestHandler;

        Builder(final String rootUri) {
            this.rootUri = Objects.requireNonNull(rootUri);
        }

        public Builder<E> add(final Operation operation) {
            this.operations.put(operation.getType(), operation);
            return this;
        }

        public Builder<E> unsupportedRequestHandler(final UnsupportedRequestHandler handler) {
            this.unsupportedRequestHandler = handler;
            return this;
        }

        public Endpoint build() {
            return new Endpoint(rootUri, operations, unsupportedRequestHandler);
        }
    }

    @FunctionalInterface
    public interface UnsupportedRequestHandler {
        void handle(HttpServletRequest request);
    }
}
