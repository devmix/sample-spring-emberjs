package com.gitlab.devmix.warehouse.core.api.web.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Value;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
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

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Sergey Grachev
 */
public final class Metadata {

    private static final Map<Class<?>, Descriptor> DESCRIPTORS = new ConcurrentHashMap<>();
    private static final Set<String> IGNORE = new HashSet<>();

    static {
        IGNORE.add("class");
    }

    private Metadata() {
    }

    public static Descriptor of(final Class entityClass) {
        return DESCRIPTORS.computeIfAbsent(entityClass, Metadata::readMetadata);
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
        for (final PropertyDescriptor property : properties) {
            final String propertyName = property.getName();
            if (IGNORE.contains(propertyName)) {
                continue;
            }

            final Method getter = property.getReadMethod();
            final Method setter = property.getWriteMethod();
            final Field field = findField(entityClass, propertyName);
            final Class type = property.getPropertyType();

            final boolean isRelationshipCollection = hasAnyAnnotation(
                    field, getter, ManyToMany.class, OneToMany.class, Many.class);

            final boolean isRelationshipSingle = hasAnyAnnotation(
                    field, getter, ManyToOne.class, OneToOne.class, One.class);

            final boolean isId = hasAnyAnnotation(
                    field, getter, javax.persistence.Id.class, Id.class);

            final Class genericType = isRelationshipCollection && getter != null
                    ? (Class) ((ParameterizedType) getter.getGenericReturnType()).getActualTypeArguments()[0] : null;

            if (isId) {
                idAttributeName = propertyName;
            }

            fields.put(propertyName, new Descriptor.Attribute(
                    type, genericType, isRelationshipCollection, isRelationshipSingle, isId, getter, setter));
        }

        return new Descriptor(entityClass, entityName, fields, idAttributeName);
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

    @Nullable
    private static String findName(final Class<?> entityClass) {
        // TODO SG cache
        Class<?> next = entityClass;
        while (Object.class != next && next != null) {
            final Name annotationName = next.getDeclaredAnnotation(Name.class);
            if (annotationName != null) {
                return annotationName.value();
            }

            final Entity annotationEntity = next.getDeclaredAnnotation(Entity.class);
            if (annotationEntity != null) {
                return annotationEntity.name();
            }

            next = next.getSuperclass();
        }
        return entityClass.getSimpleName();
    }

    @Value
    public static final class Descriptor {

        private final Class entityClass;
        private final String entityName;
        private final Map<String, Attribute> attributes;
        @Nullable
        private final String idAttributeName;

        @SuppressWarnings("unchecked")
        @Nullable
        public <T> T getId(final Object source) {
            if (isNotBlank(idAttributeName)) {
                final Attribute id = attributes.get(idAttributeName);
                if (id != null) {
                    return (T) id.readValue(source);
                }
            }
            return null;
        }

        public void setId(final Object target, final Object value) {
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
            setId(result, id);
            return result;
        }

        @Value
        public static class Attribute {

            private final Class fieldType;

            @Nullable
            private final Class genericType;

            private final boolean isRelationshipCollection;

            private final boolean isRelationshipSingle;

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

            public boolean isRelationship() {
                return isRelationshipCollection || isRelationshipSingle;
            }

            public Class<?> getEntityType() {
                return isRelationshipSingle ? fieldType : genericType;
            }

            public <T> Collection<T> newCollection() {
                if (Set.class.isAssignableFrom(fieldType)) {
                    return new HashSet<>();
                } else if (List.class.isAssignableFrom(fieldType)) {
                    return new ArrayList<>();
                }
                throw new UnsupportedOperationException("Unsupported type of collection " + fieldType);
            }
        }
    }

    @Target(TYPE)
    @Retention(RUNTIME)
    public @interface Name {
        String value();
    }

    @Target(FIELD)
    @Retention(RUNTIME)
    public @interface One {
    }

    @Target({FIELD, METHOD})
    @Retention(RUNTIME)
    public @interface Many {
    }

    @Target({FIELD, METHOD})
    @Retention(RUNTIME)
    public @interface Id {
    }
}
