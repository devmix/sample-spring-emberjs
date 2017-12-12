package com.gitlab.devmix.warehouse.core.impl.services;

import com.gitlab.devmix.warehouse.core.api.services.EntityRestRegistry;
import com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sergey Grachev
 */
@Service
public class EntityRestRegistryImpl implements EntityRestRegistry {

    private final Map<String, Endpoint> endpoints = new ConcurrentHashMap<>();

    @Override
    public void add(final Endpoint endpoint) {
        endpoints.put(endpoint.getRootUri(), endpoint);
    }

    @Nullable
    @Override
    public Endpoint findEndpoint(final String uri) {
        Endpoint result = null;
        for (final Endpoint endpoint : endpoints.values()) {
            if (uri.startsWith(endpoint.getRootUri())
                    && (result == null || endpoint.getRootUri().length() > result.getRootUri().length())) {
                result = endpoint;
            }
        }
        return result;
    }
}
