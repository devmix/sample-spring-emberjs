package com.gitlab.devmix.warehouse.core.api.services.entity;

import com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint;

import javax.annotation.Nullable;

/**
 * @author Sergey Grachev
 */
public interface EntityRestRegistry {

    void add(Endpoint endpoint);

    @Nullable
    Endpoint findEndpoint(String uri);
}
