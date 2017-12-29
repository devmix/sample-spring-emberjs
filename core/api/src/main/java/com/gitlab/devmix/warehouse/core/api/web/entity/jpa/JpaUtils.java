package com.gitlab.devmix.warehouse.core.api.web.entity.jpa;

import com.gitlab.devmix.warehouse.core.api.entity.BaseEntity;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singletonList;

/**
 * @author Sergey Grachev
 */
public final class JpaUtils {

    private JpaUtils() {
    }

    public static <E extends BaseEntity> List<E> getReferenceList(@Nullable final Collection<E> entities,
                                                                  final Class<E> entityClass, final EntityManager em) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }

        if (entities.size() == 1) {
            return singletonList(em.getReference(entityClass, entities.iterator().next().getId()));
        } else {
            final List<E> result = new ArrayList<>(entities.size());
            for (final E e : entities) {
                result.add(em.getReference(entityClass, e.getId()));
            }
            return result;
        }
    }

    public static <E extends BaseEntity> Set<E> getReferenceSet(@Nullable final Collection<E> entities,
                                                                final Class<E> entityClass, final EntityManager em) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }

        if (entities.size() == 1) {
            final Set<E> set = new HashSet<>();
            set.add(em.getReference(entityClass, entities.iterator().next().getId()));
            return set;
        } else {
            final Set<E> result = new HashSet<>(entities.size());
            for (final E e : entities) {
                result.add(em.getReference(entityClass, e.getId()));
            }
            return result;
        }
    }
}
