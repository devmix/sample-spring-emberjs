package com.gitlab.devmix.warehouse.storage.books.impl.managers;

import com.gitlab.devmix.warehouse.core.api.web.entity.Metadata;
import com.gitlab.devmix.warehouse.core.api.web.entity.RequestParameters;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.ListOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.repository.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Sergey Grachev
 */
public final class EntityQueryUtils {

    private EntityQueryUtils() {
    }

    private static <E, R extends RequestParameters> void applyFetchRules(
            final Class<E> entityClass, final ListOperation<E, R> operation, final Root<E> e) {
        final Set<Class<?>> relationships = operation.getRelationships();
        if (isNotEmpty(relationships)) {
            final Metadata.Descriptor meta = Metadata.of(entityClass);
            for (final Map.Entry<String, Metadata.Descriptor.Attribute> entry : meta.getAttributes().entrySet()) {
                final Metadata.Descriptor.Attribute attribute = entry.getValue();
                if (attribute.isRelationshipCollection()) {
                    if (relationships.contains(attribute.getGenericType())) {
                        e.fetch(entry.getKey(), JoinType.LEFT);
                    }
                } else if (attribute.isRelationshipSingle()) {
                    if (relationships.contains(attribute.getType())) {
                        e.fetch(entry.getKey(), JoinType.LEFT);
                    }
                }
            }
        }
    }

    private static <E> CriteriaQuery<Long> createTotalQuery(
            final Class<E> entityClass, final CriteriaBuilder cb, final CriteriaQuery<E> query, final Root<E> e,
            final Predicate[] where) {

        final CriteriaQuery<Long> queryCount = cb.createQuery(Long.class);
        queryCount.from(entityClass);

        if (query.isDistinct()) {
            queryCount.select(cb.countDistinct(e));
        } else {
            queryCount.select(cb.count(e));
        }

        queryCount.where(where);

        return queryCount;
    }

    private static <E, R extends RequestParameters> Predicate[] createWhere(
            final R parameters, final CriteriaBuilder cb, final Root<E> e,
            final SearchPredicateSupplier<E, R> searchPredicateSupplier) {

        final List<Predicate> where = new ArrayList<>();

        final Predicate nonDeletedCondition = cb.equal(e.get("deleted"), false);
        where.add(nonDeletedCondition);

        if (isNotBlank(parameters.getSearch()) && searchPredicateSupplier != null) {
            where.add(searchPredicateSupplier.supply(parameters, cb, e));
        }

        return where.toArray(new Predicate[where.size()]);
    }

    static <E, R extends RequestParameters> Page<E> listOfEntities(
            final Class<E> entityClass, final EntityManager em, final ListOperation<E, R> operation, final R parameters,
            final SearchPredicateSupplier<E, R> searchPredicateSupplier) {

        final Pageable pageable = parameters.asPageable();

        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<E> query = cb.createQuery(entityClass);
        final Root<E> e = query.from(entityClass);

        final Predicate[] where = createWhere(parameters, cb, e, searchPredicateSupplier);

        query.select(e)
                .where(where)
                .orderBy(QueryUtils.toOrders(pageable.getSort(), e, cb));

        applyFetchRules(entityClass, operation, e);

        final List<E> resultList = em.createQuery(query)
                .setFirstResult(pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return PageableExecutionUtils.getPage(resultList, pageable,
                () -> em.createQuery(createTotalQuery(entityClass, cb, query, e, where)).getSingleResult());
    }

    @FunctionalInterface
    interface SearchPredicateSupplier<E, R extends RequestParameters> {
        Predicate supply(R parameters, CriteriaBuilder cb, Root<E> e);
    }
}
