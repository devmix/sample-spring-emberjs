package com.gitlab.devmix.warehouse.core.api.web.entity.jpa;

import com.gitlab.devmix.warehouse.core.api.App;
import com.gitlab.devmix.warehouse.core.api.entity.RecoverableEntity;
import com.gitlab.devmix.warehouse.core.api.web.entity.Projection;
import com.gitlab.devmix.warehouse.core.api.web.entity.Request;
import com.gitlab.devmix.warehouse.core.api.web.entity.metadata.EntityMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.util.Assert;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * @author Sergey Grachev
 */
public final class JpaQuery<E, R extends Request> {

    private static final Predicate[] EMPTY = new Predicate[0];

    private final Class<E> entityClass;
    private final Set<Class<?>> fetchEntities;
    private final Projection projection;
    private final EntityManager em;
    private final R parameters;
    private final SearchBuilder<E, R> searchBuilder;
    private final EntityMetadata.Descriptor entityMeta;
    private final CriteriaBuilder cb;

    private JpaQuery(
            final Class<E> entityClass, final Set<Class<?>> fetchEntities, final Projection projection,
            @Nullable final EntityManager em, final R parameters, final SearchBuilder<E, R> searchBuilder) {
        this.entityClass = entityClass;
        this.fetchEntities = fetchEntities;
        this.projection = projection == null ? Projection.full() : projection;
        this.em = Objects.requireNonNull(em == null ? App.getBean(EntityManager.class) : em);
        this.parameters = parameters;
        this.searchBuilder = searchBuilder;

        this.entityMeta = EntityMetadata.of(entityClass);
        this.cb = this.em.getCriteriaBuilder();
    }

    public static <E, R extends Request> JpaQuery<E, R> list(
            final Class<E> entityClass, final Set<Class<?>> fetchEntities,
            final Projection projection, @Nullable final EntityManager em, final R parameters,
            final SearchBuilder<E, R> searchBuilder) {

        return new JpaQuery<E, R>(entityClass, fetchEntities, projection, em, parameters, searchBuilder);
    }

    public static <E, R extends Request> JpaQuery<E, R> single(
            final Class<E> entityClass, final Set<Class<?>> fetchEntities,
            final Projection projection, @Nullable final EntityManager em,
            final Object o, final SearchBuilder<E, R> searchBuilder) {

        return new JpaQuery<E, R>(entityClass, fetchEntities, projection, em, null, searchBuilder);
    }

    public List<E> getList() {
        final Pageable pageable = parameters == null ? null : parameters.asPageable();
        final CriteriaQuery<E> query = cb.createQuery(entityClass);
        final Root<E> e = query.from(entityClass);
        final Predicate[] where = createWhere(e);

        query.select(e).where(where);

        if (pageable != null) {
            query.orderBy(QueryUtils.toOrders(pageable.getSort(), e, cb));
        }

        return fetch(pageable, query, e);
    }

    public Page<E> getPage() {
        final Pageable pageable = parameters == null ? null : parameters.asPageable();
        final CriteriaQuery<E> query = cb.createQuery(entityClass);
        final Root<E> e = query.from(entityClass);

        query.select(e).where(createWhere(e));

        if (pageable != null) {
            query.orderBy(QueryUtils.toOrders(pageable.getSort(), e, cb));
        }

        return getPage(fetch(pageable, query, e), pageable,
                () -> em.createQuery(total(query)).getSingleResult());
    }

    @Nullable
    public E getSingle() {
        final List<E> list = getList();
        return list.isEmpty() ? null : list.get(0);
    }

    private Predicate[] createWhere(final Root<E> e) {
        final List<Predicate> where = new ArrayList<>();

        if (RecoverableEntity.class.isAssignableFrom(entityClass)) {
            final Predicate nonDeletedCondition = cb.equal(e.get("deleted"), false);
            where.add(nonDeletedCondition);
        }

        if (searchBuilder != null) {
            final Predicate predicate = searchBuilder.build(parameters, cb, e);
            if (predicate != null) {
                where.add(predicate);
            }
        }

        return where.isEmpty() ? EMPTY : where.toArray(new Predicate[where.size()]);
    }

    private List<E> fetch(@Nullable final Pageable pageable, final CriteriaQuery<E> query, final Root<E> e) {
        final List<E> resultList;
        if (pageable == null || (pageable.getOffset() == 0 && pageable.getPageSize() == 0)) {
            applySimpleFetch(e);

            resultList = em.createQuery(query).getResultList();
        } else {
            resultList = em.createQuery(query)
                    .setFirstResult(pageable.getOffset())
                    .setMaxResults(pageable.getPageSize())
                    .getResultList();

            applyComplexFetch(resultList);
        }
        return resultList;
    }

    private void applySimpleFetch(final Root<E> e) {
        if (isEmpty(fetchEntities)) {
            return;
        }

        final EntityMetadata.Descriptor meta = EntityMetadata.of(entityClass);
        for (final Map.Entry<String, EntityMetadata.Descriptor.Attribute> entry : meta.getAttributes().entrySet()) {
            final EntityMetadata.Descriptor.Attribute attribute = entry.getValue();
            if (attribute.isAssociationMany()) {
                if (fetchEntities.contains(attribute.getGenericType())) {
                    e.fetch(entry.getKey(), JoinType.LEFT);
                }
            } else if (attribute.isAssociationOne()) {
                if (fetchEntities.contains(attribute.getFieldType())) {
                    e.fetch(entry.getKey(), JoinType.LEFT);
                }
            }
        }
    }

    private CriteriaQuery<Long> total(final CriteriaQuery<E> query) {
        final CriteriaQuery<Long> queryCount = cb.createQuery(Long.class);
        final Root<E> e = queryCount.from(entityClass);

        if (query.isDistinct()) {
            queryCount.select(cb.countDistinct(e));
        } else {
            queryCount.select(cb.count(e));
        }

        return queryCount.where(createWhere(e));
    }

    private void applyComplexFetch(final List<E> resultList) {
        if (isEmpty(fetchEntities)) {
            return;
        }

        final Set<Object> ids = resultList.stream()
                .map(entityMeta::readId).filter(Objects::nonNull).collect(toSet());

        for (final Map.Entry<String, EntityMetadata.Descriptor.Attribute> entry : entityMeta.getAttributes().entrySet()) {
            final String relationName = entry.getKey();
            final EntityMetadata.Descriptor.Attribute attribute = entry.getValue();
            if (!attribute.isAssociation() || !isInProjection(projection, relationName)) {
                continue;
            }

            final EntityMetadata.Descriptor relationMeta = EntityMetadata.of(attribute.getEntityType());
            final CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
            final Root<E> e = query.from(entityClass);
            query
                    .select(cb.array(
                            e.get(entityMeta.getIdAttributeName()),
                            e.join(relationName).get(relationMeta.getIdAttributeName())))
                    .where(e.get(entityMeta.getIdAttributeName()).in(ids));

            final List<Object[]> links = em.createQuery(query).getResultList();
            if (isEmpty(links)) {
                continue;
            }

            final Map<Object, Object> parentToRelation = new HashMap<>();
            if (fetchEntities.contains(attribute.getEntityType())) {
                final Set<Object> relationIds = new HashSet<>();
                for (final Object[] link : links) {
                    relationIds.add(link[1]);
                }
                final Map<Object, Object> relations = fetchFullRelations(relationMeta, relationIds);
                addRelation(attribute, links, parentToRelation, relations::get);
            } else {
                addRelation(attribute, links, parentToRelation, relationMeta::newInstanceWithId);
            }

            for (final E entity : resultList) {
                attribute.writeValue(entity, parentToRelation.get(entityMeta.readId(entity)));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void addRelation(final EntityMetadata.Descriptor.Attribute attribute, final List<Object[]> links,
                             final Map<Object, Object> parentToRelation, final Function<Object, Object> relationSupplier) {
        for (final Object[] link : links) {
            final Object parentId = link[0];
            final Object relation = relationSupplier.apply(link[1]);
            if (attribute.isAssociationOne()) {
                parentToRelation.put(parentId, relation);
            } else {
                ((Collection<Object>) parentToRelation.computeIfAbsent(parentId, k -> attribute.newCollection()))
                        .add(relation);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Map<Object, Object> fetchFullRelations(final EntityMetadata.Descriptor meta, final Set<Object> ids) {
        final CriteriaQuery query = cb.createQuery(meta.getEntityClass());
        final Root e = query.from(meta.getEntityClass());

        query.select(e).where(e.get(meta.getIdAttributeName()).in(ids));

        final List list = em.createQuery(query).getResultList();
        if (isEmpty(list)) {
            return emptyMap();
        }

        final Map<Object, Object> result = new HashMap<>(list.size());
        for (final Object entity : list) {
            result.put(meta.readId(entity), entity);
        }

        return result;
    }

    private static boolean isInProjection(final Projection projection, final String attributeName) {
        return !projection.isAny() && projection.find(attributeName) != null;
    }

    /**
     * Copied from PageableExecutionUtils.getPage
     */
    private static <T> Page<T> getPage(final List<T> content, @Nullable final Pageable pageable,
                                       final PageableExecutionUtils.TotalSupplier totalSupplier) {

        Assert.notNull(content, "Content must not be null!");
        Assert.notNull(totalSupplier, "TotalSupplier must not be null!");

        if (pageable == null || pageable.getOffset() == 0) {

            if (pageable == null || pageable.getPageSize() > content.size()) {
                return new PageImpl<T>(content, pageable, content.size());
            }

            return new PageImpl<T>(content, pageable, totalSupplier.get());
        }

        if (content.size() != 0 && pageable.getPageSize() > content.size()) {
            return new PageImpl<T>(content, pageable, pageable.getOffset() + content.size());
        }

        return new PageImpl<T>(content, pageable, totalSupplier.get());
    }

    @FunctionalInterface
    public interface SearchBuilder<E, R extends Request> {
        Predicate build(R parameters, CriteriaBuilder c, Root<E> e);
    }
}
