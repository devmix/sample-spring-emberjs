package com.gitlab.devmix.warehouse.core.api.web.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.devmix.warehouse.core.api.web.entity.metadata.EntityMetadata;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Sergey Grachev
 */
@ToString
public final class RequestData<E> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final E entity;

    private RequestData(final E entity) {
        this.entity = entity;
    }

    public static <E> Builder<E> of(final Class<E> entityClass) {
        return new Builder<>(entityClass);
    }

    @Nullable
    public E getEntity() {
        return entity;
    }

    public static final class Builder<E> {

        private final Class<E> entityClass;
        private String id;
        private Payload payload;

        private Builder(final Class<E> entityClass) {
            this.entityClass = entityClass;
        }

        @SuppressWarnings("unchecked")
        private E readEntity() {
            final EntityMetadata.Descriptor descriptor = EntityMetadata.of(entityClass);
            final Object value = payload.get(descriptor.getEntityName());
            if (value != null) {
                if (value instanceof Map && isNotBlank(id) && isNotBlank(descriptor.getIdAttributeName())) {
                    ((Map) value).put(descriptor.getIdAttributeName(), id);
                }
                final Object normalized = readRelationship(value, entityClass);
                return MAPPER.convertValue(normalized, entityClass);
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        private Object readRelationship(final Object data, final Class entityClass) {
            final Map<String, Object> result = new HashMap<>();
            final EntityMetadata.Descriptor descriptor = EntityMetadata.of(entityClass);
            if (data instanceof Map) {
                for (final Map.Entry<String, EntityMetadata.Descriptor.Attribute> entry : descriptor.getAttributes().entrySet()) {
                    final EntityMetadata.Descriptor.Attribute attribute = entry.getValue();
                    if (!attribute.hasSetter()) {
                        continue;
                    }
                    final String name = entry.getKey();
                    final Object value = ((Map) data).get(name);
                    if (value == null) {
                        continue;
                    }
                    if (attribute.isAssociationMany()) {
                        final List<Object> collection = new ArrayList<>();
                        for (final Object entity : (Collection<Object>) value) {
                            collection.add(readRelationship(entity, attribute.getGenericType()));
                        }
                        result.put(name, collection);
                    } else if (attribute.isAssociationOne()) {
                        result.put(name, readRelationship(value, attribute.getFieldType()));
                    } else {
                        result.put(name, value);
                    }
                }
            } else {
                result.put(descriptor.getIdAttributeName(), data);
            }
            return result;
        }

        public Builder<E> id(final String id) {
            this.id = id;
            return this;
        }

        public Builder<E> payload(final Payload payload) {
            this.payload = payload;
            return this;
        }

        public RequestData<E> build() {
            return new RequestData<>(readEntity());
        }
    }
}
