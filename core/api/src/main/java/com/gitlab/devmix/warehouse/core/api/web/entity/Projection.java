package com.gitlab.devmix.warehouse.core.api.web.entity;

import lombok.Getter;
import lombok.ToString;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.TreeMap;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author Sergey Grachev
 */
@ToString
public final class Projection implements ProjectionProperty {

    private static final Projection FULL = new Projection(true);

    private final ProjectionProperty nodes;

    private Projection(final boolean full) {
        if (full) {
            nodes = new AnyProperty();
        } else {
            nodes = new ExpressionProperty(false);
        }
    }

    public static Projection of(final String... properties) {
        final Projection projection = new Projection(false);
        for (final String property : properties) {
            projection.add(property);
        }
        return projection;
    }

    public static Projection full() {
        return FULL;
    }

    private Projection add(final String property) {
        if (isBlank(property) || nodes == FULL) {
            return this;
        }

        final String[] parts = property.split("\\.");
        if (parts.length > 0) {
            ExpressionProperty node = ((ExpressionProperty) nodes).add(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                node = node.add(parts[i]);
            }
        } else {
            ((ExpressionProperty) nodes).add(property);
        }

        return this;
    }

    @Override
    public boolean isAny() {
        return this == FULL;
    }

    @Nullable
    @Override
    public ProjectionProperty next(final String property) {
        return nodes.next(property);
    }

    @ToString
    private static final class AnyProperty implements ProjectionProperty {

        @Override
        public boolean isAny() {
            return true;
        }

        @Override
        public ProjectionProperty next(final String property) {
            return this;
        }
    }

    @ToString
    private static final class ExpressionProperty implements ProjectionProperty {

        @Getter
        private final boolean isAny;
        private Map<String, ExpressionProperty> properties;

        public ExpressionProperty(final boolean isAny) {
            this.isAny = isAny;
        }

        public ExpressionProperty add(final String name) {
            if (properties == null) {
                properties = new TreeMap<>();
            }
            return properties.computeIfAbsent(name, s -> new ExpressionProperty("*".equals(s)));
        }

        @Nullable
        @Override
        public ExpressionProperty next(final String property) {
            ExpressionProperty result = null;
            if (properties != null) {
                result = properties.get(property);
                if (result == null) {
                    result = properties.get("*");
                }
            }
            return result;
        }
    }
}
