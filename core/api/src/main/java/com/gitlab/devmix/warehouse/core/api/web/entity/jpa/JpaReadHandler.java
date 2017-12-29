package com.gitlab.devmix.warehouse.core.api.web.entity.jpa;

import com.gitlab.devmix.warehouse.core.api.web.entity.Metadata;
import com.gitlab.devmix.warehouse.core.api.web.entity.Operation;
import com.gitlab.devmix.warehouse.core.api.web.entity.Request;
import com.gitlab.devmix.warehouse.core.api.web.entity.handlers.ReadHandler;
import lombok.Builder;
import lombok.Value;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Sergey Grachev
 */
@Value
@Builder
public class JpaReadHandler<E, R extends Request> implements ReadHandler<E, R> {

    private Supplier<EntityManager> entityManager;
    private JpaQuery.SearchBuilder<E, R> searchPredicate;
    private Function<String, Object> id;

    @Override
    public E handle(final Operation<E, R> operation, final String entityId) {
        return JpaQuery.<E, R>single(
                operation.getEntityClass(),
                operation.getIncludes(),
                operation.getProjection(),
                entityManager == null ? null : entityManager.get(),
                null,
                (parameters, c, e) -> mergePredicates(operation, entityId, parameters, c, e)
        ).getSingle();
    }

    private Predicate mergePredicates(final Operation<E, R> operation, final String entityId, final R parameters,
                                      final CriteriaBuilder c, final Root<E> e) {
        final Metadata.Descriptor meta = Metadata.of(operation.getEntityClass());
        final Predicate predicate = searchPredicate != null ? searchPredicate.build(parameters, c, e) : null;
        final Predicate byId = c.equal(e.get(meta.getIdAttributeName()), id == null ? entityId : id.apply(entityId));
        return predicate == null ? byId : c.and(predicate, byId);
    }
}
