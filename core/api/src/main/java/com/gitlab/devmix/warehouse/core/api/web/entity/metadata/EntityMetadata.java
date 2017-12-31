package com.gitlab.devmix.warehouse.core.api.web.entity.metadata;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;
import org.scannotation.AnnotationDB;
import org.scannotation.ClasspathUrlFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.collections4.IterableUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Sergey Grachev
 */
public final class EntityMetadata {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityMetadata.class);
    private static final Map<Class<?>, Descriptor> DESCRIPTOR_BY_CLASS = new ConcurrentHashMap<>();
    private static final Map<String, Descriptor> DESCRIPTOR_BY_NAME = new ConcurrentHashMap<>();
    private static final Set<String> IGNORE = new HashSet<>();

    static {
        IGNORE.add("class");
    }

    private EntityMetadata() {
    }

    public static Descriptor of(final Class entityClass) {
        return DESCRIPTOR_BY_CLASS.computeIfAbsent(entityClass, EntityMetadata::readMetadata);
    }

    @Nullable
    public static Descriptor of(final String entityName) {
        return DESCRIPTOR_BY_NAME.get(entityName);
    }

    public static void scanClassBase(final List<Class<?>> classes) {
        final AnnotationDB db = new AnnotationDB();
        try {
            for (final Class clazz : classes) {
                db.scanArchives(ClasspathUrlFinder.findClassBase(clazz));
                scanClassNames(db.getAnnotationIndex().get(Entity.class.getName()));
            }
        } catch (final IOException | ClassNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private static void scanClassNames(final Set<String> names) throws ClassNotFoundException {
        if (isEmpty(names)) {
            return;
        }

        for (final String name : names) {
            DESCRIPTOR_BY_CLASS.computeIfAbsent(Class.forName(name), entityClass -> {
                final Descriptor descriptor = readMetadata(entityClass);
                DESCRIPTOR_BY_NAME.put(descriptor.getEntityName(), descriptor);
                return descriptor;
            });
        }
    }


    private static Descriptor readMetadata(final Class entityClass) {
        final String entityName = findName(entityClass);
        if (isBlank(entityName)) {
            throw new IllegalArgumentException("No entity name for class " + entityClass);
        }

        final PropertyDescriptor[] properties;
        try {
            properties = Introspector.getBeanInfo(entityClass).getPropertyDescriptors();
        } catch (final IntrospectionException e) {
            throw new IllegalArgumentException(e);
        }

        String idAttributeName = null;
        final Map<String, Descriptor.Attribute> fields = new HashMap<>(properties.length);
        boolean withAssociations = false;
        for (final PropertyDescriptor property : properties) {
            final String propertyName = property.getName();
            if (IGNORE.contains(propertyName)) {
                continue;
            }

            final Method getter = property.getReadMethod();
            final Method setter = property.getWriteMethod();
            final Field field = findField(entityClass, propertyName);
            final Class type = property.getPropertyType();

            final boolean isMany = hasAnyAnnotation(field, getter, ManyToMany.class, OneToMany.class);
            final boolean isOne = hasAnyAnnotation(field, getter, ManyToOne.class, OneToOne.class);
            final boolean isId = hasAnyAnnotation(field, getter, javax.persistence.Id.class);

            final Class genericType = isMany && getter != null
                    ? (Class) ((ParameterizedType) getter.getGenericReturnType()).getActualTypeArguments()[0] : null;

            if (isId) {
                idAttributeName = propertyName;
            }

            if (isOne || isMany) {
                withAssociations = true;
            }

            fields.put(propertyName, new Descriptor.Attribute(
                    type, genericType, isMany, isOne, isId, getter, setter));
        }

        return new Descriptor(entityClass, entityName, fields, idAttributeName, withAssociations);
    }

    @SuppressWarnings("unchecked")
    private static boolean hasAnyAnnotation(final Field field, final Method method, final Class... annotations) {
        if (method != null) {
            for (final Class annotation : annotations) {
                if (method.getDeclaredAnnotation(annotation) != null) {
                    return true;
                }
            }
        }
        if (field != null) {
            for (final Class<? extends Annotation> annotation : annotations) {
                if (field.getDeclaredAnnotation(annotation) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Nullable
    private static Field findField(final Class<?> clazz, final String name) {
        // TODO SG cache
        Class<?> next = clazz;
        while (Object.class != next && next != null) {
            try {
                return next.getDeclaredField(name);
            } catch (final NoSuchFieldException e) {
                next = next.getSuperclass();
            }
        }
        return null;
    }

    private static String findName(final Class<?> entityClass) {
        // TODO SG cache
        Class<?> next = entityClass;
        while (Object.class != next && next != null) {
            final Entity annotationEntity = next.getDeclaredAnnotation(Entity.class);
            if (annotationEntity != null) {
                final String name = annotationEntity.name();
                if (isNotBlank(name)) {
                    return name;
                }
            }
            next = next.getSuperclass();
        }
        return entityClass.getSimpleName();
    }

    @Value
    public static final class Descriptor {

        private final Class<Object> entityClass;
        private final String entityName;
        private final Map<String, Attribute> attributes;
        @Nullable
        private final String idAttributeName;
        private final boolean withAssociations;

        @Nullable
        public Attribute getIdAttribute() {
            return isNotBlank(idAttributeName) ? attributes.get(idAttributeName) : null;
        }

        @SuppressWarnings("unchecked")
        @Nullable
        public <T> T readId(final Object source) {
            if (isNotBlank(idAttributeName)) {
                final Attribute id = attributes.get(idAttributeName);
                if (id != null) {
                    return (T) id.readValue(source);
                }
            }
            return null;
        }

        public void writeId(final Object target, final Object value) {
            if (isNotBlank(idAttributeName)) {
                final Attribute id = attributes.get(idAttributeName);
                if (id != null) {
                    id.writeValue(target, value);
                }
            }
        }

        public boolean isEntity() {
            return entityName != null;
        }

        public Object newInstance() {
            try {
                return entityClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new UnsupportedOperationException(e);
            }
        }

        public Object newInstanceWithId(final Object id) {
            final Object result = newInstance();
            writeId(result, id);
            return result;
        }

        public Set<Object> readId(final Collection<?> list) {
            final Attribute attribute = getIdAttribute();
            return attribute == null ? emptySet() : list.stream().map(attribute::readValue).collect(toSet());
        }

        @Value
        public static class Attribute {

            private final Class<Object> fieldType;

            @Nullable
            private final Class<Object> genericType;

            private final boolean isAssociationMany;

            private final boolean isAssociationOne;

            private final boolean isId;

            @Nullable
            @Getter(AccessLevel.NONE)
            private final Method getter;

            @Nullable
            @Getter(AccessLevel.NONE)
            private final Method setter;

            public boolean hasGetter() {
                return getter != null;
            }

            public boolean hasSetter() {
                return setter != null;
            }

            public Object readValue(final Object o) {
                try {
                    return getter != null ? getter.invoke(o) : null;
                } catch (IllegalAccessException | InvocationTargetException e) {
                    return null;
                }
            }

            public void writeValue(final Object o, final Object value) {
                try {
                    if (setter != null) {
                        setter.invoke(o, value);
                    }
                } catch (IllegalAccessException | InvocationTargetException e) {
                    // ignore
                }
            }

            public boolean isAssociation() {
                return isAssociationMany || isAssociationOne;
            }

            public Class<Object> getEntityType() {
                return isAssociationOne ? fieldType : genericType;
            }

            public <T> Collection<T> newCollection() {
                if (Set.class.isAssignableFrom(fieldType)) {
                    return new HashSet<>();
                } else if (List.class.isAssignableFrom(fieldType)) {
                    return new ArrayList<>();
                } else if (Collection.class.isAssignableFrom(fieldType)) {
                    return new ArrayList<>();
                }
                throw new UnsupportedOperationException("Unsupported type of collection " + fieldType);
            }
        }
    }
}
