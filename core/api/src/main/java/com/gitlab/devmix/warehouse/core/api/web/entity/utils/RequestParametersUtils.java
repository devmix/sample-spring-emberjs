package com.gitlab.devmix.warehouse.core.api.web.entity.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.function.Supplier;

/**
 * @author Sergey Grachev
 */
public final class RequestParametersUtils {

    private RequestParametersUtils() {
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> queryToMap(final Map<String, Object> query) {
        final Map<String, Object> result = new HashMap<>();
        for (final String key : query.keySet()) {
            final Object value = query.get(key);
            if (key.indexOf('[') != -1) {
                final StringTokenizer tokenizer = new StringTokenizer(key, "[");

                String prevProperty = tokenizer.nextToken();
                int prevIndex = 0;
                Type prevType = Type.MAP;
                Object prevObject = result;

                while (tokenizer.hasMoreTokens()) {
                    final String subProperty = subPropertyToken(tokenizer);
                    final Type type = isArrayIndex(subProperty) ? Type.LIST : Type.MAP;
                    final int index = Type.LIST == type ? Integer.parseInt(subProperty) : 0;
                    final Supplier factory = Type.LIST == type ? ArrayList::new : HashMap::new;
                    final Object object = Type.LIST == prevType
                            ? addToList((List<Object>) prevObject, prevIndex, factory)
                            : addToMap((Map<String, Object>) prevObject, prevProperty, factory);

                    prevType = type;
                    prevObject = object;
                    prevIndex = index;
                    prevProperty = subProperty;
                }

                if (Type.LIST == prevType) {
                    ((List<Object>) prevObject).add(value);
                } else {
                    ((Map<String, Object>) prevObject).put(prevProperty, value);
                }
            } else {
                result.put(key, value);
            }
        }
        return result;
    }

    private static Object addToMap(final Map<String, Object> map, final String property, final Supplier factory) {
        Object result = map.get(property);
        if (result == null) {
            result = factory.get();
            map.put(property, result);
        }
        return result;
    }

    private static Object addToList(final List<Object> list, final int index, final Supplier factory) {
        Object result = index >= list.size() ? null : list.get(index);
        if (result == null) {
            result = factory.get();
            list.add(index, result);
        }
        return result;
    }

    private static String subPropertyToken(final StringTokenizer tokenizer) {
        final String value = tokenizer.nextToken();
        return value.substring(0, value.length() - 1);
    }

    private static boolean isArrayIndex(final String value) {
        if (!"".equals(value)) {
            for (int i = 0; i < value.length(); i++) {
                if (!Character.isDigit(value.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    private enum Type {
        MAP,
        LIST
    }
}
