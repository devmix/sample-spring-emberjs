package com.gitlab.devmix.warehouse.core.api.web.entity;

import lombok.Value;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;

import javax.annotation.Nullable;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * @author Sergey Grachev
 */
public final class ResponseData<E> extends HashMap<String, Object> {

    private static final long serialVersionUID = -550468192552101123L;

    private static final Payload EMPTY = new Payload();
    private static final String META_SECTION = "meta";

    private ResponseData() {
        put(META_SECTION, new HashMap<>());
    }

    private void putMeta(final String key, final Object value) {
        @SuppressWarnings("unchecked")
        Map<String, Object> meta = (Map<String, Object>) get(META_SECTION);
        if (meta == null) {
            meta = new HashMap<>();
            put(META_SECTION, meta);
        }
        meta.put(key, value);
    }

    public static <E> Builder<E> of(final Class<E> entityClass) {
        return new ResponseData.Builder<>(entityClass);
    }

    public static <E> ResponseData<E> delete(final Class<E> entityClass) {
        return new ResponseData<>();
    }

    public static final class Builder<E> {
        private final Class<E> entityClass;
        private Set<Class> inline;
        private Set<Class> include;
        private List<E> entities;
        private PageInfo page;
        private Projection projection = Projection.full();

        private Builder(final Class<E> entityClass) {
            this.entityClass = entityClass;
        }

        public Builder<E> add(final Iterable<E> iterable) {
            ensureEntities();

            for (final E e : iterable) {
                entities.add(e);
            }

            return this;
        }

        public Builder<E> add(final Page<E> page) {
            add((Iterable<E>) page);
            this.page = new PageInfo(page.getSize(), page.getNumber(), page.getTotalElements());
            return this;
        }

        public Builder<E> add(final E entity) {
            ensureEntities().add(Objects.requireNonNull(entity));
            return this;
        }

        public Builder<E> addIfNotNull(@Nullable final E entity) {
            if (entity == null) {
                return this;
            }
            return add(entity);
        }

        public Builder<E> inline(final Class entityClass) {
            if (this.inline == null) {
                this.inline = new HashSet<>();
            }
            this.inline.add(entityClass);
            return this;
        }

        public Builder<E> include(final Class entityClass) {
            if (this.include == null) {
                this.include = new HashSet<>();
            }
            this.include.add(entityClass);
            return this;
        }

        public Builder<E> include(@Nullable final Collection<Class> include) {
            if (include == null) {
                return this;
            }
            if (this.include == null) {
                this.include = new HashSet<>();
            }
            this.include.addAll(include);
            return this;
        }

        public Builder<E> projection(final String... properties) {
            this.projection = Projection.of(properties);
            return this;
        }

        public Builder<E> projection(@Nullable final Projection projection) {
            this.projection = projection == null ? Projection.full() : projection;
            return this;
        }

        public ResponseData<E> single() {
            final Metadata.Descriptor descriptor = Metadata.of(entityClass);
            final ResponseData<E> payload = new ResponseData<>();
            payload.put(descriptor.getEntityName(), entities == null || entities.isEmpty() ? EMPTY
                    : addEntity(entities.get(0), descriptor, payload, projection));
            putPageInformation(payload);
            return payload;
        }

        public ResponseData<E> list() {
            final Metadata.Descriptor descriptor = Metadata.of(entityClass);
            final ResponseData<E> payload = new ResponseData<>();
            payload.put(descriptor.getEntityName(), entities == null || entities.isEmpty() ? emptyList()
                    : entities.stream().map(e -> addEntity(e, descriptor, payload, projection)).collect(toList()));
            putPageInformation(payload);
            return payload;
        }

        private void putPageInformation(final ResponseData<E> payload) {
            if (page == null) {
                return;
            }
            payload.putMeta("page", page);
        }

        private List<E> ensureEntities() {
            if (entities == null) {
                entities = new ArrayList<>();
            }
            return entities;
        }

        @SuppressWarnings("ConstantConditions")
        private Payload addEntity(final Object entity, final Metadata.Descriptor descriptor, final ResponseData<E> responseData,
                                  final ProjectionProperty projectionProperty) {

            final Payload result = new Payload();
            for (final Map.Entry<String, Metadata.Descriptor.Attribute> entry : descriptor.getAttributes().entrySet()) {
                final String name = entry.getKey();
                final Metadata.Descriptor.Attribute attribute = entry.getValue();
                if (!attribute.hasGetter()) {
                    continue;
                }

                final ProjectionProperty attrProjection = projectionProperty.find(name);
                if (attrProjection == null) {
                    continue;
                }

                final Object value = attribute.readValue(entity);
                if (value == null) {
                    continue;
                }

                if (attribute.isRelationshipCollection()) {
                    final List<Object> list = new ArrayList<>();
                    for (final Object o : (Collection) value) {
                        list.add(addRelationship(o, responseData, attrProjection));
                    }
                    result.put(name, list);
                } else if (attribute.isRelationshipSingle()) {
                    result.put(name, addRelationship(value, responseData, attrProjection));
                } else if (value != null) {
                    result.put(name, value);
                }
            }

            return result;
        }

        private Object addRelationship(final Object e, final ResponseData<E> responseData, final ProjectionProperty projectionProperty) {
            final Class entityClass = findActualClass(e);
            final Metadata.Descriptor descriptor = Metadata.of(entityClass);
            final Payload entity = addEntity(e, descriptor, responseData, projectionProperty);
            if (inline != null && inline.contains(entityClass)) {
                return entity;
            }

            if (include != null && include.contains(entityClass)) {
                ensureEntitiesOf(entityClass, responseData).add(entity);
            }

            return descriptor.getId(e);
        }

        private Class findActualClass(final Object o) {
            final Class actualClass = Hibernate.getClass(o);
            if (Proxy.isProxyClass(actualClass)) {
                for (final Class i : o.getClass().getInterfaces()) {
                    if (Metadata.of(i).isEntity()) {
                        return i;
                    }
                }
            }
            return actualClass;
        }

        private List<Payload> ensureEntitiesOf(final Class<?> entityClass, final ResponseData<E> responseData) {
            final String name = Objects.requireNonNull(Metadata.of(entityClass).getEntityName());
            @SuppressWarnings("unchecked")
            List<Payload> entities = (List<Payload>) responseData.get(name);
            if (entities == null) {
                entities = new ArrayList<>();
                responseData.put(name, entities);
            }
            return entities;
        }
    }

    @Value
    private static final class PageInfo {
        private int size;
        private int page;
        private long total;
    }
}
