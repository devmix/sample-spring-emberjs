package com.gitlab.devmix.warehouse.core.impl.services.export;

import com.gitlab.devmix.warehouse.core.api.web.entity.metadata.EntityMetadata;
import lombok.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * TODO SG Hazelcast/Infinispan
 *
 * @author Sergey Grachev
 */
@Component
public class ExportCache {

    @Inject
    private EntityManager em;

    private final Map<Class<Object>, Map<Object, Object>> fetchedEntities = new HashMap<>();
    private final Map<EntityToAssociationKey, Map<Object, Set<Object>>> entityToAssociation = new HashMap<>();
    private final Map<Class<Object>, Set<Object>> storedEntities = new HashMap<>();

    public Collection<Object> loadAssociations(
            final EntityMetadata.Descriptor entityMeta, final EntityMetadata.Descriptor associationMeta,
            final String associationName, final Object entityId, final Set<Object> prefetchIds,
            final Supplier<? extends Collection<Object>> containerFactory) {

        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final Class<Object> entityClass = entityMeta.getEntityClass();
        final Class<Object> associationClass = associationMeta.getEntityClass();
        final Map<Object, Set<Object>> ids = entityToAssociation
                .computeIfAbsent(new EntityToAssociationKey(entityClass, associationClass), k -> new HashMap<>());
        final Map<Object, Object> entities = fetchedEntities.computeIfAbsent(associationClass, c -> new HashMap<>());

        final Set<Object> missed = new HashSet<>();

        if (ids.containsKey(entityId)) {
            for (final Object associationId : ids.get(entityId)) {
                if (!entities.containsKey(associationId)) {
                    missed.add(associationId);
                }
            }
        } else {
            final List<Object[]> links = fetchLinks(entityMeta, associationMeta, associationName, prefetchIds, cb);
            if (links.isEmpty()) {
                return Collections.emptyList();
            }

            for (final Object[] link : links) {
                final Object associationId = link[1];
                ids.computeIfAbsent(link[0], k -> new HashSet<>()).add(associationId);
                if (!entities.containsKey(associationId)) {
                    missed.add(associationId);
                }
            }
        }

        if (isNotEmpty(missed)) {
            fetchEntities(associationMeta, entities, missed, cb);
        }

        final Set<Object> associationIds = ids.get(entityId);
        if (isNotEmpty(associationIds)) {
            final Collection<Object> result = containerFactory.get();
            for (final Object id : associationIds) {
                final Object e = entities.get(id);
                if (e != null) {
                    result.add(e);
                }
            }
            return result;
        }

        return Collections.emptyList();
    }

    private void fetchEntities(final EntityMetadata.Descriptor meta, final Map<Object, Object> entities,
                               final Set<Object> missed, final CriteriaBuilder cb) {
        final Class<Object> entityClass = meta.getEntityClass();
        final CriteriaQuery<Object> q = cb.createQuery(entityClass);
        final Root<Object> e = q.from(entityClass);

        q.select(e).where(e.get(meta.getIdAttributeName()).in(missed));

        for (final Object entity : em.createQuery(q).getResultList()) {
            entities.put(meta.readId(entity), entity);
        }
    }

    private List<Object[]> fetchLinks(final EntityMetadata.Descriptor entityMeta, final EntityMetadata.Descriptor associationMeta,
                                      final String associationName, final Set<Object> prefetchIds, final CriteriaBuilder cb) {

        final CriteriaQuery<Object[]> q = cb.createQuery(Object[].class);
        final Root<?> e = q.from(entityMeta.getEntityClass());
        final Path<Object> entityId = e.get(entityMeta.getIdAttributeName());
        final Path<Object> associationId = e.join(associationName).get(associationMeta.getIdAttributeName());

        q.select(cb.array(entityId, associationId))
                .where(e.get(entityMeta.getIdAttributeName()).in(prefetchIds));

        return em.createQuery(q).getResultList();
    }

    public void clear() {
        storedEntities.clear();
        clearEntities();
    }

    public void clearEntities() {
        fetchedEntities.clear();
        entityToAssociation.clear();
    }

    public boolean isStored(final Class<Object> entityClass, final Object entityId) {
        return storedEntities.computeIfAbsent(entityClass, c -> new HashSet<>()).contains(entityId);
    }

    public void addToStored(final Class<Object> entityClass, final Object entityId) {
        storedEntities.computeIfAbsent(entityClass, c -> new HashSet<>()).add(entityId);
    }

    @Value
    private static final class EntityToAssociationKey {
        private Class<Object> entityClass;
        private Class<Object> associationClass;
    }
}
