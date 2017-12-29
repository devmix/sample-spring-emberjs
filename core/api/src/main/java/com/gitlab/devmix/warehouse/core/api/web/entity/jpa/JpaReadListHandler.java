package com.gitlab.devmix.warehouse.core.api.web.entity.jpa;

import com.gitlab.devmix.warehouse.core.api.web.entity.Operation;
import com.gitlab.devmix.warehouse.core.api.web.entity.Request;
import com.gitlab.devmix.warehouse.core.api.web.entity.handlers.ReadListHandler;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import java.util.function.Supplier;

import static com.gitlab.devmix.warehouse.core.api.web.entity.jpa.JpaQuery.list;

/**
 * @author Sergey Grachev
 */
@Value
@Builder
public class JpaReadListHandler<E, R extends Request> implements ReadListHandler<E, R> {

    private Supplier<EntityManager> entityManager;
    private JpaQuery.SearchBuilder<E, R> searchPredicate;

    @Override
    public Page<E> handle(final Operation<E, R> operation, final R parameters) {
        return list(
                operation.getEntityClass(),
                operation.getIncludes(),
                operation.getProjection(),
                entityManager == null ? null : entityManager.get(),
                parameters,
                searchPredicate
        ).getPage();
    }
}
